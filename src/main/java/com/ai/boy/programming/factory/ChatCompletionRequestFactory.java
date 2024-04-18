package com.ai.boy.programming.factory;

import com.ai.boy.programming.enums.ProviderEnum;
import com.ai.boy.programming.setting.AppSettingsState;
import com.intellij.openapi.diagnostic.Logger;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * @author zhaoning
 * @date 2024/04/09
 * @desc
 */
public class ChatCompletionRequestFactory {

    private static final Logger LOGGER = Logger.getInstance(ChatCompletionRequestFactory.class);

    public static Object buildChatCompletionRequest(String text) {
        String provider = AppSettingsState.getInstance().getProvider();
        String model = AppSettingsState.getInstance().getSavedModel();
        LOGGER.info(format("provider: %s, module: %s", provider, model));

        ProviderEnum providerEnum = ProviderEnum.codeOf(provider);
        assert providerEnum != null;

        return switch (providerEnum) {
            case MOONSHOT -> buildMoonshotRequest(text, model);
            case ZHI_PU -> buildZhiPuRequest(text, model);
            case MINI_MAX -> buildMiniMaxRequest(text, model);
        };
    }

    private static Prompt buildMoonshotRequest(String text, String model) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage("你是一名java开发工程师"));
        messages.add(
                new UserMessage("写出这段文字描述的java方法，只写代码，不要带任何Markdown标记，不用解释：\n" + text));
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel(model)
                .build();
        return new Prompt(messages, options);
    }

    private static ChatCompletionRequest buildZhiPuRequest(String text, String model) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "你是一名java开发工程师"));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(),
                "写出这段文字描述的java方法，只写代码，不要带任何Markdown标记，不用解释：\n" + text));

        String requestId = format("pb-%d", System.currentTimeMillis());

        return ChatCompletionRequest.builder()
                .model(model)
                .invokeMethod(Constants.invokeMethod)
                .stream(Boolean.FALSE)
                .messages(messages)
                .requestId(requestId)
                .build();
    }

    /**
     * <a href="https://www.minimaxi.com/document/guides/chat-model/V2?id=65e0736ab2845de20908e2dd">...</a>
     *
     * @param text
     * @param model
     * @return
     */
    private static ChatCompletionRequest buildMiniMaxRequest(String text, String model) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), "你是一名java开发工程师"));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(),
                "写出这段文字描述的java方法，只写代码，不要带任何Markdown标记，不用解释：\n" + text));

        return ChatCompletionRequest.builder()
                .model(model)
                .stream(Boolean.FALSE)
                .messages(messages)
                .build();
    }
}
