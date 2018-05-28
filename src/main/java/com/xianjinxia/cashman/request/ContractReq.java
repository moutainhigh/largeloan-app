package com.xianjinxia.cashman.request;

import javax.validation.constraints.NotNull;

/**
 * Created by liquan on 2017/12/1.
 */
public class ContractReq extends BaseRequest{

    @NotNull(message="trdLoanOrderId can't be null")
    private Long trdLoanOrderId;;

    @NotNull(message="userId can't be null")
    private Long userId;

    @NotNull(message="contractType can't be null")
    private String contractType;

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

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public ContractReq() {
    }

    public ContractReq(Long trdLoanOrderId, Long userId, String contractType) {
        this.trdLoanOrderId = trdLoanOrderId;
        this.userId = userId;
        this.contractType = contractType;
    }

    @Override
    public String toString(){
        return "ContractReq:{"+"userId ="+userId+",trdLoanOrderId ="+trdLoanOrderId+",contractType = "+contractType+"}";
    }
}
