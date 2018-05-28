package com.xianjinxia.cashman.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Administrator on 2017/10/14 0014.
 */
@ApiModel
public class ContractDto {
    @ApiModelProperty(example = "还款协议",value = "合同名称")
    private String contractName;
    @ApiModelProperty(example = "http://www.baidu.com",value = "合同url")
    private String contractUrl;

    private String contractType;

    public ContractDto(String contractName, String contractUrl, String contractType) {
        this.contractName = contractName;
        this.contractUrl = contractUrl;
        this.contractType = contractType;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    @Override
    public String toString() {
        return "ContractDto{" +
                "contractName='" + contractName + '\'' +
                ", contractUrl='" + contractUrl + '\'' +
                ", contractType='" + contractType + '\'' +
                '}';
    }
}
