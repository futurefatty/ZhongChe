package com.neusoft.zcapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.neusoft.zcapplication.R;


/**
 * 自定义可以设置图标大小的radioButton
 */

public class NavBarRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    private int drawableSize;//图片尺寸大小
    private String textSourceName;
    public NavBarRadioButton(Context context) {
        super(context);
    }

    public NavBarRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavBarRadioButton);
        textSourceName = a.getString(R.styleable.NavBarRadioButton_radioTextSourceName);
//        setChangeFontTextViewLanguage(context);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.NavBarRadioButton_drawableSize:
                    drawableSize = a.getDimensionPixelSize(R.styleable.NavBarRadioButton_drawableSize, 50);
                    break;
                case R.styleable.NavBarRadioButton_drawableTop:
                    drawableTop = a.getDrawable(attr);
                    break;
                case R.styleable.NavBarRadioButton_drawableBottom:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.NavBarRadioButton_drawableRight:
                    drawableBottom = a.getDrawable(attr);
                    break;
                case R.styleable.NavBarRadioButton_drawableLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                default :
                    break;
            }
        }
        a.recycle();

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);

    }
//    public void setChangeFontTextViewLanguage(Context context){
//        if(textSourceName != null){
//            int sourceId = context.getResources().getIdentifier(textSourceName,"string",context.getApplicationInfo().packageName);
//            this.setText(context.getString(sourceId));
//        }
//    }
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, drawableSize, drawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, drawableSize, drawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, drawableSize, drawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, drawableSize, drawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }
}
