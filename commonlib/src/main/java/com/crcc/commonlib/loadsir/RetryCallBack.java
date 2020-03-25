package com.crcc.commonlib.loadsir;

import com.crcc.commonlib.R;
import com.kingja.loadsir.callback.Callback;

/**
 * author:Six
 * Date:2019/6/5
 */
public class RetryCallBack extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_retry;
    }
}
