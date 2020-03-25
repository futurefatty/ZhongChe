package com.neusoft.zcapplication.Bean;

import java.util.List;
import java.util.Map;

public interface IBunbleParam {
    Map<String, Object> getMap();

    void setMap(Map<String, Object> map);

    List<Map<String, Object>> getList();

    void setList(List<Map<String, Object>> list);
}
