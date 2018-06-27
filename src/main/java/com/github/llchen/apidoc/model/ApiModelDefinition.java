package com.github.llchen.apidoc.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Data
public class ApiModelDefinition implements Serializable {

    private String name;
    private String description;
    private String remark;

    private List<ApiModelPropertyDefinition> propertyList;
}
