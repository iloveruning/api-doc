package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.annotation.ApiModel;
import io.github.llchen.apidoc.model.ApiModelDefinition;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class ModelClassParser implements ClassParser<ApiModel,ApiModelDefinition> {


    @Override
    public ApiModelDefinition parse(Class<?> clazz, Class<ApiModel> annotationClass) {

        return null;
    }
}
