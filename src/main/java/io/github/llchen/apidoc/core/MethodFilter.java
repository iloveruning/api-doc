package io.github.llchen.apidoc.core;

import java.lang.reflect.Method;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public interface MethodFilter {

    boolean accept(Method method);
}
