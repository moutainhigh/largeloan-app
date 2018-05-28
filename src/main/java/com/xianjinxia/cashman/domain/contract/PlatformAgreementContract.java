package com.xianjinxia.cashman.domain.contract;

import com.xianjinxia.cashman.enums.PlatformAgreementContractParamEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liquan on 2017/11/24.
 */
public class PlatformAgreementContract extends BaseContract{
    /**
     * 状态
     */
    private String status;
    /**
     * 平台名称
     */
    private  String appName;
    /**
     * 应还总金额
     */
    private String repaymentAmount;
    /**
     * 利息费率
     */
    private String overdueRate;
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(String repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }

    /**
     * 把对象数据转换为MAP
     * @return
     */
    @Override
    public Map<String,String> getMapResult(){
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put(PlatformAgreementContractParamEnum.PARM_LOAN_ORDER_ID.getCode(),null==this.getLoanOrderId()?"":this.getLoanOrderId());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_COMPANY_TITLE.getCode(),null==this.getCompanyTitle()?"":this.getCompanyTitle());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_REAL_NAME.getCode(),null==this.getRealName()?"":this.getRealName());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_MASK_IDCARD_NUM.getCode(),null==this.getMaskIdCardNum()?"":this.getMaskIdCardNum());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_LOAN_ENDTIME.getCode(),null==this.getLoanEndTime()?"":this.getLoanEndTime());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_MONEYA_MOUNT.getCode(),null==this.getMoneyAmount()?"":this.getMoneyAmount());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_INTEREST.getCode(),null==this.getInterest()?"":this.getInterest());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_STATUS.getCode(),null==this.getStatus()?"":this.getStatus());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_LOAN_TIME.getCode(),null==this.getLoanTime()?"":this.getLoanTime());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_APP_NAME.getCode(),null==this.getAppName()?"":this.getAppName());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_PERIOD.getCode(),null==this.getPeriod()?"1":this.getPeriod());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_REPAYMENT_AMOUNT.getCode(),null==this.getRepaymentAmount()?"1":this.getRepaymentAmount());
        resultMap.put(PlatformAgreementContractParamEnum.PARM_OVERDUE_RARE.getCode(),null==this.getOverdueRate()?"":this.getOverdueRate());
        return resultMap;
    }

    @Override
    public String toString(){
        return "PlatformAgreementContract:{"
                +",	loanOrderId	 = "+this.getLoanOrderId()
                +",	realName	 = "+this.getRealName()
                +",	maskIdCardNum	 = "+this.getMaskIdCardNum()
                +",	companyTitle	 = "+this.getCompanyTitle()
                +",	moneyAmount	 = "+this.getMoneyAmount()
                +",	cnMoneyAmount	 = "+this.getCnMoneyAmount()
                +",	loanTime	 = "+this.getLoanTime()
                +",	loanEndTime	 = "+this.getLoanEndTime()
                +",	period	 = "+this.getPeriod()
                +",	interest	 = "+this.getInterest()
                +", status = "+status
                +", appName = "+appName
                +", repaymentAmount = "+repaymentAmount
                +", overdueRate = "+overdueRate
                +"}";
    }
}
