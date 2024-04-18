package com.ai.boy.programming.client.impl;

import com.ai.boy.programming.client.LlmClient;
import com.ai.boy.programming.setting.AppSettingsState;
import com.intellij.openapi.components.Service;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;

/**
 * @author zhaoning
 * @date 2024/04/08
 * @desc
 */
@Service
public final class ZhiPuClient implements LlmClient {

    private static ClientV4 clientV4;

    public static ClientV4 getClient() {
        if (null == clientV4) {
            String apiKey = AppSettingsState.getInstance().getSavedApiKey();
            clientV4 = new ClientV4.Builder(apiKey)
                    .build();
        }
        return clientV4;
    }

    @Override
    public String chatCompletion(Object request) {
        ModelApiResponse response = getClient().invokeModelApi((ChatCompletionRequest) request);
        return null != response
                ? response.getData().getChoices().get(0).getMessage().getContent().toString()
                : null;
    }
}
