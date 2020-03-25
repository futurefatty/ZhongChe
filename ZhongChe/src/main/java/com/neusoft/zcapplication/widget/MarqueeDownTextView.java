package com.neusoft.zcapplication.widget;

import android.widget.LinearLayout;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.widget.MarqueeTextViewClickListener;

public class MarqueeDownTextView extends LinearLayout {
    private Context mContext;
    private ViewFlipper viewFlipper;
    private View marqueeTextView;
    private String[] textArrays;
    private MarqueeTextViewClickListener marqueeTextViewClickListener;

    public MarqueeDownTextView(Context context) {
        super(context);
        mContext = context;
        initBasicView();
    }


    public MarqueeDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBasicView();
    }

    public void setTextArraysAndClickListener(String[] textArrays, MarqueeTextViewClickListener marqueeTextViewClickListener) {
        this.textArrays = textArrays;
        this.marqueeTextViewClickListener = marqueeTextViewClickListener;
        initMarqueeTextView(textArrays, marqueeTextViewClickListener);
    }

    public void initBasicView() {
        marqueeTextView = LayoutInflater.from(mContext).inflate(R.layout.marquee_textview_layout, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(marqueeTextView, layoutParams);
        viewFlipper = (ViewFlipper) marqueeTextView.findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
        viewFlipper.startFlipping();
    }

    public void stop(){
        viewFlipper.stopFlipping();
    }

    public void initMarqueeTextView(String[] textArrays, MarqueeTextViewClickListener marqueeTextViewClickListener) {
        if (textArrays.length == 0) {
            return;
        }

        int i = 0;
        viewFlipper.removeAllViews();
        while (i < textArrays.length) {
            TextView textView = new TextView(mContext);
            textView.setText(textArrays[i]);
            textView.setTextColor(getResources().getColor(R.color.NormalTextColor));
            textView.setOnClickListener(marqueeTextViewClickListener);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            viewFlipper.addView(textView, lp);
            i++;
        }
        viewFlipper.startFlipping();
    }

    public void releaseResources() {
        if (marqueeTextView != null) {
            if (viewFlipper != null) {
                viewFlipper.stopFlipping();
                viewFlipper.removeAllViews();
                viewFlipper = null;
            }
            marqueeTextView = null;
        }
    }

}
