package com.neusoft.zcapplication.update;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

/**
 * 加载框
 */
public class LoadProgressDialog extends Dialog implements
		View.OnClickListener {
	Button cancelBtn ;
	TextView titleTv,progressTv,sizeTv;
	View.OnClickListener nOnclick ;
	private ProgressBar progressBar;

	public LoadProgressDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(R.layout.progress_dialog);
		cancelBtn = (Button) findViewById(R.id.progress_cancel);
		titleTv = (TextView) findViewById(R.id.progress_title);
        sizeTv = (TextView) findViewById(R.id.progress_size);
        progressTv = (TextView) findViewById(R.id.progress_now_prg);
		progressBar = (ProgressBar)findViewById(R.id.progress_progressbar);
		cancelBtn.setOnClickListener(this);
		this.getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;

	}
    public LoadProgressDialog setSize(String size){
        sizeTv.setText(size);
        return this;
    }
    public LoadProgressDialog setNowProgress(String nowProgress){
        progressTv.setText(nowProgress);
        return this;
    }
	public LoadProgressDialog setProgress(int progress){
		progressBar.setProgress(progress);
		return this;
	}
	public LoadProgressDialog setCancelButton(String text,
                                              View.OnClickListener onclick) {
		cancelBtn.setText(text);
		this.nOnclick = onclick;
		return this;
	}

	public LoadProgressDialog setTitle(String title) {
		this.titleTv.setText(title);
		return this;
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.progress_cancel:
			if (nOnclick != null){
				nOnclick.onClick(view);
			}
			dismiss();
			break;
		}
	}

}
