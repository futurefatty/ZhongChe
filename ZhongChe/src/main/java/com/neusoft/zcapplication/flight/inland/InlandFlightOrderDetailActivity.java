package com.neusoft.zcapplication.flight.inland;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crcc.commonlib.base.SyBaseActivity;
import com.crcc.commonlib.utils.StatusBarUtil;
import com.crcc.commonlib.utils.ZCWebViewSettings;
import com.just.agentweb.AgentWeb;
import com.neusoft.zcapplication.MainActivity;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.mine.order.OrderActivity;
import com.neusoft.zcapplication.tools.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InlandFlightOrderDetailActivity extends SyBaseActivity {
    @BindView(R.id.comm_title_back)
    LinearLayout commTitleBack;
    @BindView(R.id.comm_title_close)
    TextView commTitleClose;
    @BindView(R.id.comm_title_title)
    TextView commTitleTitle;
    @BindView(R.id.comm_title_toolbar)
    Toolbar commTitleToolbar;
    private AgentWeb mAgentWeb;
    private static final String URL = "URL";
    private static final String OPEN_WHEN = "OPEN_WHEN";

    /**
     * 国内h5机票提交订单
     */
    public static final int CREATE_ORDER_WHEN = 0X0001;

    /**
     * 国内h5机票改签
     */
    public static final int SUBMIT_CHANGE_TICKET = 0X0002;

    /**
     * 机票订单列表进来
     */
    public static final int ORDER_LIST_PAY_WHEN = 0X0003;


    private AndroidInterface nAndroidInterface;
    /**
     * 微信
     */
    public static final String WEBCHAT_PAY_SCHEME = "weixin://wap/pay?";

    @Override
    protected void initView(Bundle savedInstanceState) {
        new Builder(this)
                .isMuliteView(false)
                .setTitleModel(Builder.NO_TITLE)
                .build();
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        setSupportActionBar(commTitleToolbar);
        nAndroidInterface = new AndroidInterface();
        View noNetWorkView = View.inflate(this, R.layout.layout_no_net_work, null);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((ViewGroup) findViewById(R.id.webView_container), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimary))
                .addJavascriptInterface("ZhongChe", nAndroidInterface)
                .setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        commTitleTitle.setText(title);
                    }

                    @Override
                    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                        return super.onJsAlert(view, url, message, result);
                    }
                })
                .setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (isIntercept(url)) return true;
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        String url = request.getUrl().toString();
                        if (isIntercept(url)) return true;
                        return super.shouldOverrideUrlLoading(view, request);
                    }
                })
                .setAgentWebWebSettings(new ZCWebViewSettings())
                .setMainFrameErrorView(noNetWorkView)
                .createAgentWeb()
                .ready()
                .go(getIntent().getStringExtra(URL));
    }

    private boolean isIntercept(String url) {
        if (url.startsWith(WEBCHAT_PAY_SCHEME)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            return true;
        }
        return false;
    }


    public static void startActivity(Context context, String url, int openWhen) {
        Intent intent = new Intent(context, InlandFlightOrderDetailActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(OPEN_WHEN, openWhen);
        LogUtil.d(url);
        context.startActivity(intent);
    }


    @OnClick(R.id.comm_title_back)
    public void onBack() {
        back();
    }

    @OnClick({R.id.comm_title_back, R.id.comm_title_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.comm_title_back:
                back();
                break;
            case R.id.comm_title_close:
                finish();
                break;
        }
    }


    public class AndroidInterface {
        private boolean paySuccess;

        public boolean isPaySuccess() {
            return paySuccess;
        }

        public void setPaySuccess(boolean paySuccess) {
            this.paySuccess = paySuccess;
        }

        @JavascriptInterface
        public void toFlightOrderList() {
            turnToFlightOrderList();
        }

        @JavascriptInterface
        public void toFlightHome() {
            turnToFlightHome();
        }

        @JavascriptInterface
        public void paySuccess() {
            commTitleBack.post(() -> {
                commTitleClose.setVisibility(View.GONE);
                commTitleBack.setVisibility(View.GONE);
            });
            setPaySuccess(true);
        }
    }

    private void turnToFlightHome() {
        MainActivity.startMainActivityOfFlight(this);
        finish();
    }

    private void turnToFlightOrderList() {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderActivity.POSITION, 0);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inland_flight_order_detail;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (!nAndroidInterface.isPaySuccess()) {
            int openWhen = getIntent().getIntExtra(OPEN_WHEN, CREATE_ORDER_WHEN);
            switch (openWhen) {
                case CREATE_ORDER_WHEN:
                case SUBMIT_CHANGE_TICKET:
                    turnToFlightOrderList();
                    break;
                case ORDER_LIST_PAY_WHEN:
                    finish();
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}
