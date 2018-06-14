package io.github.llchen.apidoc.annotation;

import java.lang.annotation.*;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiDoc {

    /**
     *文档名称
     */
    String name();

    /**
     * api描述
     */
    String description() default "";

    /**
     *api基本path
     */
    String basePath() default "";

    /**
     *文档版本
     */
    String version() default "";

    /**
     * 响应码
     */
    ApiCode[] codes() default {};
}
