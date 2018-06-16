package io.github.llchen.apidoc.core;

import com.alibaba.fastjson.JSON;
import io.github.llchen.apidoc.DataType;
import io.github.llchen.apidoc.annotation.*;
import io.github.llchen.apidoc.config.ApiDocProperties;
import io.github.llchen.apidoc.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author llchen12
 * @date 2018/6/11
 */
public class ApiDocBuilder {

    private ConcurrentHashMap<String, Document> docMap = new ConcurrentHashMap<>(16);

    private ConcurrentHashMap<String, ApiModelDefinition> modelMap = new ConcurrentHashMap<>(32);

    private ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();


    public void addApiDoc(ApiDoc apiDoc, Class<?> apiDocClass, Object apiDocObject, ApiDocProperties properties) {
        Document document = buildApiDoc(apiDoc, apiDocClass, properties);
        Method[] methods = apiDocClass.getDeclaredMethods();
        List<ApiDefinition> apiList = new LinkedList<>();
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            Api api = method.getAnnotation(Api.class);
            if (api == null) {
                continue;
            }
            apiList.add(buildApi(api, method, document.getBasePath()));
        }
        document.setApiList(apiList);
        docMap.put(document.getName(), document);
    }


    public void addApiDoc(Controller controller, Class<?> apiDocClass, Object apiDocObject, ApiDocProperties properties) {
        Document document = buildApiDoc(apiDoc, apiDocClass, properties);
        Method[] methods = apiDocClass.getDeclaredMethods();
        List<ApiDefinition> apiList = new LinkedList<>();
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            Api api = method.getAnnotation(Api.class);
            if (api == null) {
                continue;
            }
            apiList.add(buildApi(api, method, document.getBasePath()));
        }
        document.setApiList(apiList);
        docMap.put(document.getName(), document);
    }

    public void addApiDoc(RestController apiDoc, Class<?> apiDocClass, Object apiDocObject, ApiDocProperties properties) {
        Document document = buildApiDoc(apiDoc, apiDocClass, properties);
        Method[] methods = apiDocClass.getDeclaredMethods();
        List<ApiDefinition> apiList = new LinkedList<>();
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            Api api = method.getAnnotation(Api.class);
            if (api == null) {
                continue;
            }
            apiList.add(buildApi(api, method, document.getBasePath()));
        }
        document.setApiList(apiList);
        docMap.put(document.getName(), document);
    }

    private Document buildApiDoc(ApiDoc apiDoc, Class<?> apiDocClass, ApiDocProperties properties) {

        String docName = apiDoc.name();
        Document document = new Document();
        document.setBasePath(getApiBasePath(apiDoc, apiDocClass));
        document.setDescription(apiDoc.description());
        document.setName(docName);
        String version = apiDoc.version();
        if (StringUtils.isEmpty(version)) {
            version = properties.getVersion();
        }
        document.setVersion(version);
        document.setContact(properties.getContact());
        document.setHost(properties.getHost());

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


    private String getApiUrl(Api api, Method method, String basePath) {
        String path = api.path();
        if (StringUtils.isNotEmpty(path)) {
            return basePath + (path.startsWith("/") ? path : "/" + path);
        }

        RequestMapping rm = method.getAnnotation(RequestMapping.class);
        if (rm != null) {
            return buildUrl(basePath, rm.value());
        }

        GetMapping gm = method.getAnnotation(GetMapping.class);
        if (gm != null) {
            return buildUrl(basePath, gm.value());
        }

        PostMapping pm = method.getAnnotation(PostMapping.class);
        if (pm != null) {
            return buildUrl(basePath, pm.value());
        }

        DeleteMapping dm = method.getAnnotation(DeleteMapping.class);
        if (dm != null) {
            return buildUrl(basePath, dm.value());
        }

        PutMapping ptm = method.getAnnotation(PutMapping.class);
        if (ptm != null) {
            return buildUrl(basePath, ptm.value());
        }
        return basePath;
    }


    private String getApiHttpMethod(Api api, Method method) {
        String[] httpMethods = api.httpMethod();
        if (httpMethods.length > 0) {
            return Arrays.toString(httpMethods);
        }

        RequestMapping rm = method.getAnnotation(RequestMapping.class);
        if (rm != null) {
            return buildHttpMethod(rm.method());
        }

        GetMapping gm = method.getAnnotation(GetMapping.class);
        if (gm != null) {
            return buildHttpMethod(RequestMethod.GET);
        }

        PostMapping pm = method.getAnnotation(PostMapping.class);
        if (pm != null) {
            return buildHttpMethod(RequestMethod.POST);
        }

        DeleteMapping dm = method.getAnnotation(DeleteMapping.class);
        if (dm != null) {
            return buildHttpMethod(RequestMethod.DELETE);
        }

        PutMapping ptm = method.getAnnotation(PutMapping.class);
        if (ptm != null) {
            return buildHttpMethod(RequestMethod.PUT);
        }
        return "[GET,POST,PUT,DELETE]";
    }

    private String buildHttpMethod(RequestMethod... methods) {
        int len = methods.length;
        String[] httpMethods = new String[len];

        for (int i = 0; i < len; i++) {
            httpMethods[i] = methods[i].name();
        }
        return Arrays.toString(httpMethods);
    }

    private String buildUrl(String basePath, String[] paths) {
        StringBuilder url = new StringBuilder();
        for (String p : paths) {
            url.append(basePath)
                    .append(p.startsWith("/") ? p : "/" + p)
                    .append(",");

        }
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }

    private String getApiName(Api api, Method method) {
        String name = api.name();
        if (StringUtils.isNotEmpty(name)) {
            return name;
        }
        return method.getName();
    }

    private ApiDefinition buildApi(Api api, Method method, String basePath) {
        ApiDefinition apiDefinition = new ApiDefinition();
        apiDefinition.setName(getApiName(api, method));
        apiDefinition.setPath(getApiUrl(api, method, basePath));
        apiDefinition.setRemark(api.remark());
        apiDefinition.setHttpMethod(getApiHttpMethod(api, method));
        apiDefinition.setDescription(api.description());
        String demoResponse = api.demoResponse();
        String responseExample;
        try {
            responseExample = JSON.toJSONString(JSON.parseObject(demoResponse), true);
        } catch (Exception e) {
            responseExample = demoResponse;
        }
        apiDefinition.setDemoResponse(responseExample);

        //构建requestParams
        ApiParam[] requestParams = api.requestParams();
        List<ApiParamDefinition> requestParamList = buildRequestApiParams(requestParams, method);
        apiDefinition.setRequestParams(requestParamList);

        //构建responseParams
        ApiParam[] responseParams = api.responseParams();
        List<ApiParamDefinition> responseParamList = buildResponseApiParams(responseParams, method);
        apiDefinition.setResponseParams(responseParamList);

        //构建model
        Class<?> resultModel = api.resultModel();
        if (Void.class.equals(resultModel)) {
            resultModel = method.getReturnType();
        }
        String key = resultModel.getName();
        if (modelMap.containsKey(key)) {
            apiDefinition.setResultModel(modelMap.get(key));
        }

        return apiDefinition;
    }

    private List<ApiParamDefinition> buildResponseApiParams(ApiParam[] apiParams, Method method) {
        List<ApiParamDefinition> apiParamList = new LinkedList<>();
        ApiParamDefinition apiParamDefinition;
        Class<?> returnType = method.getReturnType();
        if (modelMap.containsKey(returnType.getName())) {
            String returnTypeName = returnType.getSimpleName();
            apiParamDefinition = new ApiParamDefinition();
            apiParamDefinition.setType(DataType.MODEL.getValue());
            apiParamDefinition.setDescription("详细信息请参见数据字典-" + returnTypeName);
            apiParamDefinition.setName(returnTypeName.toLowerCase());
            apiParamDefinition.setRequired("是");
            apiParamDefinition.setExample("");
            apiParamDefinition.setDefaultValue("");
            apiParamList.add(apiParamDefinition);
            return apiParamList;
        }
        if (apiParams.length > 0) {
            for (ApiParam apiParam : apiParams) {
                apiParamDefinition = new ApiParamDefinition();
                apiParamDefinition.setName(apiParam.name());
                apiParamDefinition.setDescription(apiParam.description());
                apiParamDefinition.setExample(apiParam.example());
                apiParamDefinition.setRequired(apiParam.required() ? "是" : "否");
                apiParamDefinition.setType(apiParam.type().getValue());
                apiParamDefinition.setDefaultValue(apiParam.defaultValue());
                apiParamList.add(apiParamDefinition);
            }
        } else {
            //反射获取
            Field[] fields = returnType.getDeclaredFields();
            for (Field field : fields) {
                apiParamDefinition = new ApiParamDefinition();
                apiParamDefinition.setType(DataType.getType(field.getType()));
                apiParamDefinition.setName(field.getName());
                apiParamDefinition.setRequired("是");
                apiParamDefinition.setExample("");
                apiParamDefinition.setDescription("");
                apiParamDefinition.setDefaultValue("");
                apiParamList.add(apiParamDefinition);
            }
        }
        return apiParamList;

    }

    private List<ApiParamDefinition> buildRequestApiParams(ApiParam[] apiParams, Method method) {
        List<ApiParamDefinition> apiParamList = new LinkedList<>();
        ApiParamDefinition apiParamDefinition;
        if (apiParams.length > 0) {
            for (ApiParam apiParam : apiParams) {
                apiParamDefinition = new ApiParamDefinition();
                apiParamDefinition.setName(apiParam.name());
                apiParamDefinition.setDescription(apiParam.description());
                apiParamDefinition.setExample(apiParam.example());
                apiParamDefinition.setRequired(apiParam.required() ? "是" : "否");
                apiParamDefinition.setType(apiParam.type().getValue());
                apiParamDefinition.setDefaultValue(apiParam.defaultValue());
                apiParamList.add(apiParamDefinition);
            }
        } else {
            String[] parameterNames = discoverer.getParameterNames(method);
            Parameter[] parameters = method.getParameters();
            for (int i = 0, l = parameterNames.length; i < l; i++) {
                Parameter parameter = parameters[i];
                apiParamDefinition = new ApiParamDefinition();
                apiParamDefinition.setName(parameterNames[i]);
                apiParamDefinition.setRequired("是");
                apiParamDefinition.setExample("");
                apiParamDefinition.setDescription("");
                apiParamDefinition.setDefaultValue("");
                Class<?> parameterType = parameter.getType();
                if (String.class.equals(parameterType)) {
                    apiParamDefinition.setType(DataType.STRING.getValue());
                } else if (Integer.class.equals(parameterType)) {
                    apiParamDefinition.setType(DataType.INT.getValue());
                } else if (Long.class.equals(parameterType)) {
                    apiParamDefinition.setType(DataType.LONG.getValue());
                } else {
                    String key = parameterType.getName();
                    if (modelMap.containsKey(key)) {
                        //apiParamDefinition.setModel(modelMap.get(key));
                        apiParamDefinition.setDescription("详细信息请参见数据字典-" + parameterType.getSimpleName());
                        apiParamDefinition.setType(DataType.MODEL.getValue());
                    } else {
                        apiParamDefinition.setType(DataType.REFERENCE.getValue());
                        apiParamDefinition.setModel(buildReferenceModel(parameterType));
                    }
                }
                apiParamList.add(apiParamDefinition);
            }
        }
        return apiParamList;
    }


    private ApiModelDefinition buildReferenceModel(Class<?> referenceClass) {
        ApiModelDefinition apiModelDefinition = new ApiModelDefinition();
        apiModelDefinition.setRemark("");
        apiModelDefinition.setDescription("");

        Field[] fields = referenceClass.getDeclaredFields();
        List<ApiModelPropertyDefinition> propertyList = new LinkedList<>();
        ApiModelPropertyDefinition propertyDefinition;
        for (Field field : fields) {
            propertyDefinition = new ApiModelPropertyDefinition();
            propertyDefinition.setType(DataType.getType(field.getType()));
            propertyDefinition.setName(field.getName());
            propertyDefinition.setRequired("是");
            propertyDefinition.setExample("");
            propertyDefinition.setDescription("");
            propertyDefinition.setDefaultValue("");
            propertyList.add(propertyDefinition);
        }
        apiModelDefinition.setPropertyList(propertyList);
        return apiModelDefinition;
    }

    public void addModel(ApiModel apiModel, Class<?> modelClass) {
        ApiModelDefinition apiModelDefinition = new ApiModelDefinition();
        apiModelDefinition.setName(modelClass.getSimpleName());
        apiModelDefinition.setDescription(apiModel.description());
        apiModelDefinition.setRemark(apiModel.remark());
        Field[] fields = modelClass.getDeclaredFields();
        List<ApiModelPropertyDefinition> propertyList = new LinkedList<>();
        for (Field field : fields) {
            ApiModelProperty modelProperty = field.getAnnotation(ApiModelProperty.class);
            if (modelProperty == null) {
                continue;
            }
            ApiModelPropertyDefinition propertyDefinition = new ApiModelPropertyDefinition();
            propertyDefinition.setDescription(modelProperty.description());
            propertyDefinition.setExample(modelProperty.example());
            propertyDefinition.setName(modelProperty.name());
            propertyDefinition.setRequired(modelProperty.required() ? "是" : "否");
            propertyDefinition.setType(modelProperty.type().getValue());
            propertyDefinition.setDefaultValue(modelProperty.defaultValue());
            propertyList.add(propertyDefinition);
        }
        apiModelDefinition.setPropertyList(propertyList);
        modelMap.put(modelClass.getName(), apiModelDefinition);
    }

    public HashMap<String, Document> getDocMap() {
        return new HashMap<>(docMap);
    }

    public HashMap<String, ApiModelDefinition> getModelMap() {
        return new HashMap<>(modelMap);
    }
}
