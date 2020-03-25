package com.crcc.commonlib.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crcc.commonlib.R;

/**
 * author:Six
 * Date:2019/5/31
 */
public class HorLineWidget extends LinearLayout {

    private HorLineWidget(Context context, Builder builder) {
        super(context);
        View view = View.inflate(context, R.layout.item_hor_line_widget, this);
        TextView tvHorLineKey = (TextView) view.findViewById(R.id.tv_hor_line_key);
        TextView tvHorLineValue = (TextView) view.findViewById(R.id.tv_hor_line_value);
        if (builder.keyLayoutParams != null) {
            tvHorLineKey.setLayoutParams(builder.keyLayoutParams);
        }
        if (builder.valueLayoutParams != null) {
            tvHorLineValue.setLayoutParams(builder.keyLayoutParams);
        }
        if (builder.keyTextSize != -1) {
            tvHorLineKey.setTextSize(builder.keyTextSize);
        }
        if (builder.valueTextSize != -1) {
            tvHorLineValue.setTextSize(builder.valueTextSize);
        }
        if (builder.keyTextColor != -1) {
            tvHorLineKey.setTextColor(builder.keyTextColor);
        }
        if (builder.valueTextColor != -1) {
            tvHorLineValue.setTextColor(builder.valueTextColor);
        }
        tvHorLineKey.setText(builder.key);
        tvHorLineValue.setText(builder.value);

    }


    public static class Builder {
        private CharSequence key;
        private CharSequence value;
        private LayoutParams keyLayoutParams;
        private LayoutParams valueLayoutParams;
        @ColorInt
        private int keyTextColor = -1;
        @ColorInt
        private int valueTextColor = -1;
        private int keyTextSize = -1;
        private int valueTextSize = -1;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setKey(CharSequence key) {
            this.key = key;
            return this;
        }


        public Builder setValue(CharSequence value) {
            this.value = value;
            return this;
        }


        public Builder setKeyLayoutParams(LayoutParams keyLayoutParams) {
            this.keyLayoutParams = keyLayoutParams;
            return this;
        }


        public Builder setValueLayoutParams(LayoutParams valueLayoutParams) {
            this.valueLayoutParams = valueLayoutParams;
            return this;
        }


        public Builder setKeyTextColor(int keyTextColor) {
            this.keyTextColor = keyTextColor;
            return this;
        }

        public Builder setValueTextColor(int valueTextColor) {
            this.valueTextColor = valueTextColor;
            return this;
        }


        public Builder setKeyTextSize(int keyTextSize) {
            this.keyTextSize = keyTextSize;
            return this;
        }


        public Builder setValueTextSize(int valueTextSize) {
            this.valueTextSize = valueTextSize;
            return this;
        }

        public HorLineWidget build() {
            return new HorLineWidget(context, this);
        }
    }


}

