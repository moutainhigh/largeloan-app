package com.xianjinxia.cashman.domain.contract;

import com.xianjinxia.cashman.enums.LargeLoanAgreementContractParmEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liquan on 2017/11/24.
 * 大额分期借款协议
 */
public class LargeLoanAgreementContract extends BaseContract{
    /**
     * 乙方(出借人)名称
     */
   private String partBRealName;
    /**
     *  丁方公司名称
     */
   private String capitalName;
    /**
     * 丙方公司简称
     */
   private String companyShortTitle;
    /**
     * 丁方城市
     */
    private String capitalCity;
    /**
     * 借款用途
     */
    private String loanPurpose;

    /**
     * 还款方式
     */
    private String paymentMethod;
    /**
     * 主体公司所在市
     */
    private String companyCity;

    public String getPartBRealName() {
        return partBRealName;
    }

    public void setPartBRealName(String partBRealName) {
        this.partBRealName = partBRealName;
    }

    public String getCapitalName() {
        return capitalName;
    }

    public void setCapitalName(String capitalName) {
        this.capitalName = capitalName;
    }

    public String getCompanyShortTitle() {
        return companyShortTitle;
    }

    public void setCompanyShortTitle(String companyShortTitle) {
        this.companyShortTitle = companyShortTitle;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    @Override
    public Map<String, String> getMapResult() {
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_LOAN_ORDER_ID.getCode(),null==this.getLoanOrderId()?"":this.getLoanOrderId());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_REAL_NAME.getCode(),null==this.getRealName()?"":this.getRealName());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_MASK_IDCARD_NUM.getCode(),null==this.getMaskIdCardNum()?"":this.getMaskIdCardNum());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_COMPANY_TITLE.getCode(),null==this.getCompanyTitle()?"":this.getCompanyTitle());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_PARTB_REAL_NAME.getCode(),null==this.getPartBRealName()?"":this.getPartBRealName());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_COMPANY_SHORT_TITLE.getCode(),null==this.getCompanyShortTitle()?"":this.getCompanyShortTitle());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_CAPITAL_NAME.getCode(),null==this.getCapitalName()?"":this.getCapitalName());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_CAPITAL_CITY.getCode(),null==this.getCapitalCity()?"":this.getCapitalCity());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_MONEYA_MOUNT.getCode(),null==this.getMoneyAmount()?"":this.getMoneyAmount());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_CN_MONEY_AMOUNT.getCode(),null==this.getCnMoneyAmount()?"":this.getCnMoneyAmount());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_LOAN_PURPOSE.getCode(),null==this.getLoanPurpose()?"":this.getLoanPurpose());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_LOAN_TIME.getCode(),null==this.getLoanTime()?"":this.getLoanTime());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_LOAN_ENDTIME.getCode(),null==this.getLoanEndTime()?"":this.getLoanEndTime());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_PAYMENT_METHOD.getCode(),null==this.getLoanPurpose()?"":this.getLoanPurpose());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_INTEREST.getCode(),null==this.getInterest()?"":this.getInterest());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_PERIOD.getCode(),null==this.getPeriod()?"":this.getPeriod());
        resultMap.put(LargeLoanAgreementContractParmEnum.PARM_COMPANY_CITY.getCode(),null==this.getCompanyCity()?"":this.getCompanyCity());
        return resultMap;
    }

    @Override
    public String toString(){
        return "LargeLoanAgreementContract:"+"{"
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
                +",	partBRealName	 = "+	partBRealName
                +",	capitalName	 = "+	capitalName
                +",	companyShortTitle	 = "+	companyShortTitle
                +",	capitalCity	 = "+	capitalCity
                +",	loanPurpose	 = "+	loanPurpose
                +",	paymentMethod	 = "+	paymentMethod
                +"}";
    }
}
