package com.neusoft.zcapplication.CarService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetSelectEmployee;

import java.util.List;
import java.util.Map;

/**
 * 弹窗内容列表
 */

public class PopupWinBusinessCarListAdapter extends BaseAdapter{

    private Context context;
    private List<GetSelectEmployee.CompanyBean> list;

    public PopupWinBusinessCarListAdapter(Context context, List<GetSelectEmployee.CompanyBean> list){
        this.context = context;
        this.list = list;
    }

    public List<GetSelectEmployee.CompanyBean> getList(){
        return this.list;
    }

    public void setList(List<GetSelectEmployee.CompanyBean> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetSelectEmployee.CompanyBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GetSelectEmployee.CompanyBean item = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_popup_list,null);
            holder.item_name =(TextView) convertView.findViewById(R.id.item_name);
            holder.icon_check =(ImageView) convertView.findViewById(R.id.icon_check);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.item_name.setText(item.getCompanyName());
        if(item.isCheck()) {
            holder.icon_check.setImageResource(R.drawable.btn_singleselection_pressed);
        }else {
            holder.icon_check.setImageResource(R.drawable.btn_singleselection_nor);
        }
        return convertView;
    }

    class ViewHolder{
        TextView item_name;
        ImageView icon_check;
    }

}
