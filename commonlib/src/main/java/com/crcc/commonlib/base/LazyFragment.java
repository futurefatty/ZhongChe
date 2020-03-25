package com.crcc.commonlib.base;

import android.os.Bundle;

/**
 * Created  2018/11/20.
 * frgament 懒加载基类
 *
 * @author six
 */

public abstract class LazyFragment extends BaseFragment {
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }


    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        boolean isFetchData = isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate);
        if (isFetchData) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }


    protected abstract int getLayoutId();


}
