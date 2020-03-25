package com.neusoft.zcapplication.city;

import com.neusoft.zcapplication.widget.recycleview.expand.ExpandGroupItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * author:Six
 * Date:2019/5/22
 */
public class CityListModel implements Serializable {
    private List<CityModel> historyCityModels;
    private List<CityModel> hotCityModels;
    private List<ExpandGroupItemEntity<String, CityModel>> expandGroupItemEntities;

    public List<CityModel> getHistoryCityModels() {
        return historyCityModels;
    }

    public void setHistoryCityModels(List<CityModel> historyCityModels) {
        this.historyCityModels = historyCityModels;
    }

    public List<CityModel> getHotCityModels() {
        return hotCityModels;
    }

    public void setHotCityModels(List<CityModel> hotCityModels) {
        this.hotCityModels = hotCityModels;
    }

    public List<ExpandGroupItemEntity<String, CityModel>> getExpandGroupItemEntities() {
        return expandGroupItemEntities;
    }

    public void setExpandGroupItemEntities(List<ExpandGroupItemEntity<String, CityModel>> expandGroupItemEntities) {
        this.expandGroupItemEntities = expandGroupItemEntities;
    }
}
