package com.yaping.leisureTime.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * 功能：ViewPager + Fragment 通用的 Adapter
 * @创建： Created by yaping on 2017/10/17 0017.
 */
public class CommonPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments; //创建一个List<Fragment>

    //实例
    public CommonPagerAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments=mFragments;
    }

    //为当前item返回 对应显示的碎片
    @Override
    public Fragment getItem(int position) {//返回第几个fragment
        return mFragments.get(position);
    }

    //view装载的数量
    @Override
    public int getCount() {//总共有多少个fragment
        return mFragments.size();
    }
}
