package com.github.llchen.apidoc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Data
@AllArgsConstructor
public class ApiCodeDefinition implements Serializable {

    private String code;
    private String description;
}
