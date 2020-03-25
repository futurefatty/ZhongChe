package com.neusoft.zcapplication.gestureLock;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.gestureLock.widget.GestureContentView;
import com.neusoft.zcapplication.gestureLock.widget.GestureDrawline;
import com.neusoft.zcapplication.gestureLock.widget.LockIndicator;
import com.neusoft.zcapplication.tools.AppUtils;

/**
 * 手势密码设置界面
 */
public class GestureEditActivity extends BaseActivity implements OnClickListener {

	private TextView mTextTitle;
	private LockIndicator mLockIndicator;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextReset;
	private boolean mIsFirstInput = true;
	private String mFirstPassword = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_edit);
		setUpViews();
		setUpListeners();
	}

	private void setUpViews() {
		mTextTitle = (TextView) findViewById(R.id.text_title);
		findViewById(R.id.btn_back).setOnClickListener(this);
		mTextReset = (TextView) findViewById(R.id.text_reset);
		mTextReset.setClickable(false);
		mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, false, "",
				new GestureDrawline.GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
				if("".equals(inputCode)){
					return;
				}
				if (!isInputPassValidate(inputCode)) {
					mTextTip.setText(Html.fromHtml("<font color='#fd7732'>至少连接4个点, 请重新输入</font>"));
					mGestureContentView.clearDrawlineState(0L);
					return;
				}
				mTextTip.setText(Html.fromHtml("请再次绘制手势密码"));
				if (mIsFirstInput) {
					mFirstPassword = inputCode;
					updateCodeList(inputCode);
					mGestureContentView.clearDrawlineState(0L);
					mTextReset.setClickable(true);
					mTextReset.setText(getString(R.string.reset_gesture_code));
				} else {
					if (inputCode.equals(mFirstPassword)) {
                        User user = AppUtils.getUserInfo(GestureEditActivity.this);
                        String employeeCode = user.getEmployeeCode();
						AppUtils.saveGestureData(GestureEditActivity.this,inputCode,employeeCode);

						Toast.makeText(GestureEditActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
						mGestureContentView.clearDrawlineState(0L);
						GestureEditActivity.this.finish();
					} else {
						mTextTip.setText(Html.fromHtml("<font color='#fd7732'>与上一次绘制不一致，请重新绘制</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						// 保持绘制的线，1.5秒后清除
						mGestureContentView.clearDrawlineState(1300L);
					}
				}
				mIsFirstInput = false;
			}

			@Override
			public void checkedSuccess() {

			}

			@Override
			public void checkedFail() {

			}
		});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
		updateCodeList("");
        AppUtils.setStateBar(GestureEditActivity.this,findViewById(R.id.frg_status_bar));
	}

	private void setUpListeners() {
		mTextReset.setOnClickListener(this);
	}

	private void updateCodeList(String inputCode) {
		// 更新选择的图案
		mLockIndicator.setPath(inputCode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
				this.finish();
				break;
			case R.id.text_reset:
				mIsFirstInput = true;
				updateCodeList("");
				mTextTip.setText(getString(R.string.set_gesture_pattern));
				v.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}

	private boolean isInputPassValidate(String inputPassword) {
		if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
			return false;
		}
		return true;
	}

}
