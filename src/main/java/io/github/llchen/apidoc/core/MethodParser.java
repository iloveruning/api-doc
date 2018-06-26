package io.github.llchen.apidoc.core;


import java.lang.reflect.Method;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public interface MethodParser<R> {

    int order();

    R parse(Method method);
}
