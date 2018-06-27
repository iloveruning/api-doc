package com.github.llchen.apidoc.core;

import com.github.llchen.apidoc.model.ApiModelDefinition;

/**
 * @author chenliangliang
 * @date 2018/6/16
 */
public interface ModelClassBuilder {

    ApiModelDefinition build(Class<?> modelClass);
}
