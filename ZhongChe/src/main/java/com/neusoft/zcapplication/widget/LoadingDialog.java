package com.neusoft.zcapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

/**
 * 加载框
 */
public class LoadingDialog extends Dialog {
	private Context context = null;
	private static LoadingDialog loadingDialog = null;
	
	public LoadingDialog(Context context){
		super(context);
		this.context = context ;
	}
	
	public LoadingDialog(Context context, int theme) {
		super(context,theme);
		this.context = context;
	}
	
	public static LoadingDialog createDialog(Context context){
		loadingDialog = new LoadingDialog(context, R.style.loadingDialog);
		loadingDialog.setContentView(R.layout.loading_dialog);
		loadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
//		loadingDialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
//		loadingDialog.getWindow().getAttributes().horizontalMargin = 10;
		
		return loadingDialog;
	}
	public void onWindowFocusChanged(boolean hasFocus){
		if (loadingDialog == null){
			return;
		}
		ImageView imageView = (ImageView) loadingDialog.findViewById(R.id.loading_dialog_img);
		//帧动画
//		imageView.setBackgroundResource(R.anim.progress_rotate);
//		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//		animationDrawable.start();
		//tween动画
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.progress_rotate);
		imageView.setAnimation(anim);
		anim.start();
	}

	public LoadingDialog setMessage(String strMessage){
		TextView tvMsg = (TextView)loadingDialog.findViewById(R.id.loading_dialog_tv);
		if (tvMsg != null){
			tvMsg.setText(TextUtils.isEmpty(strMessage)?"正在加载...":strMessage);
		}
		return loadingDialog;
	}
}
