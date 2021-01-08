package com.teclan.desktop.client.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.teclan.desktop.client.config.CommonConfig;
import com.teclan.desktop.client.contant.Constant;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient CLIENT = new OkHttpClient();
    private static String BASRURL = CommonConfig.getConfig().getString("server.base_url");

    private static String[] getDefaultHeaders(){
        return new String[]{"user", Constant.USER,"token",Constant.TOKEN};
    }

    public static JSONObject get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(BASRURL+url)
                .get()
                .headers(Headers.of(getDefaultHeaders()))
                .build();
        LOGGER.info("请求：{}",BASRURL+url);
        Response response = CLIENT.newCall(request).execute();
        String ret = response.body().string();
        return JSONObject.parseObject(ret, Feature.OrderedField);
    }

    public static JSONObject post(String url, JSONObject paramater) throws Exception {
        RequestBody body = RequestBody.create(JSON, paramater.toJSONString());
        Request request = new Request.Builder()
                .url(BASRURL+url)
                .post(body)
                .headers(Headers.of(getDefaultHeaders()))
                .build();
        try {
            LOGGER.info("请求：{}",BASRURL+url);
            Response response = CLIENT.newCall(request).execute();
            String ret = response.body().string();
            return JSONObject.parseObject(ret, Feature.OrderedField);
        }catch (Exception e){
            if(e.getMessage().contains("Failed to connect to")){
                throw new Exception(String.format("无法连接 %s，请确认服务已启动且网络可连通 ...",BASRURL));
            }else{
                throw e;
            }
        }
    }

    public static JSONObject post(String url,JSONObject paramater, String... namesAndValues ) throws IOException {
        RequestBody body = RequestBody.create(JSON, paramater.toJSONString());
        Request request = new Request.Builder()
                .url(BASRURL+url)
                .post(body)
                .headers(Headers.of(namesAndValues))
                .build();
        LOGGER.info("请求：{}",BASRURL+url);
        Response response = CLIENT.newCall(request).execute();
        String ret = response.body().string();
        return JSONObject.parseObject(ret, Feature.OrderedField);
    }
}
