package com.ai.boy.programming.client;

import com.intellij.openapi.diagnostic.Logger;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.hutool.json.JSONUtil.toJsonStr;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author zhaoning
 * @date 2024/04/03
 * @desc
 */
public class OkHttpClient {

    private static final Logger log = Logger.getInstance(OkHttpClient.class.getName());

    public static String postCompletion(String url, String apiKey, String body) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", APPLICATION_JSON_VALUE)
                    .post(RequestBody.create(MediaType.parse(APPLICATION_JSON_VALUE), body))
                    .build();

            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
            log.info("request: " + toJsonStr(request));
            log.info("body: " + toJsonStr(body));
            Response response = client.newCall(request).execute();
            log.info("response: " + toJsonStr(response));

            return response.body().string();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
