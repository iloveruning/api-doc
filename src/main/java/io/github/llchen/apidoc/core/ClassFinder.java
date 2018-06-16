package io.github.llchen.apidoc.core;

import java.util.List;

/**
 * @author llchen12
 * @date 2018/6/14
 */
public interface ClassFinder {

    /**
     *
     * @param path class路径
     * @return
     */
    List<Class<?>> find(String path);
}
