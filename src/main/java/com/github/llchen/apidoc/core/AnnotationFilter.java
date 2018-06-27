package com.github.llchen.apidoc.core;

import java.lang.annotation.Annotation;

/**
 * @author llchen12
 * @date 2018/6/14
 */
public class AnnotationFilter implements ClassFilter {

    private Class annotationClass;

    public AnnotationFilter(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean accept(Class<?> clazz) {
        return clazz.getAnnotation(annotationClass) != null;
    }

}
