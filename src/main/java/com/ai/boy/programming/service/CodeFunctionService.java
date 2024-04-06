package com.ai.boy.programming.service;

import com.ai.boy.programming.rpc.OkHttpClientUtils;
import com.ai.boy.programming.rpc.builder.ChatCompletionRequestBuilder;
import com.ai.boy.programming.rpc.dto.ChatCompletionRequest;
import com.ai.boy.programming.rpc.dto.ChatCompletionResponse;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import org.apache.commons.lang3.StringUtils;

import static cn.hutool.json.JSONUtil.toBean;
import static cn.hutool.json.JSONUtil.toJsonStr;

/**
 * @author zhaoning
 * @date 2024/04/03
 * @desc
 */
@Service
public final class CodeFunctionService {

    public static CodeFunctionService getInstance() {
        return ApplicationManager.getApplication().getService(CodeFunctionService.class);
    }

    public String coding(String text, String apiKey) {
        ChatCompletionRequest request = ChatCompletionRequestBuilder.buildChatCompletionRequest(text);

        String response = OkHttpClientUtils.postCompletion(toJsonStr(request), apiKey);

        ChatCompletionResponse completionResponse = toBean(response, ChatCompletionResponse.class);
        String content = completionResponse.getChoices().get(0).getMessage().getContent();

        return removeMarkDown(content);
    }

    private String removeMarkDown(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        return text.replace("```java", "")
                .replace("```", "");
    }
}
