package com.neusoft.zcapplication.widget.recycleview.layoutmanager;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created  2018/12/16.
 *
 * @author six
 */

public class ScrollSpeedLinearLayoutManger extends LinearLayoutManager {
    private float MILLISECONDS_PER_INCH = 0.3f;

    public ScrollSpeedLinearLayoutManger(Context context) {
        super(context);
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 0.1f;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return ScrollSpeedLinearLayoutManger.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    @Override
                    protected float calculateSpeedPerPixel
                            (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.density;
                        //返回滑动一个pixel需要多少毫秒
                    }

                    @Override
                    protected int getVerticalSnapPreference() {
                        return SNAP_TO_START;
                    }

                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}