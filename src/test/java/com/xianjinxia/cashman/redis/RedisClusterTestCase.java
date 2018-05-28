package com.xianjinxia.cashman.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xianjinxia.cashman.CashmanApplication;

import redis.clients.jedis.JedisCluster;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CashmanApplication.class)
public class RedisClusterTestCase {

	@Autowired
	private JedisCluster jedisCluster;

	@Test
	public void testSetKeyValue() {
		for (int x=0; x<10000; x++) {
			String result = jedisCluster.set("test-key", "test-val");
			System.out.println(result);
		}
	}
	
	
}
