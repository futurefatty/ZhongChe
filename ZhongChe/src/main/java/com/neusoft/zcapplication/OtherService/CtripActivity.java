package com.neusoft.zcapplication.OtherService;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewForNonBusinessActivity;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;

/**
 * 跳转
 */
public class CtripActivity extends BaseActivity {
    private TextView tv_title;
    private WebView webView;
    private RelativeLayout ly_failed_page;
    /***
     * 设置Web视图的方法
     */
    private WebViewClient webClient = new WebViewClient(){//处理网页加载失败时
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//          //显示错误页面
            webView.setVisibility(View.GONE);
            ly_failed_page.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            if(url.startsWith("ctrip://")){
                return false;
            }else{
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                boolean hasPay = getIntent().getBooleanExtra("hasPay",false);//true 会跳转支付
                if(hasPay){
                    if((url.contains("alipays") || url.contains("weixin")) ){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }else {
                        view.loadUrl(url);
                    }
                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        }
//        设置WebView接受所有网站的证书
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    };
    private boolean backDirect;//用户点击返回按钮，直接退到首页(用于用户下单成功后)
    private LinearLayout ly_content;
    private String url = "";
//    private String html2 = "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><script type=\"text/javascript\">function formSubmit(){document.getElementById(\"fLogin\").submit();}</script></head><body><form name=\"fLogin\" id=\"fLogin\" method=\"post\" action=\"https://www.corporatetravel.ctrip.com/corpservice/authorize/login\"><input type=\"hidden\" name=\"AppKey\" value=\"obk_ZCZZDLYJS\" /><input type=\"hidden\" name=\"Ticket\" value=\"59e5daa36c9c4265900664d1\" /><input type=\"hidden\" name=\"EmployeeID\" value=\"123\"/><input type=\"hidden\" name=\"Signature\" value=\"8550cbb560d83a3b2881a53892f32d59\"/><script language=\"javascript\">formSubmit();</script></form></body>";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 提交订单页面，需要在h5端填写信息，
         * 若使用全屏模式或沉浸式模式时，弹出软键盘会将input标签遮住,
         * 这是一个系统bug
         */
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_show_view);
        AppUtils.setStateBar(CtripActivity.this,findViewById(R.id.frg_status_bar));
        initView();
    }

    public void initView() {
        boolean showMoreBtn = getIntent().getBooleanExtra("showMoreBtn",false);
        if (showMoreBtn){
            //如果当前选择的是机票列表，则不显示更多按钮
            final boolean isHotel = getIntent().getBooleanExtra("isHotel",false);
            if(isHotel){
                findViewById(R.id.btn_more_res).setVisibility(View.VISIBLE);
            }
            findViewById(R.id.btn_more_res).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CtripActivity.this,ShowViewForNonBusinessActivity.class);
                    if (isHotel){
//                        intent.putExtra("isHotel",true);//false机票 ，true 酒店
                        intent.putExtra("loginType",2);//1机票 ，2 酒店 3 携程订单
                    }else {
//                        intent.putExtra("isHotel",false);//false机票 ，true 酒店
                        intent.putExtra("loginType",1);//1机票 ，2 酒店 3 携程订单
                    }
                    String bills = null == getIntent().getStringExtra("bills")
                            ? "" : getIntent().getStringExtra("bills");
                    if(!bills.equals("")){
                        int billLen = bills.split(",").length;
                        if(billLen > 1){
                            AlertUtil.show2(CtripActivity.this, "最多只能选择一个预定申请单预定携程酒店~", "确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                            return;
                        }
                        intent.putExtra("bills",bills);//预定申请单号
                    }
                    int corpPayType = getIntent().getIntExtra("dataType",1);
                    intent.putExtra("dataType",corpPayType);
                    //入住人员工编号
                    String employeeCodes = null == getIntent().getStringExtra("employeeCodes")
                            ? "" : getIntent().getStringExtra("employeeCodes");
                    if(!employeeCodes.equals("")){
                        intent.putExtra("employeeCodes",employeeCodes);//预定申请单号
                    }
                    intent.putExtra("url","ctrip");
                    startActivity(intent);
                }
            });
        }
        tv_title = (TextView) findViewById(R.id.title);
        LinearLayout btn_back = (LinearLayout)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView)findViewById(R.id.webview);

        ly_failed_page = (RelativeLayout) findViewById(R.id.load_fail_page);
        ly_content = (LinearLayout) findViewById(R.id.content_ly);
        ly_content.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                webView.reload();
                webView.setVisibility(View.VISIBLE);
                ly_failed_page.setVisibility(View.GONE);
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setBlockNetworkImage(false);//解决图片不显示
        //
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setDefaultTextEncodingName("utf-8");
        //第二个参数，js使用该参数来调用第一个参数里面的方法
//        webView.addJavascriptInterface(new MethodForJs(), "ZhongChe");
        settings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        //不加载缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);

        final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBar);
        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title.setText(title);
                // android 6.0 以下通过title获取
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (title.contains("404") || title.contains("500") || title.contains("Error") || title.contains("找不到网页")) {
                        //隐藏系统默认的网页加载失败页面
                        webView.setVisibility(View.GONE);
                        ly_failed_page.setVisibility(View.VISIBLE);
                    }
                    else {
                        webView.setVisibility(View.VISIBLE);
                        ly_failed_page.setVisibility(View.GONE);
                    }
                }
            }
        });
        webView.setWebViewClient(webClient);
        url = getIntent().getStringExtra("url");
        webView.loadUrl(url);

    }
}
