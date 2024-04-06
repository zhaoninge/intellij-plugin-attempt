package com.ai.boy.programming.rpc.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ning.zhao
 * @date 2024/04/01
 * @desc
 */
@Data
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatCompletionChoice> choices;
    private Usage usage;
}
