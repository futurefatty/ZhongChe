package com.neusoft.zcapplication.TicketService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewForNonBusinessActivity;
import com.neusoft.zcapplication.base.BaseActivity;
import com.neusoft.zcapplication.mine.order.OrderActivity;
import com.neusoft.zcapplication.tools.AppUtils;
import com.neusoft.zcapplication.tools.DisplayUtil;
import com.neusoft.zcapplication.tools.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机票服务
 */
public class TicketServiceActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener{
    private GridView gridView;
    private ServiceGridAdapter adapter;
    private int longClickIndex;//长按下标
    private PopupWindow copyPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_service);
        initView();
        initPopupWindow();
    }

    private void initView(){
        TextView title = (TextView) findViewById(R.id.tv_page_title);
        title.setText("机票服务");
        findViewById(R.id.btn_back).setOnClickListener(this);
        String[] textArr = {"机票查询","国内机票订单","国际机票订单","因私-携程机票","在线值机","因私-美亚机票"};
        String[] picArr =
                {
                        "icon_ticket_flightinformation",
                        "icon_ticket_inland",
                        "icon_ticket_international",
                        "icon_hotel_ctrip_order",
                        "icon_ticket_checkinandrescheduling",
                        "icon_ticket_international"};
        List<Map<String,String>> datalist = new ArrayList<>();
        for(int i=0;i<picArr.length;i++) {
            Map<String,String> item = new HashMap<>();
            item.put("name",textArr[i]);
            item.put("ids",picArr[i]);
            if(i<picArr.length-1)
                datalist.add(item);
        }
        gridView  =  (GridView) findViewById(R.id.ticket_gridView);
        adapter = new ServiceGridAdapter(TicketServiceActivity.this,datalist);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                String url = "";
                switch (position) {
                    case 0:
                        //跳转机票信息界面
                        intent = new Intent(TicketServiceActivity.this,SearchFlightTicketActivity.class);
                        startActivity(intent);

                        break;
                    case 1:
                        intent = new Intent(TicketServiceActivity.this,OrderActivity.class);
                        intent.putExtra("position",0);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(TicketServiceActivity.this,OrderActivity.class);
                        intent.putExtra("position",1);
                        startActivity(intent);
//                        Toast.makeText(TicketServiceActivity_.this,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case 3 :
                        intent = new Intent(TicketServiceActivity.this,ShowViewForNonBusinessActivity.class);
                        url = "ctrip";
                        intent.putExtra("url",url);
                        intent.putExtra("loginType",1);//1机票 ，2 酒店 3 携程订单
                        intent.putExtra("dataType",2);//1因公 ，2 因私
                        startActivity(intent);
                        break;
                    case 4:
                        ToastUtil.toastNeedData(TicketServiceActivity.this,"敬请期待");
                        break;
                    case 5:
                        intent = new Intent(TicketServiceActivity.this,PrivateInternationalApplyActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        AppUtils.setStateBar(TicketServiceActivity.this,findViewById(R.id.frg_status_bar));

        findViewById(R.id.act_service_tel_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.act_ticket_table).setVisibility(View.VISIBLE);

        findViewById(R.id.act_hotel_tel_self).setOnClickListener(this);//企业
        findViewById(R.id.act_ticket_tel_my_inner).setOnClickListener(this);//美亚国内
        findViewById(R.id.act_ticket_tel_my_aboard).setOnClickListener(this);//美亚国际
        findViewById(R.id.act_ticket_tel_my_aboard2).setOnClickListener(this);//美亚国际
        findViewById(R.id.act_ticket_tel_xc1).setOnClickListener(this);//携程
        findViewById(R.id.act_ticket_tel_xc2).setOnClickListener(this);//携程客服
        findViewById(R.id.act_ticket_tel_xc3).setOnClickListener(this);
        findViewById(R.id.act_ticket_tel_mei_international).setOnClickListener(this);//美亚国际服务热线
        //qq  邮箱长按事件
        findViewById(R.id.act_ticket_qq_my).setOnLongClickListener(this);//美亚国内机票qq
        findViewById(R.id.act_ticket_email_my).setOnLongClickListener(this);//美亚国内机票邮箱
        findViewById(R.id.act_ticket_qq_my1).setOnLongClickListener(this);//美亚国际机票qq
        findViewById(R.id.act_ticket_email_my1).setOnLongClickListener(this);//美亚国际机票邮箱
        findViewById(R.id.act_ticket_email_my2).setOnLongClickListener(this);//美亚国际机票邮箱

        findViewById(R.id.act_ticket_qq_xc1).setOnLongClickListener(this);//携程国际机票qq
        findViewById(R.id.act_ticket_email_xc1).setOnLongClickListener(this);//携程国际机票邮箱
        findViewById(R.id.act_ticket_email_xc2).setOnLongClickListener(this);//携程国际机票邮箱
        findViewById(R.id.act_ticket_email_xc3).setOnLongClickListener(this);//携程国际机票邮箱
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.act_hotel_tel_self:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:073128493444"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_ticket_tel_my_inner:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4006139139"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_ticket_tel_my_aboard2:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+862022830800"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_ticket_tel_my_aboard:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4006139139"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_ticket_tel_xc1:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4009806666"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_ticket_tel_xc2:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02152604658"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_ticket_tel_xc3:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02759536666"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_ticket_tel_mei_international:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:073128210607"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {

        /* findViewById(R.id.act_ticket_qq_my).setOnLongClickListener(this);//美亚国内机票qq
        findViewById(R.id.act_ticket_email_my).setOnLongClickListener(this);//美亚国内机票邮箱
        findViewById(R.id.act_ticket_qq_my1).setOnLongClickListener(this);//美亚国际机票qq
        findViewById(R.id.act_ticket_email_my1).setOnLongClickListener(this);//美亚国际机票邮箱
        findViewById(R.id.act_ticket_email_my2).setOnLongClickListener(this);//美亚国际机票邮箱

        findViewById(R.id.act_ticket_qq_xc1).setOnLongClickListener(this);//携程国际机票qq
        findViewById(R.id.act_ticket_email_xc1).setOnLongClickListener(this);//携程国际机票邮箱
        findViewById(R.id.act_ticket_email_xc2).setOnLongClickListener(this);//携程国际机票邮箱
        findViewById(R.id.act_ticket_email_xc3).setOnLongClickListener(this);//携程国际机票邮箱*/

        TextView selectTv = (TextView)v;
        selectTv.setTextColor(Color.parseColor("#c70019"));

        int dis = DisplayUtil.dpTopx(TicketServiceActivity.this,26) +
                DisplayUtil.spTopx(TicketServiceActivity.this,14) * 2;
        switch (v.getId()){
            case R.id.act_ticket_qq_my:
                longClickIndex = 0;
                copyPopupWindow.showAsDropDown(v);
                break;
            case R.id.act_ticket_email_my:
                longClickIndex = 1;
                copyPopupWindow.showAsDropDown(v);
                break;
            case R.id.act_ticket_qq_my1:
                longClickIndex = 2;
//                copyPopupWindow.showAsDropDown(v);
                copyPopupWindow.showAsDropDown(v,0,-dis);
                break;
            case R.id.act_ticket_email_my1:
                longClickIndex = 3;
//                copyPopupWindow.showAsDropDown(v);
                copyPopupWindow.showAsDropDown(v,0,-dis);
                break;
            case R.id.act_ticket_email_my2:
                longClickIndex = 4;
                copyPopupWindow.showAsDropDown(v,0,-dis);
                break;
            case R.id.act_ticket_qq_xc1:
                longClickIndex = 5;
                copyPopupWindow.showAsDropDown(v,0,-dis);
                break;
            case R.id.act_ticket_email_xc1:
                longClickIndex = 6;
                copyPopupWindow.showAsDropDown(v,0,-dis);
                break;
            case R.id.act_ticket_email_xc2:
                longClickIndex = 7;
                copyPopupWindow.showAsDropDown(v,0,-dis);
                break;
            case R.id.act_ticket_email_xc3:
                longClickIndex = 8;
                copyPopupWindow.showAsDropDown(v,0,-dis);
                break;
        }
        return true;
    }

    /**
     * 复制功能弹窗
     */
    private void initPopupWindow() {
        View popupView = getLayoutInflater().inflate(R.layout.pop_copy_layout, null);
        copyPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        copyPopupWindow.setTouchable(true);
        copyPopupWindow.setOutsideTouchable(true);
        copyPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        copyPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(longClickIndex == 0){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_qq_my);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if(longClickIndex == 1){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_my);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if(longClickIndex == 2){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_qq_my1);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if( longClickIndex == 3){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_my1);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if(longClickIndex == 4){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_my2);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if(longClickIndex == 5){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_qq_xc1);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if( longClickIndex == 6){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_xc1);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if(longClickIndex == 7){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_xc2);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if(longClickIndex == 8){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_xc3);
                    textView.setTextColor(Color.parseColor("#666666"));
                }
            }
        });

        TextView btnCopy = (TextView) popupView.findViewById(R.id.pop_btn_copy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData mClipData = ClipData.newPlainText("Label", "csrzic@hrs.com");
//                cm.setPrimaryClip(mClipData);

                if(longClickIndex == 0){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_qq_my);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if(longClickIndex == 1){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_my);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if(longClickIndex == 2){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_qq_my1);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if( longClickIndex == 3){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_my1);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if(longClickIndex == 4){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_my2);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if(longClickIndex == 5){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_qq_xc1);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if( longClickIndex == 6){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_xc1);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if(longClickIndex == 7){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_xc2);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if(longClickIndex == 8){
                    TextView textView = (TextView)findViewById(R.id.act_ticket_email_xc3);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }

                copyPopupWindow.dismiss();
                ToastUtil.toastNeedData(TicketServiceActivity.this,"复制成功");
            }
        });
    }
}
