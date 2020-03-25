package com.neusoft.zcapplication.visa;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseFragment;


/**
 * 常见问题
 * Created by Administrator on 2017/3/7.
 *
 */

public class CommonProblemFragment extends BaseFragment implements View.OnClickListener{
    public static CommonProblemFragment getInstance(){
        CommonProblemFragment fragment = new CommonProblemFragment();
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
            frgView = inflater.inflate(R.layout.frg_visa_common_problem, container, false);
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

    }

    @Override
    public void onClick(View v) {

    }
}
