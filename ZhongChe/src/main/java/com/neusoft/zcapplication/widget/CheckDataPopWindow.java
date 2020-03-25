package com.neusoft.zcapplication.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

public class CheckDataPopWindow extends PopupWindow{
    private View popupView;
    private Context context;
    private int position;
    private PopWindowBtnListener clickListener;
    /**
     *
     * @param context
     * @param dataList
     * @param dataType 1选择同行人界面，2选择出行方式； 3选择核算主体界面，6 选择证件类型界面，10选择
     * @param viewType
     */
    public CheckDataPopWindow(final Activity context,final List<Map<String,Object>> dataList
    		,final int dataType,final int viewType,int p,int width,int height) {
        super(context);  
        this.context = context;
        this.position = p;
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.popupwindow_select_person, null);

		final PopupWinListAdapter popAdapter = new PopupWinListAdapter(context,dataList,dataType);
		ListView listView = (ListView) popupView.findViewById(R.id.popupwin_list);
		listView.setAdapter(popAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				for(int i=0;i < popAdapter.getCount();i++) {
					dataList.get(i).put("isCheck","false");
				}
				dataList.get(position).put("isCheck","true");
				popAdapter.notifyDataSetChanged();
			}
		});

		TextView popupTitle = (TextView) popupView.findViewById(R.id.popup_title);
        if(dataType == 1){
		    popupView.findViewById(R.id.select_person).setVisibility(View.VISIBLE);
        }else{
		    popupView.findViewById(R.id.select_person).setVisibility(View.GONE);
        }
		popupView.findViewById(R.id.popupwin_list).setVisibility(View.VISIBLE);
        popupTitle.setText("选择退票原因");
		TextView tv_ok = (TextView) popupView.findViewById(R.id.select_person_ok);
		tv_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clickListener.queryClick(popAdapter.getSelectMap(), position);
            	dismiss();
			}
		});

		popupView.findViewById(R.id.select_person_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

        //设置SelectPicPopupWindow的View  
        this.setContentView(popupView);
        //设置SelectPicPopupWindow弹出窗体的宽、高
//        this.setWidth(LayoutParams.MATCH_PARENT);
//        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setWidth(width);
        this.setHeight(height);
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
//        this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(90000000);
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        
        //PopupWindow关闭时，恢复正常背景       
        this.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
            // TODO Auto-generated method stub
            if(viewType == 1){
                WindowManager.LayoutParams lp=context.getParent().getParent().getWindow().getAttributes();
                lp.alpha = 1f;
                context.getParent().getParent().getWindow().setAttributes(lp);
            }else{
                WindowManager.LayoutParams lp = context.getWindow().getAttributes();
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
			}
		});
    }

    public void setBtnClickListener(PopWindowBtnListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface PopWindowBtnListener{
		void queryClick(Map<String,Object> map,int position);
	}
}
