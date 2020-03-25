package com.neusoft.zcapplication.visa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.widget.DefinedGridView;

import java.util.List;
import java.util.Map;


public class VisaListAdapter extends BaseAdapter{
    public static String[] Continents = {"亚洲","欧洲","美洲","澳洲"};
    private Context context;
    private List<List<Map<String,String>>> list;
    private VisaGridAdapter vAdapter;

    public VisaListAdapter(Context context, List<List<Map<String,String>>> list){
        this.context = context;
        this.list = list;
    }
    public void setList(List<String[]> list){
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public List<Map<String,String>> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(null == convertView){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_visa_list,null);

//            holder.item1 =(PxTextView) convertView.findViewById(R.id.item_order_list_item1);
            holder.vGreidView =(DefinedGridView) convertView.findViewById(R.id.visa_country_gridView);
            holder.continent =(TextView) convertView.findViewById(R.id.item_continent);

            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }

        final List<Map<String,String>> gridList = getItem(position);
        vAdapter = new VisaGridAdapter(context,gridList);
        holder.vGreidView.setAdapter(vAdapter);
        holder.vGreidView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,VisaHelpActivity.class);
                Map<String,String> map = gridList.get(position);
                String code = map.get("code").toUpperCase();
                intent.putExtra("code",code);
                context.startActivity(intent);
            }
        });
        holder.continent.setText(Continents[position]);

        return convertView;
    }

    class ViewHolder{
        DefinedGridView vGreidView;
        TextView continent;
    }
}
