package com.neusoft.zcapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.api.SupplierApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.neusoft.zcapplication.R.id.webview;

/**
 * 非公机票、非公酒店页面
 */

public class ShowViewForNonBusinessActivity extends BaseActivity {

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
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器\
            if(url.startsWith("ctrip://")){
                return false;
            }else{
                if((url.contains("alipays") || url.contains("weixin")) ){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        }
    };

    private LinearLayout ly_content;
    private String url = "";
//    private String html2 = "<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><script type=\"text/javascript\">function formSubmit(){document.getElementById(\"fLogin\").submit();}</script></head><body><form name=\"fLogin\" id=\"fLogin\" method=\"post\" action=\"https://www.corporatetravel.ctrip.com/corpservice/authorize/login\"><input type=\"hidden\" name=\"AppKey\" value=\"obk_ZCZZDLYJS\" /><input type=\"hidden\" name=\"Ticket\" value=\"59e5daa36c9c4265900664d1\" /><input type=\"hidden\" name=\"EmployeeID\" value=\"123\"/><input type=\"hidden\" name=\"Signature\" value=\"8550cbb560d83a3b2881a53892f32d59\"/><script language=\"javascript\">formSubmit();</script></form></body>";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        /**
//         * 提交订单页面，需要在h5端填写信息，
//         * 若使用全屏模式或沉浸式模式时，弹出软键盘会将input标签遮住,
//         * 这是一个系统bug
//         */

        setContentView(R.layout.activity_show_view);
        AppUtils.setStateBar(ShowViewForNonBusinessActivity.this,findViewById(R.id.frg_status_bar));
        initView();
    }

    public void initView() {
        findViewById(R.id.btn_more_res).setVisibility(View.GONE);
        tv_title = (TextView) findViewById(R.id.title);
        LinearLayout btn_back = (LinearLayout)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
            }
        });
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
            }
        });

        webView = (WebView)findViewById(webview);
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

        url = getIntent().getStringExtra("url");
        if(url.equals("ctrip")) {
            personalTicket();
//            webView.loadDataWithBaseURL("", html2,"text/html", "utf-8", null);
        }else {
//            Log.i("--->","链接：" + url);
            webView.loadUrl(url);
//            MhethodForJs m = new MhethodForJs();
//            m.getOrderInfo();
        }
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("utf-8");
        //第二个参数，js使用该参数来调用第一个参数里面的方法
        webView.addJavascriptInterface(new MhethodForJs(), "ZhongChe");
        settings.setBlockNetworkImage(false);//解决图片不显示
        //
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        //不加载缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);

        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
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
    }

    @Override
    public void onBackPressed() {
        clickBack();
    }

    /**
     * 获取国内因私机票HTML
     */
    private void personalTicket() {
        Map<String,Object> params = new HashMap<>();
        //预定申请单里面的入住人（乘机人）的员工编号
        String employeeCodes = null == getIntent().getStringExtra("employeeCodes")
                ? "" : getIntent().getStringExtra("employeeCodes");
        if(employeeCodes.equals("")){
            User user = new AppUtils().getUserInfo(ShowViewForNonBusinessActivity.this);
            params.put("employeeId", user.getEmployeeCode());
        }else{
            params.put("employeeId", employeeCodes);
        }
        params.put("ciphertext","test");
//        params.put("employeeId", 20107090);
        params.put("interfaceType",2);
//        boolean isHotel = getIntent().getBooleanExtra("isHotel",false);
//        int loginType = isHotel? 2 : 1;
        int loginType= getIntent().getIntExtra("loginType",1);
        int corpPayType = getIntent().getIntExtra("dataType",1);
//        Log.i("--->","类型：" + loginType + ";因公、因私 ：" + corpPayType);
        params.put("loginType",loginType);//1机票 ，2 酒店 3 携程订单
        params.put("corpPayType",corpPayType);//1因公 ，2 因私
        params.put("style",4);
        String bills = getIntent().getStringExtra("bills");
        if(null != bills && !bills.equals("")){
            params.put("orderId",bills);//预定申请单号(只有预定酒店的情况下才传入该参数)
        }

        showLoading();
        RetrofitFactory.getInstance().createApi(SupplierApi.class).personalH5Ticket(params)
                .enqueue(new CallBack<String>() {
                    @Override
                    public void success(String url) {
                        dismissLoading();
                        String baseUrl = "androidwebdata://" + System.currentTimeMillis();
                        webView.loadDataWithBaseURL(baseUrl, url,"text/html", "utf-8", null);
                        int loginType= getIntent().getIntExtra("loginType",1);//1机票 ，2 酒店 3 携程订单
                        if(loginType == 2){
                            AlertUtil.showAlert(mContext,"温馨提示",
                                    "请勿自行添加入住人、入住房间数等，否则无法正常关联报销！","知道了",null);
                        }
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    private void clickBack(){
        if(url.equals("ctrip")){
            finish();
        }else{
            if(webView.canGoBack()){
                webView.goBack();
            }else{
                finish();
            }
        }
    }

    // js调用的类
    public class MhethodForJs {

        @JavascriptInterface
        public void back() {
            finish();
        }

        /**
         * 订单改签获取订单信息
         */
        @JavascriptInterface
        public String getOrderInfo() {
//            Bundle bundle = getIntent().getExtras();
//            BunbleParam bunbleParam = (BunbleParam)bundle.getSerializable("orderItem");
//            Map<String,Object> orderItem = bunbleParam.getMap();
            Map<String,Object> orderItem = (Map<String, Object>)getIntent().getSerializableExtra("orderItem");
//            double mobil = Double.parseDouble(orderItem.get("TELEPHONE").toString());
//            int telInt = (int)mobil;
            BigDecimal bd = new BigDecimal(orderItem.get("TELEPHONE")+"");
            String mc = bd.toPlainString();
            orderItem.put("TELEPHONE",mc);
//            Log.i("*****:mc",mc);
//            Log.i("*****:orderItem",orderItem.toString());
            JSONObject json = new JSONObject(orderItem);
//            Log.i("*****json.toString()",json.toString());
            return json.toString();
        }
    }

}
