package com.stark.web.dao;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Component
public class RedisDataSource implements IRedisDataSource{
	 private static final Logger log = Logger.getLogger(RedisDataSource.class);

	    @Resource
	    private ShardedJedisPool    shardedJedisPool;
	    
	    @Override
	    public ShardedJedis getRedisClient() {
	        try {
	            ShardedJedis shardJedis = shardedJedisPool.getResource();
	            return shardJedis;
	        } catch (Exception e) {
	            log.error("getRedisClent error",e.fillInStackTrace());
	        }
	        return null;
	    }
	    
	    @Override
	    public void returnResource(ShardedJedis shardedJedis) {
	        shardedJedisPool.returnResource(shardedJedis);
	    }
	    
	    @Override
	    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
	        if (broken) {
	            shardedJedisPool.returnBrokenResource(shardedJedis);
	        } else {
	            shardedJedisPool.returnResource(shardedJedis);
	        }
	    }
}
