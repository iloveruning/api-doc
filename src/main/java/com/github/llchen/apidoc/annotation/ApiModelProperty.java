package com.github.llchen.apidoc.annotation;


import java.lang.annotation.*;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ApiModelProperty {

    /**
     * 参数名
     */
    String name();

    /**
     * 参数说明
     */
    String description();


    /**
     * 默认值
     */
    String defaultValue() default "";

    /**
     * 是否必选
     */
    boolean required() default false;

    /**
     * 示例
     */
    String example() default "";

}
