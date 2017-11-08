package com.yaping.leisureTime.netUtil;

import java.io.IOException;

/**
 * 功能：用于网络请求的回调,以便将请求后的数据传出
 *
 * @创建： Created by yaping on 2017/11/1 0001.
 */
public interface mOkHttpResponseCallback {
    void onSuccess(String response);
    void onError(IOException e);
}
