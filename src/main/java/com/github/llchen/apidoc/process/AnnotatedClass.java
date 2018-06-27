package com.github.llchen.apidoc.process;

import javax.lang.model.element.TypeElement;
import java.util.LinkedList;
import java.util.List;

/**
 * @author llchen12
 * @date 2018/6/26
 */
public class AnnotatedClass {

    private String className;

    private String doc;

    private List<AnnotatedMethod> methods=new LinkedList<>();

    public AnnotatedClass() {
    }

    public AnnotatedClass(TypeElement typeElement, String doc){
        this.className=typeElement.getQualifiedName().toString();
        this.doc=doc;
    }

    public AnnotatedClass(String className,String doc){
        this.className=className;
        this.doc=doc;
    }

    public void addMethod(AnnotatedMethod method){
        this.methods.add(method);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public List<AnnotatedMethod> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        return "AnnotatedClass{" +
                "className='" + className + '\'' +
                ", doc='" + doc + '\'' +
                ", methods=" + methods +
                '}';
    }
}
