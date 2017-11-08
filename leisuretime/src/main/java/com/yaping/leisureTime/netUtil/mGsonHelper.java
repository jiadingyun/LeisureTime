package com.yaping.leisureTime.netUtil;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yaping.leisureTime.duanzia.Bean.DuanziBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：解析JSON数据
 *
 * @创建： Created by yaping on 2017/11/1 0001.
 */

public class mGsonHelper {
    /**
     * 解析 段子实体
     * @param resultData 服务器响应的内容
     * @return List</段子实体>
     */
    public static List<DuanziBean> parseJSONListWithGSON(String resultData) {
        List<DuanziBean> mDuanziBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(resultData);
            String dataArrayStr = jsonObject.getJSONObject("data").getString("data");
            Type type = new TypeToken<List<DuanziBean>>(){}.getType();
            Gson gson = new Gson();
            mDuanziBeanList = gson.fromJson(dataArrayStr, type);
            return mDuanziBeanList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mDuanziBeanList;
    }

    /**
     * 解析 自定义 的实体类
     * @param resultData 服务器响应的内容
     * @param obj 需要解析的实体类型
     * @return 自定义 的实体类
     */
    public static Object parseJSONWithGSON(String resultData,Object obj) {
        //利用gson解析服务器端的数据，并将数据保存到List<DuanziBean>实体类中
        Gson gson = new Gson();
        Object duanzi= gson.fromJson(resultData, obj.getClass());
        return duanzi;
    }
    /**
     * 解析 自定义 的实体类
     * @param resultData 服务器响应的内容
     * @param obj 需要解析的实体类型
     * @return 自定义 的实体类
     */
    public static List<Object> parseJSONListWithGSON(String resultData,Object obj) {
        //利用gson解析服务器端的数据，并将数据保存到List<DuanziBean>实体类中
        Gson gson = new Gson();
        List<Object> duanzi= gson.fromJson(resultData, new TypeToken<List<Object>>(){}.getType());
        return duanzi;
    }

}
