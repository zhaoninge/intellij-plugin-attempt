package com.ai.boy.programming.rpc;

import com.intellij.openapi.diagnostic.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.nio.charset.StandardCharsets;

import static cn.hutool.json.JSONUtil.toJsonStr;

/**
 * @author zhaoning
 * @date 2024/04/03
 * @desc
 */
public class OkHttpClientUtils {

    private static final Logger log = Logger.getInstance(OkHttpClientUtils.class.getName());

    private static final String DEFAULT_BASE_URL = "https://api.moonshot.cn/v1";
    private static final String CHAT_COMPLETION_SUFFIX = "/chat/completions";

    public static String postCompletion(String body, String apiKey) {
        try {
            Request request = new Request.Builder()
                    .url(DEFAULT_BASE_URL + CHAT_COMPLETION_SUFFIX)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(body.getBytes(StandardCharsets.UTF_8)))
                    .build();

            OkHttpClient client = new OkHttpClient();
            log.info("request: " + toJsonStr(request));
            log.info("body: " + toJsonStr(body));
            Response response = client.newCall(request).execute();
            log.info("response: " + toJsonStr(response));
            //System.out.println("body: " + response.body().string());

            return response.body().string();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
