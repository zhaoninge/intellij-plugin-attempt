package com.ai.boy.programming.rpc.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * @author ning.zhao
 * @date 2024/04/01
 * @desc
 */
@Data
public class ChatCompletionRequest {

    public String model;
    public List<ChatCompletionMessage> messages;

    @SerializedName("max_tokens")
    public int maxTokens;

    @SerializedName("temperature")
    public float temperature;
    public float topP;

    public Integer n;
    public boolean stream;
    public List<String> stop;

    @SerializedName("presence_penalty")
    public float presencePenalty;

    @SerializedName("frequency_penalty")
    public float frequencyPenalty;

    public String user;
}
