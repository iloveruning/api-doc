package io.github.llchen.apidoc.core;


import io.github.llchen.apidoc.annotation.ApiDoc;
import io.github.llchen.apidoc.annotation.ApiModel;
import io.github.llchen.apidoc.config.ApiDocProperties;
import io.github.llchen.apidoc.model.ApiModelDefinition;
import io.github.llchen.apidoc.model.Document;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * @author llchen12
 * @date 2018/6/11
 */
public class ApiDocContext implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiDocContext.class);

    private static ApiDocBuilder apiDocBuilder;

    private Executor executor;

    private ApiDocProperties properties;


    public static HashMap<String, Document> getDocMap() {
        return apiDocBuilder.getDocMap();
    }


    public static HashMap<String, ApiModelDefinition> getModelMap() {
        return apiDocBuilder.getModelMap();
    }


    public ApiDocContext(ApiDocProperties properties) {
        this.properties = properties;
        apiDocBuilder = new ApiDocBuilder();
    }

    /**
     * 扫描api
     */
    private void scanApi(ApplicationContext ctx) {
        Assert.notNull(ctx, "ApplicationContext不能为空");
        LOGGER.info("开始扫描Api...");
        long start = System.currentTimeMillis();
        //初始化线程池
        initThreadPool(ctx);
        String basePath = ctx.getClass().getResource("/").getPath();
        basePath = basePath.endsWith("/") ? basePath : basePath + "/";
        String controllerPackage = properties.getControllerPackage();
        if (StringUtils.isNotBlank(controllerPackage)) {
            String modelPackage = properties.getModelPackage();
            if (StringUtils.isNotBlank(modelPackage)) {
                //先扫描model
                scanModel(basePath, modelPackage);
            }
            //扫描controller
            scanController(ctx, basePath, controllerPackage);

        } else {
            //TODO:全包扫描

        }
        executor.destroy();
        long time = System.currentTimeMillis() - start;
        LOGGER.info("扫描Api完毕,耗时:" + time / 1000.0 + "秒");
    }

    /**
     * 初始化线程池
     */
    private void initThreadPool(ApplicationContext ctx) {
        ThreadPoolTaskExecutor taskExecutor = null;
        try {
            taskExecutor = ctx.getBean(ThreadPoolTaskExecutor.class);
        } catch (Exception e) {
            //ignore
        }
        if (taskExecutor != null) {
            executor = new Executor(taskExecutor);
            return;
        }

        BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern("api-thread-pool-%d")
                .daemon(false)
                .build();

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024), threadFactory);

        executor = new Executor(poolExecutor);
    }

    /**
     * 扫描model类
     */
    private void scanModel(String basePath, String modelPackage) {
        String filePath = basePath + modelPackage.replace(".", "/");
        File dir = new File(filePath);
        String[] classNames = dir.list((dir1, name) -> name.endsWith(".class"));
        if (classNames != null && classNames.length > 0) {
            String classFullName;
            for (String className : classNames) {
                classFullName = modelPackage + "." + className.substring(0, className.lastIndexOf("."));
                try {
                    Class<?> aClass = Class.forName(classFullName);
                    ApiModel apiModel = aClass.getAnnotation(ApiModel.class);
                    if (apiModel != null) {
                        //多线程加载
                        executor.execute(() -> loadModel(aClass, apiModel));
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.warn("加载类:{}失败", classFullName, e);
                }
            }
        }

    }

    private void loadModel(Class<?> modelClass, ApiModel apiModel) {
        apiDocBuilder.addModel(apiModel, modelClass);
    }

    /**
     * 扫描controller类
     */
    private void scanController(ApplicationContext ctx, String basePath, String controllerPackage) {
        String filePath = basePath + controllerPackage.replace(".", "/");
        File dir = new File(filePath);
        String[] classNames = dir.list((dir1, name) -> name.endsWith(".class"));
        if (classNames != null && classNames.length > 0) {
            String classFullName;
            for (String className : classNames) {
                classFullName = controllerPackage + "." + className.substring(0, className.lastIndexOf("."));
                try {
                    Class<?> aClass = Class.forName(classFullName);
                    ApiDoc apiDoc = aClass.getAnnotation(ApiDoc.class);
                    if (apiDoc != null) {
                        Object apiDocObject = ctx.getBean(aClass);
                        //多线程加载
                        executor.execute(() -> loadApi(aClass, apiDocObject, apiDoc));
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.warn("加载类:{}失败", classFullName, e);
                }
            }
        }
    }

    /**
     * 加载api
     */
    private void loadApi(Class<?> apiDocClass, Object apiDocObject, ApiDoc apiDoc) {
        apiDocBuilder.addApiDoc(apiDoc, apiDocClass, apiDocObject,properties);
    }


    /**
     * 容器加载完成后执行扫描api
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (properties.isEnable()) {
            scanApi(contextRefreshedEvent.getApplicationContext());
        }
    }


    private static class Executor {

        ThreadPoolTaskExecutor taskExecutor;
        ThreadPoolExecutor poolExecutor;

        Executor(ThreadPoolTaskExecutor taskExecutor) {
            this.taskExecutor = taskExecutor;
            this.poolExecutor = null;
        }

        Executor(ThreadPoolExecutor poolExecutor) {
            this.taskExecutor = null;
            this.poolExecutor = poolExecutor;
        }

        void execute(Runnable task) {
            if (taskExecutor != null) {
                taskExecutor.execute(task);
            } else {
                poolExecutor.execute(task);
            }
        }

        void execute(Runnable task, long startTimeout) {
            this.execute(task);
        }

        Future<?> submit(Runnable task) {
            if (taskExecutor != null) {
                return taskExecutor.submit(task);
            } else {
                return poolExecutor.submit(task);
            }

        }

        <T> Future<T> submit(Callable<T> task) {
            if (taskExecutor != null) {
                return taskExecutor.submit(task);
            } else {
                poolExecutor.shutdown();
                return poolExecutor.submit(task);
            }
        }

        void destroy() {
            if (poolExecutor != null) {
                poolExecutor.shutdown();
                poolExecutor = null;
            }

            if (taskExecutor != null) {
                taskExecutor.shutdown();
            }
        }


    }

}
