package com.crcc.commonlib.utils;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;

/**
 * author:Six
 * Date:2019/6/22
 */
public class ZCWebViewSettings extends AbsAgentWebSettings {

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {

    }

    @Override
    public IAgentWebSettings toSetting(WebView webView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (webView.getContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        IAgentWebSettings iAgentWebSettings = super.toSetting(webView);
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUserAgentString(settings.getUserAgentString() + " Project/ZhongChe");
        return iAgentWebSettings;
    }
}
