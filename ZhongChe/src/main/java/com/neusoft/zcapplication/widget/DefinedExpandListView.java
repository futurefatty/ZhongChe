package com.neusoft.zcapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2017/10/25.
 */

public class DefinedExpandListView extends ExpandableListView {
    public DefinedExpandListView(Context context) {
        super(context);
    }

    public DefinedExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefinedExpandListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
