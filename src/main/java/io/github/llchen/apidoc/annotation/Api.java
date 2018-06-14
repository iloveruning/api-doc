package io.github.llchen.apidoc.annotation;

import java.lang.annotation.*;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Api {


    /**
     *接口名字
     */
    String name() default "";

    /**
     *请求URL
     */
    String path() default "";

    /**
     * 描述
     */
    String description() default "";


    /**
     * 备注
     */
    String remark() default "";


    /**
     * http请求方式
     */
    String[] httpMethod() default {};


    /**
     * 请求参数
     */
    ApiParam[] requestParams() default {};


    /**
     *返回示例
     */
    String demoResponse() default "";


    /**
     * 返回参数
     */
    ApiParam[] responseParams() default {};


    /**
     *返回值模型
     */
    Class<?> resultModel() default Void.class;

}
