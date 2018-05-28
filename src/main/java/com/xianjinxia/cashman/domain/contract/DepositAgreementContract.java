package com.xianjinxia.cashman.domain.contract;

import com.xianjinxia.cashman.enums.DepositoryAgreementContractParmEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liquan on 2017/11/24.
 * 存管协议相关数据
 */
public class DepositAgreementContract  {

    /**
     * 甲方真实名称
     */
    private  String realName;
    /**
     * 乙方公司名称
     */
    private String partBName;
    /**
     * 丙方
     */
    private String partCName;
    /**
     * 来源
     */
    private String source;
    /**
     * 商户号
     */
    private String merchantNo;
    /**
     * 把对象数据转换为MAP
     * @return
     */
    public Map<String,String> getMapResult(){
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put(DepositoryAgreementContractParmEnum.PARM_REAL_NAME.getCode(),this.getRealName());
        resultMap.put(DepositoryAgreementContractParmEnum.PARM_PARTB_NAME.getCode(),this.getPartBName());
        resultMap.put(DepositoryAgreementContractParmEnum.PARM_PARTC_NAME.getCode(),this.getPartCName());
        return resultMap;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPartBName() {
        return partBName;
    }

    public void setPartBName(String partBName) {
        this.partBName = partBName;
    }

    public String getPartCName() {
        return partCName;
    }

    public void setPartCName(String partCName) {
        this.partCName = partCName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public DepositAgreementContract(String realName, String partBName, String partCName) {
        this.realName = realName;
        this.partBName = partBName;
        this.partCName = partCName;
    }

    public DepositAgreementContract(String partBName, String partCName) {
        this.partBName = partBName;
        this.partCName = partCName;
    }

    public DepositAgreementContract() {
    }
}
