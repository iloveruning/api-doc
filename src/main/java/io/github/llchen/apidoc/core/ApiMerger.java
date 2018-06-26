package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.model.ApiDefinition;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class ApiMerger implements Merger<ApiDefinition,ApiDefinition> {


    @Override
    public ApiDefinition merge(ApiDefinition... obj) {
        ApiDefinition apiDefinition=new ApiDefinition();
        String name="";
        for (ApiDefinition api:obj){
            name=api.getName();
            if (StringUtils.isNotBlank(name)){
                break;
            }
        }
        apiDefinition.setName(name);

        String description="";
        for (ApiDefinition api:obj){
            description=api.getDescription();
            if (StringUtils.isNotBlank(description)){
                break;
            }
        }
        apiDefinition.setDescription(description);

        String remark="";
        for (ApiDefinition api:obj){
            remark=api.getRemark();
            if (StringUtils.isNotBlank(remark)){
                break;
            }
        }
        apiDefinition.setRemark(remark);

        String path="";
        for (ApiDefinition api:obj){
            path=api.getPath();
            if (StringUtils.isNotBlank(path)){
                break;
            }
        }
        apiDefinition.setPath(path);

        return apiDefinition;
    }
}
