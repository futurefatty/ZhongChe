package com.neusoft.zcapplication.city;

import java.io.Serializable;
import java.util.Objects;

/**
 * author:Six
 * Date:2019/5/22
 */
public class CityModel implements Serializable {
    private String cityName;
    private int status;
    private int isDeleted;
    private String threeCode;
    private String pK_Guid;
    private String simpleName;
    private String pinying;
    private String updateTime;

    private int isHot;
    private String countryName;
    /**
     * 1 国内
     * 2 国际
     */
    private int countryTag;


    public int getCountryTag() {
        return countryTag;
    }

    public void setCountryTag(int countryTag) {
        this.countryTag = countryTag;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CityModel)) return false;
            CityModel cityModel = (CityModel) o;
            return Objects.equals(getThreeCode(), cityModel.getThreeCode()) &&
                    Objects.equals(getpK_Guid(), cityModel.getpK_Guid());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getThreeCode(), getpK_Guid());
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getThreeCode() {
        return threeCode;
    }

    public void setThreeCode(String threeCode) {
        this.threeCode = threeCode;
    }

    public String getPK_Guid() {
        return pK_Guid;
    }

    public void setPK_Guid(String pK_Guid) {
        this.pK_Guid = pK_Guid;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getPinying() {
        return pinying;
    }

    public void setPinying(String pinying) {
        this.pinying = pinying;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.updateTime = UpdateTime;
    }


    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }


    public String getpK_Guid() {
        return pK_Guid;
    }

    public void setpK_Guid(String pK_Guid) {
        this.pK_Guid = pK_Guid;
    }
}
