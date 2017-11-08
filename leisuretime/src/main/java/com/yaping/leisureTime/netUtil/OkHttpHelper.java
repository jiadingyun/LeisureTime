package com.yaping.leisureTime.netUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 功能：Http网络通信
 *
 *
 * @创建： Created by yaping on 2017/11/1 0001.
 */

public class OkHttpHelper {
    /**
     * 用于发送Get请求的方法
     *
     * @param url 请求的网络地址
     * @param callback  用于网络回调的接口
     */
    public static void sendRequestWithOkHttp(String url,final mOkHttpResponseCallback callback){

        //声明OKhttp客户端,并设置读/写/连接超时
        OkHttpClient mClient =new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS) //设置读超时
                .writeTimeout(10, TimeUnit.SECONDS) //设置写超时
                .connectTimeout(10, TimeUnit.SECONDS) //设置连接超时范围
                .build();
        //Get请求
        Request request = new Request.Builder().url(url).build();
        //请求调度,异步get请求
        mClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultData = response.body().string();
                callback.onSuccess(resultData);
            }
         });
    }
}
