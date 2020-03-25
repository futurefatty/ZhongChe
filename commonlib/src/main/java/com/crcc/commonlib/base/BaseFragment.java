package com.crcc.commonlib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventBusUtil;
import com.crcc.commonlib.loadsir.RetryCallBack;
import com.crcc.commonlib.utils.DisposableManager;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created  2018/3/9.
 * frgament 懒加载基类
 *
 * @author six
 */

public abstract class BaseFragment extends Fragment {
    protected AppCompatActivity mContext;
    protected LoadService loadService;
    private Unbinder unbinder;
    protected DisposableManager disposableManager = DisposableManager.newInstance();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册事件
        if (isRegisterEventBus() && !EventBusUtil.isRegister(this)) {
            EventBusUtil.register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    protected void receiveEvent(Event event) {
    }

    /**
     * 子类重写此方法是否需要注册Evnet
     *
     * @return
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    protected void addSubscription(@NonNull Disposable disposable) {
        disposableManager.addSubscription(disposable);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        disposableManager.unsubscribe();
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);
        if (isNeedLoadSir()) {
            loadService = LoadSir.getDefault().register(rootView, new Callback.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    if (loadService.getCurrentCallback() == RetryCallBack.class) {
                        BaseFragment.this.onReload(v);
                    }
                }
            });
        }
        if (isBindView()) {
            unbinder = ButterKnife.bind(this, rootView);
        }
        return isNeedLoadSir() ? loadService.getLoadLayout() : rootView;
    }


    protected void onReload(View view) {

    }

    protected boolean isBindView() {
        return true;
    }


    protected boolean isNeedLoadSir() {
        return false;
    }

    protected abstract int getLayoutId();


}
