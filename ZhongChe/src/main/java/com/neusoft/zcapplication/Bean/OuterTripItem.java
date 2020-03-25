package com.neusoft.zcapplication.Bean;

import com.neusoft.zcapplication.flight.internation.model.InternationJourneySchemeModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 国外行程item
 */

public class OuterTripItem implements Serializable {
    /**
     * 唯一且不变
     * 用于在提交国际机票申请单时对比id.id相同的情况下查看有没有数据变更，如果有清除
     * {@link OuterTripItem#referenceScheme}中的数据,改变{@link OuterTripItem#isType}的值，让其=1;
     */
    private int id;
    //到达城市
    private String toCityName;
    //出发城市
    private String fromCityName;
    //出发时间
    private String startTime;
    //判断预定申请单来源（1美亚，8泛嘉）
    private int isType = 1;

    private List<InternationJourneySchemeModel> referenceScheme;

    public int getIsType() {
        return isType;
    }

    public void setIsType(int isType) {
        this.isType = isType;
    }

    public List<InternationJourneySchemeModel> getReferenceScheme() {
        return referenceScheme == null ? referenceScheme = new ArrayList<>() : referenceScheme;
    }

    public void setReferenceScheme(List<InternationJourneySchemeModel> referenceScheme) {
        this.referenceScheme = referenceScheme;
    }

    public String getToCityName() {
        return null == toCityName ? "" : toCityName;
    }

    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    public String getFromCityName() {
        return null == fromCityName ? "" : fromCityName;
    }

    public void setFromCityName(String fromCityName) {
        this.fromCityName = fromCityName;
    }

    public String getStartTime() {
        return null == startTime ? "" : startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OuterTripItem)) return false;
        OuterTripItem that = (OuterTripItem) o;
        return getId() == that.getId() &&
                getToCityName().equals(that.getToCityName()) &&
                getFromCityName().equals(that.getFromCityName()) &&
                getStartTime().equals(that.getStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToCityName(), getFromCityName(), getStartTime());
    }


    public OuterTripItem copy() {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        Object obj = null;
        try {
            oos = new ObjectOutputStream(bao);
            oos.writeObject(this);
            oos.close();
            ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (OuterTripItem) obj;
    }


}
