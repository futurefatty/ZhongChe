package com.neusoft.zcapplication.TicketService;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.LogUtil;
import com.neusoft.zcapplication.tools.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 因私国际申请适配器
 */
public class privateInternationalApplyOrderListAdapter extends BaseAdapter{

    private Context context;
    private List<Map<String,Object>> list;
    private int type; //0, 列表不显示复选框
    private int dataType;//数据类型 0 机票，1 酒店

    public privateInternationalApplyOrderListAdapter(Context context, List<Map<String,Object>> list, int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String,Object>> list){
        this.list = list;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 返回选中的某个map
     * @return
     */
    public Map<String, Object> getSelectMap() {
        for(int i = 0 ;i < list.size(); i++){
            Map<String,Object> map = list.get(i);
            String str = null == map.get("isChecked") ? "" : map.get("isChecked").toString();
            if(str.equals("yes")){
                return map;
            }
        }
        return null;
    }
    @Override
    public Map<String,Object> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Map<String,Object> item = list.get(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_apply_order_list,null);
            holder.ly_item =(LinearLayout) convertView.findViewById(R.id.ly_item);
            holder.icon_check =(ImageView) convertView.findViewById(R.id.icon_check);
            holder.orderNo =(TextView) convertView.findViewById(R.id.item_order_no);
            holder.toCity =(TextView) convertView.findViewById(R.id.item_toCity);
            holder.fromCity =(TextView) convertView.findViewById(R.id.item_from_City);
            holder.startDate =(TextView) convertView.findViewById(R.id.item_start_date);
            holder.endDateTv =(TextView) convertView.findViewById(R.id.item_end_date);
            holder.nameTv =(TextView) convertView.findViewById(R.id.item_order_name);
            holder.useName =(TextView) convertView.findViewById(R.id.item_use_name);
            holder.state =(TextView) convertView.findViewById(R.id.item_state);
            //申请人、乘机人标题
            holder.applyEmp =(TextView) convertView.findViewById(R.id.order_apply_name_title);
            holder.useEmp =(TextView) convertView.findViewById(R.id.order_use_emp_title);
            holder.approval =(TextView) convertView.findViewById(R.id.item_approval_name);
            holder.fromCityTitle =(TextView) convertView.findViewById(R.id.item_from_City_title);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
//        Log.i("--->","item:" + item);
        String empName = null == item.get("applicateName")? "" : item.get("applicateName").toString();//申请人
        String userNameStr = null == item.get("empName")? "" : item.get("empName").toString();//乘机人
        String fromCity = null == item.get("fromCity")?"" : item.get("fromCity").toString();
        String toCity = null == item.get("toCity")?"" : item.get("toCity").toString();
        String approval = "".equals(item.get("leaderName"))?"" : item.get("leaderName").toString();
        //String state = null == item.get("approveState") ? "" : item.get("approveState").toString();//订单审批状态
        double isBuy = null == item.get("isBuy") ? -1 :(double)item.get("isBuy");//订单审批状态  isBuy 为空的时候=已提交， isBuy=0 已发布,isBuy=1已确认
        holder.approval.setVisibility(View.GONE);
        String state = "已提交";
        if (isBuy==-1){
            state = "已提交";
        }else if (isBuy==0){
            state = "已发布";
        }else if (isBuy==1){
            state = "已确认";
        }
        holder.orderNo.setText(item.get("orderApplyId")+"");
        holder.toCity.setText(toCity);
//        holder.startDate.setText(item.get("fromDate") + "");
        holder.nameTv.setText(empName);
        holder.useName.setText(userNameStr);
        holder.applyEmp.setText("申请人：");
        if(dataType == 0){
            holder.useEmp.setText("乘机人：");
            holder.fromCity.setText(fromCity);
            holder.fromCity.setVisibility(View.VISIBLE);
            holder.fromCityTitle.setVisibility(View.VISIBLE);
            holder.startDate.setText("出发日期：" + item.get("fromDate"));
            holder.endDateTv.setVisibility(View.GONE);
//            holder.dateTitle.setText("出发日期 ：");
        }else{
            holder.useEmp.setText("入住人：");
            holder.fromCity.setVisibility(View.GONE);
            holder.fromCityTitle.setVisibility(View.GONE);
//            holder.dateTitle.setText("入住日期：");
            holder.startDate.setText("入住日期：" + item.get("checkInTime"));
            holder.endDateTv.setText("离店日期：" + item.get("checkOutTime"));
            holder.endDateTv.setVisibility(View.VISIBLE);
        }
        holder.approval.setText(approval);
        holder.state.setText(state);
        String isChecked = item.get("isChecked")+"";
        //
        if(type == 0){
            holder.icon_check.setVisibility(View.GONE);
//            //控制显示复选框的标识
//            String showCheckBox = null == item.get("showCheckBox")
//                    ?"no" :item.get("showCheckBox").toString();
//            if(showCheckBox.equals("no")){
//                holder.icon_check.setVisibility(View.GONE);
//            }else{
//                holder.icon_check.setVisibility(View.VISIBLE);
//            }
        }else{
            holder.icon_check.setVisibility(View.VISIBLE);
            if(isChecked!=null&&isChecked.equals("yes")){
                holder.icon_check.setImageResource(R.mipmap.btn_checkbox_pressed);
            }else{
                holder.icon_check.setImageResource(R.mipmap.btn_checkbox_nor);
            }
        }
        return convertView;
    }

    class ViewHolder{
        TextView orderNo, toCity, startDate,endDateTv,nameTv,fromCity,useName,state;
        TextView applyEmp,useEmp,approval;//申请人、使用人标题、审批人
        TextView fromCityTitle;
        LinearLayout ly_item;
        ImageView icon_check;
    }
}
