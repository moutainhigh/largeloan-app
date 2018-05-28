package com.xianjinxia.cashman.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * 订单信息
 * Created by liquan on 2018/1/4.
 */
public class OrderInfoDto {


    public OrderInfoDto(){}

    public OrderInfoDto(String id, String orderType, String moneyAmount, String apr,
        String loanMethod, String loanTerm, String time, String clientType, String status,
        String fromApp, String isDepository, List<RepaymentPlanInfoDto> repaymentInfoList,
        RepaymentDto repayment,String autoRiskCheckStatus) {
        this.id = id;
        this.order_type = orderType;
        this.money_amount = moneyAmount;
        this.apr = apr;
        this.loan_method = loanMethod;
        this.loan_term = loanTerm;
        this.time = time;
        this.client_type = clientType;
        this.status = status;
        this.from_app = fromApp;
        this.is_depository = isDepository;
//        this.repayment_period = repaymentInfoList;
//        this.repay = repay;
        this.auto_risk_check_status = autoRiskCheckStatus;
    }



    // bizSeqNo
    private String id;

    // 业务场景
    private String order_type;

    // 借款金额
    // moneyAmount
    //JSONField(name = "moneyAmount")
    // orderAmount
    private String money_amount;
    // 年化借款利率
    private String apr;

    // 借款方式[0: 天，1：月，2:年]
    private String loan_method;

    // 借款期限[根据loan_method确定，几天、几月、几年]
    private String loan_term;

    // 下单时间
//    @JSONField(name="time")
//    createdTime
    private String time;

    // 终端类型[1:android，2iOS，3:h5，4：API ]
//    @JSONField(name="clientType")
//    terminal
    private String client_type;


    // 订单状态
    private String status;

    // 申请来源
//    @JSONField(name="fromApp")
//   source
    private String from_app;

    //是否存管
    private String is_depository;

    // 风控检测状态  status 为0 时有效
    private String auto_risk_check_status;


    public String getAutoRiskCheckStatus() {
        return auto_risk_check_status;
    }

    public void setAutoRiskCheckStatus(String autoRiskCheckStatus) {
        this.auto_risk_check_status = autoRiskCheckStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderType() {
        return order_type;
    }

    public void setOrderType(String orderType) {
        this.order_type = orderType;
    }


    public String getApr() {
        return apr;
    }

    public void setApr(String apr) {
        this.apr = apr;
    }

    public String getLoanMethod() {
        return loan_method;
    }

    public void setLoanMethod(String loanMethod) {
        this.loan_method = loanMethod;
    }

    public String getLoanTerm() {
        return loan_term;
    }

    public void setLoanTerm(String loanTerm) {
        this.loan_term = loanTerm;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getIsDepository() {
        return is_depository;
    }

    public void setIsDepository(String isDepository) {
        this.is_depository = isDepository;
    }

    public String getMoneyAmount() {
        return money_amount;
    }

    public void setMoneyAmount(String moneyAmount) {
        this.money_amount = moneyAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClientType() {
        return client_type;
    }

    public void setClientType(String clientType) {
        this.client_type = clientType;
    }

    public String getFromApp() {
        return from_app;
    }

    public void setFromApp(String fromApp) {
        this.from_app = fromApp;
    }

    @Override public String toString() {
        return "OrderInfoDto{" + "id='" + id + '\'' + ", order_type='" + order_type + '\''
            + ", money_amount=" + money_amount + ", apr='" + apr + '\'' + ", loan_method='"
            + loan_method + '\'' + ", loan_term=" + loan_term + ", time=" + time + ", client_type='"
            + client_type + '\'' + ", status='" + status + '\'' + ", from_app='" + from_app + '\''
            + ", is_depository='" + is_depository + '\'' + ", auto_risk_check_status="
            + auto_risk_check_status + ", repayment_period="  + ", repay="
             + '}';
    }
}
