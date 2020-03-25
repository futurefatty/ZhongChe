package com.neusoft.zcapplication.widget;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.MapUtil;

/**
 * StartMapPopupWindow
 */
public class StartMapPopupWindow extends PopupWindow{
    private View mMenuView;
    private Context context;
	private Map<String,String> map;
    public StartMapPopupWindow(final Activity context,final Map<String,String> map ) {
        super(context);  
        this.context = context;
        this.map = map;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.start_map_layout, null);
        View layout = mMenuView.findViewById(R.id.pop_shadow);
		layout.getBackground().setAlpha(80);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		ListView listView = (ListView)mMenuView.findViewById(R.id.start_map_list_view) ;
		String[] ary = {"百度","高德"};
		ArrayAdapter adapter = new ArrayAdapter(context,R.layout.item_start_map,R.id.item_start_map_tv,ary);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listItemClick(position);
				dismiss();
			}
		});

        TextView cancel = (TextView) mMenuView.findViewById(R.id.start_map_btn_cancel);
        cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
        
        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
//        this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);
        
        //PopupWindow关闭时，恢复正常背景       
        this.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
//				if(type == 1){
//					WindowManager.LayoutParams lp = context.getParent().getParent().getWindow().getAttributes();
//					lp.alpha = 1f;
//					context.getParent().getParent().getWindow().setAttributes(lp);
//				}else{
//					WindowManager.LayoutParams lp = context.getWindow().getAttributes();
//					lp.alpha = 1f;
//					context.getWindow().setAttributes(lp);
//				}
			}
		});     
    }
    private void listItemClick(int position){
		String lat = map.get("lat");
		String lng = map.get("lng");
		String addr = map.get("addr");
		switch (position){
			case 0:
				//百度
				MapUtil.start2Baidu(context,lat,lng,addr);
				break;
			case 1:
				//跳转高德
				MapUtil.start2Gaode(context,lat,lng,addr);
				break;
		}
	}
}
