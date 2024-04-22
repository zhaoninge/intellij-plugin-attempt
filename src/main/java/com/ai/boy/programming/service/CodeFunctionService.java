package com.ai.boy.programming.service;

import com.ai.boy.programming.factory.LlmClientFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.SlowOperations;

import static com.ai.boy.programming.factory.ChatCompletionRequestFactory.buildChatCompletionRequest;
import static com.ai.boy.programming.util.StringUtils.removeCodeBlockMarker;
import static com.ai.boy.programming.util.StringUtils.removeLeadingAndTrailingLineBreaks;

/**
 * @author zhaoning
 * @date 2024/04/03
 * @desc
 */
@Service
public final class CodeFunctionService {

    private static final Logger LOGGER = Logger.getInstance(CodeFunctionService.class);

    public static CodeFunctionService getInstance() {
        return ApplicationManager.getApplication().getService(CodeFunctionService.class);
    }

    public String coding(String text) {
        Object request = buildChatCompletionRequest(text);

        String content = LlmClientFactory.getClient().chatCompletion(request);

        return removeLeadingAndTrailingLineBreaks(removeCodeBlockMarker(content));
    }
}
