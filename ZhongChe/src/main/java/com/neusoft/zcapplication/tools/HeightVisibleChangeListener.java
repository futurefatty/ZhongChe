package com.neusoft.zcapplication.tools;

import android.graphics.Rect;
import android.view.ViewTreeObserver;
import android.webkit.WebView;

public class HeightVisibleChangeListener implements ViewTreeObserver.OnGlobalLayoutListener{
    private WebView webview;
    public HeightVisibleChangeListener(WebView webView){
        this.webview = webView;
        webview.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    int lastHeight;
    int lastVisibleHeight;

    @Override
    public void onGlobalLayout() {
        Rect visible = new Rect();
        Rect size = new Rect();
        webview.getWindowVisibleDisplayFrame(visible);
        webview.getHitRect(size);

        int height = size.bottom-size.top;
        int visibleHeight = visible.bottom - visible.top;

        if(height == lastHeight && lastVisibleHeight == visibleHeight) return;

        lastHeight = height;
        lastVisibleHeight = visibleHeight;
        String js = String.format("javascript:heightChange(%1$d , %2$d)",height,visibleHeight);
        webview.loadUrl(js);
    }

}
