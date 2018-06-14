package io.github.llchen.apidoc.annotation;

import java.lang.annotation.*;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiModel {

    /**
     * 数据模型描述
     */
    String description() default "";

    /**
     * 备注
     */
    String remark() default "";

}
