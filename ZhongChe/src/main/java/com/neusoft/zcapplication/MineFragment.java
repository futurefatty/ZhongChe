package com.neusoft.zcapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseFragment;
import com.neusoft.zcapplication.gestureLock.GestureEditActivity;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.mine.AppQrCodeActivity;
import com.neusoft.zcapplication.mine.CredentialsActivity;
import com.neusoft.zcapplication.mine.SuggestAndFeedbackActivity;
import com.neusoft.zcapplication.mine.authorization.AuthorizationActivity;
import com.neusoft.zcapplication.mine.backlog.BacklogNewActivity;
import com.neusoft.zcapplication.mine.customservice.CustomServiceActivity;
import com.neusoft.zcapplication.mine.journey.JourneyActivity;
import com.neusoft.zcapplication.mine.mostusedinfo.PassengerActivity;
import com.neusoft.zcapplication.mine.order.OrderActivity;
import com.neusoft.zcapplication.mine.personalinfo.PersonalInfoActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.widget.CircleImageView;

import java.util.HashMap;
import java.util.Map;

import q.rorbin.badgeview.QBadgeView;


/**
 * 我的
 */

public class MineFragment extends BaseFragment implements View.OnClickListener{

    private View frgView;
    private TextView jobPointTv;
    private TextView interPointTv;
    private QBadgeView qBadgeView;

    public static MineFragment getInstance(){
        MineFragment fragment = new MineFragment();
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
            frgView = inflater.inflate(R.layout.frg_mine, container, false);
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
    public void onResume() {
        super.onResume();
        //设置我的代办，我的订单圆点状态,
        getJobData();
        getInterCount();
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()){
            case R.id.frg_mine_remind_layout:
                //行程
                startActivity(JourneyActivity.class);
                break;
            case R.id.frg_mine_head_layout:
                //个人资料
                startActivityForResult(PersonalInfoActivity.class,4002);
                break;
            case R.id.frg_mine_order_layout:
                //我的订单
                startActivity(OrderActivity.class);
                break;
            case R.id.frg_mine_xc_order_layout:
                //携程订单
                intent = new Intent(getActivity(),ShowViewForNonBusinessActivity.class);
                intent.putExtra("url","ctrip");
                intent.putExtra("loginType",3);//1机票 ，2 酒店 3 携程订单
                intent.putExtra("dataType",2);//1因公 ，2 因私
                startActivity(intent);
                break;
            case R.id.frg_mine_credentials_layout:
                //证件维护
                startActivity(CredentialsActivity.class);
                break;
            case R.id.frg_mine_commonly_used_info_layout:
                //常用信息
//                intent = new Intent(getActivity(),MostUsedInfoActivity.class);
                startActivity(PassengerActivity.class);
                break;
            case R.id.frg_mine_to_be_done_layout:
                //我的待办
//                AppUtils.saveJobToDoStatus(getActivity(),false);
//                changeJobPointStatus(false);//设置我的代办圆点状态
                startActivity(BacklogNewActivity.class);
                break;
            case R.id.frg_mine_authorization_layout:
                //我的授权
                startActivity(AuthorizationActivity.class);
                break;
            case R.id.frg_mine_dataanalysis_layout:
                //数据分析
                startActivity(DataAnalyzeActivity.class);
                break;
            case R.id.frg_mine_suggestion_layout:
                //投诉与建议
                startActivity(SuggestAndFeedbackActivity.class);
                break;
            case R.id.frg_mine_custom_service_layout:
                //我的客服
                startActivity(CustomServiceActivity.class);
                break;
            case R.id.frg_mine_suggest_layout:
                //意见与反馈
                startActivity(SuggestAndFeedbackActivity.class);
                break;
//            case R.id.frg_mine_login:
//                //登录
////                intent = new Intent(getActivity(),ShowViewActivity.class);
////                intent.putExtra("url","http://120.25.216.230/travel-web/phone/login");
//                intent = new Intent(getActivity(),LoginActivity.class);
//                startActivity(intent);
//                break;
            case R.id.frg_mine_qr_layout:
                //二维码界面
                startActivity(AppQrCodeActivity.class);
                break;
            case R.id.frg_mine_gst_layout:
                //手势密码
                startActivity(GestureEditActivity.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data){
            if(requestCode == 4001){
                //点击登录按钮，登录成功后返回
                loginSuccess(data);
            }else if(requestCode == 4002){
                //退出登录
                getActivity().finish();
            }else if(requestCode == 4003){
                //点击我关注的航班动态，登录成功后返回
//                startActivity(new Intent(getActivity(), LowerPriceRemindActivity.class));
            }
        }
    }
    private void loginSuccess(Intent data){
        //登录成功后，隐藏登录按钮,显示昵称、头像图片等
//        frgView.findViewById(R.id.frg_mine_login).setVisibility(View.GONE);
        TextView nickNameTv = (TextView) frgView.findViewById(R.id.frg_mine_nickName);
        nickNameTv.setVisibility(View.VISIBLE);

        String nickName = data.getStringExtra("nickName");
        nickNameTv.setText(nickName);

        String id = data.getStringExtra("id");
        String headIcon = data.getStringExtra("headIcon");
        //加载头像
        CircleImageView headImg = (CircleImageView)frgView.findViewById(R.id.frg_mine_head);

    }
    private void initView(){
        frgView.findViewById(R.id.frg_mine_remind_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_head_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_order_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_xc_order_layout).setOnClickListener(this);//携程订单
        frgView.findViewById(R.id.frg_mine_credentials_layout).setOnClickListener(this);//证件维护
        frgView.findViewById(R.id.frg_mine_suggest_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_commonly_used_info_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_to_be_done_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_authorization_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_custom_service_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_dataanalysis_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_dataanalysis_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_suggestion_layout).setOnClickListener(this);
        frgView.findViewById(R.id.frg_mine_qr_layout).setOnClickListener(this);//二维码
        frgView.findViewById(R.id.frg_mine_gst_layout).setOnClickListener(this);//设置手势密码
//        frgView.findViewById(R.id.frg_mine_login).setOnClickListener(this);//登录
        jobPointTv = (TextView)frgView.findViewById(R.id.mine_badge);
        interPointTv = (TextView)frgView.findViewById(R.id.frg_mine_inter_ticket_point);

        //登录成功后，隐藏登录按钮,显示昵称、头像图片等
//        frgView.findViewById(R.id.frg_mine_login).setVisibility(View.GONE);
        TextView nickNameTv = (TextView) frgView.findViewById(R.id.frg_mine_nickName);
        nickNameTv.setVisibility(View.VISIBLE);
        User user = AppUtils.getUserInfo(getActivity());
        String nickName = user.getEmployeeName();
        nickNameTv.setText(nickName);

        qBadgeView = (QBadgeView) new QBadgeView(getActivity()).bindTarget(jobPointTv);
    }

    /**
     * 获取该用户是否有代办事件
     */
    private void getJobData(){
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode",AppUtils.getUserInfo(getActivity()).getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);

//        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getBacklogCount(params)
//
                .enqueue(new CallBack<Map<String,Integer>>() {
                    @Override
                    public void success(Map<String,Integer> object) {
//                        dismissLoading();
                        //获取用户是否有待办事件数据的成功的回调事件
                        AppUtils.saveJobToDoStatus(getActivity(),true);//有待办事项
//                        jobPointTv.setVisibility(View.VISIBLE);
//                        if(integer > 0){
//                            AppUtils.saveJobToDoStatus(getActivity(),true);//有待办事项
//                            jobPointTv.setVisibility(View.VISIBLE);
//                        }else{
//                            AppUtils.saveJobToDoStatus(getActivity(),false);
//                            jobPointTv.setVisibility(View.GONE);
//                        }
                        Integer totalCount = object.get("total");
                        qBadgeView.setBadgeNumber(totalCount).setGravityOffset(3,true).setBadgeBackgroundColor(0xffc70019);
                    }

                    @Override
                    public void fail(String code) {
//                        dismissLoading();
                        //我的待办
                        AppUtils.saveJobToDoStatus(getActivity(),false);//无待办事项
                    }
                });
    }

    /**
     * 是否有带选择国际机票方案
     */
    private void getInterCount(){
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode",AppUtils.getUserInfo(getActivity()).getEmployeeCode());
        params.put("loginType", URL.LOGIN_TYPE);

//        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getInterCount(params)
                .enqueue(new CallBack<Integer>() {
                    @Override
                    public void success(Integer integer) {
                        dismissLoading();
                        //获取是否有带选择国际机票方案的事项，返回成功的回调事件
                        AppUtils.saveInterTicketCount(getActivity(),integer);
                        if(integer>0){
                            interPointTv.setVisibility(View.VISIBLE);
                        }else{
                            interPointTv.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                        //待选国际机票方案个数
                        AppUtils.saveInterTicketCount(getActivity(),0);
                    }
                });
    }

}
