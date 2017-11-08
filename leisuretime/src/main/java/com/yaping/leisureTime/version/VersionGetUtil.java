package com.yaping.leisureTime.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 功能：获取当前应用的版本信息
 *
 * @创建： Created by yaping on 2017/10/30 0030.
 */

public class VersionGetUtil {
    private static final String TAG = "VersionGetUtil";

    /**
     * 获取版本名
     */
    public static String getVersionName(Context context){
        //PackageManager,可以获取清单中的所有信息
        PackageManager manager = context.getPackageManager();
        //getPackageName(),获取当前程序的包名
        try {
            //获取包中的信息,getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
            String versionName = info.versionName;//版本名，是需要在APP中显示的
            Log.i(TAG, "getVersion: name" + versionName);
            return versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            Log.e("VersionGetUtil","can not get current Version Name");
        }
        //如果出现异常抛出null
        return null;
    }
    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context){
        //PackageManager,可以获取清单中的所有信息
        PackageManager manager = context.getPackageManager();
        //getPackageName(),获取当前程序的包名
        try {
            //获取包中的信息
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
            int versionCode = info.versionCode;//版本号，用于判断是否为最新版本
            Log.i(TAG, "getVersion: code" + versionCode);
            return versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            Log.e("VersionGetUtil","can not get current Version Code");
        }
        //如果出现异常抛出0
        return 0;
    }

}
