package com.ai.boy.programming.factory;

import com.ai.boy.programming.enums.ProviderEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaoning
 * @date 2024/04/09
 * @desc
 */
public class ModelFactory {

    private static final List<String> MOONSHOT_MODEL_LIST = Arrays.asList(
            "moonshot-v1-8k",
            "moonshot-v1-32k",
            "moonshot-v1-128k"
    );

    private static final List<String> ZHI_PU_MODEL_LIST = Arrays.asList(
            "GLM-4",
            "GLM-4V",
            "GLM-3-Turbo"
    );

    private static final List<String> MINI_MAX_MODEL_LIST = Arrays.asList(
            "abab6-chat",
            "abab5.5-chat",
            "abab5.5s-chat"
    );

    public static List<String> getProviderList() {
        return Arrays.stream(ProviderEnum.values())
                .map(ProviderEnum::getCode)
                .collect(Collectors.toList());
    }

    public static List<String> getModelList(String provider) {
        ProviderEnum providerEnum = ProviderEnum.codeOf(provider);
        assert providerEnum != null;
        return switch (providerEnum) {
            case MOONSHOT -> MOONSHOT_MODEL_LIST;
            case ZHI_PU -> ZHI_PU_MODEL_LIST;
            case MINI_MAX -> MINI_MAX_MODEL_LIST;
        };
    }

}
