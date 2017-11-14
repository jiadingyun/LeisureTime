package com.yaping.leisureTime.duanzia.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.yaping.leisureTime.R;
import com.yaping.leisureTime.duanzia.Bean.DuanziBean;
import com.yaping.leisureTime.duanzia.api.DuanziApi;

import com.yaping.leisureTime.netUtil.OkHttpHelper;
import com.yaping.leisureTime.netUtil.mGsonHelper;
import com.yaping.leisureTime.netUtil.mOkHttpResponseCallback;
import com.yaping.leisureTime.recyclerViewUtil.OnItemLongClickListener;


import java.io.IOException;
import java.util.ArrayList;
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

    private CustomPopWindow popWindow;

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
                        //配置适配器
                        mRvShowDuanzhi.setLayoutManager(new LinearLayoutManager(getActivity()));
                        DuanziAdapter duanziAdapter = new DuanziAdapter(getActivity(),DuanziFragment.this, mDuanziBeanList);
                        mRvShowDuanzhi.setAdapter(duanziAdapter);
                        //添加分割线
                        mRvShowDuanzhi.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                        duanziAdapter.setmOnItemLongClickListener(new OnItemLongClickListener() {
                            @Override
                            public void OnItemLongClick(View view, int position) {
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);//获取被点击控件的坐标
                                //pop菜单
                                //弹出点击菜单
                                View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_layout1,null);
                                //处理popWindow 显示内容
                                handleLogic(contentView);

                                popWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                                        .setView(contentView)//显示的布局
                                        .setFocusable(true)//是否获取焦点，默认为ture
                                        .create();//创建PopupWindow
                                int[] popLocation = new int[]{popWindow.getWidth(),popWindow.getWidth()};
//                                        .showAsDropDown(view,view.getWidth()/2-100,-view.getHeight()/2);//显示PopupWindow
                                //根据Item位置定位pop的坐标
//                                popWindow.showAtLocation(view, Gravity.TOP|Gravity.START,location[0]+view.getWidth()/2-popLocation[0]/2,location[1]+view.getHeight()/2);
                                popWindow.showAtLocation(view, Gravity.TOP|Gravity.START,location[0]+view.getWidth()/2-popLocation[0]/2,location[1]+view.getHeight()/2);

                            }
                        });

                    }
                });
            }
            @Override
            public void onError(IOException e) {
                Toast.makeText(getActivity(),"请求数据失败:"+e,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogic(View contentView){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popWindow!=null){
                    popWindow.dissmiss();
                }
                String showContent = "";
                switch (v.getId()){
                    case R.id.menu1:
                        showContent = "点击 Item菜单1";
                        break;
                    case R.id.menu2:
                        showContent = "点击 Item菜单2";
                        break;
                }
                Toast.makeText(getActivity(),showContent,Toast.LENGTH_SHORT).show();
            }
        };
        contentView.findViewById(R.id.menu1).setOnClickListener(listener);
        contentView.findViewById(R.id.menu2).setOnClickListener(listener);
    }

}
