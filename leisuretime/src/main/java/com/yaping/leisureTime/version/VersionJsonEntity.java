package com.yaping.leisureTime.version;

/**
 * 功能：应用版本信息实体类,对比服务器JSON版本
 * (使用GsonFormat插件快速实现)
 *
 * @创建： Created by yaping on 2017/10/30 0030.
 */

public class VersionJsonEntity {

    /**
     * versionName : 2.0
     * versionCode : 2
     * des : 这是升级后的版本
     * apkUrl : http://192.168.10.121:8080/app-release.apk
     */

    private String versionName;
    private int versionCode;
    private String des;
    private String apkUrl;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }
}
