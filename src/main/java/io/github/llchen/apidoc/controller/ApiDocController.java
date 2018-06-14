package io.github.llchen.apidoc.controller;


import io.github.llchen.apidoc.core.ApiDocContext;
import io.github.llchen.apidoc.model.ApiModelDefinition;
import io.github.llchen.apidoc.model.Document;
import io.github.llchen.apidoc.utils.GenUtil;
import io.github.llchen.apidoc.utils.MarkdownEntity;
import io.github.llchen.apidoc.utils.MarkdownUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author llchen12
 * @date 2018/6/12
 */
@Controller
@RequestMapping("/api")
public class ApiDocController {


    @ResponseBody
    @GetMapping("/doc")
    public Map<String, Object> getDoc() {

        HashMap<String, Document> docMap = ApiDocContext.getDocMap();
        HashMap<String, ApiModelDefinition> modelMap = ApiDocContext.getModelMap();
        Map<String, Object> data = new HashMap<>(4);
        data.put("apiList", docMap.values());
        data.put("modelList", modelMap.values());
        return data;
    }


    @GetMapping("/markdown")
    public void docToMarkdown(HttpServletResponse response) throws IOException {
        HashMap<String, Document> docMap = ApiDocContext.getDocMap();
        HashMap<String, ApiModelDefinition> modelMap = ApiDocContext.getModelMap();
        Map<String, Object> data = new HashMap<>(4);
        data.put("apiList", docMap.values());
        data.put("modelList", modelMap.values());
        // 设置响应头，控制浏览器下载该文件

        response.setHeader("content-disposition", "attachment;filename=api-doc.md");
        GenUtil.generateMarkdown(data, "md/doc.md.vm", response.getOutputStream());
    }


    @GetMapping("/html")
    public void preLook(HttpServletResponse response) throws IOException {
        HashMap<String, Document> docMap = ApiDocContext.getDocMap();
        HashMap<String, ApiModelDefinition> modelMap = ApiDocContext.getModelMap();
        Map<String, Object> data = new HashMap<>(4);
        data.put("apiList", docMap.values());
        data.put("modelList", modelMap.values());

        response.setHeader("Content-Type", "text/html");
        response.setHeader("Keep-Alive", "timeout=30, max=100");
        String markdown = GenUtil.generateMarkdown(data, "md/doc.md.vm");
        MarkdownEntity entity = MarkdownUtil.ofContent(markdown);
        FileCopyUtils.copy(entity.getHtml().getBytes(), response.getOutputStream());
    }


}
