package com.yaping.leisureTime.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yaping.leisureTime.R;

/**
 * 功能：Test for Fragment内容
 *
 * @创建： Created by yaping on 2017/10/17 0017.
 */

public class MyPagerFragment extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    public static MyPagerFragment newInstance(int page) {
        //初始化时传递参数
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        MyPagerFragment fragment = new MyPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建时 获取参数
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        TextView textView = (TextView) view.findViewById(R.id.fragment_main_textView);
        textView.setText("第"+mPage+"页");
        return view;
    }

}
