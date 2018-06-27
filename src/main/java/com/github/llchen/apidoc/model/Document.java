package com.github.llchen.apidoc.model;


import com.github.llchen.apidoc.config.ApiDocProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Data
public class Document implements Serializable {

    private String host;
    private ApiDocProperties.Contact contact;
    private String name;
    private String description;
    private String basePath;
    private String version;

    private List<ApiDefinition> apiList;
    private List<ApiCodeDefinition> codeList;
    //private List<ApiModelDefinition> modelList;



}
