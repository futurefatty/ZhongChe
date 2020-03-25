package com.neusoft.zcapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.neusoft.zcapplication.R;


/**
 * 自定义弹框
 */
public class CustomDialog extends Dialog implements
		View.OnClickListener {
	Button NegativeButton, PositiveButton;
	TextView msg ,titleTv;
	View line;
	View.OnClickListener nOnclick, pOnclick;

	public CustomDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(R.layout.alert);
		NegativeButton = (Button) findViewById(R.id.alert_negativebutton);
		PositiveButton = (Button) findViewById(R.id.alert_positivebutton);
		line = findViewById(R.id.alert_center_line);
		msg = (TextView) findViewById(R.id.alert_msg);
		titleTv = (TextView) findViewById(R.id.alert_title);
		NegativeButton.setOnClickListener(this);
		PositiveButton.setOnClickListener(this);
		this.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
		this.getWindow().getAttributes().horizontalMargin = 200;

	}

	public CustomDialog setNegativeButton(String text,
			View.OnClickListener onclick) {
		NegativeButton.setText(text);
		this.nOnclick = onclick;
		NegativeButton.setVisibility(View.VISIBLE);
		return this;
	}

	public CustomDialog setPositiveButton(String text,
			View.OnClickListener onclick) {
		if (NegativeButton != null
				&& NegativeButton.getVisibility() == View.VISIBLE)
//			NegativeButton.setBackgroundResource(R.drawable.alert_btn2);

		PositiveButton.setText(text);
		this.pOnclick = onclick;
		line.setVisibility(View.VISIBLE);
		PositiveButton.setVisibility(View.VISIBLE);
		return this;
	}

	public CustomDialog setMessage(String message) {
		this.msg.setText(message);
		return this;
	}
	public CustomDialog setTitle(String title) {
		this.titleTv.setText(title);
		return this;
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.alert_negativebutton:
			if (nOnclick != null)
				nOnclick.onClick(view);
			dismiss();
			break;
		case R.id.alert_positivebutton:
			if (pOnclick != null)
				pOnclick.onClick(view);
			dismiss();
			break;
		}
	}
}
