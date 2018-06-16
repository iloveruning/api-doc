package io.github.llchen.apidoc.core;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * @author chenliangliang
 * @date 2018/6/16
 */
public class ClassPathClassFinder implements ClassFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathClassFinder.class);

    private String classPath;

    public ClassPathClassFinder() {
        this.classPath = ClassPathClassFinder.class.getResource("/").getPath();
    }

    @Override
    public List<Class<?>> find(String path) {
        List<Class<?>> list = new LinkedList<>();
        String filePath;
        if (StringUtils.isEmpty(path) || "/".equals(path)) {
            filePath = classPath;
        } else {
            filePath = classPath + path;
        }

        Set<String> classNameSet = findClass(filePath);
        for (String className : classNameSet) {
            try {
                list.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                LOGGER.warn("加载{}类失败", className, e);
            }
        }

        return list;
    }

    private Set<String> findClass(String filePath) {
        Set<String> classNameSet = new HashSet<>();
        findClass(new File(filePath), classNameSet);
        return classNameSet;
    }

    private void findClass(File file, Set<String> classNameSet) {
        if (!file.exists()) {
            return;
        }
        int i = this.classPath.length() - 1;
        if (file.isFile()) {
            String path = file.getPath();
            if (path.endsWith(".class")) {
                String className = path.substring(i, path.lastIndexOf("."))
                        .replace("\\", ".")
                        .replace("/", ".");
                classNameSet.add(className);
            }
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                findClass(f, classNameSet);
            }
        }
    }


    public String getClassPath() {
        return classPath;
    }


}

