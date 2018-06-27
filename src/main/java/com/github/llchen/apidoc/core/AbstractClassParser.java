package com.github.llchen.apidoc.core;

import java.lang.annotation.Annotation;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public abstract class AbstractClassParser<A extends Annotation,R> extends AbstractParser<R, Class<?>> {


    private Class<A> annotationClass;

    protected AbstractClassParser(Class<A> annotationClass){
        this.annotationClass=annotationClass;
    }


    @Override
    protected int order() {
        return 0;
    }

    @Override
    public R parse(Class<?> clazz) {
        A annotation = clazz.getAnnotation(annotationClass);
        return parse2Obj(clazz,annotation);
    }


    protected abstract R parse2Obj(Class<?> clazz, A annotation);
}
