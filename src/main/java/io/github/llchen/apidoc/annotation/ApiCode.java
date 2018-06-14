package io.github.llchen.apidoc.annotation;

import java.lang.annotation.*;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ApiCode {

    /**
     * 错误码
     */
    String code();

    /**
     * 错误解释
     */
    String description();
}
