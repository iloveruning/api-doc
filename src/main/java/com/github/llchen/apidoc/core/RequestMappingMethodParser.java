package com.github.llchen.apidoc.core;

import com.github.llchen.apidoc.DataType;
import com.github.llchen.apidoc.model.ApiDefinition;
import com.github.llchen.apidoc.model.ApiParamDefinition;
import com.github.llchen.apidoc.utils.Utils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class RequestMappingMethodParser extends AbstractParser<ApiDefinition,Method> {

    @Override
    public int order() {
        return 1;
    }

    @Override
    public ApiDefinition parse(Method method) {
        RequestMappingWarp warp;
        RequestMapping rm = method.getAnnotation(RequestMapping.class);
        if (rm!=null){
            warp=new RequestMappingWarp(rm);
            return parse2Obj(warp);
        }

        GetMapping gm = method.getAnnotation(GetMapping.class);
        if (gm!=null){
            warp=new RequestMappingWarp(gm);
            return parse2Obj(warp);
        }

        PostMapping pm = method.getAnnotation(PostMapping.class);
        if (pm!=null){
            warp=new RequestMappingWarp(pm);
            return parse2Obj(warp);
        }

        DeleteMapping dm = method.getAnnotation(DeleteMapping.class);
        if (dm!=null){
            warp=new RequestMappingWarp(dm);
            return parse2Obj(warp);
        }

        PutMapping ptm = method.getAnnotation(PutMapping.class);
        if (ptm!=null){
            warp=new RequestMappingWarp(ptm);
            return parse2Obj(warp);
        }

        return new ApiDefinition();
    }

    private ApiDefinition parse2Obj(RequestMappingWarp warp){
        ApiDefinition apiDefinition=new ApiDefinition();
        apiDefinition.setPath(getPath(warp));
        apiDefinition.setRequestParams(buildParam(warp.getParams()));
        apiDefinition.setHttpMethod(Utils.requestMethod2Str(warp.getMethod()));
        apiDefinition.setName(warp.getName());
        return apiDefinition;
    }

    private List<ApiParamDefinition> buildParam(String[] params) {
        List<ApiParamDefinition> paramList=new LinkedList<>();
        for (String param:params){
            ApiParamDefinition paramDefinition=new ApiParamDefinition();
            paramDefinition.setName(param);
            paramDefinition.setType(DataType.STRING.getValue());
            paramDefinition.setRequired("是");
            paramList.add(paramDefinition);
        }
        return paramList;
    }


    private String getPath(RequestMappingWarp warp){
        String[] value = warp.getValue();
        if (value.length>0){
            return Arrays.toString(value);
        }

        String[] path = warp.getPath();
        if (path.length>0){
            return Arrays.toString(path);
        }

        return "";
    }
}
