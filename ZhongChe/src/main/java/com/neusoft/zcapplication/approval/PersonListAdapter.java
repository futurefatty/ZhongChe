package com.neusoft.zcapplication.approval;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.PersonItem;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 出行人员列表适配器
 */

public class PersonListAdapter extends BaseAdapter {
    private Context context;
    private List<PersonItem> list;
    private boolean showDel;
    private ApprovalActivity activity;

    public PersonListAdapter(Context context, List<PersonItem> list) {
        this.context = context;
        this.activity = (ApprovalActivity) context;
        this.list = list;
        this.showDel = false;
    }

    public void toggleDelBtn() {
        if (this.showDel) {
            showDel = false;
        } else {
            showDel = true;
        }
    }

    public boolean isShowDel() {
        return showDel;
    }

    @Override
    public int getCount() {
        if (this.list != null) {
            return this.list.size();
        }
        return 0;
    }

    public List<PersonItem> getList() {
        return list;
    }

    public void setList(List<PersonItem> list) {
        this.list = list;
    }

    @Override
    public PersonItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final PersonItem item = getItem(position);
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_person_list, null);

//            holder.item1 =(PxTextView) convertView.findViewById(R.id.item_order_list_item1);
            holder.del_person = (ImageView) convertView.findViewById(R.id.icon_del_person);
            holder.name = (TextView) convertView.findViewById(R.id.item_person_name);
            holder.employeeCode = (TextView) convertView.findViewById(R.id.item_person_employeeCode);
            holder.idNo = (TextView) convertView.findViewById(R.id.item_person_idNo);
            holder.mobile = (TextView) convertView.findViewById(R.id.item_person_mobile);
            holder.accountUnit = (TextView) convertView.findViewById(R.id.item_person_accountUnit);
            holder.typeTv = (TextView) convertView.findViewById(R.id.item_person_id_type);
            holder.departmentTv = (TextView) convertView.findViewById(R.id.item_person_depart);
            holder.item_project_text = (TextView) convertView.findViewById(R.id.item_project_text);
            holder.item_linear_project = (LinearLayout) convertView.findViewById(R.id.item_linear_project);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(item.getName());
        holder.employeeCode.setText(item.getEmployeeCode());
        //遍历出选择的证件类型、证件号
        List<Map<String, Object>> credentialsList = item.getCredentialsInfo();
        if (null != credentialsList) {
//            Log.i("--->","证件列表：" + credentialsList);
            Map<String, Object> creMap = getSelectMap(credentialsList);
            if (creMap != null) {
                String DOCUMENTINFO = null == creMap.get("DOCUMENTINFO") ? "" : creMap.get("DOCUMENTINFO").toString();
                holder.idNo.setText(DOCUMENTINFO);
//                double indexDb = null == creMap.get("DOCUMENTID") ? 0 :(double) creMap.get("DOCUMENTID");
//                int indexInt = (int) indexDb;
//                String text = Constant.credentialsAry[indexInt];
//                holder.typeTv.setText(text);
                String documentName = null == creMap.get("DOCUMENTNAME") ? "" : creMap.get("DOCUMENTNAME").toString();
                holder.typeTv.setText(documentName);
            }
        }
//        holder.typeTv.setText("");//设置证件名称
        holder.mobile.setText(item.getMobile());
        holder.accountUnit.setText(item.getAccountEntity());
        holder.departmentTv.setText(item.getUnitName());
        if (showDel) {
            holder.del_person.setVisibility(View.VISIBLE);
        } else {
            holder.del_person.setVisibility(View.GONE);
        }
        if (item.isShowItem()) {
            convertView.setVisibility(View.VISIBLE);
        } else {
            convertView.setVisibility(View.GONE);
        }
        String p = item.getPosition();
        if (StringUtil.isEmpty(item.getPosition())) {
            holder.item_linear_project.setVisibility(View.GONE);
        }
//        else {
//            if (!item.getPosition().equals("A") && !item.getPosition().equals("B") && !item.getPosition().equals("C")) {
//                holder.item_linear_project.setVisibility(View.VISIBLE);
//                holder.item_project_text.setText(item.getProjectLeaderName());
//                holder.item_linear_project.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        activity.showProjectPopu(item);
//                    }
//                });
//            } else {
//                holder.item_linear_project.setVisibility(View.GONE);
//            }
//        }
        return convertView;
    }

    private Map<String, Object> getSelectMap(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String check = null == map.get("isCheck") ? "false" : map.get("isCheck").toString();
            if (check.equals("true")) {
                return map;
            }
        }
        return null;
    }

    class ViewHolder {
        TextView name, employeeCode, idNo, mobile, accountUnit, typeTv, departmentTv, item_project_text;
        ImageView del_person;
        LinearLayout item_linear_project;
    }

}
