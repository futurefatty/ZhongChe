package com.neusoft.zcapplication.approval;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.Bean.HotelTripItem;
import com.neusoft.zcapplication.Bean.OuterTripItem;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.tools.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 国际机票行程列表适配器
 */

public class OutTripListAdapter extends BaseAdapter{
    private Context context;
    private List<OuterTripItem> list;
    private boolean showDetBtn;//显示删除按钮的标识
    private TripItemClickListener itemCellClick;
    private OutTripListAdapter outTripListAdapter;
    public OutTripListAdapter(final Context context, List<OuterTripItem> list){
        this.context = context;
        this.list = list;
        this.showDetBtn = false;//默认设置不显示删除按钮
    }

    public void setOutTripListAdapter(OutTripListAdapter outTripListAdapter) {
        this.outTripListAdapter = outTripListAdapter;
    }

    public void setItemCellClick(TripItemClickListener itemCellClick) {
        this.itemCellClick = itemCellClick;
    }

    public List<OuterTripItem> getList() {
        if(null == list){
            return new ArrayList<>();
        }else{
            return list;
        }
    }

    public void setList(List<OuterTripItem> list) {
        this.list = list;
    }

    public void toggleShowDelBtn() {
        if(showDetBtn){
            showDetBtn = false;
        }else{
            showDetBtn = true;
        }
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public OuterTripItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_out_trip_list,null);
            holder.delLayout = (LinearLayout)convertView.findViewById(R.id.item_out_trip_del_btn) ;
            holder.toCityEt = (EditText) convertView.findViewById(R.id.item_out_trip_to_city) ;
            holder.fromCityEt = (EditText) convertView.findViewById(R.id.item_out_trip_from_city) ;
            holder.switchLayout =(LinearLayout) convertView.findViewById(R.id.item_out_trip_switch);
            holder.timeTv =(TextView) convertView.findViewById(R.id.item_out_trip_start_time);
            holder.indexTv =(TextView) convertView.findViewById(R.id.item_out_trip_index);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        //清除输入框的焦点
        holder.fromCityEt.clearFocus();
        holder.toCityEt.clearFocus();
        //移除输入框的内容输入监听事件
        if (holder.fromCityEt.getTag() instanceof TextWatcher) {
            holder.fromCityEt.removeTextChangedListener((TextWatcher) (holder.fromCityEt.getTag()));
        }
        if (holder.toCityEt.getTag() instanceof TextWatcher) {
            holder.toCityEt.removeTextChangedListener((TextWatcher) (holder.toCityEt.getTag()));
        }

        final OuterTripItem item = getItem(position);

        String toCityName = null == item.getToCityName() ? "" : item.getToCityName();
        String fromCityName = null == item.getFromCityName() ? "" : item.getFromCityName();
        String startTime = null == item.getStartTime() ? "" : item.getStartTime();

        holder.toCityEt.setText(toCityName);
        holder.fromCityEt.setText(fromCityName);
        holder.timeTv.setText(startTime);

        EditWatcherListener fromWatcher = new EditWatcherListener(position,0);
        holder.fromCityEt.addTextChangedListener(fromWatcher);
        holder.fromCityEt.setTag(fromWatcher);

        EditWatcherListener toWatcher = new EditWatcherListener(position,1);
        holder.toCityEt.addTextChangedListener(toWatcher);
        holder.toCityEt.setTag(toWatcher);

        holder.timeTv.setOnClickListener(new ItemClickLister(position));
        holder.switchLayout.setOnClickListener(new ItemClickLister(position));
        if(showDetBtn){
            holder.delLayout.setVisibility(View.VISIBLE);
            holder.delLayout.setOnClickListener(new ItemClickLister(position));
        }else{
            holder.delLayout.setVisibility(View.GONE);
            holder.delLayout.setOnClickListener(null);
        }
        int index = position + 1;
        holder.indexTv.setText("行程" + index);
        return convertView;
    }

    class ItemClickLister implements View.OnClickListener{
        private int position;

        public ItemClickLister(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_out_trip_start_time:
                    if(null != itemCellClick){
                        itemCellClick.selectDateByType(position,1);
                    }
                    break;
                case R.id.item_out_trip_del_btn:
                    if(null != itemCellClick){
                        itemCellClick.delTripByType(position,1);
                    }
                    break;
                case R.id.item_out_trip_switch:
                    //交换出发到达城市
                    OuterTripItem outTrip = list.get(position);
                    String toCityName = null == outTrip.getToCityName() ? "" : outTrip.getToCityName();
                    String fromCityName = null == outTrip.getFromCityName() ? "" : outTrip.getFromCityName();
                    if(!toCityName.equals("") || !fromCityName.equals("")){
                        if(null != outTripListAdapter){
                            list.get(position).setToCityName(fromCityName);
                            list.get(position).setFromCityName(toCityName);
                            outTripListAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    }
    class EditWatcherListener implements TextWatcher{
        private int position;
        private int viewType;//0出发城市，1到达城市
        public EditWatcherListener(int position,int type){
            this.position = position;
            this.viewType = type;
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
            if(viewType == 0){
                list.get(position).setFromCityName(text);
            }else{
                list.get(position).setToCityName(text);
            }
        }
    }
    class ViewHolder{
        EditText toCityEt, fromCityEt;
        TextView timeTv;//出发时间
        TextView indexTv;//下标
        LinearLayout delLayout,switchLayout;
    }
}
