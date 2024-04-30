package com.ai.boy.programming.client.impl;

import com.ai.boy.programming.client.LlmClient;
import com.ai.boy.programming.setting.AppSettingsState;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.components.Service;
import org.springframework.ai.bedrock.llama2.BedrockLlama2ChatClient;
import org.springframework.ai.bedrock.llama2.api.Llama2ChatBedrockApi;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

/**
 * @author zhaoning
 * @date 2024/04/22
 * @desc
 */
@Service
public final class BedrockLlamaClient implements LlmClient {

    private static BedrockLlama2ChatClient chatClient;

    private static BedrockLlama2ChatClient getClient() {
        if (null == chatClient) {
            String model = AppSettingsState.getInstance().getSavedModel();
            String accessKeyId = AppSettingsState.getInstance().getSavedAccessKeyId();
            String secretAccessKey = AppSettingsState.getInstance().getSavedAccessKey();
            // 链接到AWS的凭证
            AwsCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider.create(credentials);

            Llama2ChatBedrockApi api = new Llama2ChatBedrockApi(model, awsCredentialsProvider, Region.US_WEST_2.id(),
                    new ObjectMapper());
            chatClient = new BedrockLlama2ChatClient(api);
        }
        return chatClient;
    }

    public static void resetClient() {
        chatClient = null;
    }

    @Override
    public String chatCompletion(Object request) {
        ChatResponse response = getClient().call((Prompt) request);
        return null != response ? response.getResult().getOutput().getContent() : null;
    }
}
