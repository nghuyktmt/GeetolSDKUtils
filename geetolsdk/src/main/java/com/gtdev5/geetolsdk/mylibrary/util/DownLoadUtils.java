package com.gtdev5.geetolsdk.mylibrary.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
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
import java.net.URL;
import java.net.URLConnection;

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
                    //下载路径，如果路径无效了，可换成你的下载路径
                    String path = Environment.getExternalStorageDirectory() + "/" + "downloadApks/";
                    if (!new File(path).exists())
                        new File(path).mkdirs();
                    final long startTime = System.currentTimeMillis();
                    //下载函数
                    fileName = url.substring(url.lastIndexOf("/") + 1);
                    //获取文件名
                    URL myURL = new URL(url);
                    URLConnection conn = myURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    int fileSize = conn.getContentLength();//根据响应获取文件大小
                    if (fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");
                    if (is == null) throw new RuntimeException("stream is null");
                    File file1 = new File(path);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    //把数据存入路径+文件名
                    FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
                    byte buf[] = new byte[1024];
                    int downLoadFileSize = 0;
                    do {
                        //循环读取
                        int numread = is.read(buf);
                        if (numread == -1) {
                            break;
                        }
                        fos.write(buf, 0, numread);
                        downLoadFileSize += numread;
                        mProgress = (downLoadFileSize * 100) / fileSize;
                        if (mProgress <= 100 && mProgress >= 0)
                            mUpdateProgressHandler.sendEmptyMessage(1);
                        Log.e("LogUtils", "mProgress  " + numread);

                    } while (true);
                    // 下载完成
                    mUpdateProgressHandler.sendEmptyMessage(2);
                    Log.i("DOWNLOAD", "download success");
                    Log.i("DOWNLOAD", "totalTime=" + (System.currentTimeMillis() - startTime));

                    is.close();
                } catch (Exception ex) {
                    Log.e("DOWNLOAD", "error: " + ex.getMessage(), ex);
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

        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mactivity.getApplication()
                    , mactivity.getApplication().getPackageName()+".Geetolprovider",
                    apkFile);
        } else {
            fileUri = Uri.fromFile(apkFile);
        }
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installIntent.setAction(Intent.ACTION_VIEW);
        installIntent.setDataAndType(fileUri,
                "application/vnd.android.package-archive");
        mactivity.startActivity(installIntent);


//        Uri uri = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(mactivity
//                    , mactivity.getApplicationContext().getPackageName() + ".FileProvider", apkFile);
//            intent.setDataAndType(contentUri, "video/*");
//        } else {
//            uri = Uri.fromFile(apkFile);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(uri, "video/*");
//        }
//
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        mactivity.startActivity(intent);
    }

}
