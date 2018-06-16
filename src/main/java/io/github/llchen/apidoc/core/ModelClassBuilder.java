package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.model.ApiModelDefinition;

/**
 * @author chenliangliang
 * @date 2018/6/16
 */
public interface ModelClassBuilder {

    ApiModelDefinition build(Class<?> modelClass);
}
