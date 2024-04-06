package com.ai.boy.programming.rpc.builder;

import com.ai.boy.programming.enums.RoleEnum;
import com.ai.boy.programming.rpc.dto.ChatCompletionMessage;
import com.ai.boy.programming.rpc.dto.ChatCompletionRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoning
 * @date 2024/04/03
 * @desc
 */
public class ChatCompletionRequestBuilder {

    public static ChatCompletionRequest buildChatCompletionRequest(String text) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel("moonshot-v1-8k");

        List<ChatCompletionMessage> messages = new ArrayList<>();
        messages.add(buildChatCompletionMessage());
        messages.add(buildChatCompletionMessage(text));
        request.setMessages(messages);
        return request;
    }

    private static ChatCompletionMessage buildChatCompletionMessage() {
        ChatCompletionMessage message = new ChatCompletionMessage();
        message.setRole(RoleEnum.system.name());
        message.setContent("你是一名java开发工程师");
        return message;
    }

    private static ChatCompletionMessage buildChatCompletionMessage(String content) {
        ChatCompletionMessage message = new ChatCompletionMessage();
        message.setRole(RoleEnum.user.name());
        message.setContent("写出这段文字描述的java方法，只写代码，不要带任何Markdown标记，不用解释：\n" + content);
        return message;
    }
}
