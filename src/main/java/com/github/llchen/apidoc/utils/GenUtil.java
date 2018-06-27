package com.github.llchen.apidoc.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author chenliangliang
 * @date 2018/6/12
 */
public class GenUtil {

    public static String generateMarkdown(Map<String, Object> data, String template) {
        //初始化模板引擎
        Velocity.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init();
        //获取模板文件
        Template tmp = Velocity.getTemplate(template, "UTF-8");
        //设置变量
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }
        StringWriter writer = new StringWriter();
        tmp.merge(context, writer);
        return writer.toString();
    }


    public static void generateMarkdown(Map<String, Object> data,String template, OutputStream outputStream) {
        //初始化模板引擎
        Velocity.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init();
        //获取模板文件
        Template tmp = Velocity.getTemplate(template, "UTF-8");
        //设置变量
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            tmp.merge(context, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
