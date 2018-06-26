package io.github.llchen.apidoc.core;

import java.lang.annotation.Annotation;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public abstract class AbstractClassParser<A extends Annotation,R> implements ClassParser<R> {


    private Class<A> annotationClass;

    protected AbstractClassParser(Class<A> annotationClass){
        this.annotationClass=annotationClass;
    }

    @Override
    public R parse(Class<?> clazz) {
        A annotation = clazz.getAnnotation(annotationClass);
        return parse2Obj(clazz,annotation);
    }


    protected abstract R parse2Obj(Class<?> clazz, A annotation);
}
