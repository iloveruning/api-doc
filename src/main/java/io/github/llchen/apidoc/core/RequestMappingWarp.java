package io.github.llchen.apidoc.core;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class RequestMappingWarp {

    private String name;
    private String[] value;
    private String[] path;
    private RequestMethod[] method;
    private String[] params;
    private String[] headers;
    private String[] consumes;

    public RequestMappingWarp() {
    }

    public RequestMappingWarp(RequestMapping m){
        this(m.name(),m.value(),m.path(),m.method(),m.params(),m.headers(),m.consumes());
    }

    public RequestMappingWarp(String name, String[] value, String[] path, RequestMethod[] method, String[] params, String[] headers, String[] consumes) {
        this.name = name;
        this.value = value;
        this.path = path;
        this.method = method;
        this.params = params;
        this.headers = headers;
        this.consumes = consumes;
    }

    public RequestMappingWarp(GetMapping m){
        this(m.name(),m.value(),m.path(),new RequestMethod[]{RequestMethod.GET},m.params(),m.headers(),m.consumes());
    }

    public RequestMappingWarp(PostMapping m){
        this(m.name(),m.value(),m.path(),new RequestMethod[]{RequestMethod.POST},m.params(),m.headers(),m.consumes());
    }

    public RequestMappingWarp(PutMapping m){
        this(m.name(),m.value(),m.path(),new RequestMethod[]{RequestMethod.PUT},m.params(),m.headers(),m.consumes());

    }

    public RequestMappingWarp(DeleteMapping m){
        this(m.name(),m.value(),m.path(),new RequestMethod[]{RequestMethod.DELETE},m.params(),m.headers(),m.consumes());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public void setMethod(RequestMethod[] method) {
        this.method = method;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public void setConsumes(String[] consumes) {
        this.consumes = consumes;
    }

    public String getName() {
        return name;
    }

    public String[] getValue() {
        return value;
    }

    public String[] getPath() {
        return path;
    }

    public RequestMethod[] getMethod() {
        return method;
    }

    public String[] getParams() {
        return params;
    }

    public String[] getHeaders() {
        return headers;
    }

    public String[] getConsumes() {
        return consumes;
    }

    @Override
    public String toString() {
        return "RequestMappingWarp{" +
                "name='" + name + '\'' +
                ", value=" + Arrays.toString(value) +
                ", path=" + Arrays.toString(path) +
                ", method=" + Arrays.toString(method) +
                ", params=" + Arrays.toString(params) +
                ", headers=" + Arrays.toString(headers) +
                ", consumes=" + Arrays.toString(consumes) +
                '}';
    }
}
