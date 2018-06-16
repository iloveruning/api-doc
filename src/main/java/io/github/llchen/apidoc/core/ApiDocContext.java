package io.github.llchen.apidoc.core;


import io.github.llchen.apidoc.annotation.Api;
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
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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
        ClassFinder classFinder = new ClassPathClassFinder();

        ConcurrentHashMap<String, Class<?>> controllerClasses = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Class<?>> modelClasses = new ConcurrentHashMap<>();

        String controllerPackage = properties.getControllerPackage();
        String modelPackage = properties.getModelPackage();
        boolean controllerFlag = StringUtils.isBlank(controllerPackage);
        boolean modelFlag = StringUtils.isBlank(modelPackage);
        if (controllerFlag || modelFlag) {
            //TODO: 全包扫描
            List<Class<?>> classList = classFinder.find("/");
            for (Class<?> clazz : classList) {
                if (clazz.getAnnotation(ApiDoc.class) != null) {
                    controllerClasses.put(clazz.getCanonicalName(), clazz);
                }

                if (clazz.getAnnotation(Controller.class) != null) {
                    controllerClasses.put(clazz.getCanonicalName(), clazz);
                }

                if (clazz.getAnnotation(RestController.class) != null) {
                    controllerClasses.put(clazz.getCanonicalName(), clazz);
                }

                if (clazz.getAnnotation(ApiModel.class) != null) {
                    modelClasses.put(clazz.getCanonicalName(), clazz);
                }
            }
        } else {
            if (!controllerFlag){
                List<Class<?>> controllerClassList = classFinder.find(controllerPackage.replace(".", "/"));
                for (Class<?> clazz:controllerClassList){
                    if (clazz.getAnnotation(ApiDoc.class) != null) {
                        controllerClasses.put(clazz.getCanonicalName(), clazz);
                    }

                    if (clazz.getAnnotation(Controller.class) != null) {
                        controllerClasses.put(clazz.getCanonicalName(), clazz);
                    }
                    if (clazz.getAnnotation(RestController.class) != null) {
                        controllerClasses.put(clazz.getCanonicalName(), clazz);
                    }
                }
            }

            if (!modelFlag){
                List<Class<?>> modelClassList = classFinder.find(modelPackage.replace(".", "/"));
                for (Class<?> clazz:modelClassList){
                    if (clazz.getAnnotation(ApiModel.class) != null) {
                        modelClasses.put(clazz.getCanonicalName(), clazz);
                    }
                }
            }
        }

        handleModel(modelClasses);
        handleController(controllerClasses);

        executor.destroy();
        long time = System.currentTimeMillis() - start;
        LOGGER.info("扫描Api完毕,耗时:" + time / 1000.0 + "秒");
    }



    private void handleController(ConcurrentHashMap<String, Class<?>> controllerClasses) {
        for (Class<?> clazz:controllerClasses.values()){
            executor.execute(()->loadApi(clazz));
        }
    }

    private void loadApi(Class<?> clazz) {
        if (clazz.getAnnotation(ApiDoc.class)!=null){
            apiDocBuilder.addApiDoc(clazz.getAnnotation(ApiDoc.class),clazz,null,properties);
        }else if (clazz.getAnnotation(Controller.class)!=null){
            apiDocBuilder.addApiDoc(clazz.getAnnotation(Controller.class),clazz,null,properties);
        }else if (clazz.getAnnotation(RestController.class)!=null){
            apiDocBuilder.addApiDoc(clazz.getAnnotation(RestController.class),clazz,null,properties);
        }
    }

    private void handleModel(ConcurrentHashMap<String, Class<?>> modelClasses) {
        for (Class<?> clazz:modelClasses.values()){
            executor.execute(()->loadModel(clazz));
        }
    }

    private void loadModel(Class<?> clazz) {
        apiDocBuilder.addModel(clazz.getAnnotation(ApiModel.class),clazz);
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
