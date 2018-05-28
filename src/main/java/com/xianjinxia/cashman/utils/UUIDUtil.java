package com.xianjinxia.cashman.utils;

import java.util.UUID;

public class UUIDUtil {
	
	public static String randomUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}

}
