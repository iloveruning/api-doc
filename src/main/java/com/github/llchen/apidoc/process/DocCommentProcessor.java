package com.github.llchen.apidoc.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author llchen12
 * @date 2018/6/26
 */
@SupportedAnnotationTypes({"*"})
public class DocCommentProcessor extends AbstractProcessor {


    static final String METADATA_PATH = "META-INF/doc-comment.json";

    static final String RESTCONTROLLER_ANNOTATION = "org.springframework.web.bind.annotation.RestController";

    static final String CONTROLLER_ANNOTATION = "org.springframework.stereotype.Controller";

    static final String REQUESTMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.RequestMapping";

    static final String GETMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.GetMapping";

    static final String POSTMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.PostMapping";

    static final String PUTMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.PutMapping";

    static final String DELETEMAPPING_ANNOTATION = "org.springframework.web.bind.annotation.DeleteMapping";

    //private static final Map<String, Class<?>> ANNOTATION_CLASS_MAP = new HashMap<>(16);

    private Elements elementUtils;
    private Messager messager;
    private Filer filer;

    private Map<String, AnnotatedClass> docClassMap = new HashMap<>(64);

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<Element> docClassSet = new HashSet<>(64);
        TypeElement restControllerEle = elementUtils.getTypeElement(RESTCONTROLLER_ANNOTATION);
        if (restControllerEle != null) {
            docClassSet.addAll(roundEnv.getElementsAnnotatedWith(restControllerEle));
        }
        TypeElement controllerEle = elementUtils.getTypeElement(CONTROLLER_ANNOTATION);
        if (controllerEle != null) {
            docClassSet.addAll(roundEnv.getElementsAnnotatedWith(controllerEle));
        }

        processDocClasses(docClassSet);

        Set<Element> docMethodSet = new HashSet<>(128);
        TypeElement requestMappingEle = elementUtils.getTypeElement(REQUESTMAPPING_ANNOTATION);
        if (requestMappingEle != null) {
            docMethodSet.addAll(roundEnv.getElementsAnnotatedWith(requestMappingEle));
        }
        TypeElement getMappingEle = elementUtils.getTypeElement(GETMAPPING_ANNOTATION);
        if (requestMappingEle != null) {
            docMethodSet.addAll(roundEnv.getElementsAnnotatedWith(getMappingEle));
        }
        TypeElement postMappingEle = elementUtils.getTypeElement(POSTMAPPING_ANNOTATION);
        if (requestMappingEle != null) {
            docMethodSet.addAll(roundEnv.getElementsAnnotatedWith(postMappingEle));
        }
        TypeElement putMappingEle = elementUtils.getTypeElement(PUTMAPPING_ANNOTATION);
        if (requestMappingEle != null) {
            docMethodSet.addAll(roundEnv.getElementsAnnotatedWith(putMappingEle));
        }
        TypeElement deleteMappingEle = elementUtils.getTypeElement(DELETEMAPPING_ANNOTATION);
        if (requestMappingEle != null) {
            docMethodSet.addAll(roundEnv.getElementsAnnotatedWith(deleteMappingEle));
        }

        processDocMethods(docMethodSet);

        if (roundEnv.processingOver()) {
            try {
                writeData();
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.WARNING, "Failed to write metadata " + e.getMessage());
            }
        }

        return false;
    }

    private void processDocMethods(Set<Element> docMethodSet) {
        for (Element ele : docMethodSet) {
            if (ele.getKind() != ElementKind.METHOD) {
                continue;
            }
            AnnotatedMethod method = new AnnotatedMethod((ExecutableElement) ele, elementUtils.getDocComment(ele));
            String className = method.getClassName();
            AnnotatedClass aClass = docClassMap.get(className);
            if (aClass == null) {
                Element parent = ele.getEnclosingElement();
                aClass = new AnnotatedClass(className, elementUtils.getDocComment(parent));
                aClass.addMethod(method);
                docClassMap.put(className, aClass);
            } else {
                aClass.addMethod(method);
            }
        }
    }

    private void processDocClasses(Set<Element> docClassSet) {
        for (Element ele : docClassSet) {
            if (ele.getKind() != ElementKind.CLASS) {
                continue;
            }
            String className = ((TypeElement) ele).getQualifiedName().toString();
            if (!docClassMap.containsKey(className)) {
                AnnotatedClass annotatedClass = new AnnotatedClass(className, elementUtils.getDocComment(ele));
                docClassMap.put(className, annotatedClass);
            }
        }
    }


    private void writeData() throws IOException {
        if (docClassMap.isEmpty()) {
            return;
        }
        FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", METADATA_PATH);
        try (OutputStream outputStream = resource.openOutputStream()) {
            String json = JSON.toJSONString(docClassMap, SerializerFeature.PrettyFormat,
                    SerializerFeature.SkipTransientField);
            outputStream.write(json.getBytes("UTF-8"));
        }
    }

//    private void processMethod(ExecutableElement ele) {
//        if (hasAnyAnnotation(ele, REQUESTMAPPING_ANNOTATION, GETMAPPING_ANNOTATION, POSTMAPPING_ANNOTATION,
//                DELETEMAPPING_ANNOTATION, PUTMAPPING_ANNOTATION)) {
//
//        }
//    }
//
//    private void processClass(TypeElement ele) {
//        //处理@Controller和@RestController注解
//        if (hasAnyAnnotation(ele, CONTROLLER_ANNOTATION, RESTCONTROLLER_ANNOTATION)) {
//
//        }
//    }

//    protected boolean hasAnnotation(Element e, String annotationClassName) {
//        Class<Annotation> annotation = getAnnotationClass(annotationClassName);
//        return annotation != null && e.getAnnotation(annotation) != null;
//    }
//
//    protected boolean hasAnyAnnotation(Element e, String... annotationClassNames) {
//        if (annotationClassNames == null || annotationClassNames.length == 0) {
//            return false;
//        }
//
//        for (String name : annotationClassNames) {
//            if (hasAnnotation(e, name)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @SuppressWarnings("unchecked")
//    protected Class<Annotation> getAnnotationClass(String annotationClassName) {
//        Class<?> aClass = ANNOTATION_CLASS_MAP.get(annotationClassName);
//        if (aClass == null) {
//            try {
//                aClass = Class.forName(annotationClassName);
//                ANNOTATION_CLASS_MAP.put(annotationClassName, aClass);
//            } catch (ClassNotFoundException e) {
//                messager.printMessage(Diagnostic.Kind.WARNING, annotationClassName + "不存在");
//                return null;
//            }
//        }
//        return (Class<Annotation>) aClass;
//    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
