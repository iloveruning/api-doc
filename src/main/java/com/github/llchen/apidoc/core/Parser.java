package com.github.llchen.apidoc.core;

/**
 * @author llchen12
 * @date 2018/6/27
 */
public interface Parser<R,T> {

    R parse(T t);
}
