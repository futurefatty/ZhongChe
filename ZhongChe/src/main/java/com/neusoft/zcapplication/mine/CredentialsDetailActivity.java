package com.neusoft.zcapplication.mine;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.OrderApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetCredentialsData;
import com.neusoft.zcapplication.entity.GetDocumentType;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DateUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.ClearIconEditText;
import com.neusoft.zcapplication.widget.TimeSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 证件详情修改页面
 */
public class CredentialsDetailActivity extends BaseActivity implements View.OnClickListener {

    private PopupWindow window = new PopupWindow();
    private View popupView;
    private GetDocumentType selectType;//选择的证件类型map
//    private String[] credentialsAry = {"","身份证","护照","港澳通行证","台胞证","台湾通行证","回乡证","其他"};
    private List<GetDocumentType> typeList;//获取到的证件类型数据
    private TimeSelector timeSelector;//时间选择器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_credentials_detail);
        typeList = new ArrayList<>();
        initView();
        //新增证件信息，获取证件类型数据；修改证件的话，不能修改证件类型
        boolean isAddData = getIntent().getBooleanExtra("addData",false);
        if(isAddData){
            getDocumentType();//获取证件类型
        }
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_save_record).setOnClickListener(this);

        AppUtils.setStateBar(CredentialsDetailActivity.this,findViewById(R.id.frg_status_bar));
        TextView typeTv = (TextView)findViewById(R.id.credentials_detail_type);//证件类型
        typeTv.setOnClickListener(this);

        final TextView validDateTv = (TextView)findViewById(R.id.credentials_detail_valid_date);
        validDateTv.setOnClickListener(this);
        //如果是编辑记录，则不添加数据到控件上
        boolean isAddData = getIntent().getBooleanExtra("addData",false);
        TextView titleTv = (TextView)findViewById(R.id.title_credentials_detail_name);
        if(!isAddData){
            titleTv.setText("证件维护");
            GetCredentialsData getCredentialsData = (GetCredentialsData) (getIntent().getSerializableExtra("data"));
            String familyName = getCredentialsData.getFamilyName();
            String secondName = getCredentialsData.getSecondName();
            String documentInfo = getCredentialsData.getDocumentInfo();//证件号码
            String endDate = getCredentialsData.getEndDate();
            selectType = new GetDocumentType();

            int documentId = getCredentialsData.getDocumentId();//证件类型
//            int typeIndex = ((int) documentId) % Constant.credentialsAry.length;
            selectType.setId(documentId);
//            String typeText = Constant.credentialsAry[typeIndex];

            String documentName = getCredentialsData.getDocumentName();

            ClearIconEditText firstNameTv = (ClearIconEditText)findViewById(R.id.credentials_detail_first_name);
            ClearIconEditText secondNameTv = (ClearIconEditText)findViewById(R.id.credentials_detail_last_name);
            ClearIconEditText numTv = (ClearIconEditText)findViewById(R.id.credentials_detail_num);

            firstNameTv.setText(familyName);
            secondNameTv.setText(secondName);
            numTv.setText(documentInfo);
//            typeTv.setText(typeText);
            typeTv.setText(documentName);
            validDateTv.setText(endDate);
        }else{
            titleTv.setText("添加证件");
        }
        //时间选择器
        timeSelector = new TimeSelector(CredentialsDetailActivity.this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                String timeStr = time.substring(0,10);
                validDateTv.setText(timeStr);
            }
        }, DateUtils.generalBeginDate(), DateUtils.generalEndDate(50));
        timeSelector.setMode(TimeSelector.MODE.YMD);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.credentials_detail_type:
                //显示类型
                boolean isAddData = getIntent().getBooleanExtra("addData",false);
                //如果是增加证件信息，则可选择证件类型
                if(isAddData){
                    if(typeList.size() > 0){
                        showPopupWindows(typeList);
                    }else{
                        ToastUtil.toastNeedData(CredentialsDetailActivity.this,"暂无可用证件类型数据！");
                    }
                }
                break;
            case R.id.btn_save_record:
                //保存证件信息
                saveRecord();
                break;
            case R.id.credentials_detail_valid_date:
                timeSelector.show();
                break;
        }
    }

    /**
     * 提交
     */
    private void saveRecord() {
        ClearIconEditText firstNameTv = (ClearIconEditText)findViewById(R.id.credentials_detail_first_name);
        ClearIconEditText secondNameTv = (ClearIconEditText)findViewById(R.id.credentials_detail_last_name);
        TextView typeTv = (TextView)findViewById(R.id.credentials_detail_type);//证件类型
        ClearIconEditText numTv = (ClearIconEditText)findViewById(R.id.credentials_detail_num);
        TextView validDateTv = (TextView)findViewById(R.id.credentials_detail_valid_date);

        String firstNameStr = firstNameTv.getText().toString().trim();
        String secondNameStr = secondNameTv.getText().toString().trim();
        String typeStr = typeTv.getText().toString();
        String numStr = numTv.getText().toString().trim();
        String validDateStr = validDateTv.getText().toString();
        if(firstNameStr.equals("")){
            ToastUtil.toastNeedData(CredentialsDetailActivity.this,"请填写您的英文姓!");
        }else if(secondNameStr.equals("")){
            ToastUtil.toastNeedData(CredentialsDetailActivity.this,"请填写您的英文名!");
        }else if(typeStr.equals("") || null == selectType){
            ToastUtil.toastNeedData(CredentialsDetailActivity.this,"请选择证件类型!");
        }else if(numStr.equals("")){
            ToastUtil.toastNeedData(CredentialsDetailActivity.this,"请填写证件号码!");
        }else if(validDateStr.equals("")){
            ToastUtil.toastNeedData(CredentialsDetailActivity.this,"请选择证件有效日期!");
        }else{
//            EditText content = (EditText) findViewById(R.id.et_content);
            User user = AppUtils.getUserInfo(CredentialsDetailActivity.this);
            Map<String,Object> params = new HashMap<>();
            params.put("ciphertext","test");
            params.put("begindate", "");//签发时间,yyyy-MM-dd
            int documentId = (int) ((double) selectType.getId());
            params.put("documentId", documentId);//证件类型ID
            params.put("documentInfo", numStr);// 证件编号
            params.put("employeeCode",user.getEmployeeCode());//员工编号
            params.put("employeeName",user.getEmployeeName());//员工编号
            params.put("enddate", validDateStr);//到期时间,yyyy-MM-dd
            params.put("familyName", firstNameStr);//英文姓名的姓
            boolean isAddData = getIntent().getBooleanExtra("addData",false);
            if(!isAddData){
                GetCredentialsData getCredentialsData = (GetCredentialsData) (getIntent().getSerializableExtra("data"));
                int idInt = getCredentialsData.getId() ;
                params.put("id", idInt);//数据id，新增操作时无需入此参，更新操作时才入
            }
            params.put("loginType", Constant.APP_TYPE);
            params.put("secondName", secondNameStr);// 英文姓名的名
            if(isAddData){
                //修改证件信息
                showLoading();
                RetrofitFactory.getInstance().createApi(OrderApi.class).saveDocument(params)
                        .enqueue(new CallBack<Object>() {
                            @Override
                            public void success(Object object) {
                                dismissLoading();
                                showToast("保存成功");
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }

                            @Override
                            public void fail(String code) {
                                dismissLoading();
                            }
                        });
            }else{
                //更新证件信息
                showLoading();
                RetrofitFactory.getInstance().createApi(OrderApi.class).updateDocument(params)
                        .enqueue(new CallBack<Object>() {
                            @Override
                            public void success(Object object) {
                                dismissLoading();
                                showToast("保存成功");
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }

                            @Override
                            public void fail(String code) {
                                dismissLoading();
                            }
                        });
            }
        }
    }

    /**
     * 获取问题分类
     */
    private void getDocumentType() {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", Constant.APP_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getDocumentType(params)
                .enqueue(new CallBack<List<GetDocumentType>>() {
                    @Override
                    public void success(List<GetDocumentType> getDocumentTypeList) {
                        dismissLoading();
                        typeList.addAll(getDocumentTypeList);
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
    private void showPopupWindows(final List<GetDocumentType> getDocumentTypeList) {
        popupView = CredentialsDetailActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final CredentialsPopupListAdapter popAdapter = new CredentialsPopupListAdapter(CredentialsDetailActivity.this,getDocumentTypeList);
        for(int i=0;i<popAdapter.getCount();i++) {
            getDocumentTypeList.get(i).setCheck(false);
        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<popAdapter.getCount();i++) {
                    getDocumentTypeList.get(i).setCheck(false);
                }
                getDocumentTypeList.get(position).setCheck(true);
                selectType = getDocumentTypeList.get(position);
                popAdapter.notifyDataSetChanged();
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
        popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
        popuptitle.setText("选择证件类型");
        TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == selectType){
                    ToastUtil.toastNeedData(CredentialsDetailActivity.this,"请选择证件类型!");
                }else{
                    //判断当前是否选择的为身份证
                    boolean hasIdCard = getIntent().getBooleanExtra("hasIdCard",false);
                    int documentId = selectType.getId();
                    if(hasIdCard && documentId == 1){
                        window.dismiss();
                        AlertUtil.show(CredentialsDetailActivity.this,"您的身份证已存在，不能重复添加~","确定",null,"取消",null,"温馨提示");
                    }else{
                        TextView typeTv = (TextView)findViewById(R.id.credentials_detail_type);
                        typeTv.setText(selectType.getName());
                        window.dismiss();
                    }
                }
            }
        });

        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        int pop_width = (int)(DisplayUtil.getDeviceWidth(this)*0.9);
        window = new PopupWindow(popupView, pop_width, (int)(DisplayUtil.getDeviceWidth(this)*1.1));
        //设置动画
//                window.setAnimationStyle(R.style.popup_window_anim);
        //设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(90000000));
        //设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        window.update();

        popOutShadow(window);
        //以下拉的方式显示，并且可以设置显示的位置
//                        window.showAsDropDown(inputSearch, 0, 50);
        window.showAtLocation(findViewById(R.id.root_credentials_layout), Gravity.CENTER,0,0);
    }

    /**
     * 弹窗外阴影
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = CredentialsDetailActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        CredentialsDetailActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = CredentialsDetailActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                CredentialsDetailActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

}
