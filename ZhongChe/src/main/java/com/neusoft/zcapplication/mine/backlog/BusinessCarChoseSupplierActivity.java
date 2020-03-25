package com.neusoft.zcapplication.mine.backlog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.api.CarApi;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.entity.GetAllSuppliers;
import com.neusoft.zcapplication.entity.GetEmployeeInfo;
import com.neusoft.zcapplication.event.Events;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.mine.authorization.AuthorizationSearchActivity;
import com.neusoft.zcapplication.mine.authorization.AuthorrizationPopuListAdapter;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: TenzLiu
 * Time: 2018/6/14 13:55
 * Desc: 用车供应商
 */

public class BusinessCarChoseSupplierActivity extends BaseActivity implements View.OnClickListener {

    private ListView lv_business_car_chose_supplier;
    private BusinessCarChoseSupplierAdapter mBusinessCarChoseSupplierAdapter;
    private List<GetAllSuppliers> dataList;
    private GetAllSuppliers choseGetAllSuppliers;

    private PopupWindow window = new PopupWindow();
    private View popupView;
    private ChoseSupplierPopuListAdapter mChoseSupplierPopuListAdapter;//弹出层列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_car_chose_supplier);
        initView();
        initData();
    }

    private void initView() {
        AppUtils.setStateBar(mContext,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        lv_business_car_chose_supplier = (ListView) findViewById(R.id.lv_business_car_chose_supplier);
        dataList = new ArrayList<>();
        mBusinessCarChoseSupplierAdapter = new BusinessCarChoseSupplierAdapter(mContext,dataList);
        lv_business_car_chose_supplier.setAdapter(mBusinessCarChoseSupplierAdapter);
        lv_business_car_chose_supplier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0; i<mBusinessCarChoseSupplierAdapter.getCount(); i++){
                    mBusinessCarChoseSupplierAdapter.getItem(i).setCheck(false);
                }
                mBusinessCarChoseSupplierAdapter.getItem(position).setCheck(true);
                choseGetAllSuppliers = mBusinessCarChoseSupplierAdapter.getItem(position);
                mBusinessCarChoseSupplierAdapter.notifyDataSetChanged();
                getAllCarTypeBySupplierId(choseGetAllSuppliers.getId());
            }
        });
    }

    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if(null != bundle){
            choseGetAllSuppliers = (GetAllSuppliers) bundle.getSerializable("mGetAllSuppliers");
        }
        getAllSuppliers();//获取数据
    }

    /**
     * 获取我的待办列表
     */
    private void getAllSuppliers() {
        Map<String,Object> params = new HashMap<>();

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).getAllSuppliers(params)
                .enqueue(new CallBack<List<GetAllSuppliers>>() {
                    @Override
                    public void success(List<GetAllSuppliers> getAllSuppliersList) {
                        dismissLoading();
                        if(getAllSuppliersList.size()>0){
                            if(choseGetAllSuppliers != null){
                                for (int i=0;i<getAllSuppliersList.size();i++){
                                    if(getAllSuppliersList.get(i).getId() == choseGetAllSuppliers.getId()){
                                        getAllSuppliersList.get(i).setCheck(true);
                                    }
                                }
                            }
                            mBusinessCarChoseSupplierAdapter.setList(getAllSuppliersList);
                        }else{
                            showToast("列表数据为空");
                            mBusinessCarChoseSupplierAdapter.setList(getAllSuppliersList);
                        }
                        mBusinessCarChoseSupplierAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_confirm:
                if(choseGetAllSuppliers == null){
                    showToast("请选择服务商");
                    return;
                }
                if(choseGetAllSuppliers.getGetAllCarTypeBySupplierId() == null){
                    showToast("请选择服务商车型");
                    return;
                }
                EventBus.getDefault().post(new Events.BusinessCarChoseSupplier(choseGetAllSuppliers));
                finish();
                break;
        }
    }

    /**
     * 获取用车类型列表
     */
    private void getAllCarTypeBySupplierId(int supplierId) {
        Map<String,Object> params = new HashMap<>();
        params.put("supplierId",""+supplierId);

        showLoading();
        RetrofitFactory.getInstance().createApi(CarApi.class).getAllCarTypeBySupplierId(params)
                .enqueue(new CallBack<List<GetAllSuppliers.GetAllCarTypeBySupplierId>>() {
                    @Override
                    public void success(List<GetAllSuppliers.GetAllCarTypeBySupplierId> getAllCarTypeBySupplierIdList) {
                        dismissLoading();
                        showPopupWindows(getAllCarTypeBySupplierIdList);
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
    private void showPopupWindows(final List<GetAllSuppliers.GetAllCarTypeBySupplierId> getAllCarTypeBySupplierIdList) {
        // 构建一个popupwindow的布局
        popupView = getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);

        mChoseSupplierPopuListAdapter = new ChoseSupplierPopuListAdapter(mContext,getAllCarTypeBySupplierIdList);
        for(int i=0;i<mChoseSupplierPopuListAdapter.getCount();i++) {
            getAllCarTypeBySupplierIdList.get(i).setCheck(false);
        }
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
        popupView.findViewById(R.id.select_person_cancel).setVisibility(View.GONE);
        popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        popuptitle.setText("选择服务商车型");
        listView.setAdapter(mChoseSupplierPopuListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<mChoseSupplierPopuListAdapter.getCount();i++) {
                    if(i == position){
                        getAllCarTypeBySupplierIdList.get(i).setCheck(true);
                    }else{
                        getAllCarTypeBySupplierIdList.get(i).setCheck(false);
                    }
                }
                mChoseSupplierPopuListAdapter.setList(getAllCarTypeBySupplierIdList);
                mChoseSupplierPopuListAdapter.notifyDataSetChanged();
            }
        });
        popupView.findViewById(R.id.select_person_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllSuppliers.GetAllCarTypeBySupplierId getAllCarTypeBySupplierId = null;
                for(int i=0;i<mChoseSupplierPopuListAdapter.getCount();i++) {
                    if(getAllCarTypeBySupplierIdList.get(i).isCheck()){
                        getAllCarTypeBySupplierId = getAllCarTypeBySupplierIdList.get(i);
                    }
                }
                if(null != getAllCarTypeBySupplierId){
                    window.dismiss();
                    choseGetAllSuppliers.setGetAllCarTypeBySupplierId(getAllCarTypeBySupplierId);
                }else{
                    showToast("请选择服务商车型");
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
        window.showAtLocation(lv_business_car_chose_supplier, Gravity.CENTER,0,0);
    }

    /**
     * 弹窗外阴影
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

}