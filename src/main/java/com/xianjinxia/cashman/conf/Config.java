package com.xianjinxia.cashman.conf;

import com.xianjinxia.cashman.conf.ExtProperties.ActiveMqConfiguration;
import com.xjx.mqclient.service.MqClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class Config {

	private static final Logger	logger	= Logger.getLogger(Config.class);

	@Autowired
	private DataSource			dataSource;

	@Autowired
	private ExtProperties		extProperties;
//
//	@Bean
//	public JedisCluster getJedisCluster() {
//		RedisCluster redisCluster = extProperties.getRedisCluster();
//		String[] serverArray = redisCluster.getClusterNodes().split(",");
//		Set<HostAndPort> nodes = new HashSet<>();
//
//		for (String ipPort : serverArray) {
//			String[] ipPortPair = ipPort.split(":");
//			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
//		}
//
//		JedisPoolConfig pool = new JedisPoolConfig();
//		pool.setMaxWaitMillis(3000);
//		pool.setMaxTotal(3000);
//		pool.setMinIdle(10);
//		pool.setMaxIdle(100);
//
//		JedisCluster jedisCluster = new JedisCluster(nodes, redisCluster.getCommandTimeout(), redisCluster.getMaxRedirections(), redisCluster.getTryNum(), redisCluster.getPassword(), pool);
//		return jedisCluster;
//	}

	@Bean
	public MqClient mqClient() {
		ActiveMqConfiguration activeMqConfiguration = extProperties.getActiveMqConfiguration();
		MqClient mq = null;
		try {
			mq = new MqClient(activeMqConfiguration.getBrokerUrl(), dataSource, activeMqConfiguration.getQueueTableName(), activeMqConfiguration.getQueueName(), activeMqConfiguration.getQueueMaxCount(), activeMqConfiguration.getIsCreateTable());
			mq.start();
		} catch (Exception e) {
			logger.error("mq启动初始化异常", e);
		}
		return mq;

	}

	//fixed thread pool
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.setQueueCapacity(20);
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		return threadPoolTaskExecutor;
	}
}
