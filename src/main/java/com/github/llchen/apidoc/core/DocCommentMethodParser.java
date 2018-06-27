package com.github.llchen.apidoc.core;

import com.github.llchen.apidoc.model.ApiDefinition;
import com.github.llchen.apidoc.model.ApiModelDefinition;
import com.github.llchen.apidoc.model.ApiParamDefinition;
import com.github.llchen.apidoc.process.Comment;
import com.github.llchen.apidoc.process.CommentParse;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author chenliangliang
 * @date 2018/6/17
 */
public class DocCommentMethodParser implements MethodParser<ApiDefinition> {


    private CommentParse commentParse;

    public DocCommentMethodParser(CommentParse commentParse) {
        this.commentParse = commentParse;
    }

    @Override
    public int order() {
        return 3;
    }

    @Override
    public ApiDefinition parse(Method method) {
        ApiDefinition apiDefinition = new ApiDefinition();
        Comment methodComment = commentParse.getMethodComment(method);
        apiDefinition.setDescription(methodComment.getComment());

        Map<String, String> params = methodComment.getParams();

        List<ApiParamDefinition> requestParams = buildRequestParams(params);
        apiDefinition.setRequestParams(requestParams);

        String ret = methodComment.getRet();
        if (StringUtils.isNotEmpty(ret)){
            ApiModelDefinition modelDefinition=new ApiModelDefinition();
            modelDefinition.setRemark(ret);
            apiDefinition.setResultModel(modelDefinition);
        }

        return apiDefinition;
    }

    private List<ApiParamDefinition> buildRequestParams(Map<String, String> params) {
        List<ApiParamDefinition> requestParams = new LinkedList<>();
        ApiParamDefinition paramDefinition;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramDefinition = new ApiParamDefinition();
            paramDefinition.setName(entry.getKey());
            paramDefinition.setDescription(entry.getValue());
            requestParams.add(paramDefinition);
        }
        return requestParams;
    }


}
