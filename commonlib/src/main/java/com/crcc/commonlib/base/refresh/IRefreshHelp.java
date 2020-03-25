package com.crcc.commonlib.base.refresh;

import java.util.Collection;

/**
 * author: Six
 * Created by on 2018/7/31
 */
public interface IRefreshHelp<D> {

    void fillAdapterData(Collection<? extends D> data);

    void loadDataFail();

    void loadMore(int pageNum);

    void stopRefresh();


}
