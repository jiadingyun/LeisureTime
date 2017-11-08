package com.yaping.leisureTime.version;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * 功能：对比本地与服务器版本
 *
 * @创建： Created by yaping on 2017/10/30 0030.
 */

public class VersionUpdateUtil extends Activity {
    /**
     * 服务器端保存版本更新信息的地址
     */
    private static final String server_url =ServerUrl.MY_URL;

    //用于构造函数
    private Activity mActivity;
    private VersionJsonEntity versionJsonEntity;//解析服务器版本信息
    private static int serverVersionCode =1;    //服务器版本号
    private int currentVersionCode;             //本地版本号

    /**
     * 声明OKhttp客户端,并设置读/写/连接超时
     */
    private OkHttpClient client =new OkHttpClient.Builder()
            .readTimeout(10,TimeUnit.SECONDS) //设置读超时
            .writeTimeout(10, TimeUnit.SECONDS) //设置写超时
            .connectTimeout(5, TimeUnit.SECONDS) //设置连接超时范围
            .build();
    /**
     * 忽略版本，保存服务器端版本号到sp中
     */
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;
    private static int savedVersion = 1;

    /**
     * 进度更新dialog
     */
    private ProgressDialog progressDialog;
    private ProgressDialog jsonProgressDialog;

    /**
     * 请求码，用于动态权限设置的回调
     */
    private static final int REQUEST_CODE = 1;

    private File file = null;
    /**
     * handler消息的处理
     */
    private static final int UPDATE_YES = 1;
    private static final int UPDATE_NO = 2;
    private static final int IO_ERROR = 3;
    private static final int SHOW_DIALOG = 4;
    private static final int UPDATE_IGNORE = 5;
    private static final int UPDATE_PROGRESS = 6;
    private static final int UPDATE_INSTALL = 7;
    private static final int UPDATE_ADD_PERMISSION = 8;
    private static final int NEWEST_VERSION = 9;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_YES:
                    Log.i(TAG, "handleMessage: 需要更新");
                    //从服务器端获取apk的下载网址
                    downLoadApk(versionJsonEntity.getApkUrl());
                    break;
                case UPDATE_NO:
                    Log.w(TAG, "handleMessage: 不需要更新");
                    break;
                case UPDATE_IGNORE:
                    Toast.makeText(mActivity,"用户忽略了该版本", Toast.LENGTH_SHORT).show();
                    saveNewestVersion(serverVersionCode);
                    break;
                case IO_ERROR:
                    Log.e(TAG, "handleMessage: IO异常");
                    Toast.makeText(mActivity,"服务器链接失败!", Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_DIALOG:
                    Log.i(TAG, "handleMessage: 弹出更新对话框");
                    showUpdateDialog();
                    break;
                case UPDATE_PROGRESS:
                    int progress = msg.arg1;
                    progressDialog.setProgress(progress);
                    break;
                case UPDATE_INSTALL:
                    Log.i(TAG, "handleMessage: file: " + file);
                    installApk(mActivity,file);
                    break;
                case UPDATE_ADD_PERMISSION:
                    Toast.makeText(mActivity,"需要添加权限,请点击允许", Toast.LENGTH_LONG).show();
                    break;
                case NEWEST_VERSION:
                    Toast.makeText(mActivity,"已经是最新版本！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 构造函数
     *
     * @param currentVersionCode 本地版本号(用于和服务器的版本号对比)
     * @param activity 上下文
     */
    public VersionUpdateUtil(int currentVersionCode, Activity activity) {
        this.mActivity = activity;
        this.currentVersionCode = currentVersionCode;
    }

    /**
     * 获取服务器版本号(用GSON 解析json)
     *///解析服务器上的版本信息
    public void getServerVersionCode() {
        // 显示进度对话框
        jsonProgressDialog();
        //构造request，并设置request参数
        final Request request = new Request.Builder()
                .url(server_url)
                .build();

        //请求调度,异步get请求
        client.newCall(request).enqueue(new Callback() {
            //失败
            @Override
            public void onFailure(Call call, IOException e) {
                jsonProgressDialog.dismiss();
                handler.sendEmptyMessage(IO_ERROR);
            }
            //成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.i(TAG, "当前响应的结果：" + result);

                //利用gson解析服务器端的数据，并将数据保存到VersionInfoEntity实体类中
                Gson gson = new Gson();
                versionJsonEntity = gson.fromJson(result, VersionJsonEntity.class);

                //获取服务器端的版本号与本地的服务端版本号作对比
                serverVersionCode = versionJsonEntity.getVersionCode();

                Log.i(TAG, "服务器端版本: " + serverVersionCode);
                Log.i(TAG, "本地版本: " + currentVersionCode);
                Log.i(TAG, "sp保存的版本：" + savedVersion);

                jsonProgressDialog.dismiss();
                /**
                 * 版本更新的判断与是否执行了忽略版本的操作
                 */
                if (serverVersionCode > currentVersionCode) {
                    if (serverVersionCode == savedVersion){
                        Log.i(TAG, "onResponse: 用户选择了忽略该版本");
                        handler.sendEmptyMessage(UPDATE_IGNORE);
                    }else {//更新
                        handler.sendEmptyMessage(SHOW_DIALOG);
                    }

                } else {
                    System.out.println("无最新版本");
                    handler.sendEmptyMessage(NEWEST_VERSION);
                }
            }
        });
    }
    /**
     * 加载提示框
     */
    private void jsonProgressDialog() {
        jsonProgressDialog = new ProgressDialog(mActivity);
        jsonProgressDialog.setCancelable(false);
        jsonProgressDialog.setMessage("Loading...");
        jsonProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        jsonProgressDialog.show();
    }


    /**
     * 弹出对话框，让用户判断是否需要更新版本
     *///选择 是否更新
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("监测到新版本");
        builder.setMessage(versionJsonEntity.getDes());
        builder.setPositiveButton("确定更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                handler.sendEmptyMessage(UPDATE_YES);
            }
        });
        builder.setNeutralButton("忽略版本", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                handler.sendEmptyMessage(UPDATE_IGNORE);
            }
        });
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                handler.sendEmptyMessage(UPDATE_NO);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    /**
     * 确定更新:从服务器下载新版本的APK
     * okhttp
     *///确定更新
    private void downLoadApk(final String downLoadApkUrl) {
        createProgressDialog();
        //请求服务器端的apk
        final Request request = new Request.Builder()
                .url(downLoadApkUrl)
                .build();

        //请求调度,异步get请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(IO_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                /**
                 * android6.0系统后增加运行时权限，需要动态添加内存卡读取权限
                 */
                if (Build.VERSION.SDK_INT >= 23) {
                    int permission = ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        progressDialog.dismiss();
                        ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                        Log.w(TAG, "checkWriteStoragePermission: 无此权限，需要添加");
                        handler.sendEmptyMessage(UPDATE_ADD_PERMISSION);
                        return;
                    } else {
                        downApkFlie(response);
                        if (progressDialog != null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        handler.sendEmptyMessage(UPDATE_INSTALL);

                    }
                } else {
                    downApkFlie(response);
                    if (progressDialog != null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    handler.sendEmptyMessage(UPDATE_INSTALL);
                }

            }
        });
    }
    /**
     * 忽略版本:(直接写入服务器上的最新版本号)
     * @param versionCode
     *///忽略版本
    private void saveNewestVersion(int versionCode) {
        sharedPreferences = mActivity.getSharedPreferences("ignore_ServerVersionCode", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt("ignore_ServerVersionCode", versionCode);
        editor.commit();

        savedVersion = sharedPreferences.getInt("ignore_ServerVersionCode",versionCode);
    }
    /**
     * 下载进度
     *///下载进度
    private void createProgressDialog() {//下载进度
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在下载");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }


    /**
     * 下载APK文件
     *
     * @param response OkHttp请求的结果
     *///下载APK文件
    private void downApkFlie(Response response) {
        InputStream iS = null;
        FileOutputStream fos = null;
        byte[] buf = new byte[1024];//每次读取1K的数据

        int len = 0;
        long sum = 0;
        int progress = 0;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), "test.apk");
            try {
                if (file.exists()) {
                    file.delete();
                } else {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            long total = response.body().contentLength();
            Log.i(TAG, "downApkFlie: total---" + total);

            iS = response.body().byteStream();

            //捕捉是否动态分配读写内存权限异常
            try {
                fos = new FileOutputStream(file);
                //捕捉输入流读取异常
                try {
                    /**
                     * read(),从输入流中读取数据的下一个字节，返回0~255范围内的字节值，如果已经到达
                     * 流末尾而没有可用的字节，则返回-1
                     */
                    while ((len = iS.read(buf)) != -1) {
                        fos.write(buf, 0, len);//write(byte[]b, off, int len), 将指定的byte数组中从偏移量off开始的len个字节写入此输出流
                        sum += len;
                        progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                        //更新进度
                        Message msg = handler.obtainMessage();
                        msg.what = UPDATE_PROGRESS;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                    fos.flush();//彻底完成输出并清空缓存区
                    Log.i(TAG, "downApkFlie: 下载完毕");
                } catch (IOException e) {
                    handler.sendEmptyMessage(IO_ERROR);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "downApkFlie: 下载失败");
            } finally {
                //清空file输入输出流
                try {
                    if (iS != null) {
                        iS.close();//关闭输入流
                    }
                    if (fos != null) {
                        fos.close();//关闭输出流
                    }
                } catch (IOException e) {
                    handler.sendEmptyMessage(IO_ERROR);
                }
            }
        }
    }

    /**
     * 安装新版本APK
     *///安装apk
    protected void installApk(Activity activity, File file) {
        if (activity == null || !file.exists()){
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //是否(Android7.0)以上
            //对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 参数(上下文,  Provider主机地址 和配置文件中android:authorities保持一致 , 共享的文件)
            Uri contentUri = FileProvider.getUriForFile(activity, "com.yaping.leisureTime.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }

    /**
     * 重新安装apk
     */
    public void installApk(){
        handler.sendEmptyMessage(UPDATE_INSTALL);
    }

}
