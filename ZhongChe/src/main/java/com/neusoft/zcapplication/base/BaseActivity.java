package com.neusoft.zcapplication.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.neusoft.zcapplication.tools.AppManagerUtil;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.LoadingDialog;


/**
 * activity基类
 */

public class BaseActivity extends AppCompatActivity {

    public Context mContext;
    private LoadingDialog loadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManagerUtil.getInstance().addActivity(this);
        mContext = this;
        LogUtil.d("activityName:-----------"+getComponentName().getClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManagerUtil.getInstance().finishActivity(this);
    }


    /**
     * 显示加载框
     */
    protected void showLoading(){
        showLoading("正在加载...", false);
    }

    /**
     * 显示加载框
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
        loadDialog = LoadingDialog.createDialog(this);
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
     * toast成功
     */
    protected void showToastSuccess(){
        ToastUtil.toast("操作成功");
    }

    /**
     * toast文字
     * @param message
     */
    protected void showToast(String message){
        ToastUtil.toast(message);
    }

    /**
     * 软键盘处理
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyBord();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyBord() {
        //点击空白位置 隐藏软键盘
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService
                (INPUT_METHOD_SERVICE);
        boolean hideSoftInputFromWindow = mInputMethodManager.hideSoftInputFromWindow(this
                .getCurrentFocus().getWindowToken(), 0);
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
        Intent intent = new Intent(this,cls);
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
        Intent intent = new Intent(this,cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

}
