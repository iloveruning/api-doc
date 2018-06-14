package io.github.llchen.apidoc.annotation;



import io.github.llchen.apidoc.DataType;

import java.lang.annotation.*;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ApiParam {

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

    /**
     * 数据类型
     */
    DataType type() default DataType.STRING;
}
