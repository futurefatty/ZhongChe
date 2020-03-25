package com.neusoft.zcapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.neusoft.zcapplication.tools.CountDownUtil;
import com.neusoft.zcapplication.tools.DateUtils;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView tvCountDown = (TextView) findViewById(R.id.tv_count_down);


    }
}
