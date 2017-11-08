package com.yaping.leisureTime;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.yaping.leisureTime.duanzia.ui.DuanziFragment;
import com.yaping.leisureTime.pager.CommonPagerAdapter;
import com.yaping.leisureTime.pager.CommonTabType;
import com.yaping.leisureTime.pager.MyPagerFragment;
import com.yaping.leisureTime.version.VersionGetUtil;
import com.yaping.leisureTime.version.VersionUpdateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";

    //升级版本
    private int currentVersionCode;
    private String currentVersionName;

    @BindView(R.id.viewPager)
    ViewPager mHomeVP;
    @BindView(R.id.tabLayout)
    CommonTabLayout mHomeTabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //滑动菜单
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    private FloatingActionButton fab;

    private List<Fragment> mFragments = new ArrayList<>();
    private static final String[] TITLES = new String[]{"日记", "段子", "妹子"};
    private static final int[] SELECTED_ICONS = new int[]{R.drawable.diary_selected, R.drawable.duanzi_selected, R.drawable.meizi_selected};
    private static final int[] UN_SELECTED_ICONS = new int[]{R.drawable.diary_unselected, R.drawable.duanzi_unselected, R.drawable.meizi_unselected};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initViewPager();
        initTabLayout();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //fab
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //滑动菜单
        navView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //获取本地版本号和版本名
        currentVersionCode = VersionGetUtil.getVersionCode(MainActivity.this);
        currentVersionName = VersionGetUtil.getVersionName(MainActivity.this);
        Log.i(TAG, "onCreate: 版本号：" + currentVersionCode + ",版本名：" + currentVersionName);
//        mainTextView.setText("当前版本: " + currentVersionName);
    }

    /**
     * 向viewpager添加fragment
     */
    private void initViewPager() {
        mFragments = new ArrayList<>();
        mFragments.add(MyPagerFragment.newInstance(1));
        mFragments.add(DuanziFragment.newInstance());
        mFragments.add(MyPagerFragment.newInstance(3));
        //Fragment+ViewPager组合的使用
        CommonPagerAdapter adapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragments);
        mHomeVP.setAdapter(adapter);
    }

    /**
     * 功能：初始化tabLayout
     */
    private void initTabLayout() {
        mHomeTabLayout = (CommonTabLayout) findViewById(R.id.tabLayout);
        //设置Tab选项卡内容/样式
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        for (int i = 0; i < TITLES.length; i++) {
            tabEntityList.add(new CommonTabType(TITLES[i]
                    , SELECTED_ICONS[i]
                    , UN_SELECTED_ICONS[i]));
        }
        mHomeTabLayout.setTabData(tabEntityList);

        //每当Tab更改或递增时,切换ViewPager页面
        mHomeTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mHomeVP.setCurrentItem(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        //每当页面更改或递增时,切换Tab选项卡
        mHomeVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mHomeTabLayout.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mHomeVP.setOffscreenPageLimit(4);//缓存优化
        mHomeVP.setCurrentItem(1);//启动时默认显示的Item
    }


    @Override //菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override //菜单点击事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "你点击了分享", Toast.LENGTH_LONG).show();
                break;
            case R.id.delete:
                break;
            case R.id.action_settings:
                break;
            default:
        }
        return true;
    }

    @Override //返回键
    public void onBackPressed() {
        super.onBackPressed();
        // TODO: 在主页面按返回键时弹出对话框，提示用户是否退出程序
    }

    @Override //滑动菜单点击事件
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_update) {
            //版本升级
            final VersionUpdateUtil UpdateUtil =
                    new VersionUpdateUtil(currentVersionCode, MainActivity.this);
            Log.i(TAG, "开始执行版本判断");
            UpdateUtil.getServerVersionCode();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //版本升级
                    final VersionUpdateUtil UpdateUtil =
                            new VersionUpdateUtil(currentVersionCode, MainActivity.this);
                    Log.i(TAG, "开始安装新版本");
                    UpdateUtil.installApk();
                } else {
                    Toast.makeText(this, "你禁用了该权限,将无法安装更新!", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
