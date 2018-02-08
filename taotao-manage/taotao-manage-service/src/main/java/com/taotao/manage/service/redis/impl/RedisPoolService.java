package com.taotao.manage.service.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.manage.service.redis.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 单机版redis
 * @author zhangwenfeng
 *
 */
public class RedisPoolService implements RedisService {
	
	//因为不确定是用单机还是集群所以需要设置为required
	@Autowired(required=false)
	private JedisPool jedisPool;

	@Override
	public String set(String key, String value) {
	
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	
	}

	@Override
	public String setex(String key, int seconds, String value) {
		Jedis jedis = jedisPool.getResource();
		String setex = jedis.setex(key, seconds, value);
		jedis.close();
		return setex;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		Long expire = jedis.expire(key, seconds);
		jedis.close();
		return expire;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String string = jedis.get(key);
		jedis.close();
		return string;
	}

	@Override
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long del = jedis.del(key);
		jedis.close();
		return del;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long incr = jedis.incr(key);
		jedis.close();
		return incr;
	}

}
