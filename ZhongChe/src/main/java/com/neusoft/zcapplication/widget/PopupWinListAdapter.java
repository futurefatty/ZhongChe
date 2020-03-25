package com.neusoft.zcapplication.widget;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Constant.Constant;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 弹窗内容列表
 */

public class PopupWinListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> list;
    private int type;

    public PopupWinListAdapter(Context context, List<Map<String, Object>> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public List<Map<String, Object>> getList() {
        return this.list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    /**
     * 获取选中的人员
     *
     * @return
     */
    public Map<String, Object> getSelectMap() {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String check = null == map.get("isCheck") ? "false" : map.get("isCheck").toString();
            if (check.equals("true")) {
                return map;
            }
        }

        return null;
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Map<String, Object> item = getItem(position);
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_popup_list, null);

            holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            holder.icon_check = (ImageView) convertView.findViewById(R.id.icon_check);
            holder.item_code = (TextView) convertView.findViewById(R.id.item_code);
            holder.item_department = (TextView) convertView.findViewById(R.id.item_department);
            holder.item_ll = (LinearLayout) convertView.findViewById(R.id.item_ll);

            ///3月
            holder.ipd_pro_ll = (LinearLayout) convertView.findViewById(R.id.ipd_pro_ll);
            holder.ipd_pmCode = (TextView) convertView.findViewById(R.id.ipd_pmCode);
            holder.ipd_projName = (TextView) convertView.findViewById(R.id.ipd_projName);
            holder.ipd_line = (TextView) convertView.findViewById(R.id.ipd_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (type == 1) {//出行人员
            holder.item_name.setText(item.get("employeeName") + "");
        } else if (type == 2) {//出行方式
            holder.item_name.setText(item.get("name") + "");
        } else if (type == 4) {//授权-员工列表
            String empName = item.get("EMPLOYEE_NAME") + "";
            String empDptName = null == item.get("unitName") ? "" : "(" + item.get("unitName") + ")";//员工所在部门名称
            String str = empName + empDptName;
            holder.item_name.setText(str);
        } else if (type == 5) {
            //维护用户证件类型时选择证件类型
            holder.item_name.setText(item.get("name") + "");
        } else if (type == 6) {
            //新增预定申请单，添加同行人员选择证件类型
//            double indexDb = null == item.get("DOCUMENTID") ? 0 :(double) item.get("DOCUMENTID");
//            int indexInt = ((int) indexDb )% Constant.credentialsAry.length;
//            String text = Constant.credentialsAry[indexInt];
//            holder.item_name.setText(text);
            String documentName = null == item.get("DOCUMENTNAME") ? "" : item.get("DOCUMENTNAME").toString();
            holder.item_name.setText(documentName);
        } else if (type == 8) {
            holder.item_name.setText(item.get("auditorName") + "");
        } else if (type == 9) {
            //选择入住时间
            String fromDate = item.get("fromDate").toString();
            fromDate = fromDate.length() > 10 ? fromDate.substring(0, 10) : fromDate;
            String toDate = item.get("toDate").toString();
            toDate = toDate.length() > 10 ? toDate.substring(0, 10) : toDate;
            String price = item.get("price").toString();
            String str = fromDate + "   " + toDate + "   ￥" + price;
            holder.item_name.setText(str);
        } else if (type == 10) {
            //选择退票原因
            String reason = item.get("reason").toString();
            holder.item_name.setText(Html.fromHtml(reason));
        } else if (type == 7) {
            holder.item_name.setText(item.get("unitName") + "");
            if (item.containsKey("isShow")) {
                if (!StringUtil.isEmpty(item.get("isShow").toString() + "") & item.get("isShow").equals("false")) {
                    holder.item_ll.setVisibility(View.GONE);
                } else {
                    holder.item_ll.setVisibility(View.VISIBLE);
                }
            }

        } else if (type == 12) {
            holder.item_code.setVisibility(View.VISIBLE);
            holder.item_department.setVisibility(View.VISIBLE);
            holder.item_name.setText(item.get("EMPLOYEENAME") + "");
            holder.item_code.setText(item.get("EMPLOYEECODE") + "");
            holder.item_department.setText(item.get("UNITNAME") + "");
        } else if (type == 14) {//IPD项目
            holder.item_department.setVisibility(View.VISIBLE);
            holder.ipd_pro_ll.setVisibility(View.VISIBLE);
            holder.ipd_line.setVisibility(View.VISIBLE);
            holder.item_name.setText("项目名称:　" + item.get("projName") + "");
            holder.item_department.setText("任务名称:　" + item.get("taskName") + "");
            holder.ipd_pmCode.setText(item.get("pmCode") + "");
            holder.ipd_projName.setText("审批人:　　" + item.get("pmName") + "");
        } else {//核算主体
            holder.item_name.setText(item.get("companyName") + "");
        }
        String isCheck = null == item.get("isCheck") ? "false" : item.get("isCheck").toString();
        if (isCheck.equals("true")) {
            holder.icon_check.setImageResource(R.drawable.btn_singleselection_pressed);
        } else {
            holder.icon_check.setImageResource(R.drawable.btn_singleselection_nor);
        }

        return convertView;
    }

    class ViewHolder {
        TextView item_name;
        TextView item_code;
        TextView item_department;
        ImageView icon_check;
        LinearLayout item_ll;
        //
        LinearLayout ipd_pro_ll;
        TextView ipd_pmCode;
        TextView ipd_projName;
        TextView ipd_line;
    }
}
