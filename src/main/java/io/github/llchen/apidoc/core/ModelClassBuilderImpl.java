package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.DataType;
import io.github.llchen.apidoc.annotation.ApiModel;
import io.github.llchen.apidoc.annotation.ApiModelProperty;
import io.github.llchen.apidoc.model.ApiModelDefinition;
import io.github.llchen.apidoc.model.ApiModelPropertyDefinition;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/6/16
 */
public class ModelClassBuilderImpl implements ModelClassBuilder {


    @Override
    public ApiModelDefinition build(Class<?> modelClass) {
        ApiModelDefinition apiModelDefinition = new ApiModelDefinition();
        ApiModel apiModel = modelClass.getAnnotation(ApiModel.class);
        String name = apiModel.name();
        if (StringUtils.isEmpty(name)) {
            name = modelClass.getSimpleName();
        }
        apiModelDefinition.setName(name);
        String description = apiModel.description();
        //TODO:从注释取
        apiModelDefinition.setDescription(description);
        apiModelDefinition.setRemark(apiModel.remark());

        //构建属性
        List<ApiModelPropertyDefinition> propertyList = new LinkedList<>();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            propertyList.add(buildProperty(field));
        }
        apiModelDefinition.setPropertyList(propertyList);
        return apiModelDefinition;
    }

    private ApiModelPropertyDefinition buildProperty(Field field) {
        ApiModelPropertyDefinition property = new ApiModelPropertyDefinition();
        ApiModelProperty modelProperty = field.getAnnotation(ApiModelProperty.class);
        if (modelProperty != null) {
            String name = modelProperty.name();
            if (StringUtils.isEmpty(name)) {
                name = field.getName();
            }
            property.setName(name);
            String description = modelProperty.description();
            //TODO:取注释
            property.setDescription(description);
            String defaultValue = modelProperty.defaultValue();
            if (StringUtils.isEmpty(defaultValue)) {
                //TODO:取默认值
            }
            property.setDefaultValue(defaultValue);

        }else {
            property.setName(field.getName());
            //TODO:取注释
            property.setDescription("");
            //TODO:取默认值
            property.setDescription("");
        }
        property.setExample(modelProperty.example());
        property.setRequired(modelProperty.required() ? "是" : "否");
        property.setType(DataType.getType(field.getType()));
        return property;
    }
}
