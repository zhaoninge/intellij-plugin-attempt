package com.ai.boy.programming.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderEnum {

    MOONSHOT("Moonshot"),
    ZHI_PU("智谱清言"),
    MINI_MAX("MiniMax"),
    ;

    private final String code;

    public static ProviderEnum codeOf(String code) {
        for (ProviderEnum item : ProviderEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
