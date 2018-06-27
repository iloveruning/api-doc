package com.github.llchen.apidoc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Data
public class ApiModelPropertyDefinition implements Serializable {

    private String name;
    private String type;
    private String description;
    private String example;
    private String required;
    private String defaultValue;
}
