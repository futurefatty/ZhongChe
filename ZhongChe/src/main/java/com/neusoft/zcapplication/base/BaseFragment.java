package com.neusoft.zcapplication.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.LoadingDialog;

/**
 * Author: TenzLiu
 * Time: 2018/6/5 22:14
 * Desc: fragment基类
 */

public class BaseFragment extends Fragment {

    /**
     * Fragment的View加载完毕的标记(懒加载)
     */
    protected boolean isViewCreate;

    /**
     * Fragment对用户可见的标记（懒加载）
     */
    protected boolean isUIVisible;

    protected boolean isVisibleRepeat;
    protected boolean isViewCreateRepeat;

    /**
     * 上下文对象
     */
    protected Context mContext;

    /**
     * 所属activity
     */
    protected Activity mActivity;
    private LoadingDialog loadDialog;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //改变isUIVisible标记,isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if(isVisibleToUser){
            isUIVisible = true;
            isVisibleRepeat = true;
            lazyLoadData();
        }else{
            isUIVisible = false;
            isVisibleRepeat = false;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mActivity = (Activity) getContext();
        isViewCreate = true;
        isViewCreateRepeat = true;
        lazyLoadData();
    }

    /**
     * 懒加载方法
     */
    protected void lazyLoadData() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,
        // 必须确保onCreateView加载完毕且页面可见,才加载数据
        if(isUIVisible && isViewCreate){
            initData();
            //数据加载完毕,恢复标记,防止重复加载
            isUIVisible = false;
            isViewCreate = false;
        }
        if(isVisibleRepeat && isViewCreateRepeat){
            initDataRepeat();
        }
    }

    /**
     * 初始化数据
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {

    }

    /**
     * 初始化数据
     * 子类可以复写此方法初始化子类数据
     * 每次切换可视状态都会执行此方法
     */
    protected void initDataRepeat() {

    }

    /**
     * 显示加载框
     */
    protected void showLoading(){
        showLoading("正在加载...", false);
    }

    /**
     * @param str 提示文字
     * @param bool true可取消
     */
    protected void showLoading(String str, boolean bool){
        if (loadDialog != null){
            if(loadDialog.isShowing()){
                loadDialog.dismiss();
            }
            loadDialog = null;
        }
        loadDialog = LoadingDialog.createDialog(mContext);
        loadDialog.setMessage(str);
        loadDialog.setCancelable(bool);
        loadDialog.show();
    }

    /**
     * 关闭加载框
     */
    protected void dismissLoading(){
        if (loadDialog != null){
            if(loadDialog.isShowing()){
                loadDialog.dismiss();
            }
            loadDialog = null;
        }
    }

    /**
     * toast
     */
    protected void showToastSuccess(){
        ToastUtil.toast("操作成功");
    }

    /**
     * toast
     * @param message
     */
    protected void showToast(String message){
        ToastUtil.toast(message);
    }

    /**
     * 跳转页面
     * @param cls
     */
    public void startActivity(Class cls){
        startActivity(cls,null);
    }

    /**
     * 跳转页面,带参数
     * @param cls
     */
    public void startActivity(Class cls, Bundle bundle){
        Intent intent = new Intent(mContext,cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转页面,有返回
     * @param cls
     * @param requestCode
     */
    public void startActivityForResult(Class cls,int requestCode){
        startActivityForResult(cls,null,requestCode);
    }

    /**
     * 跳转页面,带参数,有返回
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class cls, Bundle bundle, int requestCode){
        Intent intent = new Intent(mContext,cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }



}
