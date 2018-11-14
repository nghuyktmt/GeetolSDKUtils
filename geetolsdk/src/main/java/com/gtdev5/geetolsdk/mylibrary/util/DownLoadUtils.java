package com.gtdev5.geetolsdk.mylibrary.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gtdev5.geetolsdk.R;
import com.gtdev5.geetolsdk.mylibrary.beans.GetNewBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Walter on 2018/11/13.
 */

public class DownLoadUtils {
    private static ProgressBar progressBar;
    private static int mProgress = 0;
    private static Dialog dialog;
    private static String fileName;
    private static Activity mactivity;

    public static void showDownLoadDialog(Activity activity, GetNewBean getNewBean) {
        mactivity = activity;
        dialog = new Dialog(mactivity, R.style.DialogTransparent);
        dialog.setContentView(R.layout.dialog_getnew);
        LinearLayout v = dialog.findViewById(R.id.dialog_layout_v);
        Button sure = dialog.findViewById(R.id.dialog_sure);
        Button cancle = dialog.findViewById(R.id.dialog_cancle);
        Button sure_v = dialog.findViewById(R.id.dialog_sure_v);

        LinearLayout download = dialog.findViewById(R.id.dialog_layout_download);
        LinearLayout upload = dialog.findViewById(R.id.dialog_layout_upload);
        progressBar = dialog.findViewById(R.id.dialog_progress);
        if (getNewBean.getVername().contains("v")) {
            v.setVisibility(View.GONE);
            sure.setVisibility(View.VISIBLE);
            dialog.setCancelable(false);

        } else {
            v.setVisibility(View.VISIBLE);
            sure.setVisibility(View.GONE);
            dialog.setCancelable(true);
        }
        dialog.setCanceledOnTouchOutside(false);
        cancle.setOnClickListener(v1 -> dialog.dismiss());

        sure.setOnClickListener(v12 -> {
            upload.setVisibility(View.GONE);
            download.setVisibility(View.VISIBLE);
            downLoadAPK(getNewBean.getDownurl());
        });

        sure_v.setOnClickListener(v13 -> {
            upload.setVisibility(View.GONE);
            download.setVisibility(View.VISIBLE);
            downLoadAPK(getNewBean.getDownurl());
        });
        dialog.show();
    }

    public static void downLoadAPK(String url) {
        dialog.setCancelable(false);
        Log.e("LogUtils", "url  " + url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sdPath = Environment.getExternalStorageDirectory() + "/";
//                      文件保存路径
                    String mSavePath = sdPath + "downloadApks";

                    File dir = new File(mSavePath);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    Log.e("LogUtils", "mSavePath  " + mSavePath);

                    // 下载文件
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    int length = conn.getContentLength();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    fileName = formatter.format(curDate);

                    File apkFile = new File(mSavePath, fileName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    Log.e("LogUtils", "fileName  " + fileName);

                    int count = 0;
                    byte[] buffer = new byte[1024];
                    int numread = is.read(buffer);
                    count += numread;
                    // 计算进度条的当前位置
                    mProgress = (int) (((float) count / length) * 100);
                    // 更新进度条
                    mUpdateProgressHandler.sendEmptyMessage(1);
                    Log.e("LogUtils", "numread  " + numread);

                    // 下载完成
                    if (numread < 0) {
                        mUpdateProgressHandler.sendEmptyMessage(2);
                    }
                    fos.write(buffer, 0, numread);
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    Log.e("LogUtils", "e  " + e);

                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private static Handler mUpdateProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 设置进度条
                    progressBar.setProgress(mProgress);
                    Log.e("LogUtils", "mProgress  " + mProgress);
                    break;
                case 2:
                    // 隐藏当前下载对话框
                    dialog.dismiss();
                    // 安装 APK 文件
                    installAPK();
            }
        }

        ;
    };


    /*
     * 下载到本地后执行安装
     */
    protected static void installAPK() {
        File apkFile = new File(Environment.getExternalStorageDirectory() + "/" + "downloadApks"
                , fileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
//      安装完成后，启动app（源码中少了这句话）
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.parse("file://" + apkFile.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mactivity.startActivity(intent);
    }

}
