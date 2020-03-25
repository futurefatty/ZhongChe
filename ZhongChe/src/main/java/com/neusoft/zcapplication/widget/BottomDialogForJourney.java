package com.neusoft.zcapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.neusoft.zcapplication.R;

/**
 * Created by Administrator on 2017/10/20.
 */

public class BottomDialogForJourney {
    private Context context;
    private View.OnClickListener listener;
    private Dialog mCameraDialog;
    private int type;//1-国内机票，2-国际机票，3-酒店
    private RadioGroup radioG_type,radioG_time;
    private int typeId,timeId;

    public BottomDialogForJourney(Context context, View.OnClickListener listener, int type) {
        this.context = context;
        this.listener = listener;
        this.type = type;
    }

    public void dismiss() {
        mCameraDialog.dismiss();
    }

    /**
     * 显示底部弹框
     */
    public void showBottomDialog() {
        mCameraDialog = new Dialog(context, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.bottom_dialog_journey, null);
        //初始化视图
        root.findViewById(R.id.btn_ok).setOnClickListener(listener);
        root.findViewById(R.id.btn_cancel).setOnClickListener(listener);
        radioG_type = (RadioGroup)root.findViewById(R.id.radioG_type);
        radioG_type.check(typeId);
        radioG_time = (RadioGroup)root.findViewById(R.id.radioG_time);
        radioG_time.check(timeId);

        root.findViewById(R.id.radio_order0).setOnClickListener(listener);
        root.findViewById(R.id.radio_order1).setOnClickListener(listener);
        root.findViewById(R.id.radio_order2).setOnClickListener(listener);
        if(type == 3) {//酒店没有改签单
            root.findViewById(R.id.radio_order2).setVisibility(View.GONE);
        }
        root.findViewById(R.id.radio_order3).setOnClickListener(listener);
        root.findViewById(R.id.radio_time0).setOnClickListener(listener);
        root.findViewById(R.id.radio_time1).setOnClickListener(listener);
        root.findViewById(R.id.radio_time2).setOnClickListener(listener);
        root.findViewById(R.id.radio_time3).setOnClickListener(listener);
        root.findViewById(R.id.radio_time4).setOnClickListener(listener);
        root.findViewById(R.id.radio_time5).setOnClickListener(listener);

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    /**
     * 选中已选择的item
     * @param
     */
    public void checkItem(int type, int id){
        if (type == 1){
            typeId = id;
        }
        else{
            timeId = id;
        }
    }
}
