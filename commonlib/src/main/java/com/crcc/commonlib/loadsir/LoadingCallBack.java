package com.crcc.commonlib.loadsir;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crcc.commonlib.R;
import com.kingja.loadsir.callback.Callback;

/**
 * author:Six
 * Date:2019/5/27
 */
public class LoadingCallBack extends Callback {

    private ImageView ivLoading;

    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }


    @Override
    protected void onViewCreate(Context context, View view) {
        ivLoading = (ImageView) view.findViewById(R.id.iv_loading);
    }

    @Override
    public void onDetach() {
        ivLoading.clearAnimation();
    }

    @Override
    public void onAttach(Context context, View view) {

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.progress_rotate);
        ivLoading.setAnimation(anim);
        anim.start();
    }
}
