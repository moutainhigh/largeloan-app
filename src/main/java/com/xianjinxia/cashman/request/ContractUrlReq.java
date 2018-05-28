package com.xianjinxia.cashman.request;

import javax.validation.constraints.NotNull;

/**
 * Created by liquan on 2017/12/22.
 */
public class ContractUrlReq extends BaseRequest{
    @NotNull(message="trdLoanOrderId can't be null")
    private Long trdLoanOrderId;;

    @NotNull(message="userId can't be null")
    private Long userId;

    @NotNull(message="productId can't be null")
    private Long productId;

    @NotNull(message="merchantNo can't be null")
    private String merchantNo;

    public ContractUrlReq(Long trdLoanOrderId, Long userId, Long productId) {
        this.trdLoanOrderId = trdLoanOrderId;
        this.userId = userId;
        this.productId = productId;
    }

    public ContractUrlReq() {
    }

    public Long getTrdLoanOrderId() {
        return trdLoanOrderId;
    }

    public void setTrdLoanOrderId(Long trdLoanOrderId) {
        this.trdLoanOrderId = trdLoanOrderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    @Override
    public String toString() {
        return "ContractUrlReq{" +
                "trdLoanOrderId=" + trdLoanOrderId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", merchantNo='" + merchantNo + '\'' +
                '}';
    }
}
