package com.xianjinxia.cashman.dto;

/**
 * Created by liquan on 2018/4/10.
 * 统计还款数据DTO
 * @Author: liquan
 * @Description:
 * @Date: Created in 17:55 2018/4/10
 * @Modified By:
 *  expireNumber  //到期人数
    repaymentNumber     //还款人数
    type   // 类型 de:大额 ， xe: 小额
    noRepaymentNumber   //未还款且通知未通知未覆盖用户数
 *  merchantNo    // 商户号
 */
public class StatisticsRepayDto {
    //订单类型
    private String type;
    //商户号
    private String merchantNo;
    //当天应还人数
    private Integer expireNumber;
    //实际还款人数
    private Integer repaymentNumber;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Integer getExpireNumber() {
        return expireNumber;
    }

    public void setExpireNumber(Integer expireNumber) {
        this.expireNumber = expireNumber;
    }

    public Integer getRepaymentNumber() {
        return repaymentNumber;
    }

    public void setRepaymentNumber(Integer repaymentNumber) {
        this.repaymentNumber = repaymentNumber;
    }

    public StatisticsRepayDto() {
    }

    public StatisticsRepayDto(String type, String merchantNo, Integer expireNumber, Integer repaymentNumber) {
        this.type = type;
        this.merchantNo = merchantNo;
        this.expireNumber = expireNumber;
        this.repaymentNumber = repaymentNumber;
    }

    @Override
    public String toString() {
        return "StatisticsRepayDto{" +
                "type='" + type + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", expireNumber=" + expireNumber +
                ", repaymentNumber=" + repaymentNumber +
                '}';
    }
}
