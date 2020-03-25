package com.neusoft.zcapplication.flight.internation.model;

import com.neusoft.zcapplication.tools.DateUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * author:Six
 * Date:2019/5/30
 */
public class InternationSeachModel implements Serializable {

    private List<Schedule> schedules;
    private String searchKey;
    private String adultTicketAddedTaxPrice;

    public String getAdultTicketAddedTaxPrice() {
        return adultTicketAddedTaxPrice;
    }

    public void setAdultTicketAddedTaxPrice(String adultTicketAddedTaxPrice) {
        this.adultTicketAddedTaxPrice = adultTicketAddedTaxPrice;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public static class Schedule implements Serializable {
        private InternationCurrentJourneyModel currentJourney;
        private InternationLowerPriceModel lowerPriceInfo;

        public InternationCurrentJourneyModel getCurrentJourney() {
            return currentJourney;
        }

        public void setCurrentJourney(InternationCurrentJourneyModel currentJourney) {
            this.currentJourney = currentJourney;
        }

        public InternationLowerPriceModel getLowerPriceInfo() {
            return lowerPriceInfo;
        }

        public void setLowerPriceInfo(InternationLowerPriceModel lowerPriceInfo) {
            this.lowerPriceInfo = lowerPriceInfo;
        }

        /**
         * 时间从早到晚&直飞
         */
        public static Comparator TIMEASC_DIRECT = (Comparator<Schedule>) (o1, o2) -> {
            List<InternationCurrentJourneyModel.FlightSegmentsBean> flightSegmentsBeans1 = o1.getCurrentJourney().getFlightSegments();
            List<InternationCurrentJourneyModel.FlightSegmentsBean> flightSegmentsBeans2 = o2.getCurrentJourney().getFlightSegments();
            int flightSegmentCount1 = 0;
            int flightSegmentCount2 = 0;
            if (flightSegmentsBeans1 != null) {
                flightSegmentCount1 = flightSegmentsBeans1.size();
            }
            if (flightSegmentsBeans2 != null) {
                flightSegmentCount2 = flightSegmentsBeans2.size();
            }
            int direct = Integer.compare(flightSegmentCount1, flightSegmentCount2);
            return direct == 0 ? getTimeSort(o1, o2) : direct;
        };


        /**
         * 时间从早到晚&价格从低到高
         */
        public static Comparator TIMEASC_PRICEASC = (Comparator<Schedule>) (o1, o2) -> {
            int priceSort = getPriceSort(o1, o2);
            return priceSort == 0 ? getTimeSort(o1, o2) : priceSort;
        };


        /**
         * 时间从早到晚&价格从高到低
         */
        public static Comparator TIMEASC_PRICEDES = (Comparator<Schedule>) (o1, o2) -> {
            int priceSort = getPriceSort(o2, o1);
            return priceSort == 0 ? getTimeSort(o1, o2) : priceSort;
        };

        /**
         * 时间从早到晚
         */
        public static Comparator TIMEASC = (Comparator<Schedule>) (o1, o2) -> getTimeSort(o1, o2);
        /**
         * 时间从晚到早
         */
        public static Comparator TIMEDES = (Comparator<Schedule>) (o1, o2) -> getTimeSort(o2, o1);

        /**
         * 获取价格排序
         * 从低到高或者从高到低
         *
         * @param o1
         * @param o2
         * @return
         */
        private static int getPriceSort(Schedule o1, Schedule o2) {
            InternationLowerPriceModel lowerPriceModel1 = o1.getLowerPriceInfo();
            InternationLowerPriceModel lowerPriceModel2 = o2.getLowerPriceInfo();
            int price1 = 0;
            int price2 = 0;
            if (lowerPriceModel1 != null) {
                price1 = lowerPriceModel1.getAdultTicketPrice() + lowerPriceModel1.getAdultTaxPrice();
            }
            if (lowerPriceModel2 != null) {
                price2 = lowerPriceModel2.getAdultTicketPrice() + lowerPriceModel2.getAdultTaxPrice();
            }
            return Integer.compare(price1, price2);
        }

        /**
         * 获取时间排序
         * 从早到晚或者从晚到早
         *
         * @param o1
         * @param o2
         * @return
         */
        private static int getTimeSort(Schedule o1, Schedule o2) {
            InternationCurrentJourneyModel journeyModel1 = o1.getCurrentJourney();
            InternationCurrentJourneyModel journeyModel2 = o2.getCurrentJourney();
            int depCompare = getTimeSort(journeyModel1.getDepTime(), journeyModel2.getDepTime());
            //起飞时间一样按照到达时间排序
            if (0 == depCompare) {
                return getTimeSort(journeyModel1.getArrTime(), journeyModel2.getArrTime());
            }
            return depCompare;
        }

        private static int getTimeSort(String timeStr1, String timeStr2) {
            timeStr1 = timeStr1.replace("T", " ");
            timeStr2 = timeStr2.replace("T", " ");
            Date timeDate1 = DateUtils.parseDate(timeStr1, "yyyy-MM-dd HH:mm:ss");
            Date timeDate2 = DateUtils.parseDate(timeStr2, "yyyy-MM-dd HH:mm:ss");
            long dateMs1 = 0L;
            long dateMs2 = 0L;
            if (timeDate1 != null) {
                dateMs1 = timeDate1.getTime();
            }
            if (timeDate2 != null) {
                dateMs2 = timeDate2.getTime();
            }
            return Long.compare(dateMs1, dateMs2);
        }


    }


    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
