package com.neusoft.zcapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.visa.VisaListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/3/7.
 * 签证
 */

public class VisaFragment extends BaseFragment implements View.OnClickListener{
    private View frgView;

    public static VisaFragment getInstance(){
        VisaFragment fragment = new VisaFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == frgView){
            frgView = inflater.inflate(R.layout.activity_visa, container, false);
            initView();
        }else{
            ViewGroup parent = (ViewGroup) frgView.getParent();
            if(parent != null){
                parent.removeView(frgView);
            }
        }
        return frgView;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void initView(){
        List<List<Map<String,String>>> gridList = new ArrayList<>();
        List<Map<String,String>> mapList = new ArrayList<>();
        String[] name = {"马来西亚","新加坡","日本","韩国","泰国","越南"};
        String[] ids = {"","","","","",""};
        for(int i = 0 ;i < name.length ; i++){
            Map<String,String> m = new HashMap<>();
            m.put("name",name[i]);
            m.put("ids",ids[i]);
            mapList.add(m);
        }
        gridList.add(mapList);
        gridList.add(mapList);

        ListView visaList = (ListView) frgView.findViewById(R.id.visa_country_list);
        VisaListAdapter vAdapter = new VisaListAdapter(getActivity(),gridList);
        visaList.setAdapter(vAdapter);
    }
}
