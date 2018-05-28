package com.xianjinxia.cashman.conf;

import com.xianjinxia.cashman.dto.AliPayAccount;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

@Configuration
@ConfigurationProperties(prefix = "aliPayAccountConfig")
public class AliPayProperties {

    /**
     * 阿里支付配置
     */
    private static LinkedHashMap<String, AliPayAccount> config = new LinkedHashMap<>();

    public LinkedHashMap<String, AliPayAccount> getConfig() {
        return config;
    }

    public void setConfig(LinkedHashMap<String, AliPayAccount> config) {
        this.config = config;
    }

}
