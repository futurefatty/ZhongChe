package com.neusoft.zcapplication.mine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.entity.GetCredentialsData;

import java.util.List;

/**
 * 证件列表适配器
 */

public class CredentialsAdapter extends BaseAdapter {

    private List<GetCredentialsData> list;
    private Context context;
    private boolean showDelBtn;//是否显示删除按钮的标识
//    private String[] credentialsAry = {"","身份证","护照","港澳通行证","台胞证","台湾通行证","回乡证","其他"};

    public CredentialsAdapter(List<GetCredentialsData> list, Context context) {
        this.list = list;
        this.context = context;
        this.showDelBtn = false;//默认设置不显示删除按钮
    }

    /**
     * 触发显示列表删除按钮
     */
    public void toggleShowDelBtn(){
        if(showDelBtn){
            showDelBtn = false;
        }else{
            showDelBtn = true;
        }
    }

    public boolean isShowDelBtn() {
        return showDelBtn;
    }

    public List<GetCredentialsData> getList() {
        return list;
    }

    public void setList(List<GetCredentialsData> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetCredentialsData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder ;
        if(null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_credentials,null);
            holder.familyNameTv = (TextView) convertView.findViewById(R.id.item_list_credentials_family_name);
            holder.secondNameTv = (TextView) convertView.findViewById(R.id.item_list_credentials_second_name);
            holder.typeTv = (TextView) convertView.findViewById(R.id.item_list_credentials_type);
            holder.numTv = (TextView) convertView.findViewById(R.id.item_list_credentials_num);
            holder.validDateTv = (TextView) convertView.findViewById(R.id.item_list_credentials_valid_date);
            holder.delLayout = (LinearLayout) convertView.findViewById(R.id.item_list_credentials_del_layout);
            holder.showDetailLayout = (LinearLayout) convertView.findViewById(R.id.item_list_credentials_center_layout);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        GetCredentialsData getCredentialsData = list.get(position);
        String familyName = getCredentialsData.getFamilyName();
        String secondName = getCredentialsData.getSecondName();
        String documentInfo = getCredentialsData.getDocumentInfo();//证件号码
        String endDate = getCredentialsData.getEndDate();//证件有效期
//        double documentId = null == map.get("documentId") ? 0 : (double)map.get("documentId");//证件类型
////        String documentId = null == map.get("documentInfo") ? "0" : map.get("documentInfo").toString();//证件类型
//        int typeIndex = ((int) documentId )% Constant.credentialsAry.length;
//        holder.typeTv.setText(Constant.credentialsAry[typeIndex]);

        String documentName = getCredentialsData.getDocumentName();
        holder.familyNameTv.setText(familyName);
        holder.secondNameTv.setText(secondName);
        holder.typeTv.setText(documentName);
        holder.validDateTv.setText(endDate);
        holder.numTv.setText(documentInfo);
        if(showDelBtn){
            holder.delLayout.setVisibility(View.VISIBLE);
        }else{
            holder.delLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        TextView familyNameTv,secondNameTv,typeTv,numTv,validDateTv;
        LinearLayout delLayout,showDetailLayout;
    }
}
