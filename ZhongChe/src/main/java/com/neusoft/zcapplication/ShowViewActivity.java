package com.neusoft.zcapplication;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
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
import com.crcc.commonlib.utils.FileProvider7;
import com.crcc.commonlib.utils.JSONUtils;
import com.crcc.commonlib.utils.UsefulToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.neusoft.zcapplication.Bean.FlightItem;
import com.neusoft.zcapplication.approval.ApprovalActivity;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.flight.inland.InlandFlightOrderDetailActivity;
import com.neusoft.zcapplication.tools.AMapUtil;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AndroidBug5497Workaround;
import com.neusoft.zcapplication.tools.AndroidFileUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.StringUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.StartMapPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 跳转网页页面
 */
public class ShowViewActivity extends BaseActivity {


    private TextView tv_title;
    private WebView webView;
    private RelativeLayout ly_failed_page;
    private String download = Environment.getExternalStorageDirectory() + "/download/enclosure/document/";
    /***
     * 设置Web视图的方法
     */
    private WebViewClient webClient = new WebViewClient() {//处理网页加载失败时
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//          //显示错误页面
            webView.setVisibility(View.GONE);
            ly_failed_page.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (getIntent().getBooleanExtra("isPic", false)) {
//                String javascript = "javascript:function ResizeImages() {" +
//                        "var myimg,oldwidth;" +
//                        "var maxwidth = document.body.clientWidth;" +
//                        "for(i=0;i <document.images.length;i++){" +
//                        "myimg = document.images[i];" +
//                        "if(myimg.width > maxwidth){" +
//                        "oldwidth = myimg.width;" +
//                        "myimg.width = maxwidth;" +
//                        "}" +
//                        "}" +
//                        "}";
//                String width = String.valueOf(DisplayUtil.getDeviceWidth(ShowViewActivity.this));
//                view.loadUrl(javascript);
//                view.loadUrl("javascript:ResizeImages();");
                webView.loadUrl("javascript:(function(){"
                        + "var objs = document.getElementsByTagName('img'); "
                        + "for(var i=0;i<objs.length;i++)  " + "{"
                        + "var img = objs[i];   "
                        + "    img.style.width = '100%';   "
                        + "    img.style.height = 'auto';   "
                        + "}" + "})()");

            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            if (url.startsWith("ctrip://")) {
                return false;
            } else {
                Log.i("--->", "加载：" + url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                boolean hasPay = getIntent().getBooleanExtra("hasPay", false);//true 会跳转支付
                String payType = null == getIntent().getStringExtra("payType")
                        ? "" : getIntent().getStringExtra("payType");
                if (hasPay) {
                    if ((url.contains("alipays") || url.contains("weixin"))) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } else {
                        if (payType.equals("payMent")) {
                            //订单支付
                            Map extraHeaders = new HashMap();
                            extraHeaders.put("Referer", "http://testh5.shinetour.com:8092");
                            view.loadUrl(url, extraHeaders);
                        } else {
                            view.loadUrl(url);
                        }
                    }
                } else {
                    if ((url.contains("alipays") || url.contains("weixin"))) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } else {
                        if (payType.equals("payMent")) {
                            //订单支付
                            Map extraHeaders = new HashMap();
                            extraHeaders.put("Referer", "http://testh5.shinetour.com:8092");
                            view.loadUrl(url, extraHeaders);
                        } else {
                            view.loadUrl(url);
                        }
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
    private boolean isOrderHandling;//下单中。。。
    private LinearLayout ly_content;
    private String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 提交订单页面，需要在h5端填写信息，
         * 若使用全屏模式或沉浸式模式时，弹出软键盘会将input标签遮住,
         * 这是一个系统bug
         */

        setContentView(R.layout.activity_show_view);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        AndroidBug5497Workaround.assistActivity(this);


        AppUtils.setStateBar(ShowViewActivity.this, findViewById(R.id.frg_status_bar));
        initView();

    }

    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }

        super.onDestroy();
    }

    public void initView() {
        boolean showMoreBtn = getIntent().getBooleanExtra("showMoreBtn", false);
        if (showMoreBtn) {
            //如果当前选择的是机票列表，则不显示更多按钮
            final boolean isHotel = getIntent().getBooleanExtra("isHotel", false);
            if (isHotel) {
                findViewById(R.id.btn_more_res).setVisibility(View.VISIBLE);
            }
            findViewById(R.id.btn_more_res).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShowViewActivity.this, ShowViewForNonBusinessActivity.class);
                    if (isHotel) {
//                        intent.putExtra("isHotel",true);//false机票 ，true 酒店
                        intent.putExtra("loginType", 2);//1机票 ，2 酒店 3 携程订单
                    } else {
//                        intent.putExtra("isHotel",false);//false机票 ，true 酒店
                        intent.putExtra("loginType", 1);//1机票 ，2 酒店 3 携程订单
                    }
                    String bills = null == getIntent().getStringExtra("bills")
                            ? "" : getIntent().getStringExtra("bills");
                    if (!bills.equals("")) {
                        int billLen = bills.split(",").length;
                        if (billLen > 1) {
                            AlertUtil.show2(ShowViewActivity.this, "由于携程限制，您只能选择一个申请单进行预定~", "确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                            return;
                        }
                        intent.putExtra("bills", bills);//预定申请单号
                    }
                    int corpPayType = getIntent().getIntExtra("dataType", 1);
                    intent.putExtra("dataType", corpPayType);
                    //入住人员工编号
                    String employeeCodes = null == getIntent().getStringExtra("employeeCodes")
                            ? "" : getIntent().getStringExtra("employeeCodes");
                    if (!employeeCodes.equals("")) {
                        intent.putExtra("employeeCodes", employeeCodes);//预定申请单号
                    }
                    intent.putExtra("url", "ctrip");
                    startActivity(intent);
                }
            });
        }
        //是否显示提示框
        boolean showAlert = getIntent().getBooleanExtra("showAlert", false);
        if (showAlert) {
            String text = "请注意【提交订单】时保险费用的勾选喔~";
//            AlertUtil.show(ShowViewActivity.this,text
//                    ,"确定",null,"取消",new AlertBtnClick(0),"温馨提示");
            AlertUtil.showAlert(ShowViewActivity.this, "温馨提示", text
                    , "知道了", null);
        }
        tv_title = (TextView) findViewById(R.id.title);
        LinearLayout btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBack();
            }
        });
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOrderHandling) {
                    showToast("订单正在提交中");
                    return;
                }
                if (url.equals("ctrip")) {
                    finish();
                } else {
                    //如果下单成功，用户按返回按钮就直接退回首页
                    if (backDirect) {
                        setResult(RESULT_OK, new Intent());
                        finish();
                    } else {
                        finish();
                    }
                }
            }
        });

        webView = (WebView) findViewById(R.id.webview);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setDefaultTextEncodingName("utf-8");
        //第二个参数，js使用该参数来调用第一个参数里面的方法
        webView.addJavascriptInterface(new MethodForJs(), "ZhongChe");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        settings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        //不加载缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(true);
//        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {
//            webView.setWebContentsDebuggingEnabled(true);
//        }

        final ProgressBar bar = (ProgressBar) findViewById(R.id.myProgressBar);

        webView.setWebChromeClient(new WebChromeClient() {
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
                    } else {
                        webView.setVisibility(View.VISIBLE);
                        ly_failed_page.setVisibility(View.GONE);
                    }
                }
            }

        });
        webView.setWebViewClient(webClient);
        webView.setDownloadListener(new DownloadStart());
        url = getIntent().getStringExtra("url");
        if (url.equals("ctrip")) {
            personalTicket();
//            webView.loadDataWithBaseURL("", html2,"text/html", "utf-8", null);
        } else {
            Log.i("--->", "showViewActivity链接：" + url);

            String payType = null == getIntent().getStringExtra("payType")
                    ? "" : getIntent().getStringExtra("payType");
            if ((url.contains("alipays") || url.contains("weixin"))) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                if (payType.equals("tc")) {
                    //同程跳转过来的
                    Map extraHeaders = new HashMap();
                    extraHeaders.put("Referer", "https://wx.17u.cn");
                    webView.loadUrl(url, extraHeaders);
                } else if (payType.equals("payMent")) {
                    //订单支付
                    Map extraHeaders = new HashMap();
                    extraHeaders.put("Referer", "http://testh5.shinetour.com:8092");
                    webView.loadUrl(url, extraHeaders);
                } else {
                    webView.loadUrl(url);
                }
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
        Map<String, Object> params = new HashMap<>();


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

    private void clickBack() {
        if (isOrderHandling) {
            showToast("订单正在提交中");
            return;
        }
        if (url.equals("ctrip")) {
            finish();
        } else {
            //如果下单成功，用户按返回按钮就直接退回首页
            if (backDirect) {
                setResult(RESULT_OK, new Intent());
                finish();
            } else {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        }
    }

    private void showPopWin(Map<String, String> map) {
        StartMapPopupWindow startMapPop = new StartMapPopupWindow(ShowViewActivity.this, map);
        //显示窗口
        startMapPop.showAtLocation(ShowViewActivity.this.findViewById(R.id.webview)
                , Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
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
            Map<String, Object> orderItem = (Map<String, Object>) getIntent().getSerializableExtra("orderItem");
//            double mobil = Double.parseDouble(orderItem.get("TELEPHONE").toString());
//            int telInt = (int)mobil;
//            BigDecimal bd = new BigDecimal(orderItem.get("TELEPHONE")+"");
//            String mc = bd.toPlainString();
            orderItem.put("TELEPHONE", orderItem.get("TELEPHONE"));
            JSONObject json = new JSONObject(orderItem);
            Log.i("--->", json.toString());
            return json.toString();
        }

        /**
         * 下单成功后，调用该方法
         */
        @JavascriptInterface
        public void orderSuccess() {
            backDirect = true;
            isOrderHandling = false;
        }

        /**
         * 下单中
         */
        @JavascriptInterface
        public void ordersubmit() {
            isOrderHandling = true;
        }

        @JavascriptInterface
        public void callHtHotelTel(String tel) {
            Uri uri = Uri.parse("tel:" + tel);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        /**
         * 弹出地图窗口
         *
         * @param lat
         * @param lng
         * @param addr
         */
        @JavascriptInterface
        public void showMapPop(final String lat, final String lng, final String addr) {
            Map<String, String> map = new HashMap<>();
            map.put("lat", lat);
            map.put("lng", lng);
            map.put("addr", addr);
            //showPopWin(map);

            String hotelAddress = getIntent().getStringExtra("hotelAddress");
            Double plng = getIntent().getDoubleExtra("plng", 0);
            Double plat = getIntent().getDoubleExtra("plat", 0);
            if (StringUtil.isEmpty(hotelAddress)) {
                //出发地为当前位置
                AMapUtil mAMapUtil = AMapUtil.getInstance();
                //定位获取当前位置
                mAMapUtil.initLocation();
                mAMapUtil.setAMapLocationHandler(new AMapUtil.AMapLocationHandler() {
                    @Override
                    public void locateSuccess(double mLongitude, double mLatitude, String province, String city,
                                              String cityCode, String district, String address) {
                        //导航开始位置
                        Poi startPoi = new Poi(address, new LatLng(mLatitude, mLongitude), "");
                        //导航结束位置
                        Poi endPoi = new Poi(addr, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), "");
                        /**
                         * 启动导航
                         */
                        AmapNaviPage.getInstance().showRouteActivity(ShowViewActivity.this,
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
            } else {
                //出发地不是当前位置
                Poi startPoi = new Poi(hotelAddress, new LatLng(Double.valueOf(plat), Double.valueOf(plng)), "");
                Poi endPoi = new Poi(addr, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), "");
                AmapNaviPage.getInstance().showRouteActivity(ShowViewActivity.this,
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


        /**
         * 预定申请调用
         * startCity	String	出发城市
         * toCity	String	到达城市
         * startCityCode	String	出发城市三字码
         * toCityCode	String	到达城市三字码
         * startDate	String	预定申请日期 格式yyyy-MM-dd
         *
         * @param data
         */
        @JavascriptInterface
        public void toApplyOrder(String data) {
            try {
                JSONObject object = new JSONObject(data);
                String startCity = (String) object.get("startCity");
                String toCity = (String) object.get("toCity");
                String startCityCode = (String) object.get("startCityCode");
                String toCityCode = (String) object.get("toCityCode");
                String startDate = (String) object.get("startDate");
                FlightItem flightItem = new FlightItem();
                flightItem.setFromCity(startCity);
                flightItem.setToCity(toCity);
                flightItem.setFromCityCode(startCityCode);
                flightItem.setToCityCode(toCityCode);
                flightItem.setStartTime(startDate);
                Intent intent = new Intent(ShowViewActivity.this, ApprovalActivity.class);
                intent.putExtra(ApprovalActivity.INNTEAR_FLIGHT_APPLY_ORDER, flightItem);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                UsefulToast.showToast(ShowViewActivity.this, "出现异常:" + e.getMessage());
            }
        }

        @JavascriptInterface
        public void submitOrder(String json) {
            Map<String, Object> map = JSONUtils.gsonToMaps(json);
            String url = (String) map.get("url");
            InlandFlightOrderDetailActivity.startActivity(ShowViewActivity.this, url,
                    InlandFlightOrderDetailActivity.CREATE_ORDER_WHEN);
            finish();
        }

        @JavascriptInterface
        public void submitChangeTicket(String url) {
            InlandFlightOrderDetailActivity.startActivity(ShowViewActivity.this, url,
                    InlandFlightOrderDetailActivity.SUBMIT_CHANGE_TICKET);
            finish();
        }
    }

    private class AlertBtnClick implements View.OnClickListener {
        //0取消按钮事件，1确定
        private int btnType;

        public AlertBtnClick(int btnType) {
            this.btnType = btnType;
        }

        @Override
        public void onClick(View v) {
            switch (btnType) {
                case 0:
                    finish();
                    break;
                case 1:
                    break;
            }
        }
    }

    class DownloadStart implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            if (!contentDisposition.contains("attachment;filename=")) {
                ToastUtil.toast("加载失败，文件为空");
                return;
            }
            String subStr = contentDisposition.substring(contentDisposition.length() - 4, contentDisposition.length());
            String fileName = contentDisposition.replace("attachment;filename=", "");
            if (fileName.length() == 0) {
                ToastUtil.toast("加载失败，文件为空");
                return;
            }
            Intent intent = new Intent(ShowViewActivity.this, TbsActivity.class);
            ;

            if (subStr.equals(".jpg") || subStr.equals(".gif") || subStr.equals(".png") || subStr.equals(".bmp")) {
                intent.putExtra("isPic", true);
                intent.putExtra("fileName", fileName);
                intent.putExtra("url", url);
                startActivity(intent);
            } else {
                //word浏览器
//                intent.putExtra("isPic",false);
                initDoc(fileName, url);
            }


        }
    }

    private void initDoc(String mfileName, String docUrl) {
        File docFile = new File(download, mfileName);
        if (docFile.exists()) {
            //存在本地;
            Log.d("print", "本地存在");
            Uri fileUri = FileProvider7.getUriForFile(ShowViewActivity.this, docFile);
            Intent in = AndroidFileUtil.openFile(download + mfileName + "", fileUri);
            startActivity(in);

        } else {
            OkGo.get(docUrl)//
                    .tag(this)//
                    .execute(new FileCallback(download, mfileName) {  //文件下载时，可以指定下载的文件目录和文件名
                        @Override
                        public void onSuccess(File file, Call call, Response response) {
                            // file 即为文件数据，文件保存在指定目录
                            Log.d("print", "下载文件成功");
//                            if (isPic){
//                                imageView.setImage(ImageSource.uri(download + mfileName));
//                            }else {
//                                displayFile(download+file.getName(), file.getName());
//                            }
                            Uri fileUri = FileProvider7.getUriForFile(ShowViewActivity.this, docFile);
                            Intent in = AndroidFileUtil.openFile(download + mfileName + "", fileUri);
                            startActivity(in);
//                            Log.d("print", "" + file.getName());

//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.addCategory(Intent.CATEGORY_DEFAULT);
//                            Uri uri = Uri.fromFile(file);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setDataAndType(uri, "application/msword");
//                            startActivity(intent);

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


}
