package com.neusoft.zcapplication.Bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/20.
 */

public class BunbleParam implements Serializable, IBunbleParam {
    private Map<String, Object> map;
    private List<Map<String, Object>> list;

    @Override
    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public List<Map<String, Object>> getList() {
        return list;
    }

    @Override
    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }
}
