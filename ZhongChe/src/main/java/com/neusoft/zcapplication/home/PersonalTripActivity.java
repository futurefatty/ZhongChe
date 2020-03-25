package com.neusoft.zcapplication.home;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewActivity;
import com.neusoft.zcapplication.ShowViewForNonBusinessActivity;
import com.neusoft.zcapplication.api.SupplierApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AMapUtil;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AndroidBug5497Workaround;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.StartMapPopupWindow;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 网页跳转页面
 */
public class PersonalTripActivity extends BaseActivity {

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
                    if((url.contains("alipays") || url.contains("weixin")) ){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }else{
                        view.loadUrl(url);
                    }
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
        setContentView(R.layout.activity_personal_trip);
        AndroidBug5497Workaround.assistActivity(this);
        AppUtils.setStateBar(PersonalTripActivity.this,findViewById(R.id.frg_status_bar));
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
                    Intent intent = new Intent(PersonalTripActivity.this,ShowViewForNonBusinessActivity.class);
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
                            AlertUtil.show2(PersonalTripActivity.this, "最多只能选择一个预定申请单预定携程酒店~", "确定", new View.OnClickListener() {
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
        //是否显示提示框
        boolean showAlert = getIntent().getBooleanExtra("showAlert",false);
        if(showAlert){
            String text = "请注意【提交订单】时保险费用的勾选喔~";
            AlertUtil.show(PersonalTripActivity.this,text
                    ,"确定",null,"取消",new AlertBtnClick(0),"温馨提示");
        }
        tv_title = (TextView) findViewById(R.id.title);
        LinearLayout btn_back = (LinearLayout)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
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
        webView.addJavascriptInterface(new MethodForJs(), "ZhongChe");

        settings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        //不加载缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.setWebContentsDebuggingEnabled(true);
//        }

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
                if (url.contains("view.officeapps.live")){
                    tv_title.setText("附件详情");
                }else {
                    tv_title.setText(title);
                }
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
        if(url.equals("ctrip")) {
            personalTicket();
//            webView.loadDataWithBaseURL("", html2,"text/html", "utf-8", null);
        }else{
            Log.i("--->","showViewActivity链接：" + url);

            String payType = null == getIntent().getStringExtra("payType")
                    ?"" : getIntent().getStringExtra("payType");
            if(payType.equals("tc")){
                //同程跳转过来的
                Map extraHeaders = new HashMap();
                extraHeaders.put("Referer", "https://wx.17u.cn");
                webView.loadUrl(url,extraHeaders);
            }else{
                webView.loadUrl(url);
            }
        }
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
        User user = AppUtils.getUserInfo(PersonalTripActivity.this);
        params.put("ciphertext","test");
        params.put("employeeId", user.getEmployeeCode());
//        params.put("employeeId", 20107090);
        params.put("interfaceType",2);
        boolean isHotel = getIntent().getBooleanExtra("isHotel",false);
        int loginType = isHotel? 2 : 1;
        params.put("loginType",loginType);//1机票 ，2 酒店
        params.put("style",4);
        String bills = getIntent().getStringExtra("bills");
        if(!bills.equals("")){
            params.put("orderId",bills);//预定申请单号
        }

        /*showLoading();
        RetrofitFactory.getInstance().createApi(SupplierApi.class).personalH5Ticket(params)
                .enqueue(new CallBack<String>() {
                    @Override
                    public void success(String url) {
                        dismissLoading();
//                        webView.loadData( data,"text/html", "utf-8" );
                        String baseUrl = "androidwebdata://" + System.currentTimeMillis();
                        webView.loadDataWithBaseURL(baseUrl, url,"text/html", "utf-8", null);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });*/
    }

    private void clickBack(){
        if(url.equals("ctrip") || url.contains("view.officeapps.live")){
            finish();
        }else{
            //如果下单成功，用户按返回按钮就直接退回首页
            if(backDirect){
                setResult(RESULT_OK,new Intent());
                finish();
            }else{
                if(webView.canGoBack()){
                    webView.goBack();
                }else{
                    finish();
                }
            }
        }
    }

    /**
     *
     * @param map
     */
    private void showPopWin(Map<String,String> map){
        StartMapPopupWindow startMapPop = new StartMapPopupWindow(PersonalTripActivity.this,map);
        //显示窗口
        startMapPop.showAtLocation(PersonalTripActivity.this.findViewById(R.id.webview)
                , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//        lp.alpha = 0.4f;
        this.getWindow().setAttributes(lp);
    }

    // js调用的类
    public class MethodForJs {

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
            JSONObject json = new JSONObject(orderItem);
//            Log.i("--->",json.toString());
            return json.toString();
        }

        /**
         * 下单成功后，调用该方法
         */
        @JavascriptInterface
        public void orderSuccess(){
            backDirect = true;
        }

        @JavascriptInterface
        public void callHtHotelTel(String tel){
            Uri uri = Uri.parse("tel:" + tel);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        @JavascriptInterface
        public void showMapPop(final String lat, final String lng, final String addr){
            Map<String,String> map = new HashMap<>();
            map.put("lat",lat);
            map.put("lng",lng);
            map.put("addr",addr);
            //showPopWin(map);

            String hotelAddress = getIntent().getStringExtra("hotelAddress");
            Double plng = getIntent().getDoubleExtra("plng",0);
            Double plat = getIntent().getDoubleExtra("plat",0);
            if(StringUtil.isEmpty(hotelAddress)) {
                //出发地为当前位置
                AMapUtil mAMapUtil = AMapUtil.getInstance();
                mAMapUtil.initLocation();
                mAMapUtil.setAMapLocationHandler(new AMapUtil.AMapLocationHandler() {
                    @Override
                    public void locateSuccess(double mLongitude, double mLatitude, String province, String city,
                                              String cityCode, String district, String address) {
                        Poi startPoi = new Poi(address, new LatLng(mLatitude,mLongitude), "");
                        Poi endPoi = new Poi(addr, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), "");
                        AmapNaviPage.getInstance().showRouteActivity(PersonalTripActivity.this,
                                new AmapNaviParams(startPoi, null, endPoi, AmapNaviType.DRIVER),
                                new INaviInfoCallback() {
                                    @Override
                                    public void onInitNaviFailure() {
                                        //导航初始化失败时的回调函数
                                        ToastUtil.toast("导航初始化失败");
                                    }

                                    @Override
                                    public void onGetNavigationText(String s) {
                                        //导航播报信息回调函数

                                    }

                                    @Override
                                    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
                                        //当GPS位置有更新时的回调函数

                                    }

                                    @Override
                                    public void onArriveDestination(boolean b) {
                                        //到达目的地后回调函数

                                    }

                                    @Override
                                    public void onStartNavi(int i) {
                                        //启动导航后的回调函数

                                    }

                                    @Override
                                    public void onCalculateRouteSuccess(int[] ints) {
                                        //算路成功回调

                                    }

                                    @Override
                                    public void onCalculateRouteFailure(int i) {
                                        //步行或者驾车路径规划失败后的回调函数

                                    }

                                    @Override
                                    public void onStopSpeaking() {
                                        //停止语音回调，收到此回调后用户可以停止播放语音

                                    }

                                    @Override
                                    public void onReCalculateRoute(int i) {

                                    }

                                    @Override
                                    public void onExitPage(int i) {

                                    }

                                    @Override
                                    public void onStrategyChanged(int i) {

                                    }

                                    @Override
                                    public View getCustomNaviBottomView() {
                                        return null;
                                    }

                                    @Override
                                    public View getCustomNaviView() {
                                        return null;
                                    }

                                    @Override
                                    public void onArrivedWayPoint(int i) {

                                    }
                                });
                    }

                    @Override
                    public void locateFailed(String errorMessage) {
                        ToastUtil.toast(errorMessage);
                    }
                });
                mAMapUtil.startLocation();
            }else{
                //出发地不是当前位置
                Poi startPoi = new Poi(hotelAddress, new LatLng(Double.valueOf(plat),Double.valueOf(plng)), "");
                Poi endPoi = new Poi(addr, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), "");
                AmapNaviPage.getInstance().showRouteActivity(PersonalTripActivity.this,
                        new AmapNaviParams(startPoi, null, endPoi, AmapNaviType.DRIVER),
                        new INaviInfoCallback() {
                            @Override
                            public void onInitNaviFailure() {
                                //导航初始化失败时的回调函数
                                ToastUtil.toast("导航初始化失败");
                            }

                            @Override
                            public void onGetNavigationText(String s) {
                                //导航播报信息回调函数

                            }

                            @Override
                            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
                                //当GPS位置有更新时的回调函数

                            }

                            @Override
                            public void onArriveDestination(boolean b) {
                                //到达目的地后回调函数

                            }

                            @Override
                            public void onStartNavi(int i) {
                                //启动导航后的回调函数

                            }

                            @Override
                            public void onCalculateRouteSuccess(int[] ints) {
                                //算路成功回调

                            }

                            @Override
                            public void onCalculateRouteFailure(int i) {
                                //步行或者驾车路径规划失败后的回调函数

                            }

                            @Override
                            public void onStopSpeaking() {
                                //停止语音回调，收到此回调后用户可以停止播放语音

                            }

                            @Override
                            public void onReCalculateRoute(int i) {

                            }

                            @Override
                            public void onExitPage(int i) {

                            }

                            @Override
                            public void onStrategyChanged(int i) {

                            }

                            @Override
                            public View getCustomNaviBottomView() {
                                return null;
                            }

                            @Override
                            public View getCustomNaviView() {
                                return null;
                            }

                            @Override
                            public void onArrivedWayPoint(int i) {

                            }
                        });
            }

        }

    }

    private class AlertBtnClick implements View.OnClickListener{
        //0取消按钮事件，1确定
        private int btnType;
        public AlertBtnClick( int btnType){
            this.btnType = btnType;
        }
        @Override
        public void onClick(View v) {
            switch (btnType){
                case 0:
                    finish();
                    break;
                case 1:
                    break;
            }
        }
    }

}
