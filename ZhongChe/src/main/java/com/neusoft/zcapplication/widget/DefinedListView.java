package com.neusoft.zcapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/5/27.
 */

public class DefinedListView extends ListView {
    public DefinedListView(Context context) {
        super(context);
    }

    public DefinedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefinedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
