package com.neusoft.zcapplication.CarService;

import android.content.Context;
import android.database.DatabaseUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetAllApplyUseCars;
import com.neusoft.zcapplication.tools.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * 公务用车申请适配器
 */

public class BusinessCarApplyAdapter extends BaseAdapter{

    private Context context;
    private List<GetAllApplyUseCars> list;
    private boolean isEdit;

    public BusinessCarApplyAdapter(Context context, List<GetAllApplyUseCars> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<GetAllApplyUseCars> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetAllApplyUseCars getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GetAllApplyUseCars getAllApplyUseCars = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_business_car_apply,null);

            holder.iv_check =(ImageView) convertView.findViewById(R.id.iv_check);
            holder.tv_order_number =(TextView) convertView.findViewById(R.id.tv_order_number);
            holder.tv_car_type =(TextView) convertView.findViewById(R.id.tv_car_type);
            holder.tv_time =(TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_applicant =(TextView) convertView.findViewById(R.id.tv_applicant);
            holder.tv_status =(TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_verify_name =(TextView) convertView.findViewById(R.id.tv_verify_name);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        if(isEdit){
            holder.iv_check.setVisibility(View.VISIBLE);
            if(getAllApplyUseCars.isCheck()){
                holder.iv_check.setImageResource(R.mipmap.btn_checkbox_pressed);
            }else{
                holder.iv_check.setImageResource(R.mipmap.btn_checkbox_nor);
            }
        }else{
            holder.iv_check.setVisibility(View.GONE);
        }
        holder.tv_order_number.setText("申请单号: "+getAllApplyUseCars.getCarApplyId());
        holder.tv_car_type.setText("用车类型: "+getAllApplyUseCars.getCarType());
        holder.tv_time.setText("申请时间: "+DateUtils.DateToDayStr(new Date(getAllApplyUseCars.getApplicationDate())));
        holder.tv_applicant.setText("申请人: "+getAllApplyUseCars.getApplicatName());
        holder.tv_verify_name.setVisibility(View.VISIBLE);
        //0：待审批 11:审批中（部门审批同意） 12：审批拒绝（部门审批拒绝） 21：待办理（总经办审批同意） 22：审批拒绝（总经办审批拒绝） 3/31：已办理  32：办理拒绝
        switch (getAllApplyUseCars.getAuditStatus()){
            case 0:
                holder.tv_status.setText("待审批");
                holder.tv_verify_name.setText(getAllApplyUseCars.getAuditorName());
                break;
            case 11:
                holder.tv_status.setText("审批中");
                holder.tv_verify_name.setText(getAllApplyUseCars.getAuditorName());
                break;
            case 12:
                holder.tv_status.setText("审批拒绝");
                holder.tv_verify_name.setText(getAllApplyUseCars.getAuditorName());
                break;
            case 21:
                holder.tv_status.setText("待办理");
                holder.tv_verify_name.setText(getAllApplyUseCars.getConductorName());
                break;
            case 22:
                holder.tv_status.setText("审批拒绝");
                holder.tv_verify_name.setText(getAllApplyUseCars.getAuditorName());
                break;
            case 3:
                holder.tv_status.setText("已办理");
                holder.tv_verify_name.setVisibility(View.INVISIBLE);
                break;
            case 31:
                holder.tv_status.setText("已办理");
                holder.tv_verify_name.setVisibility(View.INVISIBLE);
                break;
            case 32:
                holder.tv_status.setText("办理拒绝");
                holder.tv_verify_name.setText(getAllApplyUseCars.getConductorName());
                break;
        }
        return convertView;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    class ViewHolder{
        ImageView iv_check;
        TextView tv_order_number;
        TextView tv_car_type;
        TextView tv_time;
        TextView tv_applicant;
        TextView tv_status;
        TextView tv_verify_name;
    }

}
