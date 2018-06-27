package com.github.llchen.apidoc.core;

import com.github.llchen.apidoc.model.ApiDefinition;
import com.github.llchen.apidoc.model.ApiModelDefinition;
import com.github.llchen.apidoc.model.ApiParamDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class ApiMerger implements Merger<ApiDefinition, ApiDefinition> {


    @Override
    public ApiDefinition merge(ApiDefinition... obj) {
        ApiDefinition apiDefinition = new ApiDefinition();
        String name = "";
        for (ApiDefinition api : obj) {
            if (StringUtils.isNotBlank(api.getName())) {
                name = api.getName();
                break;
            }
        }
        apiDefinition.setName(name);

        String description = "";
        for (ApiDefinition api : obj) {
            if (StringUtils.isNotBlank(api.getDescription())) {
                description =api.getDescription();
                break;
            }
        }
        apiDefinition.setDescription(description);

        String remark = "";
        for (ApiDefinition api : obj) {
            if (StringUtils.isNotBlank(api.getRemark())) {
                remark = api.getRemark();
                break;
            }
        }
        apiDefinition.setRemark(remark);

        String path = "";
        for (ApiDefinition api : obj) {
            if (StringUtils.isNotBlank(api.getPath())) {
                path = api.getPath();
                break;
            }
        }
        apiDefinition.setPath(path);

        String httpMethod = "";
        for (ApiDefinition api : obj) {
            if (StringUtils.isNotBlank(api.getHttpMethod())) {
                httpMethod = api.getHttpMethod();
                break;
            }
        }
        apiDefinition.setHttpMethod(httpMethod);

        String demoResponse = "";
        for (ApiDefinition api : obj) {
            if (StringUtils.isNotBlank(api.getDemoResponse())) {
                demoResponse = api.getDemoResponse();
                break;
            }
        }
        apiDefinition.setDemoResponse(demoResponse);


        List<ApiParamDefinition> requestParams=Collections.emptyList();
        for (ApiDefinition api : obj) {
            requestParams = api.getRequestParams();
            if (requestParams != null && !requestParams.isEmpty()) {
                break;
            }
        }
        apiDefinition.setRequestParams(requestParams);

        List<ApiParamDefinition> responseParams=Collections.emptyList();
        for (ApiDefinition api : obj) {
            responseParams = api.getResponseParams();
            if (responseParams != null && !responseParams.isEmpty()) {
                break;
            }
        }
        apiDefinition.setResponseParams(responseParams);

        Class<?> resultClass=null;
        for (ApiDefinition api:obj){
            resultClass=api.getResultClass();
            if (resultClass!=null){
                break;
            }
        }
        apiDefinition.setResultClass(resultClass);

        ApiModelDefinition resultModel=null;
        for (ApiDefinition api:obj){
            if (api.getResultModel()!=null){
                resultModel=api.getResultModel();
                break;
            }
        }
        apiDefinition.setResultModel(resultModel);

        return apiDefinition;

    }
}
