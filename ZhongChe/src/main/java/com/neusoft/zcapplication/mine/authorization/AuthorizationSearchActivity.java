package com.neusoft.zcapplication.mine.authorization;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetEmployeeInfo;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.widget.TimeSelector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的授权搜索
 */
public class AuthorizationSearchActivity extends BaseActivity implements View.OnClickListener {

    private PopupWindow window = new PopupWindow();
    private View popupView;
    private AuthorrizationPopuListAdapter mAuthorrizationPopuListAdapter;//弹出层列表
    private TimeSelector timeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authorization_search);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        findViewById(R.id.tv_authourization_date).setOnClickListener(this);
        final TextView authorizationDate = (TextView)findViewById(R.id.tv_authourization_date);
        authorizationDate.setText(DateUtils.getDate(365));
        timeSelector = new TimeSelector(AuthorizationSearchActivity.this,new TimeSelector.ResultHandler(){

            @Override
            public void handle(String time) {
                authorizationDate.setText(time.substring(0,10));
            }
        }, DateUtils.generalBeginDate(),DateUtils.generalEndDate());
        timeSelector.setMode(TimeSelector.MODE.YMD);

        AppUtils.setStateBar(AuthorizationSearchActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_search:
                searchEmployeeInfo();
                break;
            case R.id.tv_authourization_date:
                timeSelector.show();
                break;
        }
    }

    /**
     * 获取已授权人员列表
     */
    private void searchEmployeeInfo() {
        EditText employeeNameEt = (EditText) findViewById(R.id.et_employeeName);
        EditText employeeCodeEt = (EditText) findViewById(R.id.et_employeeCode);
        String employeeCodeStr = employeeCodeEt.getText().toString().trim();
        String employeeNameStr = employeeNameEt.getText().toString().trim();
        if(employeeCodeStr.equals("") && employeeNameStr.equals("")){
            showToast("请填写员工姓名或者员工编号搜索！");
            return;
        }
        User user = AppUtils.getUserInfo(AuthorizationSearchActivity.this);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("employeeCode", employeeCodeStr);
        params.put("employeeName", employeeNameStr);
        params.put("applicateId", user.getEmployeeCode());
        params.put("loginType", Constant.APP_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getEmployeeInfo(params)
                .enqueue(new CallBack<List<GetEmployeeInfo>>() {
                    @Override
                    public void success(List<GetEmployeeInfo> getEmployeeInfoList) {
                        dismissLoading();
                        showPopupWindows(getEmployeeInfoList);
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 显示弹窗
     */
    private void showPopupWindows(final List<GetEmployeeInfo> getEmployeeInfoList) {
        LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
        // 构建一个popupwindow的布局
        popupView = getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);

        mAuthorrizationPopuListAdapter = new AuthorrizationPopuListAdapter(mContext,getEmployeeInfoList);
        for(int i=0;i<mAuthorrizationPopuListAdapter.getCount();i++) {
            getEmployeeInfoList.get(i).setCheck(false);
        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
        popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        popuptitle.setText("被授权人");
        listView.setAdapter(mAuthorrizationPopuListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for(int i=0;i<mAuthorrizationPopuListAdapter.getCount();i++) {
                if(i == position){
                    getEmployeeInfoList.get(i).setCheck(true);
                }else{
                    getEmployeeInfoList.get(i).setCheck(false);
                }
            }
            mAuthorrizationPopuListAdapter.setList(getEmployeeInfoList);
            mAuthorrizationPopuListAdapter.notifyDataSetChanged();
            }
        });
        popupView.findViewById(R.id.select_person_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEmployeeInfo getEmployeeInfo = null;
                for(int i=0;i<mAuthorrizationPopuListAdapter.getCount();i++) {
                    if(getEmployeeInfoList.get(i).isCheck()){
                        getEmployeeInfo = getEmployeeInfoList.get(i);
                    }
                }
                if(null != getEmployeeInfo){
                    //0代表可以授权，1代表已经授权过了
                    int state = getEmployeeInfo.getState();
                    if(state == 1){
                        String empName = getEmployeeInfo.getEMPLOYEE_NAME();
                        String str = "您已对" + empName + "授过权了，请勿重复授权！";
                        showToast(str);
                    }else{
                        setAuthorization(getEmployeeInfo);
                        window.dismiss();
                    }
                }else{
                    showToast("请选择授权人");
                }

            }
        });
        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        // 创建PopupWindow对象，指定宽度和高度
        int pop_width = (int)(DisplayUtil.getDeviceWidth(this)*0.9);
        window = new PopupWindow(popupView, pop_width, (int)(DisplayUtil.getDeviceWidth(this)*1.1));
        //  设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(90000000));
        //  设置可以获取焦点
        window.setFocusable(true);
        //  设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
        popOutShadow(window);
        // 以下拉的方式显示，并且可以设置显示的位置
//                        window.showAsDropDown(inputSearch, 0, 50);
        window.showAtLocation(ly_main, Gravity.CENTER,0,0);
    }

    /**
     * 弹窗外阴影
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = AuthorizationSearchActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        AuthorizationSearchActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = AuthorizationSearchActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                AuthorizationSearchActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 授权
     * @param getEmployeeInfo
     */
    public void setAuthorization(GetEmployeeInfo getEmployeeInfo) {
        TextView authorizationDate = (TextView)findViewById(R.id.tv_authourization_date);
        String beAgreeCode = getEmployeeInfo.getEMPLOYEE_CODE();//0代表可以授权，1代表已经授权过了
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        User user = AppUtils.getUserInfo(AuthorizationSearchActivity.this);
        params.put("employeeCode", user.getEmployeeCode());
        params.put("loginType", Constant.APP_TYPE);
        params.put("beAgreeCode", beAgreeCode);
        params.put("agreeValidTime", authorizationDate.getText().toString());

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).setAuthorization(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToast("授权成功");
                        finish();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

}
