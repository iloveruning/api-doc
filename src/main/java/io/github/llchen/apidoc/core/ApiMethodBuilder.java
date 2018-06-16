package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.model.ApiDefinition;
import io.github.llchen.apidoc.model.Document;

import java.lang.reflect.Method;

/**
 * @author chenliangliang
 * @date 2018/6/16
 */
public interface ApiMethodBuilder {

    ApiDefinition build(Document document, Method method);
}
