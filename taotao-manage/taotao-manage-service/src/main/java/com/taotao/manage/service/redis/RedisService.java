package com.taotao.manage.service.redis;
/**
 * 单个版和集群版的通用接口
 *
 */
public interface RedisService {

	//设置
	public String set(String key,String value);
	
	//设置并同时设置过期时间
	public String setex(String key,int seconds,String value);
	
	//设置key过期时间
	public Long expire(String key,int seconds);
	
	//获取key
	public 	String get(String key);
	
	//删除key
	public Long del(String key);
	
	//自增
	public Long incr(String key);
	
	
}
