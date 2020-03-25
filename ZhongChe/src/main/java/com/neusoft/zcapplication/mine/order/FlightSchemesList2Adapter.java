package com.neusoft.zcapplication.mine.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.widget.DefinedExpandListView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/22.
 */

public class FlightSchemesList2Adapter extends BaseAdapter{
    private Context context;
    private List<List<Map<String,Object>>> list;
    private int type; //
    private String applicantId;
//    private FlightSchemesPassengerListAdapter passengerListAdapter;
    private ClickEvent clickEvent;

    public FlightSchemesList2Adapter(Context context, List<List<Map<String,Object>>> list, String applicantId, ClickEvent clickEvent){
        this.context = context;
        this.list = list;
        this.applicantId = applicantId;
        this.clickEvent = clickEvent;
    }
    public void setList(List<List<Map<String,Object>>> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public List<Map<String,Object>> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final List<Map<String,Object>> item = getItem(position);
        final ViewHolder holder ;
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_flight_schemes_list,null);

            holder.listView = (DefinedExpandListView) convertView.findViewById(R.id.passenger_list);
            holder.btn_book = (TextView) convertView.findViewById(R.id.btn_book);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        double totalPrice = 0;
        for(Map<String,Object> map:item){
            double tPrice = null == map.get("TOTALPRICE") ? 0.0 :(double) map.get("TOTALPRICE");
            totalPrice = totalPrice + tPrice;
        }
        holder.tv_price.setText("￥"+totalPrice);
        holder.btn_book.setVisibility(View.GONE);
//        if (applicantId.equals(User.CurrentUser.getEmployeeCode())){
//            holder.btn_book.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    double buyId = Double.parseDouble(item.get(0).get("BUYID")+"");
//                    clickEvent.book((int)buyId);
//                }
//            });
//        }else {
//            holder.btn_book.setOnClickListener(null);
//            holder.btn_book.setTextColor(Color.parseColor("#999999"));
//            holder.btn_book.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cancel_btn_border));
//        }

//        List<Map<String,Object>> passengerList = item;
//        for(int i=0;i<passengerList.size();i++) {
//            passengerList.get(i).put("isShow","false");
//        }
        //同行人listView
//        passengerListAdapter = new FlightSchemesPassengerListAdapter(context,getItem(position),position);
//        passengerListAdapter.setClickEvent(this);
//        holder.listView.setAdapter(passengerListAdapter);

        final SchemesExpandListViewAdapter expAdapter = new SchemesExpandListViewAdapter(context,item);
        expAdapter.setShowArrow(false);
        holder.listView.setAdapter(expAdapter);
        holder.listView.setGroupIndicator(null);
        for(int i=0;i<item.size();i++){
            holder.listView.expandGroup(i);
        }
        holder.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
//        holder.listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//
//                // TODO Auto-generated method stub
//                for(int i = 0 ;i < expAdapter.getGroupCount(); i ++){
//                    if(i != groupPosition){
//                        holder.listView.expandGroup(i);
//                    }
//                }
//            }
//        });

        return convertView;
    }

    interface ClickEvent{
        void book(int buyId);
    }

    class ViewHolder{
        DefinedExpandListView listView;
        TextView tv_price,btn_book;
    }
}
