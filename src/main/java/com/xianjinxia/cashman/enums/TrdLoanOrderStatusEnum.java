package com.xianjinxia.cashman.enums;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liquan on 2017/12/1.
 */
public enum TrdLoanOrderStatusEnum {

    NEW("10", "新申请", false, Arrays.asList(new TrdLoanOrderStatusEnum[]{}), RiskQueryOrderEnum.ORDER_FIRSTTRIAL, RiskQueryAutoRiskCheckStatusEnum.NOTPUSH),
    NEW_PUSH_SUCCESS("0", "推送订单给集团风控,推送成功", false, Arrays.asList(new TrdLoanOrderStatusEnum[]{NEW}), RiskQueryOrderEnum.ORDER_FIRSTTRIAL, RiskQueryAutoRiskCheckStatusEnum.PUSHOK),
    MANUAL_REVIEWING("30", "推送订单给集团风控,待人工审核", false, Arrays.asList(new TrdLoanOrderStatusEnum[]{NEW, NEW_PUSH_SUCCESS}), RiskQueryOrderEnum.ORDER_REVIEW_REJECT, null),
    REFUSED("-3", "推送订单给集团风控,审核拒绝", true, Arrays.asList(new TrdLoanOrderStatusEnum[]{NEW, NEW_PUSH_SUCCESS, MANUAL_REVIEWING}), RiskQueryOrderEnum.ORDER_REVIEW_REJECT, null),
    APPROVED("11", "推送订单给集团风控,审核通过", false, Arrays.asList(new TrdLoanOrderStatusEnum[]{NEW, NEW_PUSH_SUCCESS, MANUAL_REVIEWING}), RiskQueryOrderEnum.ORDER_REVIEW, null),
    MANUAL_APPROVED("31", "人工审核订单结果, 人工审核通过", false, Arrays.asList(new TrdLoanOrderStatusEnum[]{MANUAL_REVIEWING}), RiskQueryOrderEnum.ORDER_REVIEW, null),
    MANUAL_REFUSED("-30", "人工审核订单结果, 人工审核拒绝", true, Arrays.asList(new TrdLoanOrderStatusEnum[]{MANUAL_REVIEWING}), RiskQueryOrderEnum.ORDER_MANUAL_REJECT, null),
    LOANING("22", "推送订单至支付中心放款,放款中", false, Arrays.asList(new TrdLoanOrderStatusEnum[]{APPROVED, MANUAL_APPROVED}), RiskQueryOrderEnum.ORDER_LOAN_ING, null),
    LOAN_SUCCESS("21", "支付中心通知放款结果,放款成功", false, Arrays.asList(new TrdLoanOrderStatusEnum[]{LOANING}), RiskQueryOrderEnum.ORDER_REPAYMENT_BEGIN, null),
    LOAN_FAIL("-10", "支付中心通知放款结果,放款失败", true, Arrays.asList(new TrdLoanOrderStatusEnum[]{LOANING}), RiskQueryOrderEnum.ORDER_LOAN_REJECT, null),
    SETTLED("50", "还款结果,结清", true, Arrays.asList(new TrdLoanOrderStatusEnum[]{LOAN_SUCCESS}), RiskQueryOrderEnum.ORDER_REPAYMENT_END, null),
    OVERDUE("40", "还款结果,逾期", false, Arrays.asList(new TrdLoanOrderStatusEnum[]{LOAN_SUCCESS}), RiskQueryOrderEnum.ORDER_REPAYMENT_BEGIN, null),
    CANCEL("-22", "取消", true, Arrays.asList(new TrdLoanOrderStatusEnum[]{}), RiskQueryOrderEnum.ORDER_FIRSTTRIAL_REJECT, null),
    PUSH_FAIL("-12", "风控,推送失败", true, Arrays.asList(new TrdLoanOrderStatusEnum[]{NEW}), RiskQueryOrderEnum.ORDER_FIRSTTRIAL, RiskQueryAutoRiskCheckStatusEnum.NOTPUSH),
    FAIL("-11", "异常,失败", true, Arrays.asList(new TrdLoanOrderStatusEnum[]{}), RiskQueryOrderEnum.ORDER_FIRSTTRIAL, RiskQueryAutoRiskCheckStatusEnum.NOTPUSH);


    /**
     * 下面两个是老系统的属性，老系统没有还款表，新系统将还款抽取出来了，所以目前用不到以下两个字段
     * STATUS_KKZ("12", "扣款中"),
     * STATUS_KKSB("-7", "扣款失败");
     */

    private String code;
    private String value;
    private boolean isFinalStatus;
    private List<TrdLoanOrderStatusEnum> preStatusList;
    private RiskQueryOrderEnum riskStatus;
    private RiskQueryAutoRiskCheckStatusEnum riskCheckStatus;

    TrdLoanOrderStatusEnum(String code, String value, boolean isFinalStatus, List<TrdLoanOrderStatusEnum> preStatusList) {
        this.code = code;
        this.value = value;
        this.isFinalStatus = isFinalStatus;
        this.preStatusList = preStatusList;
    }

    TrdLoanOrderStatusEnum(String code, String value, boolean isFinalStatus, List<TrdLoanOrderStatusEnum> preStatusList, RiskQueryOrderEnum riskStatus, RiskQueryAutoRiskCheckStatusEnum riskCheckStatus) {
        this.code = code;
        this.value = value;
        this.isFinalStatus = isFinalStatus;
        this.preStatusList = preStatusList;
        this.riskStatus = riskStatus;
        this.riskCheckStatus = riskCheckStatus;
    }

    public RiskQueryAutoRiskCheckStatusEnum getRiskCheckStatus() {
        return riskCheckStatus;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public RiskQueryOrderEnum getRiskStatus() {
        return riskStatus;
    }

    public List<TrdLoanOrderStatusEnum> getPreStatusList() {
        return preStatusList;
    }

    public static String getText(int code) {
        TrdLoanOrderStatusEnum[] values = TrdLoanOrderStatusEnum.values();
        for (int i = 0; i < values.length; i++) {
            TrdLoanOrderStatusEnum enumLoanOrderStatus = values[i];
            if (enumLoanOrderStatus.getCode().equals(code)) {
                return enumLoanOrderStatus.getValue();
            }
        }

        return null;
    }

    public static String getRiskStatus(String code) {
        TrdLoanOrderStatusEnum[] values = TrdLoanOrderStatusEnum.values();
        for (int i = 0; i < values.length; i++) {
            TrdLoanOrderStatusEnum enumLoanOrderStatus = values[i];
            if (enumLoanOrderStatus.getCode().equals(code)) {
                if (null != enumLoanOrderStatus.getRiskStatus()) {
                    return enumLoanOrderStatus.getRiskStatus().getCode();
                }
            }
        }
        return null;
    }


    public static Integer getRiskCheckStatus(String code) {
        TrdLoanOrderStatusEnum[] values = TrdLoanOrderStatusEnum.values();
        for (int i = 0; i < values.length; i++) {
            TrdLoanOrderStatusEnum enumLoanOrderStatus = values[i];
            if (enumLoanOrderStatus.getCode().equals(code)) {
                if (null != enumLoanOrderStatus.getRiskStatus()) {
                    // 风控检查值 只有在  RiskQueryOrderEnum.ORDER_FIRSTTRIAL 状态下才有效
                    if (enumLoanOrderStatus.getRiskStatus().equals(RiskQueryOrderEnum.ORDER_FIRSTTRIAL)) {
                        return enumLoanOrderStatus.getRiskCheckStatus().getCode();
                    }
                }
            }
        }
        return RiskQueryAutoRiskCheckStatusEnum.NOTPUSH.getCode();
    }

    public boolean isFinalStatus() {
        return isFinalStatus;
    }

    public void setFinalStatus(boolean finalStatus) {
        isFinalStatus = finalStatus;
    }

    public static Set<String> getByFinalStatus(boolean isFinalStatus) {
        TrdLoanOrderStatusEnum[] loanOrderStatusEnums = TrdLoanOrderStatusEnum.values();
        Set<String> codeSet = new HashSet<>();

        for (int i = 0; i < loanOrderStatusEnums.length; i++) {
            TrdLoanOrderStatusEnum enumLoanOrderStatus = loanOrderStatusEnums[i];
            if (enumLoanOrderStatus.isFinalStatus() == isFinalStatus) {
                codeSet.add(enumLoanOrderStatus.getCode());
            }
        }
        return codeSet;
    }

    public static TrdLoanOrderStatusEnum getByCode(String code) {
        for (TrdLoanOrderStatusEnum e : TrdLoanOrderStatusEnum.values()) {
            if (StringUtils.equals(e.getCode(), code)) {
                return e;
            }
        }
        return null;
    }

    public static boolean canUpdate(String newStatus, String oldStatus) {
        TrdLoanOrderStatusEnum orderStatusEnum = getByCode(newStatus);
        List<String> preStatus = new ArrayList<String>();
        for (TrdLoanOrderStatusEnum e : orderStatusEnum.getPreStatusList()) {
            preStatus.add(e.getCode());
        }
        if (ArrayUtils.contains(preStatus.toArray(), oldStatus)) {
            return true;
        }
        return false;
    }


    //
    //    APPROVALING("0", "审核中"),
    //    APPROVAL_FAILURE("-3", "审核失败"),
    //    MANUALREVIEWING("30", "人工审核中"),
    //    MANUALREVIEW_FAILURE("-30", "人工审核失败"),
    //    MANUALREVIEW_SUCCESS("31","人工审核通过"),
    //    LOANING("22", "放款中"),
    //    LOAN_FAILURE("-10", "放款失败"),
    //    LOAN_SUCCESS("21", "放款成功"),
    //    LOAN_YHQ("11", "已还清");
    //
    //
    //    /** 下面两个是老系统的属性，老系统没有还款表，新系统将还款抽取出来了，所以目前用不到以下两个字段
    //    STATUS_KKZ("12", "扣款中"),
    //    STATUS_KKSB("-7", "扣款失败");*/
    //
    //    private String code;
    //    private String value;
    //
    //    LoanOrderStatusEnum(String code, String value) {
    //        this.code = code;
    //        this.value = value;
    //    }
    //
    //    public String getCode() {
    //        return this.code;
    //    }
    //
    //    public String getValue() {
    //        return this.value;
    //    }
    //

}

