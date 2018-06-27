package com.github.llchen.apidoc.process;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.beans.Transient;
import java.util.List;

/**
 * @author llchen12
 * @date 2018/6/26
 */
public class AnnotatedMethod {

    private String className;
    private String methodName;
    private String doc;
    private ExecutableElement executableElement;

    public AnnotatedMethod(ExecutableElement executableElement, String doc) {
        this.executableElement = executableElement;
        this.methodName = generateMethodName(executableElement);
        this.className = ((TypeElement) executableElement.getEnclosingElement()).getQualifiedName().toString();
        this.doc = doc;
    }

    public AnnotatedMethod() {
    }

    private String generateMethodName(ExecutableElement executableElement) {
        StringBuilder res = new StringBuilder();
        res.append(executableElement.getSimpleName().toString()).append("(");
        List<? extends VariableElement> parameters = executableElement.getParameters();
        for (VariableElement ele : parameters) {
            res.append(ele).append(",");
        }
        if (res.charAt(res.length()-1)==','){
            res.deleteCharAt(res.length() - 1);
        }
        res.append(")");
        return res.toString();
    }

    @Transient
    public String getClassName() {
        return className;
    }



    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    @Transient
    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public void setExecutableElement(ExecutableElement executableElement) {
        this.executableElement = executableElement;
    }

    @Override
    public String toString() {
        return "AnnotatedMethod{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", doc='" + doc + '\'' +
                '}';
    }
}
