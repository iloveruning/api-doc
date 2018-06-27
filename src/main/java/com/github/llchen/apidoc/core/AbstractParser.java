package com.github.llchen.apidoc.core;

import com.github.llchen.apidoc.process.CommentParse;

import java.net.URL;

/**
 * @author llchen12
 * @date 2018/6/27
 */
public abstract class AbstractParser<R,T> implements Parser<R,T> {


    private static volatile CommentParse commentParse;
    private static final Object LOCK = new Object();
    private static final String DOC_COMMENT_LOCATION = "/META-INF/doc-comment.json";


    abstract int order();

    protected CommentParse getCommentParse() {
        if (commentParse == null) {
            synchronized (LOCK) {
                if (commentParse == null) {
                    URL resource = this.getClass().getResource(DOC_COMMENT_LOCATION);
                    if (resource == null) {
                        throw new RuntimeException(DOC_COMMENT_LOCATION + " 不存在");
                    }
                    commentParse = CommentParse.load(resource.getPath());
                }
            }
        }
        return commentParse;
    }

}
