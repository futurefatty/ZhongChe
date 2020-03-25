package com.neusoft.zcapplication.city;

import android.content.Context;

import com.neusoft.zcapplication.R;
import com.neusoft.zcapplication.widget.recycleview.expand.ExpandGroupItemEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * author:Six
 * Date:2019/5/22
 */
public class CityUtil {
    /**
     * 城市分组
     *
     * @param
     * @return
     */
    public static List<ExpandGroupItemEntity<String, CityModel>> sort(List<CityModel> cityModelList) {
        List<ExpandGroupItemEntity<String, CityModel>> expandGroupItemEntities = new ArrayList<>();
        Map<String, List<CityModel>> cityMapping = new TreeMap<>(new Comparator<String>() {
            Collator collator = Collator.getInstance(Locale.CHINA);

            @Override
            public int compare(String o1, String o2) {
                CollationKey key1 = collator.getCollationKey(o1);
                CollationKey key2 = collator.getCollationKey(o2);
                return key1.compareTo(key2);
            }
        });

        for (int i = 0; i < cityModelList.size(); i++) {
            CityModel cityModelOrigin = cityModelList.get(i);
            String simpleName = cityModelOrigin.getSimpleName();
            String letter = simpleName.substring(0, 1);
            if (!cityMapping.containsKey(letter)) {
                cityMapping.put(letter, new ArrayList<>());
            }
            List<CityModel> cityModels = cityMapping.get(letter);
            cityModels.add(cityModelOrigin);
        }
        for (String key : cityMapping.keySet()) {
            ExpandGroupItemEntity<String, CityModel> expandGroupItemEntity = new ExpandGroupItemEntity<>();
            expandGroupItemEntity.setParent(key);
            expandGroupItemEntity.setChildList(cityMapping.get(key));
            expandGroupItemEntity.setExpand(true);
            expandGroupItemEntities.add(expandGroupItemEntity);
        }
        return expandGroupItemEntities;
    }


    public static String getFromRaw(Context context) {
        String result = "";
        try {
            InputStream in = context.getResources().openRawResource(
                    R.raw.city);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "utf-8");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<CityModel> initData(Context context) {
        String json = getFromRaw(context);
        //解析数据
        try {
            List<CityModel> cityModels = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonCityArray = jsonObject.getJSONArray("list");
                for (int x = 0; x < jsonCityArray.length(); x++) {
                    JSONObject jsons = (JSONObject) jsonCityArray.get(x);
                    String cityName = (String) jsons.get("realname");
                    CityModel cityModel = new CityModel();
                    cityModel.setCityName(cityName);
                    cityModels.add(cityModel);
                }
            }
            return cityModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
