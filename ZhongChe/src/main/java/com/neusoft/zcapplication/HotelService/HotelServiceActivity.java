package com.neusoft.zcapplication.HotelService;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.ShowViewForNonBusinessActivity;
import com.neusoft.zcapplication.TicketService.ServiceGridAdapter;
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
 * 酒店服务
 */
public class HotelServiceActivity extends BaseActivity implements View.OnClickListener,View.OnLongClickListener{
    private GridView gridView;
    private ServiceGridAdapter adapter;
    private PopupWindow copyPopupWindow;
    private int longClickIndex ;//长按 下标，用来定位view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_service);
        initView();
        initPopupWindow();
    }

    private void initView(){
        TextView title = (TextView) findViewById(R.id.tv_page_title);
        title.setText("酒店服务");
        findViewById(R.id.btn_back).setOnClickListener(this);
//        String[] textArr = {"因公-携程酒店","因私-携程酒店","因公-携程订单","酒店订单","税票信息","酒店入住天数确认"};
//        String[] picArr = {"icon_hotel_ctrip_order","icon_hotel_ctrip_order","icon_hotel_ctrip_order","icon_hotel_order","icon_hotel_taxreceipt","icon_hotel_days"};
        String[] textArr = {"酒店订单","因公-携程订单","因私-携程酒店","税票信息","酒店入住天数确认","差旅标准"};
        String[] picArr = {"icon_hotel_order","icon_hotel_ctrip_order","icon_hotel_ctrip_order","icon_hotel_taxreceipt","icon_hotel_days","icon_rule"};
        List<Map<String,String>> datalist = new ArrayList<>();
        for(int i=0;i<picArr.length;i++) {
            Map<String,String> item = new HashMap<>();
            item.put("name",textArr[i]);
            item.put("ids",picArr[i]);
            datalist.add(item);
        }
        gridView  =  (GridView) findViewById(R.id.ticket_gridView);
        adapter = new ServiceGridAdapter(HotelServiceActivity.this,datalist);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                String url ;
                switch (position) {
//                    case 0:
//                        intent = new Intent(HotelServiceActivity.this,ShowViewForNonBusinessActivity.class);
//                        url = "ctrip";
//                        intent.putExtra("url",url);
//                        intent.putExtra("loginType",2);//1机票 ，2 酒店 3 携程订单
//                        intent.putExtra("dataType",1);//1因公 ，2 因私
//                        startActivity(intent);
//                        break;
                    case 0:
                        intent = new Intent(HotelServiceActivity.this,OrderActivity.class);
                        intent.putExtra("position",2);
                        startActivity(intent);
//                        Toast.makeText(HotelServiceActivity.this,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        intent = new Intent(HotelServiceActivity.this,ShowViewForNonBusinessActivity.class);
                        url = "ctrip";
                        intent.putExtra("url",url);
                        intent.putExtra("loginType",3);//1机票 ，2 酒店 3 携程订单
                        intent.putExtra("dataType",2);//1因公 ，2 因私
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(HotelServiceActivity.this,ShowViewForNonBusinessActivity.class);
                        url = "ctrip";
                        intent.putExtra("url",url);
                        intent.putExtra("loginType",2);//1机票 ，2 酒店 3 携程订单
                        intent.putExtra("dataType",2);//1因公 ，2 因私
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(HotelServiceActivity.this,TaxInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(HotelServiceActivity.this,HotelListQueryActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(HotelServiceActivity.this,RuleActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        //显示酒店表格布局
        findViewById(R.id.act_service_tel_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.act_hotel_table).setVisibility(View.VISIBLE);
        AppUtils.setStateBar(HotelServiceActivity.this,findViewById(R.id.frg_status_bar));
        findViewById(R.id.act_hotel_tel_self).setOnClickListener(this);//企业
        findViewById(R.id.act_hotel_tel_hrs1).setOnClickListener(this);//hrs手机
        findViewById(R.id.act_hotel_tel_hrs2).setOnClickListener(this);//hrs固话
        findViewById(R.id.act_hotel_tel_xc1).setOnClickListener(this);//携程客服
        findViewById(R.id.act_hotel_tel_xc2).setOnClickListener(this);//携程经理
        findViewById(R.id.act_hotel_tel_ht).setOnClickListener(this);//慧通
        //邮箱等长按事件
        findViewById(R.id.act_hotel_email_hrs).setOnLongClickListener(this);//hrs邮箱
        findViewById(R.id.act_hotel_email_xc1).setOnLongClickListener(this);
        findViewById(R.id.act_hotel_email_xc2).setOnLongClickListener(this);
        findViewById(R.id.act_hotel_email_ht).setOnLongClickListener(this);

//        registerForContextMenu(textView);
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
            case R.id.act_hotel_tel_ht:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000059180"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_hotel_tel_hrs1:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008821660"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_hotel_tel_hrs2:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:8009880058"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_hotel_tel_xc1:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4009806666"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.act_hotel_tel_xc2:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02759536666"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        TextView selectTv = (TextView)v;
        selectTv.setTextColor(Color.parseColor("#c70019"));

        int dis = DisplayUtil.dpTopx(HotelServiceActivity.this,26) +
                DisplayUtil.spTopx(HotelServiceActivity.this,14) * 2;
        switch (v.getId()){
            case R.id.act_hotel_email_hrs:
                longClickIndex = 0;
                copyPopupWindow.showAsDropDown(v);
                break;
            case R.id.act_hotel_email_xc1:
                longClickIndex = 1;
                copyPopupWindow.showAsDropDown(v);
                break;
            case R.id.act_hotel_email_xc2:
                longClickIndex = 2;
//                copyPopupWindow.showAsDropDown(v);
                copyPopupWindow.showAsDropDown(v,0,-dis);
                break;
            case R.id.act_hotel_email_ht:
                longClickIndex = 3;
//                copyPopupWindow.showAsDropDown(v);
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
                    TextView textView = (TextView)findViewById(R.id.act_hotel_email_hrs);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if(longClickIndex == 1){
                    TextView textView = (TextView)findViewById(R.id.act_hotel_email_xc1);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if(longClickIndex == 2){
                    TextView textView = (TextView)findViewById(R.id.act_hotel_email_xc2);
                    textView.setTextColor(Color.parseColor("#666666"));
                }else if( longClickIndex == 3){
                    TextView textView = (TextView)findViewById(R.id.act_hotel_email_ht);
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
                    TextView textView = (TextView)findViewById(R.id.act_hotel_email_hrs);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if(longClickIndex == 1){
                    TextView textView = (TextView)findViewById(R.id.act_hotel_email_xc1);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if(longClickIndex == 2){
                    TextView textView = (TextView)findViewById(R.id.act_hotel_email_xc2);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }else if( longClickIndex == 3){
                    TextView textView = (TextView)findViewById(R.id.act_hotel_email_ht);
                    String text = textView.getText().toString();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                }

                copyPopupWindow.dismiss();
                ToastUtil.toastNeedData(HotelServiceActivity.this,"复制成功");
            }
        });
    }
}
