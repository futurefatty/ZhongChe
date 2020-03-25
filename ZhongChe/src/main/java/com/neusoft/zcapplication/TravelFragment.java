package com.neusoft.zcapplication;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.neusoft.zcapplication.base.BaseFragment;


/**
 * 旅游
 * Created by Administrator on 2017/3/7.
 *
 */

public class TravelFragment extends BaseFragment implements View.OnClickListener{
    public static TravelFragment getInstance(){
        TravelFragment fragment = new TravelFragment();
        return fragment;
    }
    private RelativeLayout ly_failed_page;
    private LinearLayout ly_content;
    public Handler getHandler(){
        return handler;
    }
    private View frgView;
    private WebView webView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if(what == 1){
                if(webView.canGoBack()){
                    webView.goBack();
                }else{
                    getActivity().finish();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(null == frgView){
            frgView = inflater.inflate(R.layout.frg_travel_webview, container, false);
            initView();

        }else{
            ViewGroup parent = (ViewGroup) frgView.getParent();
            if(parent != null){
                parent.removeView(frgView);
            }
        }
        return frgView;
    }

    private void initView(){
        ly_failed_page = (RelativeLayout) frgView.findViewById(R.id.load_fail_page);
        ly_content = (LinearLayout) frgView.findViewById(R.id.content_ly);
        ly_content.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                webView.reload();
                webView.setVisibility(View.VISIBLE);
                ly_failed_page.setVisibility(View.GONE);
            }
        });
        webView = (WebView) frgView.findViewById(R.id.wv_travel_servie);
        WebSettings settings = webView.getSettings();
        settings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        settings.setAppCacheEnabled(true);
//        String url = "http://enjoyflyingnow.com/weixin/flight/trends";
        String url = "http://120.25.216.230/travel-web/phone/travel";
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("utf-8");
        //不加载缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.loadUrl(url);

        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
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
    }

    /***
     * 设置Web视图的方法
     */
    WebViewClient webClient = new WebViewClient(){//处理网页加载失败时
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            webView.setVisibility(View.GONE);
            ly_failed_page.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            view.loadUrl(url);
            return true;
        }
    };

    @Override
    public void onClick(View v) {

    }
}
