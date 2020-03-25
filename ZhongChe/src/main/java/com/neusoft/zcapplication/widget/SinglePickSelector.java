package com.neusoft.zcapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * SinglePickSelectorView
 */
public class SinglePickSelector {

    public interface ResultHandler {
        void handle(String data);
    }

    private ResultHandler handler;
    private Context context;
    private Dialog seletorDialog;
    private SinglePickerView pv_data;

    private List<String> dataList;
    private TextView tv_cancle;
    private TextView tv_select;
    private TextView tv_title;
    private String selectData;


    public SinglePickSelector(Context context, ResultHandler resultHandler, List<String> dataList) {
        this.context = context;
        this.handler = resultHandler;
        this.dataList = dataList;
        initDialog();
        initView();
    }

    public void show() {
        initTimer();
        addListener();
        seletorDialog.show();
    }

    private void initDialog() {
        if (seletorDialog == null) {
            seletorDialog = new Dialog(context, R.style.time_dialog);
            seletorDialog.setCancelable(true);
            seletorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            seletorDialog.setContentView(R.layout.layout_dialog_single_pick_selector);
            Window window = seletorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            int width = DisplayUtil.getScreenWidth(context);
            lp.width = width;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        pv_data = (SinglePickerView) seletorDialog.findViewById(R.id.pv_data);
        tv_cancle = (TextView) seletorDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) seletorDialog.findViewById(R.id.tv_select);
        tv_title = (TextView) seletorDialog.findViewById(R.id.tv_title);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seletorDialog.dismiss();
            }
        });
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(selectData);
                seletorDialog.dismiss();
            }
        });

    }

    private void initTimer() {
        List<String> pvDataList = new ArrayList<>();
        pvDataList.addAll(dataList);
        pv_data.setData(pvDataList);
        pv_data.setSelected(0);
        selectData = dataList.get(pv_data.getCurrentSelected());
        setIsLoop(false);
        excuteScroll();
    }

    private void addListener() {
        pv_data.setOnSelectListener(new SinglePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectData = text;
            }
        });

    }

    private void excuteScroll() {
        pv_data.setCanScroll(dataList.size() > 1);
    }

    public void setTitle(String str) {
        tv_title.setText(str);
    }

    public void setIsLoop(boolean isLoop) {
        this.pv_data.setIsLoop(isLoop);
    }

}
