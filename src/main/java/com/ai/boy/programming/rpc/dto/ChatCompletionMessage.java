package com.ai.boy.programming.rpc.dto;

import lombok.Data;

/**
 * @author ning.zhao
 * @date 2024/04/01
 * @desc
 */
@Data
public class ChatCompletionMessage {
    public String role;
    public String name;
    public String content;
    public Boolean partial;
}
