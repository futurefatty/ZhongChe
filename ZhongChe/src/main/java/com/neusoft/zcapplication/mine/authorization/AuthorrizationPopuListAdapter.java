package com.neusoft.zcapplication.mine.authorization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetEmployeeInfo;

import java.util.List;

/**
 * 弹窗内容列表
 */

public class AuthorrizationPopuListAdapter extends BaseAdapter{

    private Context context;
    private List<GetEmployeeInfo> list;

    public AuthorrizationPopuListAdapter(Context context, List<GetEmployeeInfo> list){
        this.context = context;
        this.list = list;
    }

    public List<GetEmployeeInfo> getList(){
        return this.list;
    }

    public void setList(List<GetEmployeeInfo> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetEmployeeInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
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
        GetEmployeeInfo getEmployeeInfo = list.get(position);
        String empName = getEmployeeInfo.getEMPLOYEE_NAME();
        String empDptName = getEmployeeInfo.getUnitName();//员工所在部门名称
        String str = empName + "("+empDptName+")";
        holder.item_name.setText(str);
        if(getEmployeeInfo.isCheck()) {
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
