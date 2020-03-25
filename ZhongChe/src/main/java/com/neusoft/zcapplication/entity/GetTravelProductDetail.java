package com.neusoft.zcapplication.entity;

import java.util.List;

/**
 * Author: TenzLiu
 * Time: 2018/9/30 15:17
 * Desc:旅游产品详情
 */

public class GetTravelProductDetail {

    /**
     * Qq:qq
     wechatQrcode: 微信二维码图片路径
     tripDay: 出游天数
     travelTime: 班期
     shopNotes: 购物说明
     other: 其他
     introduce: 行程介绍
     bookNotes: 预订说明
     adultPrice: 成人价格
     trafficNotes: 交通工具说明
     hotelNotes: 住宿说明
     wechat: 微信
     travelNotes: 出行须知
     costInclude: 费用包含
     title: 产品标题
     flightNotes: 航班动态说明
     feature: 产品特色
     phone: 电话
     fromCityName:出发地
     costNotInclude: 费用不包含
     childPrice: 儿童价格
     qqQrcode: QQ二维码图片路径
     email:邮箱
     */

    /**
     * product : {"qq":3424242,"wechatQrcode":"http://58.20.212.75:9001/travelProductsImage/Wechat_Qrcode/1536226624499.png","tripDay":"3","travelTime":"2018-09-21","shopNotes":"ww","other":"ww","introduce":"ww","bookNotes":"ww","adultPrice":933,"trafficNotes":"ww","hotelNotes":"ww","wechat":"13812341234","travelNotes":"ww","costInclude":"ww","title":"厦门3日游","flightNotes":"ww","feature":"ww","phone":13812341234,"fromCityName":"广州","costNotInclude":"ww","childPrice":829,"qqQrcode":"http://58.20.212.75:9001/travelProductsImage/QQ_Qrcode/1536226621184.png","email":"xuaj@qq.com"}
     * picture : ["http://58.20.212.75:9001/travelProductsImage/TravelProductdetailes/1536226603758.jpg","http://58.20.212.75:9001/travelProductsImage/TravelProductdetailes/1536226614330.jpg"]
     */

    private ProductBean product;
    private List<String> picture;

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public static class ProductBean {
        /**
         * qq : 3424242
         * wechatQrcode : http://58.20.212.75:9001/travelProductsImage/Wechat_Qrcode/1536226624499.png
         * tripDay : 3
         * travelTime : 2018-09-21
         * shopNotes : ww
         * other : ww
         * introduce : ww
         * bookNotes : ww
         * adultPrice : 933
         * trafficNotes : ww
         * hotelNotes : ww
         * wechat : 13812341234
         * travelNotes : ww
         * costInclude : ww
         * title : 厦门3日游
         * flightNotes : ww
         * feature : ww
         * phone : 13812341234
         * fromCityName : 广州
         * costNotInclude : ww
         * childPrice : 829
         * qqQrcode : http://58.20.212.75:9001/travelProductsImage/QQ_Qrcode/1536226621184.png
         * email : xuaj@qq.com
         */

        private String qq;
        private String wechatQrcode;
        private String tripDay;
        private String travelTime;
        private String shopNotes;
        private String other;
        private String introduce;
        private String bookNotes;
        private String adultPrice;
        private String trafficNotes;
        private String hotelNotes;
        private String wechat;
        private String travelNotes;
        private String costInclude;
        private String title;
        private String flightNotes;
        private String feature;
        private String phone;
        private String fromCityName;
        private String costNotInclude;
        private String childPrice;
        private String qqQrcode;
        private String email;

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWechatQrcode() {
            return wechatQrcode;
        }

        public void setWechatQrcode(String wechatQrcode) {
            this.wechatQrcode = wechatQrcode;
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

        public String getShopNotes() {
            return shopNotes;
        }

        public void setShopNotes(String shopNotes) {
            this.shopNotes = shopNotes;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getBookNotes() {
            return bookNotes;
        }

        public void setBookNotes(String bookNotes) {
            this.bookNotes = bookNotes;
        }

        public String getAdultPrice() {
            return adultPrice;
        }

        public void setAdultPrice(String adultPrice) {
            this.adultPrice = adultPrice;
        }

        public String getTrafficNotes() {
            return trafficNotes;
        }

        public void setTrafficNotes(String trafficNotes) {
            this.trafficNotes = trafficNotes;
        }

        public String getHotelNotes() {
            return hotelNotes;
        }

        public void setHotelNotes(String hotelNotes) {
            this.hotelNotes = hotelNotes;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getTravelNotes() {
            return travelNotes;
        }

        public void setTravelNotes(String travelNotes) {
            this.travelNotes = travelNotes;
        }

        public String getCostInclude() {
            return costInclude;
        }

        public void setCostInclude(String costInclude) {
            this.costInclude = costInclude;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFlightNotes() {
            return flightNotes;
        }

        public void setFlightNotes(String flightNotes) {
            this.flightNotes = flightNotes;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getFromCityName() {
            return fromCityName;
        }

        public void setFromCityName(String fromCityName) {
            this.fromCityName = fromCityName;
        }

        public String getCostNotInclude() {
            return costNotInclude;
        }

        public void setCostNotInclude(String costNotInclude) {
            this.costNotInclude = costNotInclude;
        }

        public String getChildPrice() {
            return childPrice;
        }

        public void setChildPrice(String childPrice) {
            this.childPrice = childPrice;
        }

        public String getQqQrcode() {
            return qqQrcode;
        }

        public void setQqQrcode(String qqQrcode) {
            this.qqQrcode = qqQrcode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
