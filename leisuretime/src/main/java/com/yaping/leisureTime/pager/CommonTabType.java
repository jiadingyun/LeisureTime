package com.yaping.leisureTime.pager;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * 功能：Tab样式设置的 通用类
 *
 * @创建： Created by yaping on 2017/10/17 0017.
 */

public class CommonTabType implements CustomTabEntity {

    private int selectedIcon;
    private int unselectedIcon;
    private String title;

    public CommonTabType(String title){
        this.title = title;
    }

    public CommonTabType(String title, int selectedIcon, int unselectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unselectedIcon = unselectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unselectedIcon;
    }
}
