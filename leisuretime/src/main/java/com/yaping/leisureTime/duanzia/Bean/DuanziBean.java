package com.yaping.leisureTime.duanzia.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 功能：段子 解析
 *
 * @创建： Created by yaping on 2017/11/1 0001.
 */

public class DuanziBean {

    /**
     * group : {}
     * comments : []
     * type : 1
     * display_time : 1509512572
     * online_time : 1509512572
     */

    @SerializedName("group")
    private GroupBean groupBean;

    public GroupBean getGroupBean() {
        return groupBean;
    }

    public void setgroupBean(GroupBean group) {
        this.groupBean = group;
    }

}
