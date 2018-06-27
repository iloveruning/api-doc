package com.github.llchen.apidoc.core;

import com.alibaba.fastjson.JSON;
import com.github.llchen.apidoc.annotation.Api;
import com.github.llchen.apidoc.annotation.ApiParam;
import com.github.llchen.apidoc.model.ApiDefinition;
import com.github.llchen.apidoc.model.ApiParamDefinition;
import com.github.llchen.apidoc.utils.Utils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class ApiMethodParser extends AbstractParser<ApiDefinition,Method> {


    @Override
    public int order() {
        return 0;
    }

    @Override
    public ApiDefinition parse(Method method) {
        ApiDefinition apiDefinition = new ApiDefinition();
        Api api = method.getAnnotation(Api.class);
        if (api != null) {
            apiDefinition.setName(api.name());
            apiDefinition.setDemoResponse(api.demoResponse());
            apiDefinition.setDescription(api.description());
            apiDefinition.setHttpMethod(Utils.requestMethod2Str(api.httpMethod()));
            apiDefinition.setPath(api.path());
            apiDefinition.setRemark(api.remark());

            String demoResponse = api.demoResponse();
            String responseExample;
            try {
                responseExample = JSON.toJSONString(JSON.parseObject(demoResponse), true);
            } catch (Exception e) {
                responseExample = demoResponse;
            }
            apiDefinition.setDemoResponse(responseExample);

            //构建requestParams
            ApiParam[] requestParams = api.requestParams();
            List<ApiParamDefinition> requestParamList = buildParam(requestParams);
            apiDefinition.setRequestParams(requestParamList);

            //构建responseParams
            ApiParam[] responseParams = api.responseParams();
            List<ApiParamDefinition> responseParamList = buildParam(responseParams);
            apiDefinition.setResponseParams(responseParamList);

            //构建model
            apiDefinition.setResultClass(api.resultModel());

        }
        return apiDefinition;
    }

    private List<ApiParamDefinition> buildParam(ApiParam[] params){
        List<ApiParamDefinition> paramList=new LinkedList<>();
        ApiParamDefinition apiParamDefinition;
        for (ApiParam apiParam:params){
            apiParamDefinition = new ApiParamDefinition();
            apiParamDefinition.setName(apiParam.name());
            apiParamDefinition.setDescription(apiParam.description());
            apiParamDefinition.setExample(apiParam.example());
            apiParamDefinition.setRequired(apiParam.required() ? "是" : "否");
            apiParamDefinition.setType(apiParam.type().getValue());
            apiParamDefinition.setDefaultValue(apiParam.defaultValue());
            paramList.add(apiParamDefinition);
        }
        return paramList;
    }
}
