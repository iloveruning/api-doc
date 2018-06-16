package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.model.Document;

/**
 * @author chenliangliang
 * @date 2018/6/16
 */
public interface ApiClassBuilder {

    Document build(Class<?> apiClass);
}
