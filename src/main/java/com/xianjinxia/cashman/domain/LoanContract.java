package com.xianjinxia.cashman.domain;

import com.xianjinxia.cashman.enums.DataValidEnum;

import java.util.Date;

public class LoanContract {
    private Long id;

    private String contractName;

    private String contractType;

    private Long trdLoanId;

    private Long userId;

    private String contractPath;

    private String loanContractDesc;

    private String status;

    private Date createdTime;

    private String createdUser;

    private Date updatedTime;

    private String updatedUser;

    private Boolean dataValid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 合同生效
     */
    public static final String CONTRACT_EFFICIENT="1";
    /**
     * 合同失效
     */
    public static final String CONTRACT_DISABLED="0";
    public LoanContract(){}

    /**
     * 用于放款成功 插入借款合同记录
     * @param contractName
     * @param contractType
     * @param trdLoanId
     * @param userId
     * @param status
     */
    public LoanContract(String contractName, String contractType, Long trdLoanId, Long userId, String status) {
        this.contractName = contractName;
        this.contractType = contractType;
        this.trdLoanId = trdLoanId;
        this.userId = userId;
        this.status = status;
        this.dataValid= DataValidEnum.DATA_VALID_YES.getCode();
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName == null ? null : contractName.trim();
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType == null ? null : contractType.trim();
    }

    public Long getTrdLoanId() {
        return trdLoanId;
    }

    public void setTrdLoanId(Long trdLoanId) {
        this.trdLoanId = trdLoanId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContractPath() {
        return contractPath;
    }

    public void setContractPath(String contractPath) {
        this.contractPath = contractPath == null ? null : contractPath.trim();
    }

    public String getLoanContractDesc() {
        return loanContractDesc;
    }

    public void setLoanContractDesc(String loanContractDesc) {
        this.loanContractDesc = loanContractDesc == null ? null : loanContractDesc.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser == null ? null : createdUser.trim();
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser == null ? null : updatedUser.trim();
    }

    public Boolean getDataValid() {
        return dataValid;
    }

    public void setDataValid(Boolean dataValid) {
        this.dataValid = dataValid;
    }
}