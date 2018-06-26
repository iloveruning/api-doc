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
 * @date 2018/6/17
 */
public class ApiDocClassParser extends AbstractClassParser<ApiDoc, Document> {


    public ApiDocClassParser() {
        super(ApiDoc.class);
    }


    @Override
    protected Document parse2Obj(Class<?> clazz, ApiDoc annotation) {
        Document document = new Document();
        List<ApiCodeDefinition> codeList = new LinkedList<>();
        if (annotation == null) {
            //TODO:取注释，取className
            document.setName(clazz.getSimpleName());
            document.setDescription("");

        } else {
            //构建ApiCode
            ApiCode[] apiCodes = annotation.codes();
            for (ApiCode code : apiCodes) {
                codeList.add(new ApiCodeDefinition(code.code(), code.description()));
            }
        }
        document.setCodeList(codeList);
        document.setBasePath(getBasePath(annotation,clazz));
        return document;
    }



    private String getBasePath(ApiDoc apiDoc, Class<?> apiDocClass) {
        if (apiDoc!=null){
            String basePath = apiDoc.basePath();
            if (StringUtils.isNotEmpty(basePath)) {
                return basePath.startsWith("/") ? basePath : "/" + basePath;
            }
        }else {
            RequestMapping rm = apiDocClass.getAnnotation(RequestMapping.class);
            if (rm != null) {
                String[] value = rm.value();
                return value[0];
            }
        }
        return "";
    }
}
