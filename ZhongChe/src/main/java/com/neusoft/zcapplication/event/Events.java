package com.neusoft.zcapplication.event;

import com.neusoft.zcapplication.entity.GetAllSuppliers;

import java.util.List;

/**
 * Author: TenzLiu
 * Time: 2018/6/15 16:17
 * Desc: 时间响应类
 */

public class Events {

    /**
     * 公车申请选择供应商
     */
    public static class BusinessCarChoseSupplier{
        private GetAllSuppliers mGetAllSuppliers;

        public BusinessCarChoseSupplier(GetAllSuppliers getAllSuppliers) {
            mGetAllSuppliers = getAllSuppliers;
        }

        public GetAllSuppliers getGetAllSuppliers() {
            return mGetAllSuppliers;
        }

        public void setGetAllSuppliers(GetAllSuppliers getAllSuppliers) {
            mGetAllSuppliers = getAllSuppliers;
        }
    }

    /**
     * 删除预定申请单
     */
    public static class DeleteOrderIds{
        private List<String> orderApplyId;

        public DeleteOrderIds(List<String> orderApplyId) {
            this.orderApplyId = orderApplyId;
        }

        public List<String> getOrderApplyId() {
            return orderApplyId;
        }

        public void setOrderApplyId(List<String> orderApplyId) {
            this.orderApplyId = orderApplyId;
        }
    }

    /**
     * 预定申请审核
     */
    public static class ApplyVerifyChange{

    }

    /**
     * 用车申请改变
     */
    public static class BusinessCarApplyChange{

    }

    /**
     * 用车审核改变
     */
    public static class BusinessCarVerifyChange{

    }

    /**
     * 用车办理改变
     */
    public static class BusinessCarHandleChange{

    }


    /**
     * 个人报表
     */
    public static class PersonDataHandleChange{

    }
    /**
     * 组织报表
     */
    public static class CompanyDataHandleChange{

    }
}
