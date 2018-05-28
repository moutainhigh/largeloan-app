package com.xianjinxia.cashman.dto;

public class AliPayAccount {

    private String appId;
    private String serverUrl;
    private String account;
    private String privateKeyPath;
    private String privateKey;
    private String aliPayPublicKeyPath;
    private String aliPayPublicKey;
    private String payeeType;
    private String payeeRealName;

    public String getPayeeRealName() {
        return payeeRealName;
    }

    public void setPayeeRealName(String payeeRealName) {
        this.payeeRealName = payeeRealName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAliPayPublicKeyPath() {
        return aliPayPublicKeyPath;
    }

    public void setAliPayPublicKeyPath(String aliPayPublicKeyPath) {
        this.aliPayPublicKeyPath = aliPayPublicKeyPath;
    }

    public String getAliPayPublicKey() {
        return aliPayPublicKey;
    }

    public void setAliPayPublicKey(String aliPayPublicKey) {
        this.aliPayPublicKey = aliPayPublicKey;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }
}
