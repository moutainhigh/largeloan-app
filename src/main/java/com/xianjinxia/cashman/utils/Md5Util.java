package com.xianjinxia.cashman.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * md5工具类
 */
public class Md5Util {

    private static Logger logger = LoggerFactory.getLogger(Md5Util.class);

    public static String md5(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(value.getBytes("utf-8"));
            byte[] digest = md.digest();

            StringBuffer hexValue = new StringBuffer();

            for (int i = 0; i < digest.length; i++) {
                int val = ((int) digest[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("md5LowerCase NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("md5LowerCase UnsupportedEncodingException", e);
        }
        return null;
    }

}
