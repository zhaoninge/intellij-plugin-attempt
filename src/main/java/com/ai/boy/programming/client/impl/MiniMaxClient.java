package com.ai.boy.programming.client.impl;

import com.ai.boy.programming.client.LlmClient;
import com.ai.boy.programming.client.OkHttpClient;
import com.ai.boy.programming.setting.AppSettingsState;
import com.intellij.openapi.components.Service;
import com.zhipu.oapi.service.v4.model.ModelData;

import static org.springframework.ai.model.ModelOptionsUtils.jsonToObject;
import static org.springframework.ai.model.ModelOptionsUtils.toJsonString;

/**
 * @author zhaoning
 * @date 2024/04/09
 * @desc
 */
@Service
public final class MiniMaxClient implements LlmClient {

    private static final String URL = "https://api.minimax.chat/v1/text/chatcompletion_v2";

    @Override
    public String chatCompletion(Object request) {
        String apiKey = AppSettingsState.getInstance().getSavedApiKey();

        String response = OkHttpClient.postCompletion(URL, apiKey, toJsonString(request));
        ModelData modelData = jsonToObject(response, ModelData.class);

        return null != modelData
                ? modelData.getChoices().get(0).getMessage().getContent().toString()
                : null;
    }
}
