package io.github.llchen.apidoc.core;

/**
 * 类过滤器
 *
 * @author llchen12
 * @date 2018/6/14
 */
public interface ClassFilter {

    /**
     * 是否通过这个类
     * @param clazz 类
     * @return true->通过，false->不通过
     */
    boolean accept(Class<?> clazz);
}
