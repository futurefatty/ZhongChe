package com.neusoft.zcapplication.gestureLock;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.gestureLock.widget.GestureContentView;
import com.neusoft.zcapplication.gestureLock.widget.GestureDrawline;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

/**
 *
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity extends BaseActivity implements View.OnClickListener {

	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
    private int counts = 5;//最大错误次数

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_verify);
		AppUtils.setStateBar(mContext,findViewById(R.id.frg_status_bar));
		setUpViews();
	}

	private void setUpViews() {
		//显示用户姓名
		TextView mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
		User user = AppUtils.getUserInfo(GestureVerifyActivity.this);
		String userName = user.getEmployeeName();
		mTextPhoneNumber.setText(userName);

		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		TextView mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		mTextForget.setOnClickListener(this);
		// 初始化一个显示各个点的viewGroup
		final String employeeCode = user.getEmployeeCode();
		String gstPass = AppUtils.getGestureData(GestureVerifyActivity.this,employeeCode);
		mGestureContentView = new GestureContentView(this, true, gstPass,
			new GestureDrawline.GestureCallBack() {

				@Override
				public void onGestureCodeInput(String inputCode) {
				}

				@Override
				public void checkedSuccess() {
					mGestureContentView.clearDrawlineState(0L);
					Intent intent = new Intent();
					setResult(RESULT_OK,intent);
					GestureVerifyActivity.this.finish();
				}

				@Override
				public void checkedFail() {
					mGestureContentView.clearDrawlineState(1300L);
					mTextTip.setVisibility(View.VISIBLE);
                    counts--;
                    if(counts > 0){
                        String text = "<font color='#fd7732'>密码错误,还可再输入"+counts+"次</font>";
                        mTextTip.setText(Html
                                .fromHtml(text));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                    }else{
                        ToastUtil.toastNeedData(GestureVerifyActivity.this,"手势密码已失效~");
                        AppUtils.saveGestureData(GestureVerifyActivity.this,"",employeeCode);
                        finish();
                    }
				}
			});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.text_forget_gesture:
				finish();
				break;
			default:
				break;
		}
	}

}

