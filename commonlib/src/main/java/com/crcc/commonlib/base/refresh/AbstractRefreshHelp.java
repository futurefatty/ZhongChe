package com.crcc.commonlib.base.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crcc.commonlib.R;


import java.util.Collection;
import java.util.List;

/**
 * author: Six
 * Created by on 2018/7/31
 */
public abstract class AbstractRefreshHelp<D, B extends BaseQuickAdapter<D, ? extends BaseViewHolder>>
        implements IRefreshHelp<D>, SwipeRefreshLayout.OnRefreshListener {

    private int pagerNum = 1;
    protected B baseQuickAdapter;
    public static final int PAGER_SIZE = 20;
    private final SwipeRefreshLayout swipeRefreshLayout;

    protected AbstractRefreshHelp(BaseQuickAdapter baseQuickAdapter, RecyclerView recyclerView) {
        swipeRefreshLayout = findSwipeRefreshLayout(recyclerView);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.CF1532D);
            swipeRefreshLayout.setOnRefreshListener(this);
        }
        baseQuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pagerNum++;
                loadMore(pagerNum);
            }
        }, recyclerView);
        recyclerView.setAdapter(baseQuickAdapter);
        this.baseQuickAdapter = (B) baseQuickAdapter;
    }

    private SwipeRefreshLayout findSwipeRefreshLayout(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof SwipeRefreshLayout) {
            return (SwipeRefreshLayout) parent;
        }
        if (view instanceof SwipeRefreshLayout) {
            return (SwipeRefreshLayout) view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                findSwipeRefreshLayout(viewGroup.getChildAt(i));
            }
        }
        return null;
    }


    @Override
    public void fillAdapterData(Collection<? extends D> newData) {
        stopRefresh();
        List<D> oldData = baseQuickAdapter.getData();
        if (pagerNum == 1) {
            int size = newData == null ? 0 : newData.size();
            oldData.clear();
            if (newData != null && !newData.isEmpty()) {
                oldData.addAll(newData);
            }
            changeViewStatus(oldData.isEmpty());
            baseQuickAdapter.notifyDataSetChanged();
            baseQuickAdapter.loadMoreComplete();
            if (size < PAGER_SIZE) {
                baseQuickAdapter.loadMoreEnd();
            }
        } else {
            if (newData != null && !newData.isEmpty()) {
                baseQuickAdapter.addData(newData);
                baseQuickAdapter.loadMoreComplete();
            } else {
                baseQuickAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void loadDataFail() {
        stopRefresh();
        if (pagerNum == 1) {
            List oldData = baseQuickAdapter.getData();
            changeViewStatus(oldData.isEmpty());
        } else {
            baseQuickAdapter.loadMoreFail();
        }
    }


    @Override
    public void stopRefresh() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            baseQuickAdapter.setEnableLoadMore(true);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    protected abstract void changeViewStatus(boolean isEmpty);

    @Override
    public void onRefresh() {
        pagerNum = 1;
        baseQuickAdapter.setEnableLoadMore(false);
        loadMore(pagerNum);
    }

    public int getPagerNum() {
        return pagerNum;
    }

    public void setPagerNum(int pagerNum) {
        this.pagerNum = pagerNum;
    }
}
