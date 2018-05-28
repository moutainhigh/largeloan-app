package com.xianjinxia.cashman.conf.serials;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.xianjinxia.cashman.conf.MyGsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;

/**
 * 序列化反序列化基类
 * @author hym
 * 2017年9月14日
 * @param <T>
 */
public abstract class BaseSerial<T> implements JsonSerializer<T>, JsonDeserializer<T>{

	@Autowired
	private MyGsonHttpMessageConverter converter;
	
	@PostConstruct
	private void addToConverter(){
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		converter.registerTypeSerialOrDeseril(pt.getActualTypeArguments()[0], this);
	}
}
