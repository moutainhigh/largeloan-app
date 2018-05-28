package com.xianjinxia.cashman.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Component
public class ErrorProperties {

    private static final Logger logger = LoggerFactory.getLogger(ErrorProperties.class);

    private static Map<String, String> errorMap = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties("error.properties");
            for (Object key : properties.keySet()) {
                String errorCode = key.toString();
                try {
                    errorMap.put(errorCode, new String(properties.getProperty(errorCode).getBytes("ISO-8859-1"), "utf-8"));
                } catch (Exception e) {
                    logger.error("load error.properties error, errorCode:{}", errorCode, e);
                }
            }
        } catch (IOException e) {
            logger.error("load error.properties error:", e);
        }
    }


    public static String getErrorMsg(String errorCode) {
        if (StringUtils.isEmpty(errorCode)) {
            return "system error";
        }


        String errorMsg = errorMap.get(errorCode);
        if (StringUtils.isEmpty(errorMsg)) {
            return "system error";
        }

        return errorMsg;
    }
}