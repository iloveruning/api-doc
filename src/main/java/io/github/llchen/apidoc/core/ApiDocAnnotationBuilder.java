package io.github.llchen.apidoc.core;

import io.github.llchen.apidoc.annotation.ApiCode;
import io.github.llchen.apidoc.annotation.ApiDoc;
import io.github.llchen.apidoc.model.ApiCodeDefinition;
import io.github.llchen.apidoc.model.Document;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;


/**
 * @author chenliangliang
 * @date 2018/6/16
 */
public class ApiDocAnnotationBuilder implements ApiClassBuilder<ApiDoc> {


    @Override
    public Document build(Class<?> apiClass, Class<ApiDoc> annotationClass) {
        ApiDoc apiDoc = apiClass.getAnnotation(annotationClass);
        Document document=new Document();
        String name = apiDoc.name();
        if (StringUtils.isEmpty(name)){
            name=apiClass.getSimpleName();
        }
        document.setName(name);
        document.setBasePath(getApiBasePath(apiDoc,apiClass));
        String description = apiDoc.description();
        if (StringUtils.isEmpty(description)){
            //TODO:取注释
        }
        document.setDescription(description);

        //构建ApiCode
        ApiCode[] apiCodes = apiDoc.codes();
        List<ApiCodeDefinition> codeList = new LinkedList<>();
        for (ApiCode code : apiCodes) {
            codeList.add(new ApiCodeDefinition(code.code(), code.description()));
        }
        document.setCodeList(codeList);
        return document;
    }


    private String getApiBasePath(ApiDoc apiDoc, Class<?> apiDocClass) {
        String basePath = apiDoc.basePath();
        if (StringUtils.isNotEmpty(basePath)) {
            return basePath.startsWith("/") ? basePath : "/" + basePath;
        }

        RequestMapping rm = apiDocClass.getAnnotation(RequestMapping.class);
        if (rm != null) {
            String[] value = rm.value();
            return value[0];
        }

        return "";
    }
}
