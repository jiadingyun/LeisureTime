package com.yaping.leisureTime.duanzia.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yaping.leisureTime.R;
import com.yaping.leisureTime.duanzia.Bean.DuanziBean;
import com.yaping.leisureTime.duanzia.api.DuanziApi;

import com.yaping.leisureTime.netUtil.OkHttpHelper;
import com.yaping.leisureTime.netUtil.mGsonHelper;
import com.yaping.leisureTime.netUtil.mOkHttpResponseCallback;


import java.io.IOException;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能：段子的Fragment碎片
 *
 * @创建： Created by yaping on 2017/11/1 0001.
 */
public class DuanziFragment extends Fragment {
    @BindView(R.id.duanzi_RecyclerView)
    RecyclerView mRvShowDuanzhi;
    @BindView(R.id.duanzi_refresh)
    SwipeRefreshLayout mRefresh;

    public static DuanziFragment newInstance() {
        return new DuanziFragment();
    }

    //碎片绑定布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duanzi, container, false);
        ButterKnife.bind(this, view);
        initView();
        initRefresh();
        return view;
    }

    /**
     * 下拉刷新
     *///下拉刷新
    private void initRefresh() {
        mRefresh.setColorSchemeResources(R.color.colorPrimary);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
                mRefresh.setRefreshing(false);
            }
        });
    }

    /**
     * 加载控件
     *///加载控件
    private void initView() {
        //请求网络数据
        OkHttpHelper.sendRequestWithOkHttp(DuanziApi.GET_DUANZI, new mOkHttpResponseCallback() {
            @Override
            public void onSuccess(String response) {
                //解析JSON内容
                final List<DuanziBean> mDuanziBeanList = mGsonHelper.parseJSONListWithGSON(response);
                Log.e("OkHttpHelper:------", "成功:"+mDuanziBeanList.size());
                //进入UI主线程更新UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if (mDuanziBeanList.size() > 4) {
//                            mDuanziBeanList.remove(3);
//                        }
                        //配置适配器
                        mRvShowDuanzhi.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRvShowDuanzhi.setAdapter(new DuanziAdapter(DuanziFragment.this, mDuanziBeanList));
                    }
                });
            }
            @Override
            public void onError(IOException e) {
                Toast.makeText(getActivity(),"请求数据失败:"+e,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
