package com.neusoft.zcapplication.mine;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.neusoft.zcapplication.entity.GetSuggestionInfo;
import com.neusoft.zcapplication.http.now.CallBack;
import com.neusoft.zcapplication.http.now.RetrofitFactory;
import com.neusoft.zcapplication.tools.AlertUtil;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 意见与反馈
 */
public class SuggestAndFeedbackActivity extends BaseActivity implements View.OnClickListener {

    private PopupWindow window = new PopupWindow();
    private View popupView;
    private String questionVal;//问题分类id
    private GetSuggestionInfo selectQuestionVal;
    private int typeIndex,objIndex;//类型下标，投诉对象下标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_feedback);
        initView();
    }

    private void initView(){
        AppUtils.setStateBar(SuggestAndFeedbackActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.tv_problem_type).setOnClickListener(this);
        //选择问题类型
        findViewById(R.id.tv_suggest_type).setOnClickListener(this);
        findViewById(R.id.tv_suggest_object).setOnClickListener(this);//选择投诉建议对象
        findViewById(R.id.ll_record).setOnClickListener(this);//历史记录

        typeIndex = -1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_submit:
                setSuggestion();
                break;
            case R.id.tv_problem_type:
                getProblemType();
                break;
            case R.id.tv_suggest_type:
                showTypeWindow();
                break;
            case R.id.tv_suggest_object:
                showObjectWindow();
                break;
            case R.id.ll_record:
                startActivity(SuggestAndFeedbackRecordActivity.class);
                break;
        }
    }

    /**
     * 意见反馈
     */
    public void setSuggestion() {
//        if(questionVal==null||questionVal.equals("")) {
//            AlertUtil.show(SuggestAndFeedbackActivity.this, "请选择问题分类", "确定", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            }, "取消", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            },"选择问题分类");
//            return;
//        }
        TextView typeTv = (TextView)findViewById(R.id.tv_suggest_type);
        String typeStr = typeTv.getText().toString();
        //选择投诉对象
        TextView objTv = (TextView)findViewById(R.id.tv_suggest_object);
        String objStr = objTv.getText().toString();
        if(typeStr.equals("")){
            AlertUtil.showAlert(SuggestAndFeedbackActivity.this, "提示","请选择类型~", "确定", null);
            return;
        }
        if(objStr.equals("")){
            AlertUtil.showAlert(SuggestAndFeedbackActivity.this,  "提示","请选投诉建议对象~", "确定", null );
            return;
        }
        EditText content = (EditText) findViewById(R.id.et_content);
        String detail = content.getText().toString().trim();
        if(detail.equals("")){
            AlertUtil.showAlert(SuggestAndFeedbackActivity.this,"提示~", "请填写您的意见或反馈内容~", "确定",null);
            return;
        }
        User user = AppUtils.getUserInfo(SuggestAndFeedbackActivity.this);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("contractInfo", user.getMobil());
        params.put("contracts", user.getEmployeeName());
        params.put("employeeCode", user.getEmployeeCode());
        params.put("detail", detail);//投诉与建议内容
        params.put("loginType", Constant.APP_TYPE);
//        params.put("questionVal", questionVal);//问题分类id
        params.put("suggestionId", objIndex);//投诉或建议的对象 1美亚 2慧通 3 HRS 4携程 5商旅平台
        params.put("suggestionType", typeIndex);//问题分类id问题类型 0 建议 1 投诉

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).setSuggestion(params)
                .enqueue(new CallBack<Object>() {
                    @Override
                    public void success(Object object) {
                        dismissLoading();
                        showToastSuccess();
                        finish();
                    }

                    @Override
                    public void fail(String code) {
                        dismissLoading();
                    }
                });
    }

    /**
     * 获取问题分类
     */
    private void getProblemType() {
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", Constant.APP_TYPE);

        showLoading();
        RetrofitFactory.getInstance().createApi(OrderApi.class).getProblemType(params)
                .enqueue(new CallBack<List<GetSuggestionInfo>>() {
                    @Override
                    public void success(List<GetSuggestionInfo> getSuggestionInfoList) {
                        dismissLoading();
                        showPopupWindows(getSuggestionInfoList,3);
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
    private void showPopupWindows(final List<GetSuggestionInfo> datalist, final int selectType) {
        LinearLayout ly_main = (LinearLayout) findViewById(R.id.ly_main);
        //构建一个popupwindow的布局
        popupView = SuggestAndFeedbackActivity.this.getLayoutInflater().inflate(R.layout.popupwindow_select_person, null);
        final SuggestAndFeedbackPopuListAdapter popAdapter = new SuggestAndFeedbackPopuListAdapter(mContext,datalist);
        ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
        listView.setAdapter(popAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<popAdapter.getCount();i++) {
                    datalist.get(i).setCheck(false);
                }
                selectQuestionVal = datalist.get(position);
                datalist.get(position).setCheck(true);
                popAdapter.notifyDataSetChanged();
            }
        });

        TextView popuptitle = (TextView) popupView.findViewById(R.id.popup_title);
        popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
        popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
        if(selectType == 0){
            popuptitle.setText("选择类型");
        }else if(selectType  == 1){
            popuptitle.setText("选择投诉建议对象");
        }else{
            popuptitle.setText("选择问题分类");
        }
        TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSuggestionInfo getSuggestionInfo = null;
                for(int i=0;i<popAdapter.getCount();i++) {
                    if(datalist.get(i).isCheck()){
                        getSuggestionInfo = datalist.get(i);
                    }
                }
                if(getSuggestionInfo == null){
                    showToast("请选择");
                    return;
                }
                String nameStr = getSuggestionInfo.getName();
                if(selectType == 0){
                    //选择问题类型
                    TextView typeTv = (TextView)findViewById(R.id.tv_suggest_type);
                    int id = getSuggestionInfo.getId();
                    typeTv.setText(nameStr);
                    typeIndex = id;
                }else if(selectType == 1){
                    //选择投诉对象
                    TextView objTv = (TextView)findViewById(R.id.tv_suggest_object);
                    objTv.setText(nameStr);
                    int id = getSuggestionInfo.getId();
                    objIndex = id;
                }else{
                    //选择
                    TextView question = (TextView)findViewById(R.id.tv_problem_type);
                    question.setText(selectQuestionVal.getName());
                    double idDouble = (double) selectQuestionVal.getId();
                    questionVal = (int) idDouble + "";
                }
                window.dismiss();
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
        // 设置动画
//      window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(90000000));
        //  设置可以获取焦点
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();

        popOutShadow(window);
//                        window.showAsDropDown(inputSearch, 0, 50);
        window.showAtLocation(ly_main, Gravity.CENTER,0,0);
    }

    public int getDeviceWidth(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }

    /**
     * 弹窗外阴影
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = SuggestAndFeedbackActivity.this.getWindow().getAttributes();
        lp.alpha = 0.3f;//设置阴影透明度
        SuggestAndFeedbackActivity.this.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            /**当关闭弹窗给筛选条件赋值*/
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = SuggestAndFeedbackActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                SuggestAndFeedbackActivity.this.getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 显示问题类型选择框
     */
    private void showTypeWindow(){
        String[] nameStr = {"建议","投诉"};
        List<GetSuggestionInfo> getSuggestionInfoList = new ArrayList<>();
        for(int i = 0 ;i < nameStr.length;i++){
            GetSuggestionInfo  getSuggestionInfo = new GetSuggestionInfo();
            if(typeIndex == -1){
                if(i == 0){
                    getSuggestionInfo.setCheck(true);
                }
            }else{
                if(typeIndex == i){
                    getSuggestionInfo.setCheck(true);
                }
            }
            getSuggestionInfo.setId(i);
            getSuggestionInfo.setName(nameStr[i]);
            getSuggestionInfoList.add(getSuggestionInfo);
        }
        showPopupWindows(getSuggestionInfoList,0);
    }

    private void showObjectWindow(){
        //1美亚 2慧通 3 HRS 4携程 5商旅平台
        String[] nameStr = {"美亚","慧通","HRS","携程","商旅平台"};
        List<GetSuggestionInfo> getSuggestionInfoList = new ArrayList<>();
        for(int i = 0 ;i < nameStr.length;i++){
            GetSuggestionInfo getSuggestionInfo = new GetSuggestionInfo();
            int realId = i + 1;
            if(objIndex == realId){
                getSuggestionInfo.setCheck(true);
            }else{
                if(objIndex == 0 && i == 0){
                    getSuggestionInfo.setCheck(true);
                }
            }
            getSuggestionInfo.setId(realId);
            getSuggestionInfo.setName(nameStr[i]);
            getSuggestionInfoList.add(getSuggestionInfo);
        }
        showPopupWindows(getSuggestionInfoList,1);
    }

}
