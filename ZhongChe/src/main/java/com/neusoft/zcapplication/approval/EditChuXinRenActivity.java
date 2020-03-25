package com.neusoft.zcapplication.approval;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neusoft.zcapplication.Bean.PersonItem;
import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;
import com.neusoft.zcapplication.widget.PopupWinListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.internal.operators.observable.ObservableNever;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 编辑出行人
 **/
public class EditChuXinRenActivity extends BaseActivity implements View.OnClickListener, RequestCallback {
    private TextView tv_person, tv_accountEntity, tv_depart;
    private Map<String, Object> selectPerson, currentUser;//选择的出行人，当前用户
    private Map<String, Object> selectAccountEntity;//选择的核算主体
    private Map<String, Object> selectunitEntity;//选择的核算主体
    private List<Map<String, Object>> accountEntityList;//核算主体列表
    private List<Map<String, Object>> unitEntityList;//核算主体列表
    private PopupWindow window = new PopupWindow();
    private View popupView;
    //    private PersonItem personItem;
    private Map<String, Object> personInfoMap;
    private List<Map<String, Object>> credentialsList;//证件列表
    private List<Map<String, Object>> managerList;//证件列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_chuxinren);
        initView();
    }


    private void initView() {
        personInfoMap = (Map<String, Object>) getIntent().getSerializableExtra("item");
        accountEntityList = (List<Map<String, Object>>) personInfoMap.get("accountEntityList");
        String accountEntityText = null == personInfoMap.get("accountEntity")
                ? "" : personInfoMap.get("accountEntity").toString();//用户选择的核算主体名称
        String unitNametext = null == personInfoMap.get("unitName")
                ? "" : personInfoMap.get("unitName").toString();
        for (int i = 0; i < accountEntityList.size(); i++) {
            Map<String, Object> map = accountEntityList.get(i);
            String mapCompanyName = null == map.get("companyName") ? "" : map.get("companyName").toString();
            if (mapCompanyName.equals(accountEntityText)) {
                selectAccountEntity = map;
                accountEntityList.get(i).put("isCheck", "true");

                if (map.get("unitInfo") != null || ((List<Map<String, Object>>) map.get("unitInfo")).size() > 0) {
                    List<Map<String, Object>> unitList = (List<Map<String, Object>>) map.get("unitInfo");
                    unitEntityList = unitList;
                    for (int j = 0; j < unitList.size(); j++) {
                        Map<String, Object> unitItem = unitList.get(j);
                        if (unitItem.get("unitName").equals(unitNametext)) {
                            unitEntityList.get(j).put("isCheck", "true");
                            selectunitEntity = unitEntityList.get(j);
                        } else {
                            unitEntityList.get(j).put("isCheck", "false");
                        }
                    }
                }
            } else {
                accountEntityList.get(i).put("isCheck", "false");
            }
        }

        ;//用户选择的部门
        tv_person = (TextView) findViewById(R.id.tv_person);
//        tv_person.setText(personItem.getName());
        String name = null == personInfoMap.get("name").toString() ? "" : personInfoMap.get("name").toString();
        tv_person.setText(name);
        tv_person.setOnClickListener(this);
        tv_accountEntity = (TextView) findViewById(R.id.tv_accountEntity);

//        tv_accountEntity.setText(personItem.getAccountEntity());
        tv_accountEntity.setText(accountEntityText);
        tv_accountEntity.setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);

        tv_depart = (TextView) findViewById(R.id.tv_depart);
        tv_depart.setText(unitNametext);
        tv_depart.setOnClickListener(this);

        //选择证件类型
        TextView credentialsInfoTv = (TextView) findViewById(R.id.act_edit_trip_person_card);
        credentialsInfoTv.setOnClickListener(this);
//        credentialsList = personItem.getCredentialsInfo();
        credentialsList = (List<Map<String, Object>>) personInfoMap.get("credentialsInfo");
        for (Map<String, Object> map : credentialsList) {
            String isChecked = null == map.get("isCheck") ? "false" : map.get("isCheck").toString();
            if (isChecked.equals("true")) {
                String documentName = null == map.get("DOCUMENTNAME") ? "" : map.get("DOCUMENTNAME").toString();
                credentialsInfoTv.setText(documentName);
            }
        }

        AppUtils.setStateBar(EditChuXinRenActivity.this, findViewById(R.id.frg_status_bar));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_person:
//                getPersonList();
                break;
            case R.id.tv_accountEntity:
                if (accountEntityList != null && accountEntityList.size() != 0) {
                    if (accountEntityList.size() == 1) {
                        ToastUtil.toast("当前员工只有一个核算主体");
                        return;
                    }
                    showPopupWindows(3, accountEntityList);
                } else {
                    ToastUtil.toast("当前员工没有核算主体");
                }
                break;
            case R.id.tv_depart:
                if (unitEntityList != null && unitEntityList.size() != 0) {
                    if (unitEntityList.size() == 1) {
                        ToastUtil.toast("当前核算主体下只有一个部门");
                        return;
                    }
                    showPopupWindows(7, unitEntityList);
                } else {
                    ToastUtil.toast("部门列表为空");
                }

                break;
            case R.id.act_edit_trip_person_card:
                showPopupWindows(6, credentialsList);
                break;
            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_finish:
                int position = getIntent().getIntExtra("position", 0);
                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("item",personItem);
//                intent.putExtras(bundle);
//                intent.putExtra("item",(Serializable) personItem);
                intent.putExtra("item", (Serializable) personInfoMap);

                intent.putExtra("position", position);
                setResult(101, intent);
                finish();
                break;

        }
    }

    /**
     * 获取已授权人员列表
     */
    private void getPersonList() {
        User user = AppUtils.getUserInfo(EditChuXinRenActivity.this);
        String employeeCode = user.getEmployeeCode();
        Map<String, Object> params = new HashMap<>();
        params.put("ciphertext", "test");
        params.put("employeeCode", employeeCode);
        params.put("loginType", URL.LOGIN_TYPE);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String, Object>> call = request.getPersonList(params);
        new RequestUtil().requestData(call, this, 1, "正在加载数据", false, EditChuXinRenActivity.this);
    }

    /**
     * 显示弹窗
     *
     * @param type
     */
    private void showPopupWindows(final int type, final List<Map<String, Object>> datalist) {
        RelativeLayout ly_main = (RelativeLayout) findViewById(R.id.ly_title_bar);
        //构建一个popupwindow的布局
        popupView = EditChuXinRenActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final PopupWinListAdapter popAdapter = new PopupWinListAdapter(EditChuXinRenActivity.this, datalist, type);
//        for(int i=0;i<popAdapter.getCount();i++) {
//            datalist.get(i).put("isCheck","false");
//        }


        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < popAdapter.getCount(); i++) {
                    datalist.get(i).put("isCheck", "false");
                }
                datalist.get(position).put("isCheck", "true");
                popAdapter.notifyDataSetChanged();
                if (type == 1) {
                    ImageView icon_check = (ImageView) popupView.findViewById(R.id.icon_check);
                    icon_check.setImageResource(R.drawable.btn_singleselection_nor);
                    selectPerson = datalist.get(position);
                } else if (type == 3) {
                    selectAccountEntity = datalist.get(position);
                    List<Map<String, Object>> uintInfoList = null;
                    unitEntityList = (List<Map<String, Object>>) selectAccountEntity.get("unitInfo");
                    if (uintInfoList == null || uintInfoList.size() == 0) {
                        return;
                    }
                    if (uintInfoList.size() == 1) {
                        selectunitEntity = uintInfoList.get(0);
                        return;
                    }
                    if (unitEntityList.size() > 1) {
                        for (int i = 0; i < uintInfoList.size(); i++) {
                            Map<String, Object> unitItem = unitEntityList.get(i);
                            if (unitItem.get("unitName").equals(selectAccountEntity.get("unitName"))) {
                                return;
                            }
                        }

                    }
                } else if (type == 6) {
                    credentialsList = datalist;
                } else if (type == 7) {
                    selectunitEntity = datalist.get(position);
                }
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        if (type == 1) {//出行人员
            TextView current_username = (TextView) popupView.findViewById(R.id.item_current_username);
            current_username.setText(currentUser.get("employeeName") + "");
            popupView.findViewById(R.id.select_person).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    accountEntityList = (ArrayList) selectPerson.get("company");
                    if (accountEntityList == null || accountEntityList.size() == 0) {
                        Toast.makeText(EditChuXinRenActivity.this, "该人员没有核算主体", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (accountEntityList.size() == 1) {
                            selectAccountEntity = accountEntityList.get(0);
                            tv_accountEntity.setText(selectAccountEntity.get("companyName") + "");
//                            personItem.setAccountEntity(selectAccountEntity.get("companyName")+"");
                            //设置核算主体
                            personInfoMap.put("accountEntity", selectAccountEntity.get("companyName") + "");
                        } else {
                            showPopupWindows(3, accountEntityList);
                        }
                    }

                    tv_person.setText(selectPerson.get("employeeName") + "");
//                    personItem.setName(selectPerson.get("employeeName")+"");
//                    personItem.setEmployeeCode(selectPerson.get("employeeCode")+"");
//                    personItem.setIdNo(selectPerson.get("idCard")+"");
//                    personItem.setMobile(selectPerson.get("mobil")+"");

                    personInfoMap.put("name", selectPerson.get("employeeName") + "");
                    personInfoMap.put("employeeCode", selectPerson.get("employeeCode") + "");
                    personInfoMap.put("idNo", selectPerson.get("idCard") + "");
                    personInfoMap.put("mobile", selectPerson.get("mobil") + "");
                }
            });
            //选择当前用户（自己）时的事件
            popupView.findViewById(R.id.item_current_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView icon_check = (ImageView) popupView.findViewById(R.id.icon_check);
                    icon_check.setImageResource(R.drawable.btn_singleselection_pressed);
                    for (int i = 0; i < popAdapter.getCount(); i++) {
                        datalist.get(i).put("isCheck", "false");
                    }
                    popAdapter.notifyDataSetChanged();
                    selectPerson = currentUser;
                }
            });
            popuptitle.setText("选择出行人员");
        } else if (type == 3) {//核算主体
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            popuptitle.setText("选择核算主体");
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectAccountEntity == null) {
                        AlertUtil.show(EditChuXinRenActivity.this, "请选择核算主体", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "选择核算主体");
                        return;
                    }
                    window.dismiss();
                    tv_accountEntity.setText(selectAccountEntity.get("companyName") + "");

                    List<Map<String, Object>> tmpAccountEntityList = (List<Map<String, Object>>) personInfoMap.get("accountEntityList");
                    for (int i = 0; i < tmpAccountEntityList.size(); i++) {
                        Map<String, Object> map = tmpAccountEntityList.get(i);
                        String mapCompanyName = null == map.get("companyName") ? "" : map.get("companyName").toString();
                        if (mapCompanyName.equals(selectAccountEntity.get("companyName") + "")) {
                            tmpAccountEntityList.get(i).put("isCheck", "true");

                        } else {
                            tmpAccountEntityList.get(i).put("isCheck", "false");
                        }
                    }

//                    personInfoMap.get("accountEntityList") tmpAccountEntityList;
                    personInfoMap.put("accountEntityList", tmpAccountEntityList);
                    List<Map<String, Object>> unitInfo = (List<Map<String, Object>>) selectAccountEntity.get("unitInfo");
                    if (unitInfo.size() > 1) {
                        showPopupWindows(7, unitInfo);
                    } else if (unitInfo.size() == 1) {
                        Map<String, Object> unitItem = unitInfo.get(0);
                        tv_depart.setText(unitItem.get("unitName") + "");
                        personInfoMap.put("unitName", unitItem.get("unitName"));
                        personInfoMap.put("unitCode", unitItem.get("unitCode"));
                    }
//                    personInfoMap.put("unitInfo",unitInfo);
//                    personItem.setAccountEntity(selectAccountEntity.get("companyName")+"");
                    personInfoMap.put("accountEntity", selectAccountEntity.get("companyName") + "");
                    personInfoMap.put("companyCode", selectAccountEntity.get("companyCode") + "");

                }
            });
        } else if (type == 6) {
            //选择证件类型
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            popuptitle.setText("选择证件类型");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    for (Map<String, Object> map : credentialsList) {
                        String isChecked = null == map.get("isCheck") ? "false" : map.get("isCheck").toString();
                        if (isChecked.equals("true")) {
                            String documentName = null == map.get("DOCUMENTNAME") ? "" : map.get("DOCUMENTNAME").toString();
                            TextView credentialsInfoTv = (TextView) findViewById(R.id.act_edit_trip_person_card);
                            credentialsInfoTv.setText(documentName);
                        }
                    }
                }
            });
        } else if (type == 7) {
            popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
            popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
            TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
            popuptitle.setText("选择部门");
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                    tv_depart.setText(selectunitEntity.get("unitName") + "");
                    personInfoMap.put("unitInfo", selectunitEntity);
                    personInfoMap.put("unitName", selectunitEntity.get("unitName") + "");
                    personInfoMap.put("unitCode", selectunitEntity.get("unitCode") + "");
                }
            });
        }

        popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();

            }
        });

        // 创建PopupWindow对象，指定宽度和高度
        int pop_width = (int) (getDeviceWidth() * 0.9);
        window = new PopupWindow(popupView, pop_width, (int) (getDeviceWidth() * 1.1));
        //  设置动画
//                window.setAnimationStyle(R.style.popup_window_anim);
        //设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(90000000));
        //设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        //  更新popupwindow的状态
        window.update();

        popOutShadow(window);
        //  以下拉的方式显示，并且可以设置显示的位置
//                        window.showAsDropDown(inputSearch, 0, 50);
        window.showAtLocation(ly_main, Gravity.CENTER, 0, 0);
    }

    /**
     * 弹窗外阴影
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = EditChuXinRenActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        EditChuXinRenActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = EditChuXinRenActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                EditChuXinRenActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 获取手机屏幕宽度
     *
     * @return
     */
    public int getDeviceWidth() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String, Object> result = (Map<String, Object>) map;
        switch (type) {
            case 1:
                if (null != result) {
                    String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = (String) result.get("codeMsg");
                    if (code.equals("00000")) {
                        final List<Map<String, Object>> datalist = (ArrayList) result.get("data");
                        currentUser = datalist.get(0);
                        selectPerson = currentUser;
                        datalist.remove(0);
                        showPopupWindows(1, datalist);
                    } else {
                        ToastUtil.toastError(EditChuXinRenActivity.this);
                    }
                } else {
                    //请求失败
                    ToastUtil.toastError(EditChuXinRenActivity.this);
                }
                break;

        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastFail(EditChuXinRenActivity.this);
    }

    @Override
    public void requestCancel(int type) {

    }


}
