package com.neusoft.zcapplication;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AppUtils;
//import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class TbsActivity extends BaseActivity implements View.OnClickListener {

    private SubsamplingScaleImageView imageView;
//    private TbsReaderView mTbsReaderView;
    private FrameLayout frameLayout;
    private String mfileName= "";
    private String docUrl = "";
    private boolean isPic ;
    private String download = Environment.getExternalStorageDirectory() + "/download/enclosure/document/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tbs);
        AppUtils.setStateBar(TbsActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        docUrl =  getIntent().getStringExtra("url");
        mfileName = getIntent().getStringExtra("fileName");

        isPic = getIntent().getBooleanExtra("isPic",false);
        if (isPic){
            imageView = (SubsamplingScaleImageView) findViewById(R.id.picDetailImageView);
            imageView.setBackgroundColor(getResources().getColor(R.color.type_tab_unchecked));
            imageView.setVisibility(View.VISIBLE);
        }else {
//            mTbsReaderView = new TbsReaderView(this, this);
//            frameLayout = (FrameLayout) findViewById(R.id.tbsView);
//            frameLayout.setVisibility(View.VISIBLE);
//            frameLayout.addView(mTbsReaderView);
        }

        initDoc();
    }

    private void initDoc() {
        File docFile = new File(download, mfileName);
        if (docFile.exists()) {
            //存在本地;
            Log.d("print", "本地存在");
            if (isPic){
                imageView.setImage(ImageSource.uri(download + mfileName));
            }else {
                displayFile(docFile.toString(),  mfileName);
            }
        } else {
            OkGo.get(docUrl)//
                    .tag(this)//
                    .execute(new FileCallback(download, mfileName) {  //文件下载时，可以指定下载的文件目录和文件名
                        @Override
                        public void onSuccess(File file, Call call, Response response) {
                            // file 即为文件数据，文件保存在指定目录
                            Log.d("print", "下载文件成功");
                            if (isPic){
                                imageView.setImage(ImageSource.uri(download + mfileName));
                            }else {
                                displayFile(download+file.getName(), file.getName());
                            }
                            Log.d("print", "" + file.getName());
                        }

                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            //这里回调下载进度(该回调在主线程,可以直接更新ui)
                            Log.d("print", "总大小---" + totalSize + "---文件下载进度---" + progress);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                        }
                    });
        }

    }

    private String tbsReaderTemp = Environment.getExternalStorageDirectory() + "/TbsReaderTemp";
    private void displayFile(String filePath, String fileName) {

        //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
        String bsReaderTemp = tbsReaderTemp;
        File bsReaderTempFile =new File(bsReaderTemp);
        if (!bsReaderTempFile.exists()) {
            Log.d("print","准备创建/TbsReaderTemp！！");
            boolean mkdir = bsReaderTempFile.mkdir();
            if(!mkdir){
                Log.d("print","创建/TbsReaderTemp失败！！！！！");
            }
        }
        Bundle bundle = new Bundle();
   /*   1.TbsReader: Set reader view exception:Cannot add a null child view to a ViewGroup
        TbsReaderView: OpenFile failed! [可能是文件的路径错误]*/
   /*   2.插件加载失败
        so文件不支持;*/
   /*
   ndk {
            //设置支持的SO库架构  'arm64-v8a',
            abiFilters 'armeabi', "armeabi-v7a",  'x86'
        } */
   /*
        3.自适应大小

    */
        Log.d("print","filePath"+filePath);//可能是路径错误
        Log.d("print","tempPath"+tbsReaderTemp);
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", tbsReaderTemp);
//        boolean result = mTbsReaderView.preOpen(getFileType(fileName), false);
//        Log.d("print","查看文档---"+result);
//        if (result) {
//            mTbsReaderView.openFile(bundle);
//        }else{
//
//        }
//        mTbsReaderView.openFile(bundle);
    }

    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d("print", "paramString---->null");
            return str;
        }
        Log.d("print", "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d("print", "i <= -1");
            return str;
        }

        str = paramString.substring(i + 1);
        Log.d("print", "paramString.substring(i + 1)------>" + str);
        return str;
    }
//    @Override
//    public void onCallBackAction(Integer integer, Object o, Object o1) {
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mTbsReaderView != null){
//            mTbsReaderView.onStop();
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;

        }
    }
}
