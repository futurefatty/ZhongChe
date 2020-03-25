package com.neusoft.zcapplication.flight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crcc.commonlib.model.MapperModel;
import com.crcc.commonlib.utils.UtilDisplay;
import com.crcc.commonlib.view.HorLineWidget;
import com.neusoft.zcapplication.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * author:Six
 * Date:2019/5/31
 */
public class FlightTicketChangingDialog extends Dialog {
    LinearLayout llTicketChangingInfo;
    TextView tvBaggageConsignRule;
    LinearLayout llBaggageConsignRule;
    private Context context;

    public FlightTicketChangingDialog(@NonNull Context context) {
        super(context, R.style.BottomShowDialog);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = (int) ((UtilDisplay.getScreenHeight(context) -
                UtilDisplay.getStatusBarHeight(context)) * 0.75);
        window.setAttributes(attributes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        View view = View.inflate(context, R.layout.dialog_flight_ticket_changing, null);
        llTicketChangingInfo = (LinearLayout) view.findViewById(R.id.ll_ticket_changing_info);
        tvBaggageConsignRule = (TextView) view.findViewById(R.id.tv_baggage_consign_rule);
        llBaggageConsignRule = (LinearLayout) view.findViewById(R.id.ll_baggage_consign_rule);
        ButterKnife.bind(this, view);
        if (TextUtils.isEmpty(baggageConsignRule)) {
            llBaggageConsignRule.setVisibility(View.GONE);
        } else {
            llBaggageConsignRule.setVisibility(View.VISIBLE);
            tvBaggageConsignRule.setText(baggageConsignRule);
        }
        if (mapperModels != null) {
            for (MapperModel mapperModel : mapperModels) {
                int childCount = llTicketChangingInfo.getChildCount();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (childCount > 0) {
                    layoutParams.topMargin = UtilDisplay.dip2px(context, 16);
                }
                HorLineWidget horLineWidget = new HorLineWidget.Builder(context)
                        .setKey(mapperModel.getKey())
                        .setKeyTextColor(ContextCompat.getColor(context, R.color.C888888))
                        .setValue(mapperModel.getValue())
                        .setValueTextColor(ContextCompat.getColor(context, R.color.C333333))
                        .build();
                llTicketChangingInfo.addView(horLineWidget, layoutParams);
            }
        }
        setContentView(view);
    }

    private List<MapperModel> mapperModels;

    public void setTicketChangingInfo(List<MapperModel> mapperModels) {
        this.mapperModels = mapperModels;
    }

    private String baggageConsignRule;

    public void setBaggagConsignRule(String baggageConsignRule) {
        this.baggageConsignRule = baggageConsignRule;
    }


}
