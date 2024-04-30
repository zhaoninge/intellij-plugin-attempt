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
            case MOONSHOT -> buildPrompt(text, model);
            case ZHI_PU -> buildZhiPuRequest(text, model);
            case MINI_MAX -> buildMiniMaxRequest(text, model);
            case AMAZON -> buildPrompt(text);
        };
    }

    private static final String SYSTEM_MESSAGE = "你是一名java开发工程师，思维缜密，编码规范，只会输出代码，不会包含代码解释";
    private static final String USER_MESSAGE = """
            深呼吸，一步一步完成下述任务：
            写一个方法代码实现，不包含类代码，不带任何Markdown标记，方法的注释描述如下：
            """;

    private static Prompt buildPrompt(String text, String model) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(SYSTEM_MESSAGE));
        messages.add(new UserMessage(USER_MESSAGE + text));
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel(model)
                .build();
        return new Prompt(messages, options);
    }

    private static Prompt buildPrompt(String text) {
        List<Message> messages = new ArrayList<>();
        messages.add(new SystemMessage(SYSTEM_MESSAGE));
        messages.add(new UserMessage(USER_MESSAGE + text));
        return new Prompt(messages);
    }

    private static ChatCompletionRequest buildZhiPuRequest(String text, String model) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), SYSTEM_MESSAGE));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), USER_MESSAGE + text));

        String requestId = format("pb-%d", System.currentTimeMillis());

        return ChatCompletionRequest.builder()
                .model(model)
                .invokeMethod(Constants.invokeMethod)
                .stream(Boolean.FALSE)
                .temperature(0.1f)
                .requestId(requestId)
                .messages(messages)
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
        messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), SYSTEM_MESSAGE));
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), USER_MESSAGE + text));

        return ChatCompletionRequest.builder()
                .model(model)
                .stream(Boolean.FALSE)
                .messages(messages)
                .build();
    }
}
