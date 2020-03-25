package com.neusoft.zcapplication.mine.backlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetAllSuppliers;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 用车供应商适配器
 */

public class BusinessCarChoseSupplierAdapter extends BaseAdapter{

    private Context context;
    private List<GetAllSuppliers> list;

    public BusinessCarChoseSupplierAdapter(Context context, List<GetAllSuppliers> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<GetAllSuppliers> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetAllSuppliers getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_business_car_chose_supplier,null);

            holder.iv_status =(ImageView) convertView.findViewById(R.id.iv_status);
            holder.tv_apply_name =(TextView) convertView.findViewById(R.id.tv_apply_name);
            holder.tv_contact_name =(TextView) convertView.findViewById(R.id.tv_contact_name);
            holder.tv_contact_mobile =(TextView) convertView.findViewById(R.id.tv_contact_mobile);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        GetAllSuppliers getAllSuppliers = list.get(position);
        if(getAllSuppliers.isCheck()){
            holder.iv_status.setImageResource(R.mipmap.btn_checkbox_pressed);
        }else{
            holder.iv_status.setImageResource(R.mipmap.btn_checkbox_nor);
        }
        holder.tv_apply_name.setText("服务商名称: " + getAllSuppliers.getSupplierName());
        holder.tv_contact_name.setText("联系人名称: " + getAllSuppliers.getContactName());
        holder.tv_contact_mobile.setText("联系方式: " + getAllSuppliers.getMobile());

        return convertView;
    }

    class ViewHolder{
        ImageView iv_status;
        TextView tv_apply_name;
        TextView tv_contact_name;
        TextView tv_contact_mobile;
    }
}
