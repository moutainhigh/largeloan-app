package com.xianjinxia.cashman.dto;

import java.util.Date;

/**
 * Created by liquan on 2018/3/30.
 *
 * @Author: liquan
 * @Description:
 * @Date: Created in 13:46 2018/3/30
 * @Modified By:
 */
public class LoanOrderSpeedDto {
    private Long id;
    private String status;
    private Long trdLoanOrderId;
    private String traceNo;
    private Integer productCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTrdLoanOrderId() {
        return trdLoanOrderId;
    }

    public void setTrdLoanOrderId(Long trdLoanOrderId) {
        this.trdLoanOrderId = trdLoanOrderId;
    }

   public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return "LoanOrderSpeedDto{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", trdLoanOrderId=" + trdLoanOrderId +
                ", traceNo='" + traceNo + '\'' +
                ", productCategory=" + productCategory +
                '}';
    }
}
