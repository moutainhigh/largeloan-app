package com.xianjinxia.cashman.enums;

import java.util.HashSet;
import java.util.Set;

public enum RepaymentPlanStatusEnum {


	Waiting(10,"待还款",RiskQueryRepaymentPlanStatusEnum.AWAIT,false),
	Part(20,"部分还款"),//目前不支持，待扩展的还款类型
	Repaymented(30,"已还款",RiskQueryRepaymentPlanStatusEnum.REPAYMENT,true),
	Canceled(40,"已取消",null,true); //风控审核失败，还款计划改成已取消


	//YHZ(-20,"已坏账"),
	//YQYHK(34,"逾期已还款")

	private int code;
	private String text;
	private RiskQueryRepaymentPlanStatusEnum riskStatus;
	private Boolean isEndStatus;
	RepaymentPlanStatusEnum(int code, String text) {
		this.code = code;
		this.text = text;
	}

	RepaymentPlanStatusEnum(int code, String text,RiskQueryRepaymentPlanStatusEnum riskStatus) {
		this.code = code;
		this.text = text;
		this.riskStatus=riskStatus;
	}

	RepaymentPlanStatusEnum(int code, String text, RiskQueryRepaymentPlanStatusEnum riskStatus, Boolean isEndStatus) {
		this.code = code;
		this.text = text;
		this.riskStatus = riskStatus;
		this.isEndStatus = isEndStatus;
	}

	public static String getRiskStatus(int code){
		RepaymentPlanStatusEnum[] values = RepaymentPlanStatusEnum.values();
		for(RepaymentPlanStatusEnum source:values){
			if(source.getCode()==code){
				return source.getRiskStatus().getCode();
			}
		}
		return RiskQueryRepaymentPlanStatusEnum.AWAIT.getCode();
	}

	public RiskQueryRepaymentPlanStatusEnum getRiskStatus() {
		return riskStatus;
	}

	public int getCode() {
		return this.code;
	}
	
	public String getText() {
		return this.text;
	}

	public static String getText(int code){
		RepaymentPlanStatusEnum[] values = RepaymentPlanStatusEnum.values();
		for (int i = 0; i < values.length; i++) {
			RepaymentPlanStatusEnum enumRepaymentPlanStatus = values[i];
			if (enumRepaymentPlanStatus.getCode() == code){
				return enumRepaymentPlanStatus.getText();
			}
		}

		return null;
	}

	public Boolean getEndStatus() {
		return isEndStatus;
	}

	public void setEndStatus(Boolean endStatus) {
		isEndStatus = endStatus;
	}

	public static Set<Integer> getByFinalStatus(boolean isFinalStatus){
		RepaymentPlanStatusEnum[] loanOrderStatusEnums = RepaymentPlanStatusEnum.values();
		Set<Integer> codeSet = new HashSet<>();
		for(RepaymentPlanStatusEnum enumRepaymentStatus:loanOrderStatusEnums){
			if ((null!=enumRepaymentStatus.isEndStatus)&&(enumRepaymentStatus.isEndStatus == isFinalStatus)){
				codeSet.add(enumRepaymentStatus.getCode());
			}
		}
		return codeSet;
	}
}
