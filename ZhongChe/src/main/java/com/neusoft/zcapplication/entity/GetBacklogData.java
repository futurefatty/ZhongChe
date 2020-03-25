package com.neusoft.zcapplication.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author: TenzLiu
 * Time: 2018/6/11 14:06
 * Desc: 我的的待办
 */

public class GetBacklogData implements Parcelable {


    /**
     * dateTime : 2018-06-11
     * reason : 那你
     * applicateName : 潘智奇
     * id : 3554
     * tripInfo : [{"fromDate":"2018-06-11","travelName":"飞机-经济舱","toCity":"北京","fromCity":"大连","travelTypeId":"2"}]
     * state : 待审批
     * type : 预订申请
     * orderApplyId : SL2018061107262
     */

    private String dateTime;
    private String reason;
    private String applicateName;
    private String id;
    private String state;
    private String type;
    private String orderApplyId;
    private List<TripInfoBean> tripInfo;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApplicateName() {
        return applicateName;
    }

    public void setApplicateName(String applicateName) {
        this.applicateName = applicateName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderApplyId() {
        return orderApplyId;
    }

    public void setOrderApplyId(String orderApplyId) {
        this.orderApplyId = orderApplyId;
    }

    public List<TripInfoBean> getTripInfo() {
        return tripInfo;
    }

    public void setTripInfo(List<TripInfoBean> tripInfo) {
        this.tripInfo = tripInfo;
    }

    public static class TripInfoBean implements Parcelable {
        /**
         * fromDate : 2018-06-11
         * travelName : 飞机-经济舱
         * toCity : 北京
         * fromCity : 大连
         * travelTypeId : 2
         */

        private String fromDate;
        private String travelName;
        private String toCity;
        private String fromCity;
        private String travelTypeId;

        public String getFromDate() {
            return fromDate;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getTravelName() {
            return travelName;
        }

        public void setTravelName(String travelName) {
            this.travelName = travelName;
        }

        public String getToCity() {
            return toCity;
        }

        public void setToCity(String toCity) {
            this.toCity = toCity;
        }

        public String getFromCity() {
            return fromCity;
        }

        public void setFromCity(String fromCity) {
            this.fromCity = fromCity;
        }

        public String getTravelTypeId() {
            return travelTypeId;
        }

        public void setTravelTypeId(String travelTypeId) {
            this.travelTypeId = travelTypeId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.fromDate);
            dest.writeString(this.travelName);
            dest.writeString(this.toCity);
            dest.writeString(this.fromCity);
            dest.writeString(this.travelTypeId);
        }

        public TripInfoBean() {
        }

        protected TripInfoBean(Parcel in) {
            this.fromDate = in.readString();
            this.travelName = in.readString();
            this.toCity = in.readString();
            this.fromCity = in.readString();
            this.travelTypeId = in.readString();
        }

        public static final Creator<TripInfoBean> CREATOR = new Creator<TripInfoBean>() {
            @Override
            public TripInfoBean createFromParcel(Parcel source) {
                return new TripInfoBean(source);
            }

            @Override
            public TripInfoBean[] newArray(int size) {
                return new TripInfoBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dateTime);
        dest.writeString(this.reason);
        dest.writeString(this.applicateName);
        dest.writeString(this.id);
        dest.writeString(this.state);
        dest.writeString(this.type);
        dest.writeString(this.orderApplyId);
        dest.writeTypedList(this.tripInfo);
    }

    public GetBacklogData() {
    }

    protected GetBacklogData(Parcel in) {
        this.dateTime = in.readString();
        this.reason = in.readString();
        this.applicateName = in.readString();
        this.id = in.readString();
        this.state = in.readString();
        this.type = in.readString();
        this.orderApplyId = in.readString();
        this.tripInfo = in.createTypedArrayList(TripInfoBean.CREATOR);
    }

    public static final Creator<GetBacklogData> CREATOR = new Creator<GetBacklogData>() {
        @Override
        public GetBacklogData createFromParcel(Parcel source) {
            return new GetBacklogData(source);
        }

        @Override
        public GetBacklogData[] newArray(int size) {
            return new GetBacklogData[size];
        }
    };
}
