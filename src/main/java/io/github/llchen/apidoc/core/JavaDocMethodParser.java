package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.model.ApiDefinition;

import java.lang.reflect.Method;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class JavaDocMethodParser implements MethodParser<ApiDefinition> {



    @Override
    public int order() {
        return 3;
    }

    @Override
    public ApiDefinition parse(Method method) {
        ApiDefinition apiDefinition=new ApiDefinition();

        return apiDefinition;
    }
}
