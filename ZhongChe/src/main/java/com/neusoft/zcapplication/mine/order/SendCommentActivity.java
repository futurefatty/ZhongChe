package com.neusoft.zcapplication.mine.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neusoft.zcapplication.Bean.User;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.http.NetWorkRequest;
import com.neusoft.zcapplication.http.RequestCallback;
import com.neusoft.zcapplication.http.RequestUtil;
import com.neusoft.zcapplication.http.URL;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;


/**
 * 发表评价
 */
public class SendCommentActivity extends BaseActivity implements View.OnClickListener,RequestCallback {
    private int serviceQuality = 1;
    private String responseSpeed = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_comment);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        LinearLayout star_comment = (LinearLayout)findViewById(R.id.ly_star_comment);
//        ImageView starView = (ImageView)findViewById(R.id.icon_star_nor);
        ImageView icon_star = (ImageView) findViewById(R.id.icon_star);
        ViewGroup.LayoutParams params = icon_star.getLayoutParams();
        star_comment.removeAllViews();
        for (int i=0;i<5;i++) {
            ImageView starView = new ImageView(SendCommentActivity.this);
            starView.setImageResource(R.drawable.icon_evaluation_nor);
            if (i==0){
                starView.setImageResource(R.drawable.icon_evaluation_pressed);
            }
            starView.setLayoutParams(params);
            starView.setOnClickListener(new StarClickListener(i,star_comment,1));
            star_comment.addView(starView);
        }

        LinearLayout star_comment2 = (LinearLayout)findViewById(R.id.ly_star_comment2);
//        ImageView starView = (ImageView)findViewById(R.id.icon_star_nor);
//        ImageView icon_star2 = (ImageView) findViewById(R.id.icon_star2);
//        ViewGroup.LayoutParams params2 = icon_star.getLayoutParams();
        star_comment2.removeAllViews();
        for (int i=0;i<5;i++) {
            ImageView starView = new ImageView(SendCommentActivity.this);
            starView.setImageResource(R.drawable.icon_evaluation_nor);
            if (i==0){
                starView.setImageResource(R.drawable.icon_evaluation_pressed);
            }
            starView.setLayoutParams(params);
            starView.setOnClickListener(new StarClickListener(i,star_comment2,2));
            star_comment2.addView(starView);
        }

        AppUtils.setStateBar(SendCommentActivity.this,findViewById(R.id.frg_status_bar));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_submit:
                sendComment();
                break;
        }
    }

    @Override
    public void requestSuccess(Object map, int type) {
        Map<String,Object> result = (Map<String, Object>) map;
        if(null != result){
            String code = null == result.get("code") ? "" : result.get("code").toString();
//            String codeMsg = null == result.get("codeMsg") ? "" : result.get("codeMsg").toString();
//            String data  = (String)result.get("data");
            if(code.equals("00000")) {
                ToastUtil.toastHandleSuccess(SendCommentActivity.this);
                finish();
            }
            else {
                ToastUtil.toastHandleError(SendCommentActivity.this);
            }
        }else{
            //请求失败
            ToastUtil.toastHandleError(SendCommentActivity.this);
        }
    }

    @Override
    public void requestFail(int type) {
        ToastUtil.toastHandleFail(SendCommentActivity.this);
    }

    @Override
    public void requestCancel(int type) {

    }

    /**
     * 发布评价
     */
    private void sendComment() {
        String[] orderTypes = {"2","3","1"};
        String supplierId = getIntent().getStringExtra("supplierId");//供应商ID
        if(supplierId==null||supplierId.equals("")||supplierId.equals("null")) {
            supplierId = "";
        }
        else {
            supplierId = supplierId.substring(0,supplierId.indexOf("."));
        }
        String id = getIntent().getStringExtra("id");//订单id
        id = id.substring(0,id.indexOf("."));
        int ticketType = getIntent().getIntExtra("ticketType",0);//机票类型
        int type = getIntent().getIntExtra("type",0);//评价类型
        EditText description = (EditText) findViewById(R.id.et_description);
        String dscStr = description.getText().toString().trim();
        if(dscStr.equals("")){
            Toast.makeText(SendCommentActivity.this,"请填写描述内容！",Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new AppUtils().getUserInfo(SendCommentActivity.this);
        Map<String,Object> params = new HashMap<>();
        params.put("ciphertext","test");
        params.put("loginType", URL.LOGIN_TYPE);
        params.put("description",dscStr);
        params.put("employeeCode",user.getEmployeeCode());
        params.put("id",id);
        params.put("orderType",orderTypes[type-1]);
        params.put("responseSpeed",responseSpeed);
        params.put("serviceQuality",serviceQuality);
        params.put("supplierId",supplierId);
        params.put("ticketType",ticketType);
        params.put("type",type);
        Retrofit retrofit = new RequestUtil().getRequestClient(URL.ZHONGCHE_93);
        NetWorkRequest request = retrofit.create(NetWorkRequest.class);
        Call<Map<String,Object>> call = request.sendComment(params);
        new RequestUtil().requestData(call,this,1,mContext);
    }

    class StarClickListener implements View.OnClickListener {
        private int position;
        private LinearLayout layout;
        private int type;
        private String[] commentLevelArr = {"很差","差","中","良","优"};

        public StarClickListener (int position, LinearLayout layout, int type) {
            this.position = position;
            this.layout = layout;
            this.type = type;
        }
        @Override
        public void onClick(View v) {
            TextView commnetLevel;
            if(type == 1) {
                commnetLevel = (TextView) findViewById(R.id.text_comment_level);
                serviceQuality = position + 1;
            }
            else {
                commnetLevel = (TextView) findViewById(R.id.text_comment_level2);
                responseSpeed = (position + 1) + "";
            }

            commnetLevel.setText(commentLevelArr[position]);
            for(int i=0;i<=position;i++) {
                ImageView img = (ImageView) layout.getChildAt(i);
                img.setImageResource(R.drawable.icon_evaluation_pressed);
            }
            for(int i=position+1;i<5;i++) {
                ImageView img = (ImageView) layout.getChildAt(i);
                img.setImageResource(R.drawable.icon_evaluation_nor);
            }
        }
    }
}
