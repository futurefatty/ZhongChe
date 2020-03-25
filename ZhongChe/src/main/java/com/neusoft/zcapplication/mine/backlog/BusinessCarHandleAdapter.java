package com.neusoft.zcapplication.mine.backlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetApplyCarsAudit;
import com.neusoft.zcapplication.entity.GetApplyCarsDeal;
import com.neusoft.zcapplication.tools.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 用车办理适配器
 */

public class BusinessCarHandleAdapter extends BaseAdapter{

    private Context context;
    private List<GetApplyCarsDeal> list;

    public BusinessCarHandleAdapter(Context context, List<GetApplyCarsDeal> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<GetApplyCarsDeal> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetApplyCarsDeal getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_business_car_handle,null);

            holder.tv_status =(TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_title =(TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_number =(TextView) convertView.findViewById(R.id.tv_number);
            holder.tv_time =(TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_applicant =(TextView) convertView.findViewById(R.id.tv_applicant);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        GetApplyCarsDeal getApplyCarsDeal = getItem(position);
        holder.tv_number.setText("申请单号: "+getApplyCarsDeal.getCarApplyId());
        holder.tv_time.setText("申请日期: "+DateUtils.DateToDayStr(new Date(getApplyCarsDeal.getApplicationDate())));
        holder.tv_applicant.setText("申请人: "+getApplyCarsDeal.getApplicatName());
        //0：待审批 11:审批中（部门审批同意） 12：审批拒绝（部门审批拒绝） 21：待办理（总经办审批同意） 22：审批拒绝（总经办审批拒绝） 3/31：已办理  32：办理拒绝
        switch (getApplyCarsDeal.getAuditStatus()){
            case 0:
                holder.tv_status.setText("待审批");
                break;
            case 11:
                holder.tv_status.setText("审批中");
                break;
            case 12:
                holder.tv_status.setText("审批拒绝");
                break;
            case 21:
                holder.tv_status.setText("待办理");
                break;
            case 22:
                holder.tv_status.setText("审批拒绝");
                break;
            case 3:
                holder.tv_status.setText("已办理");
                break;
            case 31:
                holder.tv_status.setText("已办理");
                break;
            case 32:
                holder.tv_status.setText("办理拒绝");
                break;
        }
        return convertView;
    }

    class ViewHolder{
        TextView tv_status;
        TextView tv_title;
        TextView tv_number;
        TextView tv_time;
        TextView tv_applicant;
    }
}
