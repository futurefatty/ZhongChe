package com.neusoft.zcapplication.entity;

import java.util.List;

/**
 * Author: TenzLiu
 * Time: 2018/8/29 10:29
 * Desc:预订申请单
 */

public class GetPersonalData {

    /**
     * costList;//费用； costPirce 费油金额 ；resultType 对应类型 0 国内 1 国际 2 酒店 ；describe 描述

     averageDaysList//平均提前预定天数；cont 次数；ranks 排名；resultType 对应类型 0 国内 1 国际 2 酒店；describe 描述

     planChangeList//机票改签次数； cont 次数；ranks 排名；resultType 对应类型 0 国内 1 国际 2 酒店；describe 描述

     planeReturnList//机票退票次数； cont 次数；ranks 排名；resultType 对应类型 0 国内 1 国际 2 酒店；describe 描述

     hotelRetunList//酒店退订次数； cont 次数；ranks 排名；resultType 对应类型 0 国内 1 国际 2 酒店；describe 描述

     hotelMonthList//酒店月结订购次数 ；cont 次数；ranks 排名；resultType 对应类型 0 国内 1 国际 2 酒店；describe 描述

     violationsList//违规订票次数 ；cont 次数；ranks 排名；resultType 对应类型 0 国内 1 国际 2 酒店；describe 描述
     */
    //报表
    private List<CostListBean> costList;
    //平均提前
    private List<AverageDaysListBean> averageDaysList;
    //机票改签
    private List<PlanChangeListBean> planChangeList;
    //机票退订
    private List<PlaneReturnListBean> planeReturnList;
    //酒店退订
    private List<HotelRetunListBean> hotelRetunList;
    //月结酒店
    private List<HotelMonthListBean> hotelMonthList;
    //违规订票
    private List<ViolationsListBean> violationsList;
    //机票（改签，退票）总计，总排名
    private List<TotalRanksListBean> totalRanksList;

    public List<CostListBean> getCostList() {
        return costList;
    }

    public void setCostList(List<CostListBean> costList) {
        this.costList = costList;
    }

    public List<AverageDaysListBean> getAverageDaysList() {
        return averageDaysList;
    }

    public void setAverageDaysList(List<AverageDaysListBean> averageDaysList) {
        this.averageDaysList = averageDaysList;
    }

    public List<PlanChangeListBean> getPlanChangeList() {
        return planChangeList;
    }

    public void setPlanChangeList(List<PlanChangeListBean> planChangeList) {
        this.planChangeList = planChangeList;
    }

    public List<PlaneReturnListBean> getPlaneReturnList() {
        return planeReturnList;
    }

    public void setPlaneReturnList(List<PlaneReturnListBean> planeReturnList) {
        this.planeReturnList = planeReturnList;
    }

    public List<HotelRetunListBean> getHotelRetunList() {
        return hotelRetunList;
    }

    public void setHotelRetunList(List<HotelRetunListBean> hotelRetunList) {
        this.hotelRetunList = hotelRetunList;
    }

    public List<HotelMonthListBean> getHotelMonthList() {
        return hotelMonthList;
    }

    public void setHotelMonthList(List<HotelMonthListBean> hotelMonthList) {
        this.hotelMonthList = hotelMonthList;
    }

    public List<ViolationsListBean> getViolationsList() {
        return violationsList;
    }

    public void setViolationsList(List<ViolationsListBean> violationsList) {
        this.violationsList = violationsList;
    }

    public List<TotalRanksListBean> getTotalRanksList() {
        return totalRanksList;
    }

    public void setTotalRanksList(List<TotalRanksListBean> totalRanksList) {
        this.totalRanksList = totalRanksList;
    }

    public static class ViolationsListBean {
        /**
         * ranks : 1
         * describe : 违规订票次数
         * cont : 8
         * resultType : 0
         */

        private int ranks;
        private String describe;
        private int count;
        private int totalCount;
        private int resultType;

        public int getRanks() {
            return ranks;
        }

        public void setRanks(int ranks) {
            this.ranks = ranks;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }
    }

    public static class CostListBean {
        /**
         * describe : 费用统计
         * resultType : 0
         * costPirce : 70690
         */

        private String describe;
        private int resultType;
        private float costPirce;
        private int ticketCount;

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }

        public float getCostPirce() {
            return costPirce;
        }

        public void setCostPirce(float costPirce) {
            this.costPirce = costPirce;
        }

        public int getTicketCount() {
            return ticketCount;
        }

        public void setTicketCount(int ticketCount) {
            this.ticketCount = ticketCount;
        }
    }

    public static class AverageDaysListBean {
        private int ranks;
        private String describe;
        private String days;
        private int resultType;

        public int getRanks() {
            return ranks;
        }

        public void setRanks(int ranks) {
            this.ranks = ranks;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }
    }

    public static class PlanChangeListBean {
        private int ranks;
        private String describe;
        private int count;
        private int totalCount;
        private int resultType;

        public int getRanks() {
            return ranks;
        }

        public void setRanks(int ranks) {
            this.ranks = ranks;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }
    }

    public static class HotelRetunListBean {

        private int ranks;
        private String describe;
        private int count;
        private int totalCount;
        private int resultType;

        public int getRanks() {
            return ranks;
        }

        public void setRanks(int ranks) {
            this.ranks = ranks;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }
    }

    public static class PlaneReturnListBean {
        private int ranks;
        private String describe;
        private int count;
        private int totalCount;
        private int resultType;

        public int getRanks() {
            return ranks;
        }

        public void setRanks(int ranks) {
            this.ranks = ranks;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }
    }

    public static class HotelMonthListBean {
        private int ranks;
        private String describe;
        private int count;
        private int totalCount;
        private int resultType;

        public int getRanks() {
            return ranks;
        }

        public void setRanks(int ranks) {
            this.ranks = ranks;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }
    }

    public static class TotalRanksListBean{

        private int total;
        private int totalRanks;
        private String describe;
        private int resultType;//3:改签  4:退票

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalRanks() {
            return totalRanks;
        }

        public void setTotalRanks(int totalRanks) {
            this.totalRanks = totalRanks;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }
    }
}
