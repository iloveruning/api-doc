package com.github.llchen.apidoc.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class Utils {

    private static ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    public static String requestMethod2Str(RequestMethod ... methods){
        StringBuilder sb=new StringBuilder("[");
        if (methods!=null&&methods.length>0) {
            for (RequestMethod method : methods) {
                sb.append(method.name()).append(",");
            }
            sb.deleteCharAt(sb.length()-1);
        }else {
            sb.append("GET,POST,PUT,DELETE");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String[] getParameterNames(Method method){
        return discoverer.getParameterNames(method);
    }
}
