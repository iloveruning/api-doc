package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.DataType;
import io.github.llchen.apidoc.model.ApiDefinition;
import io.github.llchen.apidoc.model.ApiParamDefinition;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class ReflectMethodParser implements MethodParser<ApiDefinition> {

    private static ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Override
    public int order() {
        return 2;
    }

    @Override
    public ApiDefinition parse(Method method) {
        ApiDefinition apiDefinition = new ApiDefinition();
        apiDefinition.setName(method.getName());
        apiDefinition.setResultClass(method.getReturnType());
        apiDefinition.setRequestParams(buildRequestParams(method));
        Class<?> returnType = method.getReturnType();
        apiDefinition.setResponseParams(buildResponseParams(returnType));
        apiDefinition.setResultClass(returnType);
        return apiDefinition;
    }


    private List<ApiParamDefinition> buildRequestParams(Method method) {
        List<ApiParamDefinition> apiParamList = new LinkedList<>();
        ApiParamDefinition apiParamDefinition;
        String[] parameterNames = discoverer.getParameterNames(method);
        Parameter[] parameters = method.getParameters();
        for (int i = 0, l = parameterNames.length; i < l; i++) {
            Parameter parameter = parameters[i];
            apiParamDefinition = new ApiParamDefinition();
            apiParamDefinition.setName(parameterNames[i]);
            apiParamDefinition.setRequired("是");
            apiParamDefinition.setClazz(parameter.getType());
            apiParamList.add(apiParamDefinition);
        }
        return apiParamList;
    }

    private List<ApiParamDefinition> buildResponseParams(Class<?> returnType) {
        List<ApiParamDefinition> apiParamList = new LinkedList<>();
        ApiParamDefinition apiParamDefinition;
        //反射获取
        Field[] fields = returnType.getDeclaredFields();
        for (Field field : fields) {
            apiParamDefinition = new ApiParamDefinition();
            apiParamDefinition.setType(DataType.getType(field.getType()));
            apiParamDefinition.setName(field.getName());
            apiParamDefinition.setRequired("是");
            apiParamDefinition.setExample("");
            apiParamDefinition.setDescription("");
            apiParamDefinition.setDefaultValue("");
            apiParamList.add(apiParamDefinition);
        }
        return apiParamList;
    }
}
