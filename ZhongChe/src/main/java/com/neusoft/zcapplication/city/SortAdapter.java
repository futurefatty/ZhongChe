package com.neusoft.zcapplication.city;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

/**
 * 排序适配器
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer{
	private List<AirportBean> list = null,abroadlist = null;
	private Context mContext;
	private List<AirportBean> historyCity = null;//历史城市
	private List<AirportBean> hotCity = null, abroadHotCity = null;//热门城市
	private GridItemCheckListener gridItemCheckListener;//历史，热门数据点击事件

	private boolean showAbroadData;//true显示国际城市 false 显示国内城市
	public SortAdapter(Context mContext, List<AirportBean> list,List<AirportBean> historyCity,List<AirportBean> hotCity) {
//	public SortAdapter(Context mContext, List<AirportBean> list,List<AirportBean> hotCity) {
		this.mContext = mContext;
		this.list = list;
		this.historyCity = historyCity;
		this.hotCity = hotCity;
        this.abroadlist = new ArrayList<>();
		this.abroadlist.add(new AirportBean());
		this.abroadlist.add(new AirportBean());

        this.abroadHotCity = new ArrayList<>();
	}

    public void setShowAbroadData(boolean showAbroadData) {
        this.showAbroadData = showAbroadData;
    }

    public void setList(List<AirportBean> list, List<AirportBean> hotList) {
		this.list = list;
        if(null != hotList){
		    this.hotCity = hotList;
        }
	}

	public List<AirportBean> getList() {
		return list;
	}

	public void setAbroadList(List<AirportBean> list, List<AirportBean> hotList) {
		this.abroadlist = list;
		this.abroadHotCity = hotList;
	}
    public void setGridItemCheckListener(GridItemCheckListener gridItemCheckListener) {
        this.gridItemCheckListener = gridItemCheckListener;
    }

	public int getCount() {
		if(showAbroadData){
			return this.abroadlist.size() ;
		}else{
			return this.list.size() ;
		}
	}

	public AirportBean getItem(int position) {
		if(showAbroadData){
			return abroadlist.get(position);
		}else{
			return list.get(position);
		}
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取首字母的ascii码值
	 */
	public int getSectionForPosition(int position) {
		if(showAbroadData){
			return abroadlist.get(position).getSortLetters().charAt(0);
		}else{
			return list.get(position).getSortLetters().charAt(0);
		}
	}

	/**
	 */
	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		List<AirportBean> theList ;
		if(showAbroadData){
			theList = abroadlist;
		}else{
			theList = list;
		}
		for (int i = 2; i < theList.size(); i++) {
			String sortStr = theList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public int getItemViewType(int position) {
		return position > 1 ? 2 : position;
	}

	@Override
	public int getViewTypeCount() {
		return 3 ;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		GridViewHolder gridViewHolder = null;
		//item 视图类型 1  历史城市，0 热门城市
		int viewType = getItemViewType(position);
		if(viewType == 1){
			if(convertView == null){
				gridViewHolder = new GridViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_airport, null);

				gridViewHolder.typeTv = (TextView)convertView.findViewById(R.id.item_grid_airport_type);
				gridViewHolder.hotGrid = (GridView) convertView.findViewById(R.id.item_grid_airport_grid);

				convertView.setTag(R.id.hot_city_type,gridViewHolder);
			}else{
				gridViewHolder = (GridViewHolder)convertView.getTag(R.id.hot_city_type);
			}
			if(showAbroadData){
				gridViewHolder.hotGrid.setAdapter(new HotCityAdapter(mContext, this.abroadHotCity));
				if(this.abroadHotCity.size() > 0){
					gridViewHolder.typeTv.setText("热门");
				}
			}else{
				gridViewHolder.hotGrid.setAdapter(new HotCityAdapter(mContext, this.hotCity));
				if(this.hotCity.size() > 0){
					gridViewHolder.typeTv.setText("热门");
				}
			}
			gridViewHolder.hotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					//
					AirportBean bean = hotCity.get(position);
					if(null != gridItemCheckListener){
						gridItemCheckListener.gridChecked(bean);
					}
				}
			});
		}else if(viewType == 0){
			//历史
			if(convertView == null){
				gridViewHolder = new GridViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_airport, null);

				gridViewHolder.typeTv = (TextView)convertView.findViewById(R.id.item_grid_airport_type);
				gridViewHolder.hotGrid = (GridView) convertView.findViewById(R.id.item_grid_airport_grid);

				convertView.setTag(R.id.history_city_type,gridViewHolder);
			}else{
				gridViewHolder = (GridViewHolder)convertView.getTag(R.id.history_city_type);
			}
			gridViewHolder.typeTv.setText("历史");
			if(showAbroadData){
				gridViewHolder.hotGrid.setAdapter(new HotCityAdapter(mContext, this.historyCity));
			}else{
				gridViewHolder.hotGrid.setAdapter(new HotCityAdapter(mContext, this.historyCity));
//				gridViewHolder.typeTv.setText("历史");
			}
			gridViewHolder.hotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					//
					AirportBean bean = historyCity.get(position);
					if(null != gridItemCheckListener){
						gridItemCheckListener.gridChecked(bean);
					}
				}
			});
		}else{
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_airport, null);
				holder.checkImg = (ImageView) convertView.findViewById(R.id.item_airport_checkImg);
				holder.txtNetName = (TextView) convertView.findViewById(R.id.item_airport_name);
				holder.txtCatalog = (TextView) convertView.findViewById(R.id.item_airport_type);
				convertView.setTag(R.id.normal_city_type,holder);
			}else{
				holder = (ViewHolder) convertView.getTag(R.id.normal_city_type);
			}

			AirportBean bean ;
			if(showAbroadData){
				bean = this.abroadlist.get(position);
			}else{
				bean = this.list.get(position);
			}
			//如果是分类的第一个数据，则显示分类名称
			int section = getSectionForPosition(position);
			if(position == getPositionForSection(section)){
				holder.txtCatalog.setVisibility(View.VISIBLE);
				holder.txtCatalog.setText(bean.getSortLetters());
			}else{
				holder.txtCatalog.setVisibility(View.GONE);
			}
			boolean isChecked = bean.isChecked();
			if(isChecked){
				holder.checkImg.setImageResource(R.drawable.btn_singleselection_pressed);
			}else{
				holder.checkImg.setImageResource(R.drawable.btn_singleselection_nor);
			}
			holder.txtNetName.setText(bean.getAirportName());
		}

//		holder.txtNetName.setText(this.list.get(position).getName());

		return convertView;
	}

	interface GridItemCheckListener{
		void gridChecked(AirportBean bean);
	}

    final static class ViewHolder {
		ImageView checkImg;
		TextView txtNetName;
		TextView txtCatalog;
	}

	class GridViewHolder{
		GridView hotGrid;
		TextView typeTv;
	}

	class HotCityAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<AirportBean> hotCites;

		public HotCityAdapter(Context context, List<AirportBean> hotCites) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			this.hotCites = hotCites;
		}

		@Override
		public int getCount() {
			return hotCites.size();
		}

		@Override
		public AirportBean getItem(int position) {
			return hotCites.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.item_airport_grid_view, null);

			TextView city = (TextView) convertView.findViewById(R.id.item_airport_grid_view_name);
			AirportBean  bean = hotCites.get(position);
            boolean isChecked = bean.isChecked();
            if(isChecked){
                convertView.setBackgroundResource(R.drawable.bg_item_airport_grid_checked);
                city.setTextColor(Color.parseColor("#ffffff"));
            }else{
                city.setTextColor(Color.parseColor("#333333"));
                convertView.setBackgroundResource(R.drawable.bg_item_airport_grid);
            }
			String name = bean.getAirportName();
			city.setText(name);
			return convertView;
		}
	}
}