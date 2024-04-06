package com.ai.boy.programming.rpc.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author ning.zhao
 * @date 2024/04/01
 * @desc
 */
@Data
public class ChatCompletionChoice {

    private int index;
    private ChatCompletionMessage message;

    @SerializedName("finish_reason")
    private String finishReason;
}
