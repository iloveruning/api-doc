package io.github.llchen.apidoc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Data
public class ApiDefinition implements Serializable {

    private String name;
    private String path;
    private String description;
    private String remark;
    private String httpMethod;

    private String demoResponse;

    private List<ApiParamDefinition> requestParams;
    private List<ApiParamDefinition> responseParams;

    private ApiModelDefinition resultModel;

}
