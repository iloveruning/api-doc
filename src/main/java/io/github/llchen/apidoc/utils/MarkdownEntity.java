package io.github.llchen.apidoc.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author llchen12
 * @date 2018/6/12
 */
public class MarkdownEntity {

    public static String CSS_HREF = "<link type=\"text/css\" rel=\"stylesheet\" href=\"%s\">";

    /**
     * css样式
     */
    private String css;

    /**
     * 最外网的div标签， 可以用来设置样式，宽高，字体等
     */
    private Map<String, String> divStyle = new HashMap<>(8);

    /**
     * markdown内容
     */
    private String htmlBody;

    /**
     * 标题
     */
    private String title;

    public MarkdownEntity() {
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public String getTitle() {
        return title;
    }

    public String getHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>")
                .append("</title>").append("<style type=\"text/css\">").append(css).append("</style>")
                .append("</head><body>");
        if (!divStyle.isEmpty()) {
            html.append("<div ").append(parseDiv())
                    .append(">").append(htmlBody).append("</div>");
        } else {
            html.append(htmlBody);
        }
        html.append("</body></html>");
        return html.toString();
    }

    private String parseDiv() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : divStyle.entrySet()) {
            builder.append(entry.getKey()).append("=\"")
                    .append(entry.getValue()).append("\" ");
        }
        return builder.toString();
    }


    public void addDivStyle(String attrKey, String value) {
        if (divStyle.containsKey(attrKey)) {
            divStyle.put(attrKey, divStyle.get(attrKey) + " " + value);
        } else {
            divStyle.put(attrKey, value);
        }
    }

//    public void addCss(String cssHref) {
//        css += String.format(CSS_HREF, cssHref);
//    }

}
