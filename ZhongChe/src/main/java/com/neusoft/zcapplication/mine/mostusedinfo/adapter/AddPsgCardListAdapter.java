package com.neusoft.zcapplication.mine.mostusedinfo.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.mine.mostusedinfo.bean.PsgCardBean;

import java.util.List;

/**
 * 添加常用乘客证件列表适配器
 */

public class AddPsgCardListAdapter extends BaseAdapter {
    private List<PsgCardBean> list ;
    private Context context;
    private boolean showDelBtn;
    private ItemViewClick itemViewClick;
    public AddPsgCardListAdapter(List<PsgCardBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setItemViewClick(ItemViewClick itemViewClick) {
        this.itemViewClick = itemViewClick;
    }

    public List<PsgCardBean> getList() {
        return list;
    }

    public void setList(List<PsgCardBean> list) {
        this.list = list;
    }

    public void toggleDelBtn(){
        if(showDelBtn){
            showDelBtn = false;
        }else{
            showDelBtn = true;
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PsgCardBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(null == convertView){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_psd_card,null);
            holder.dateTv = (TextView)convertView.findViewById(R.id.item_psg_card_date);//证件有效期
            holder.typeTv = (TextView)convertView.findViewById(R.id.item_psg_card_type);//证件类型
            holder.infoEt = (EditText)convertView.findViewById(R.id.item_psg_card_et);//证件号码
            holder.delLayout = (LinearLayout)convertView.findViewById(R.id.icon_psg_del_layout);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        PsgCardBean bean = list.get(position);
        if(showDelBtn){
            holder.delLayout.setVisibility(View.VISIBLE);
        }else{
            holder.delLayout.setVisibility(View.GONE);
        }

        holder.infoEt.clearFocus();
        //移除输入框的内容输入监听事件
        if (holder.infoEt.getTag() instanceof TextWatcher) {
            holder.infoEt.removeTextChangedListener((TextWatcher) (holder.infoEt.getTag()));
        }

        EditWatcherListener fromWatcher = new EditWatcherListener(position,0);
        holder.infoEt.addTextChangedListener(fromWatcher);
        holder.infoEt.setTag(fromWatcher);
        String cardType = null == bean.getCardType() ?"" : bean.getCardType();
        String cardNum = null == bean.getCardNum() ?"" : bean.getCardNum();
        String cardDate = null == bean.getCardDate() ?"" : bean.getCardDate();
//        String name = map.get("name").toString();
//        String type = map.get("type").toString();
//        String data = map.get("data").toString();
//
        holder.typeTv.setText(cardType);//显示证件类型
        holder.infoEt.setText(cardNum);
        if(!cardDate.equals("")){
            holder.dateTv.setText(cardDate);
        }
        //删除按钮点击事件
        holder.delLayout.setOnClickListener(new ItemClickListener(position,0));
        holder.dateTv.setOnClickListener(new ItemClickListener(position,2));//选择日期
        holder.typeTv.setOnClickListener(new ItemClickListener(position,1));//选择证件类型
        return convertView;
    }

    public interface ItemViewClick{
        void delItem(int position);//删除证件类型
        void selectDate(int position);//选择日期
        void selectCardType(int position);//选择证件类型
    }

    class ViewHolder{
        LinearLayout delLayout;
        TextView typeTv,dateTv;
        EditText infoEt;
    }

    class EditWatcherListener implements TextWatcher{
        private int position;
        public EditWatcherListener(int position,int type){
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString().trim();
            list.get(position).setCardNum(text);
        }
    }

    private class ItemClickListener implements View.OnClickListener{
        private int position;
        private int type ;//0删除 1选择证件 2 选择日期

        public ItemClickListener(int position,int type) {
            this.position = position;
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            if(type == 0 ){
                itemViewClick.delItem(position);
            }else if(type  == 1){
                itemViewClick.selectCardType(position);
            }else {
                itemViewClick.selectDate(position);
            }
        }
    }
}
