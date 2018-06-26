package io.github.llchen.apidoc.core;



/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public interface ClassParser<R>{

    R parse(Class<?> clazz);
}
