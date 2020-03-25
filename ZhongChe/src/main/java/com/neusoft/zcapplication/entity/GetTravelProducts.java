package com.neusoft.zcapplication.entity;

import java.io.Serializable;

/**
 * Author: TenzLiu
 * Time: 2018/9/30 14:39
 * Desc:旅游产品
 */

public class GetTravelProducts implements Serializable {


    /**
     * toCityName : 厦门
     * tripDay : 3
     * travelTime : 2018-09-21
     * smallImage : http://58.20.212.75:9001/travelProductsImage/smallImage/1536226582418.jpg
     * productId : 47
     * price : 933
     * fromCityName : 广州
     * title : 厦门3日游
     */

    private String toCityName;
    private String tripDay;
    private String travelTime;
    private String smallImage;
    private int productId;
    private String price;
    private String fromCityName;
    private String title;

    public String getToCityName() {
        return toCityName;
    }

    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    public String getTripDay() {
        return tripDay;
    }

    public void setTripDay(String tripDay) {
        this.tripDay = tripDay;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFromCityName() {
        return fromCityName;
    }

    public void setFromCityName(String fromCityName) {
        this.fromCityName = fromCityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
