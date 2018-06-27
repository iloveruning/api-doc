package com.github.llchen.apidoc.process;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author llchen12
 * @date 2018/6/26
 */
public class Comment {

    private String comment = "";
    private Map<String, String> params = new HashMap<>();
    private String ret;
    private String demoResponse;


    public Comment(String comment, String ret, String demoResponse) {
        this.comment = comment;
        this.ret = ret;
        this.demoResponse = demoResponse;
    }

    public Comment() {

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }

    public void addParam(String paramName, String paramDesc) {
        this.params.put(paramName, paramDesc);
    }


    public String getParam(String paramName) {
        return this.params.get(paramName);
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getDemoResponse() {
        return demoResponse;
    }

    public void setDemoResponse(String demoResponse) {
        this.demoResponse = demoResponse;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", params=" + params +
                ", ret='" + ret + '\'' +
                ", demoResponse='" + demoResponse + '\'' +
                '}';
    }
}
