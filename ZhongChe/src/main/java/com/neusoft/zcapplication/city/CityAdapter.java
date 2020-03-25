package com.neusoft.zcapplication.city;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.crcc.commonlib.inter.OnItemClickListener;
import com.crcc.commonlib.utils.StringUtils;
import com.crcc.commonlib.utils.UtilDisplay;
import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.widget.recycleview.expand.ExpandGroupIndexEntity;
import com.neusoft.zcapplication.widget.recycleview.expand.ExpandGroupItemEntity;
import com.neusoft.zcapplication.widget.recycleview.expand.RecyclerExpandBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author:Six
 * Date:2019/5/21
 */
public class CityAdapter extends RecyclerExpandBaseAdapter<String, CityModel, RecyclerView.ViewHolder> {

    /**
     * 历史城市
     */
    public static final int HISTORY_CITY = 2;
    /**
     * 热门城市
     */
    public static final int HOT_CITY = 3;


    private Map<Integer, Integer> itemViewTypes = new HashMap<>();


    private CityListModel cityListModel;


    public CityAdapter(CityListModel cityListModel) {
        this.cityListModel = cityListModel;
        setData(cityListModel.getExpandGroupItemEntities());
    }


    /**
     * group->position
     *
     * @param groupIndex
     * @return
     */
    public int getGroupOfPosition(int groupIndex) {
        //历史 热门 of position
        if (null != itemViewTypes.get(groupIndex)) {
            return groupIndex;
        }
        //group A
        if (groupIndex - itemViewTypes.size() == 0) {
            return groupIndex;
        }
        int groupForPosition = itemViewTypes.size();
        for (int index = 0; index < groupIndex - itemViewTypes.size(); index++) {
            ExpandGroupItemEntity<String, CityModel> expandGroupItemEntity = mDataList.get(index);
            //group
            groupForPosition += 1;
            //children
            groupForPosition += expandGroupItemEntity.getChildList().size();
        }
        return groupForPosition;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case HISTORY_CITY:
            case HOT_CITY:
                holder = new GridCityViewHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_city_head, parent, false), viewType);
                break;
            case VIEW_TYPE_ITEM_TITLE:
                holder = new CapitalViewHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_city_capital, parent, false));
                break;
            case VIEW_TYPE_ITEM_CONTENT:
                holder = new CityNameViewHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_linear_city, parent, false));
                break;
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CapitalViewHolder) {
            position = position - itemViewTypes.size();
            CapitalViewHolder capitalViewHolder = (CapitalViewHolder) holder;
            ExpandGroupIndexEntity expandGroupIndexEntity = mIndexMap.get(position);
            int groupIndex = expandGroupIndexEntity.getGroupIndex();
            ExpandGroupItemEntity<String, CityModel> expandGroupItemEntity = mDataList.get(groupIndex);
            String capital = expandGroupItemEntity.getParent();
            capitalViewHolder.tvCityCapital.setText(capital);
        } else if (holder instanceof CityNameViewHolder) {
            position = position - itemViewTypes.size();
            CityNameViewHolder cityNameViewHolder = (CityNameViewHolder) holder;
            ExpandGroupIndexEntity expandGroupIndexEntity = mIndexMap.get(position);
            int groupIndex = expandGroupIndexEntity.getGroupIndex();
            int childIndex = expandGroupIndexEntity.getChildIndex();
            ExpandGroupItemEntity<String, CityModel> expandGroupItemEntity = mDataList.get(groupIndex);
            List<CityModel> cityModels = expandGroupItemEntity.getChildList();
            CityModel cityModel = cityModels.get(childIndex);
            holder.itemView.setTag(childIndex);
            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClickListener(cityModel, (Integer) v.getTag()));
            cityNameViewHolder.tvCityName.setText(cityModel.getCityName());
            cityNameViewHolder.tvCitySanZiMa.setText(StringUtils.getString(cityModel.getThreeCode()));
            cityNameViewHolder.tvCountry.setText(StringUtils.getString(cityModel.getCountryName()));
            if (childIndex == cityModels.size() - 1) {
                cityNameViewHolder.viewLine.setVisibility(View.GONE);
            } else {
                cityNameViewHolder.viewLine.setVisibility(View.VISIBLE);
            }
        } else if (holder instanceof GridCityViewHolder) {
            GridCityViewHolder gridCityViewHolder = (GridCityViewHolder) holder;
            int itemViewType = getItemViewType(position);
            gridCityViewHolder.bind(cityListModel, itemViewType);
        }
    }


    @Override
    public int getItemViewType(int position) {
        Integer viewType = itemViewTypes.get(position);
        if (viewType != null) {
            return viewType;
        }
        position = position - itemViewTypes.size();
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        itemViewTypes.clear();
        List<CityModel> historyCityModels = cityListModel.getHistoryCityModels();
        if (historyCityModels != null && !historyCityModels.isEmpty()) {
            itemViewTypes.put(itemViewTypes.size(), HISTORY_CITY);
        }
        List<CityModel> hotCityModels = cityListModel.getHotCityModels();
        if (hotCityModels != null && !hotCityModels.isEmpty()) {
            itemViewTypes.put(itemViewTypes.size(), HOT_CITY);
        }
        return super.getItemCount() + itemViewTypes.size();
    }

    private OnItemClickListener<CityModel> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<CityModel> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 城市 gridList
     */
    public class GridCityViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_head_title)
        TextView tvGridTitle;
        @BindView(R.id.grid_city)
        GridView gridCity;
        private List<CityModel> cityModels = new ArrayList<>();
        private GridCityAdapter gridCityAdapter;
        private Context context;

        public GridCityViewHolder(View itemView, int viewType) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
            setGridTitle(viewType);
            gridCityAdapter = new GridCityAdapter(context, cityModels, gridCity);
            gridCity.setOnItemClickListener((parent, view, position, id) -> {
                CityModel cityModel = cityModels.get(position);
                onItemClickListener.onItemClickListener(cityModel, position);
            });
            gridCity.setAdapter(gridCityAdapter);
        }

        private void setGridTitle(int viewType) {
            int paddingLeft = UtilDisplay.dip2px(context, 14);
            switch (viewType) {
                case HISTORY_CITY:
                    tvGridTitle.setText("历史");
                    int paddingTop = UtilDisplay.dip2px(context, 17);
                    tvGridTitle.setPadding(paddingLeft, paddingTop, paddingLeft, 0);
                    break;
                case HOT_CITY:
                    paddingTop = UtilDisplay.dip2px(context, 7);
                    tvGridTitle.setPadding(paddingLeft, paddingTop, paddingLeft, 0);
                    tvGridTitle.setText("热门城市");
                    break;
            }
        }


        public void bind(CityListModel cityListModel, int viewType) {
            setGridTitle(viewType);
            List<CityModel> cityModels = null;
            switch (viewType) {
                case HISTORY_CITY:
                    cityModels = cityListModel.getHistoryCityModels();
                    break;
                case HOT_CITY:
                    cityModels = cityListModel.getHotCityModels();
                    break;
            }
            this.cityModels.clear();
            if (cityModels != null) {
                this.cityModels.addAll(cityModels);
            }
            gridCityAdapter.notifyDataSetChanged();
        }


        public class GridCityAdapter extends BaseAdapter {
            private List<CityModel> cityModels;
            private Context context;
            private GridView gridView;

            public GridCityAdapter(Context context, List<CityModel> cityModels, GridView gridView) {
                this.context = context;
                this.cityModels = cityModels;
                this.gridView = gridView;
            }

            @Override
            public int getCount() {
                return cityModels.size();
            }

            @Override
            public Object getItem(int position) {
                return cityModels.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_city, parent, false);
                    viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tv_city_name);
                    ViewGroup.LayoutParams layoutParams = viewHolder.tvCityName.getLayoutParams();
                    int surplusWidth = UtilDisplay.getScreenWidth(context) -
                            gridCity.getPaddingLeft() -
                            gridView.getPaddingRight() -
                            gridView.getVerticalSpacing() * (gridView.getNumColumns() - 1);
                    layoutParams.width = surplusWidth / gridView.getNumColumns();
                    viewHolder.tvCityName.setLayoutParams(layoutParams);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tvCityName.setText(cityModels.get(position).getCityName());
                return convertView;
            }

            public class ViewHolder {
                TextView tvCityName;
            }
        }
    }

    /**
     * 大写字母
     */
    public static class CapitalViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_city_capital)
        TextView tvCityCapital;

        public CapitalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public static class CityNameViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_city_name)
        TextView tvCityName;
        @BindView(R.id.tv_city_san_zi_ma)
        TextView tvCitySanZiMa;
        @BindView(R.id.tv_country)
        TextView tvCountry;
        @BindView(R.id.view_line)
        View viewLine;

        public CityNameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
