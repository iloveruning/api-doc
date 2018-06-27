package com.github.llchen.apidoc.core;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public interface Merger<T,R> {

    R merge(T...obj);
}
