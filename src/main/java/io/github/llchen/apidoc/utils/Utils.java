package io.github.llchen.apidoc.utils;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class Utils {


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
}
