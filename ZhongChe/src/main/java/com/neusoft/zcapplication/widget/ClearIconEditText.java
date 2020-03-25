package com.neusoft.zcapplication.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.neusoft.zcapplication.R;

/**
 * 带删除按钮的输入框
 */

public class ClearIconEditText extends AppCompatEditText {
    private static final int DRAWABLE_LEFT = 0;
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_RIGHT = 2;
    private static final int DRAWABLE_BOTTOM = 3;
//    private Drawable mClearDrawable;

    public ClearIconEditText(Context context) {
        super(context);
        init();
    }

    public ClearIconEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearIconEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
//        mClearDrawable = getResources().getDrawable(R.mipmap.icon_et_del);
    }
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
//        setClearIconVisible(hasFocus() && text.length() > 0);
        setClearIconVisible(text.length() > 0);
    }

//    @Override
//    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        setClearIconVisible(focused && length() > 0);
//        if(length() > 0){
//            setClearIconVisible(true);
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Drawable drawable = getCompoundDrawables()[DRAWABLE_RIGHT];
                if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
                        && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                    setText("");
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
        Drawable mClearDrawable = getResources().getDrawable(R.mipmap.icon_et_del);
        if(visible){
            mClearDrawable.setBounds(0,0,40,40);
        }
        setCompoundDrawablePadding(10);
        setCompoundDrawables(getCompoundDrawables()[DRAWABLE_LEFT], getCompoundDrawables()[DRAWABLE_TOP],
            visible ? mClearDrawable : null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
//        setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[DRAWABLE_LEFT], getCompoundDrawables()[DRAWABLE_TOP],
//                visible ? mClearDrawable : null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
    }

}
