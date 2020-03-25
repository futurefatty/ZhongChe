package com.crcc.commonlib.utils;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * author:Six
 * Date:2019/5/16
 */
public class DisposableManager {


    public static DisposableManager newInstance() {
        return new DisposableManager();
    }

    private CompositeDisposable compositeDisposable;


    public void addSubscription(@NonNull Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void unsubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
