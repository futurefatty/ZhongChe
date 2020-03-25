package com.neusoft.zcapplication.mine.mostusedinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 旅客
 * Created by Administrator on 2017/3/7.
 *
 */

public class PassengerFragment extends BaseFragment implements View.OnClickListener{
    public static PassengerFragment getInstance(){
        PassengerFragment fragment = new PassengerFragment();
        return fragment;
    }
    private View frgView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(null == frgView){
            frgView = inflater.inflate(R.layout.frg_passenger, container, false);
            initView();

        }else{
            ViewGroup parent = (ViewGroup) frgView.getParent();
            if(parent != null){
                parent.removeView(frgView);
            }
        }
        return frgView;
    }

    private void initView(){
        List<String[]> dataList = new ArrayList<>();
        String[] dataStrs = {"邓小等","身份证","441220**********21"};
        dataList.add(dataStrs);
        ListView listView  =  (ListView)frgView.findViewById(R.id.frg_passenger_list);
        BaseListAdapter adapter = new BaseListAdapter(getActivity(),dataList,0);
        listView.setAdapter(adapter);

        frgView.findViewById(R.id.btn_add_passenger).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_add_passenger:
                intent = new Intent(getContext(),AddPassengerActivity.class);
                startActivity(intent);
        }
    }
}
