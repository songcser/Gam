package com.stark.web.dao;

import redis.clients.jedis.ShardedJedis;

public interface IRedisDataSource {

	public ShardedJedis getRedisClient();

	public void returnResource(ShardedJedis shardedJedis);

	public void returnResource(ShardedJedis shardedJedis, boolean broken);

}
