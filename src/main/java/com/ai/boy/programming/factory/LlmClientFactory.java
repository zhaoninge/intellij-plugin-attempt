package com.ai.boy.programming.factory;

import com.ai.boy.programming.client.LlmClient;
import com.ai.boy.programming.client.impl.MiniMaxClient;
import com.ai.boy.programming.client.impl.MoonshotClient;
import com.ai.boy.programming.client.impl.ZhiPuClient;
import com.ai.boy.programming.enums.ProviderEnum;
import com.ai.boy.programming.setting.AppSettingsState;

import static com.intellij.openapi.application.ApplicationManager.getApplication;

/**
 * @author zhaoning
 * @date 2024/04/09
 * @desc
 */
public class LlmClientFactory {

    public static LlmClient getClient() {
        String provider = AppSettingsState.getInstance().getProvider();
        ProviderEnum providerEnum = ProviderEnum.codeOf(provider);
        assert providerEnum != null;

        return switch (providerEnum) {
            case MOONSHOT -> getApplication().getService(MoonshotClient.class);
            case ZHI_PU -> getApplication().getService(ZhiPuClient.class);
            case MINI_MAX -> getApplication().getService(MiniMaxClient.class);
        };
    }

}
