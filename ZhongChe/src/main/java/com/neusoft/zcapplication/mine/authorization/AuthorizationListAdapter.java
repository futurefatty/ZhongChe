package com.neusoft.zcapplication.mine.authorization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetAuthorizationInfo;
import com.neusoft.zcapplication.tools.StringUtil;

import java.util.List;
import java.util.Map;


/**
 *我的授权列表
 */

public class AuthorizationListAdapter extends BaseAdapter{

    private Context context;
    private List<GetAuthorizationInfo> list;
    private int type; //
    private ClickEvent clickEvent;

    public AuthorizationListAdapter(Context context, List<GetAuthorizationInfo> list, int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public List<GetAuthorizationInfo> getList() {
        return list;
    }

    public void setList(List<GetAuthorizationInfo> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public GetAuthorizationInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final GetAuthorizationInfo getAuthorizationInfo = getItem(position);
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_authorization_list,null);

            holder.item_employeeName =(TextView) convertView.findViewById(R.id.item_employeeName);
            holder.item_employeeCode =(TextView) convertView.findViewById(R.id.item_employeeCode);
            holder.item_idCard =(TextView) convertView.findViewById(R.id.item_idCard);
            holder.item_dept =(TextView) convertView.findViewById(R.id.item_dept);
            holder.item_available_date =(TextView) convertView.findViewById(R.id.item_available_date);
            holder.btn_cancelAuthorization =(TextView) convertView.findViewById(R.id.btn_cancel_authorization);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.item_employeeName.setText(getAuthorizationInfo.getEmployeeName());
        holder.item_employeeCode.setText(getAuthorizationInfo.getEmployeeCode());
        holder.item_idCard.setVisibility(View.GONE);
//        holder.item_idCard.setText(item.get("idCard")+"");
        String cpStr = StringUtil.isEmpty(getAuthorizationInfo.getUnitName()) ? "无主岗信息" :  getAuthorizationInfo.getUnitName();
        holder.item_dept.setText(cpStr);
        holder.item_available_date.setText(getAuthorizationInfo.getExpiryTime());
        holder.btn_cancelAuthorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEvent.cancelAuthorization(getAuthorizationInfo.getEmployeeCode(),position);
            }
        });

        return convertView;
    }

    public void setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    interface ClickEvent {
        /**
         * 取消授权
         * @param beAgreeCode
         * @param position
         */
        void cancelAuthorization(String beAgreeCode, int position);
    }

    class ViewHolder{
        TextView item_employeeName, item_employeeCode, item_idCard, item_dept, item_available_date;
        TextView btn_cancelAuthorization;
    }
}
