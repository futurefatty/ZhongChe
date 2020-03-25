package com.neusoft.zcapplication.flight.internation.model;

import java.io.Serializable;
import java.util.List;

/**
 * author:Six
 * Date:2019/5/30
 */
public class InternationCurrentJourneyModel implements Serializable {


    private String marketingAirline;
    private String marketingFlightNo;
    private String productKey;
    private String depAirportCode;
    private String duration;
    private String arrCityCode;
    private String operatingCompanyName;
    private String cabinClassName;
    private String depCityName;
    private String arrAirportName;
    private String marketingCompanyName;
    private String operatingFlightNo;
    private String arrCityName;
    private String depTime;
    private String journeySegsKey;
    private String journeyCode;
    private String cabinNumber;
    private String operatingCompanyLogo;
    private String depAirportName;
    private String arrAirportCode;
    private String arrTime;
    private String depCityCode;
    private String marketingCompanyLogo;
    private String operatingAirline;
    private List<FlightSegmentsBean> flightSegments;

    public String getMarketingAirline() {
        return marketingAirline;
    }

    public void setMarketingAirline(String marketingAirline) {
        this.marketingAirline = marketingAirline;
    }

    public String getMarketingFlightNo() {
        return marketingFlightNo;
    }

    public void setMarketingFlightNo(String marketingFlightNo) {
        this.marketingFlightNo = marketingFlightNo;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDepAirportCode() {
        return depAirportCode;
    }

    public void setDepAirportCode(String depAirportCode) {
        this.depAirportCode = depAirportCode;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArrCityCode() {
        return arrCityCode;
    }

    public void setArrCityCode(String arrCityCode) {
        this.arrCityCode = arrCityCode;
    }

    public String getOperatingCompanyName() {
        return operatingCompanyName;
    }

    public void setOperatingCompanyName(String operatingCompanyName) {
        this.operatingCompanyName = operatingCompanyName;
    }

    public String getCabinClassName() {
        return cabinClassName;
    }

    public void setCabinClassName(String cabinClassName) {
        this.cabinClassName = cabinClassName;
    }

    public String getDepCityName() {
        return depCityName;
    }

    public void setDepCityName(String depCityName) {
        this.depCityName = depCityName;
    }

    public String getArrAirportName() {
        return arrAirportName;
    }

    public void setArrAirportName(String arrAirportName) {
        this.arrAirportName = arrAirportName;
    }

    public String getMarketingCompanyName() {
        return marketingCompanyName;
    }

    public void setMarketingCompanyName(String marketingCompanyName) {
        this.marketingCompanyName = marketingCompanyName;
    }

    public String getOperatingFlightNo() {
        return operatingFlightNo;
    }

    public void setOperatingFlightNo(String operatingFlightNo) {
        this.operatingFlightNo = operatingFlightNo;
    }

    public String getArrCityName() {
        return arrCityName;
    }

    public void setArrCityName(String arrCityName) {
        this.arrCityName = arrCityName;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getJourneySegsKey() {
        return journeySegsKey;
    }

    public void setJourneySegsKey(String journeySegsKey) {
        this.journeySegsKey = journeySegsKey;
    }

    public String getJourneyCode() {
        return journeyCode;
    }

    public void setJourneyCode(String journeyCode) {
        this.journeyCode = journeyCode;
    }

    public String getCabinNumber() {
        return cabinNumber;
    }

    public void setCabinNumber(String cabinNumber) {
        this.cabinNumber = cabinNumber;
    }

    public String getOperatingCompanyLogo() {
        return operatingCompanyLogo;
    }

    public void setOperatingCompanyLogo(String operatingCompanyLogo) {
        this.operatingCompanyLogo = operatingCompanyLogo;
    }

    public String getDepAirportName() {
        return depAirportName;
    }

    public void setDepAirportName(String depAirportName) {
        this.depAirportName = depAirportName;
    }

    public String getArrAirportCode() {
        return arrAirportCode;
    }

    public void setArrAirportCode(String arrAirportCode) {
        this.arrAirportCode = arrAirportCode;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public String getDepCityCode() {
        return depCityCode;
    }

    public void setDepCityCode(String depCityCode) {
        this.depCityCode = depCityCode;
    }

    public String getMarketingCompanyLogo() {
        return marketingCompanyLogo;
    }

    public void setMarketingCompanyLogo(String marketingCompanyLogo) {
        this.marketingCompanyLogo = marketingCompanyLogo;
    }

    public String getOperatingAirline() {
        return operatingAirline;
    }

    public void setOperatingAirline(String operatingAirline) {
        this.operatingAirline = operatingAirline;
    }

    public List<FlightSegmentsBean> getFlightSegments() {
        return flightSegments;
    }

    public void setFlightSegments(List<FlightSegmentsBean> flightSegments) {
        this.flightSegments = flightSegments;
    }

    public static class FlightSegmentsBean implements Serializable {
        /**
         * marketingAirline : CA
         * cabinClass : Y
         * depTerm : T1
         * marketingFlightNo : CA1701
         * arrCity : PEK
         * depCity : HGH
         * cabin : V/V
         * duration : 150
         * aircraftType : 中
         * operatingCompanyName :
         * codeShare : false
         * cabinClassName : 经济舱
         * depCityName : 杭州
         * arrAirport : PEK
         * arrAirportName : 北京首都国际机场
         * arrTerm : T3
         * marketingCompanyName : 国航
         * operatingFlightNo :
         * aircraftName : 空客32A
         * stopCity :
         * arrCityName : 北京首都
         * depTime : 2019-06-30 06:55:00
         * segmentCode : ArrTime=2019-06-30 09:25:00DepTime=2019-06-30 06:55:00FlightNo=CA1701
         * cabinNumber : A
         * depAirport : HGH
         * stopCityName :
         * meal : 99
         * operatingCompanyLogo :
         * depAirportName : 萧山国际机场
         * arrTime : 2019-06-30 09:25:00
         * marketingCompanyLogo : http://www.fjmobile.cn/Images/Airimg/hk_guohang.png
         * operatingAirline :
         */

        private String marketingAirline;
        private String cabinClass;
        private String depTerm;
        private String marketingFlightNo;
        private String arrCity;
        private String depCity;
        private String cabin;
        private String duration;
        private String aircraftType;
        private String operatingCompanyName;
        private boolean codeShare;
        private String cabinClassName;
        private String depCityName;
        private String arrAirport;
        private String arrAirportName;
        private String arrTerm;
        private String marketingCompanyName;
        private String operatingFlightNo;
        private String aircraftName;
        private String stopCity;
        private String arrCityName;
        private String depTime;
        private String segmentCode;
        private String cabinNumber;
        private String depAirport;
        private String stopCityName;
        private String meal;
        private String operatingCompanyLogo;
        private String depAirportName;
        private String arrTime;
        private String marketingCompanyLogo;
        private String operatingAirline;


        public String getMarketingAirline() {
            return marketingAirline;
        }

        public void setMarketingAirline(String marketingAirline) {
            this.marketingAirline = marketingAirline;
        }

        public String getCabinClass() {
            return cabinClass;
        }

        public void setCabinClass(String cabinClass) {
            this.cabinClass = cabinClass;
        }

        public String getDepTerm() {
            return depTerm;
        }

        public void setDepTerm(String depTerm) {
            this.depTerm = depTerm;
        }

        public String getMarketingFlightNo() {
            return marketingFlightNo;
        }

        public void setMarketingFlightNo(String marketingFlightNo) {
            this.marketingFlightNo = marketingFlightNo;
        }

        public String getArrCity() {
            return arrCity;
        }

        public void setArrCity(String arrCity) {
            this.arrCity = arrCity;
        }

        public String getDepCity() {
            return depCity;
        }

        public void setDepCity(String depCity) {
            this.depCity = depCity;
        }

        public String getCabin() {
            return cabin;
        }

        public void setCabin(String cabin) {
            this.cabin = cabin;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getAircraftType() {
            return aircraftType;
        }

        public void setAircraftType(String aircraftType) {
            this.aircraftType = aircraftType;
        }

        public String getOperatingCompanyName() {
            return operatingCompanyName;
        }

        public void setOperatingCompanyName(String operatingCompanyName) {
            this.operatingCompanyName = operatingCompanyName;
        }

        public boolean isCodeShare() {
            return codeShare;
        }

        public void setCodeShare(boolean codeShare) {
            this.codeShare = codeShare;
        }

        public String getCabinClassName() {
            return cabinClassName;
        }

        public void setCabinClassName(String cabinClassName) {
            this.cabinClassName = cabinClassName;
        }

        public String getDepCityName() {
            return depCityName;
        }

        public void setDepCityName(String depCityName) {
            this.depCityName = depCityName;
        }

        public String getArrAirport() {
            return arrAirport;
        }

        public void setArrAirport(String arrAirport) {
            this.arrAirport = arrAirport;
        }

        public String getArrAirportName() {
            return arrAirportName;
        }

        public void setArrAirportName(String arrAirportName) {
            this.arrAirportName = arrAirportName;
        }

        public String getArrTerm() {
            return arrTerm;
        }

        public void setArrTerm(String arrTerm) {
            this.arrTerm = arrTerm;
        }

        public String getMarketingCompanyName() {
            return marketingCompanyName;
        }

        public void setMarketingCompanyName(String marketingCompanyName) {
            this.marketingCompanyName = marketingCompanyName;
        }

        public String getOperatingFlightNo() {
            return operatingFlightNo;
        }

        public void setOperatingFlightNo(String operatingFlightNo) {
            this.operatingFlightNo = operatingFlightNo;
        }

        public String getAircraftName() {
            return aircraftName;
        }

        public void setAircraftName(String aircraftName) {
            this.aircraftName = aircraftName;
        }

        public String getStopCity() {
            return stopCity;
        }

        public void setStopCity(String stopCity) {
            this.stopCity = stopCity;
        }

        public String getArrCityName() {
            return arrCityName;
        }

        public void setArrCityName(String arrCityName) {
            this.arrCityName = arrCityName;
        }

        public String getDepTime() {
            return depTime;
        }

        public void setDepTime(String depTime) {
            this.depTime = depTime;
        }

        public String getSegmentCode() {
            return segmentCode;
        }

        public void setSegmentCode(String segmentCode) {
            this.segmentCode = segmentCode;
        }

        public String getCabinNumber() {
            return cabinNumber;
        }

        public void setCabinNumber(String cabinNumber) {
            this.cabinNumber = cabinNumber;
        }

        public String getDepAirport() {
            return depAirport;
        }

        public void setDepAirport(String depAirport) {
            this.depAirport = depAirport;
        }

        public String getStopCityName() {
            return stopCityName;
        }

        public void setStopCityName(String stopCityName) {
            this.stopCityName = stopCityName;
        }

        public String getMeal() {
            return meal;
        }

        public void setMeal(String meal) {
            this.meal = meal;
        }

        public String getOperatingCompanyLogo() {
            return operatingCompanyLogo;
        }

        public void setOperatingCompanyLogo(String operatingCompanyLogo) {
            this.operatingCompanyLogo = operatingCompanyLogo;
        }

        public String getDepAirportName() {
            return depAirportName;
        }

        public void setDepAirportName(String depAirportName) {
            this.depAirportName = depAirportName;
        }

        public String getArrTime() {
            return arrTime;
        }

        public void setArrTime(String arrTime) {
            this.arrTime = arrTime;
        }

        public String getMarketingCompanyLogo() {
            return marketingCompanyLogo;
        }

        public void setMarketingCompanyLogo(String marketingCompanyLogo) {
            this.marketingCompanyLogo = marketingCompanyLogo;
        }

        public String getOperatingAirline() {
            return operatingAirline;
        }

        public void setOperatingAirline(String operatingAirline) {
            this.operatingAirline = operatingAirline;
        }
    }
}
