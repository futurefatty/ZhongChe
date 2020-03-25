package com.neusoft.zcapplication.update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.crcc.commonlib.utils.FileProvider7;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;

/**
 * 更新apk
 */
public class UpdateApk {

    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    private static final int CHANGE_TEXT = 3;
    private static final int STOP_LOADING = 4;
    //apk保存路径
    private String mSavePath = Environment.getExternalStorageDirectory() + "/crrc";

    private boolean cancelUpdate = false;

    private String url;
    private String apkName;
    private Context mContext;
    private LoadProgressDialog dialog;//下载提示框
    private DownloadThread loadThread;//下载线程
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD:

                    int currentProgress = (int) msg.obj;
                    dialog.setNowProgress(currentProgress + "%");
                    dialog.setProgress(currentProgress);
                    break;
                case DOWNLOAD_FINISH:
                    dialog.setNowProgress("100%");
                    dialog.setProgress(100);
                    String arr = UpdateApk.this.apkName;
                    while (arr.contains(".")) {
                        arr = arr.substring(arr.indexOf(".") + 1, arr.length());
                    }
                    if (arr.equals("apk")) {
                        installApk();
                    } else {
                        try {
                            Intent intent = UpdateApk.getWordFileIntent(mSavePath + "/" + UpdateApk.this.apkName);
                            ((Context) UpdateApk.this.mContext).startActivity(intent);
                        } catch (Exception e) {
//                            ShowToast.showToast(mContext, "文件无法打开!");
                        }
                    }
                    break;
                case CHANGE_TEXT:
                    dialog.setSize(msg.arg1 + "m");
                    break;
                case STOP_LOADING:
                    ((Activity) mContext).finish();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 构造方法
     */
    public UpdateApk(Context context, String url, String name) {
        this.mContext = context;
        this.url = url;
        this.apkName = name;
        dialog = new LoadProgressDialog(context);
        String titleStr = "正在下载";
        String cancelStr = "取消";
        dialog.setProgress(0).setTitle(titleStr).setCancelButton(cancelStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"----",Toast.LENGTH_LONG).show();
                //停止下载
                cancelUpdate = true;
                Message msg = mHandler.obtainMessage();
                msg.what = STOP_LOADING;
                mHandler.sendMessage(msg);
//                if(null != loadThread){
//                    loadThread.stop();
//                }
            }
        }).setCancelable(false);
        dialog.show();
        //开始下载
        loadThread = new DownloadThread(url);
        loadThread.start();
    }

    /**
     * 打开文档
     *
     * @param path
     * @return
     */
    public static Intent getWordFileIntent(String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        String arr = path;
        while (arr.contains(".")) {
            arr = arr.substring(arr.indexOf(".") + 1, arr.length());
        }
        if (arr.equals("doc") || arr.equals("docx") || arr.equals("txt")) {
            intent.setDataAndType(uri, "application/msword");
        } else if (arr.equals("xls")) {
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (arr.equals("pdf")) {
            intent.setDataAndType(uri, "application/pdf");
        } else if (arr.equals("jpg") || arr.equals("jpeg")) {
            intent.setDataAndType(uri, "image/jpeg");
        } else if (arr.equals("png")) {
            intent.setDataAndType(uri, "image/png");
        } else if (arr.equals("gif")) {
            intent.setDataAndType(uri, "image/gif");
        }
        return intent;
    }

    /**
     * apk下载完成后，调用此方法，安装Apk文件
     */
    private void installApk() {
        File apkFile = new File(mSavePath, apkName);
        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            第二个参数便是 Manifest 文件中注册 FileProvider 时设置的 authorities 属性值，
// 第三个参数为要共享的文件，filepath.xml中  path 属性中添加的子目录里面。
            Uri uriForFile = FileProvider7.getUriForFile(mContext, apkFile);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            i.setDataAndType(uriForFile, mContext.getContentResolver().getType(uriForFile));
            //"application/vnd.android.package-archive"是文件类型，具体对应apk类型。
            i.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            mContext.startActivity(i);
        } else {
//            i.setDataAndType(Uri.fromFile(apkFile), getMIMEType(apkFile));
            i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            //设置最后安装好了，点打开，打开新版本应用。
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
            //设置安装完成后提示完成、打开
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }

    /**
     * 下载线程
     */
    private class DownloadThread extends Thread {
        private String url;
        private NumberFormat numberFormat;

        public DownloadThread(String loadUrl) {
            this.url = loadUrl;
            numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMinimumIntegerDigits(0);//不保留小数位
        }

        @Override
        public void run() {
            try {
                URL uri = new URL(url);

                HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                if (length != -1) {
                    int fileSize = length / 1024 / 1024;
                    if (null != dialog) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = CHANGE_TEXT;
                        msg.arg1 = fileSize;
                        Log.e("--->", "apk大小：" + length);
//                        dialog.setSize(fileSize + "m");
                    }
                }
                InputStream is = conn.getInputStream();

                File file = new File(mSavePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                File downFile = new File(mSavePath, apkName);
                if (!downFile.exists()) {
                    downFile.createNewFile();
                } else {
                    downFile.delete();
                    downFile.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(downFile);
                int count = 0;
                byte buf[] = new byte[1024];


                do {
                    int numread = is.read(buf);

//                    Log.i("--->","^^^^^^^^^^^^^^^read:"+numread);
                    count += numread;
                    int currentProgress = (int) (((float) count / length) * 100);
                    Message msg = mHandler.obtainMessage();
                    msg.what = DOWNLOAD;
                    msg.obj = currentProgress;
                    mHandler.sendMessage(msg);
                    if (numread <= 0) {
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!cancelUpdate);
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
//                Log.e("--->","   MalformedURLException"+e);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != dialog) {
                    dialog.dismiss();
                }
            }
        }
    }
}
