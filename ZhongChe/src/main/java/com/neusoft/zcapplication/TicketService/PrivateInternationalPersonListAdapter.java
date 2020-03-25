package com.neusoft.zcapplication.TicketService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.PersonItem;
import com.neusoft.zcapplication.R;

import java.util.List;
import java.util.Map;

/**
 * 出行人员列表适配器
 */

public class PrivateInternationalPersonListAdapter extends BaseAdapter{
    private Context context;
    private List<PersonItem> list;
    private boolean showDel;
    private Option mOption;

    public interface Option{
        void onDeleteClick(int position);
    }

    public PrivateInternationalPersonListAdapter(Context context, List<PersonItem> list, Option option){
        this.context = context;
        this.list = list;
        this.showDel = false;
        this.mOption = option;
    }
    public void toggleDelBtn(){
        if(this.showDel){
            showDel = false;
        }else{
            showDel = true;
        }
    }
    public boolean isShowDel(){
        return showDel;
    }

    @Override
    public int getCount() {
        if(this.list!=null) {
            return this.list.size();
        }
        return 0;
    }

    public List<PersonItem> getList() {
        return list;
    }

    public void setList(List<PersonItem> list){
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
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_private_international_person_list,null);

//            holder.item1 =(PxTextView) convertView.findViewById(R.id.item_order_list_item1);
            holder.del_person =(ImageView) convertView.findViewById(R.id.icon_del_person);
            holder.name =(TextView) convertView.findViewById(R.id.item_person_name);
            holder.employeeCode =(TextView) convertView.findViewById(R.id.item_person_employeeCode);
            holder.idNo = (EditText) convertView.findViewById(R.id.item_person_idNo);
            holder.mobile = (EditText) convertView.findViewById(R.id.item_person_mobile);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        holder.name.setText(item.getName());
        holder.employeeCode.setText(item.getEmployeeCode());
//        holder.typeTv.setText("");//设置证件名称
        holder.mobile.setText(item.getMobile());
        if(showDel){
            holder.del_person.setVisibility(View.VISIBLE);
        }else{
            holder.del_person.setVisibility(View.GONE);
        }
//        holder.del_person.setOnClickListener(new DelListener(convertView,position));
//
//        //点击“删除航程”，显示或隐藏item前面的删除按钮
//        boolean isShowDel = item.isShowDel();
//        if(isShowDel) {
//            holder.del_person.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.del_person.setVisibility(View.GONE);
//        }
        holder.del_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOption!=null){
                    mOption.onDeleteClick(position);
                }
            }
        });

        if(item.isShowItem()) {
            convertView.setVisibility(View.VISIBLE);
        }
        else {
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private Map<String,Object> getSelectMap(List<Map<String,Object>> list){
        for(int i = 0 ; i < list.size() ; i++){
            Map<String,Object> map = list.get(i);
            String check = null == map.get("isCheck") ? "false" : map.get("isCheck").toString();
            if(check.equals("true")){
                return map;
            }
        }
        return null;
    }

    class ViewHolder{
        TextView name, employeeCode;
        EditText idNo, mobile;
        ImageView del_person;
    }

//    class DelListener implements View.OnClickListener {
//        private View view;
//        private int position;
//
//        public DelListener (View view, int position) {
//            this.view = view;
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View v) {
//            list.remove(position);
//            view.setVisibility(View.GONE);
//        }
//    }
}
