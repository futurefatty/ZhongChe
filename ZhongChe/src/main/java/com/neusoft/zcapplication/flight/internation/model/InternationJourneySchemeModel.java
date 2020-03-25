package com.neusoft.zcapplication.flight.internation.model;

import java.io.Serializable;

/**
 * author:Six
 * Date:2019/6/1
 */
public class InternationJourneySchemeModel implements Serializable {
    private String flightNumber;
    private String depCity;
    private String depCityAirport;
    private String arrCity;
    private String arrCityAirport;
    private String depTime;
    private String arrTime;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepCity() {
        return depCity;
    }

    public void setDepCity(String depCity) {
        this.depCity = depCity;
    }

    public String getDepCityAirport() {
        return depCityAirport;
    }

    public void setDepCityAirport(String depCityAirport) {
        this.depCityAirport = depCityAirport;
    }

    public String getArrCity() {
        return arrCity;
    }

    public void setArrCity(String arrCity) {
        this.arrCity = arrCity;
    }

    public String getArrCityAirport() {
        return arrCityAirport;
    }

    public void setArrCityAirport(String arrCityAirport) {
        this.arrCityAirport = arrCityAirport;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }


}
