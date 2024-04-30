package com.ai.boy.programming.client.impl;

import com.ai.boy.programming.client.LlmClient;
import com.ai.boy.programming.setting.AppSettingsState;
import com.intellij.openapi.components.Service;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;

/**
 * @author zhaoning
 * @date 2024/04/07
 * @desc
 */
@Service
public final class MoonshotClient implements LlmClient {

    private static final String CHAT_URL = "https://api.moonshot.cn";

    private static OpenAiChatClient chatClient;

    private static OpenAiChatClient getClient() {
        if (null == chatClient) {
            String apiKey = AppSettingsState.getInstance().getSavedApiKey();
            chatClient = new OpenAiChatClient(new OpenAiApi(CHAT_URL, apiKey));
        }
        return chatClient;
    }

    @Override
    public String chatCompletion(Object request) {
        ChatResponse response = getClient().call((Prompt) request);
        return null != response ? response.getResult().getOutput().getContent() : null;
    }
}
