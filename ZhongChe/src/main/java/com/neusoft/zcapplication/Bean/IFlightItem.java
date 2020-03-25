package com.neusoft.zcapplication.Bean;

public interface IFlightItem {
    String getFromCity();

    void setFromCity(String fromCity);

    String getToCity();

    void setToCity(String toCity);

    String getStartTime();

    void setStartTime(String startTime);

    String getTripMode();

    void setTripMode(String tripMode);

    int getTripType();

    void setTripType(int tripType);

    int isBookHotel();

    void setBookHotel(int bookHotel);

    String getCheckinTime();

    void setCheckinTime(String checkinTime);

    String getCheckoutTime();

    void setCheckoutTime(String checkoutTime);

    boolean isShowDel();

    void setShowDel(boolean showDel);

    boolean isShowItem();

    void setShowItem(boolean showItem);

    String getFromCityCode();

    void setFromCityCode(String fromCityCode);

    String getToCityCode();

    void setToCityCode(String toCityCode);
}
