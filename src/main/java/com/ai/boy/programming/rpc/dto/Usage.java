package com.ai.boy.programming.rpc.dto;

import lombok.Data;

/**
 * @author ning.zhao
 * @date 2024/04/01
 * @desc
 */
@Data
public class Usage {

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
}
