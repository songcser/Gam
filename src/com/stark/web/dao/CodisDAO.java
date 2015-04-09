package com.stark.web.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wandoulabs.jodis.JedisResourcePool;
import com.wandoulabs.jodis.RoundRobinJedisPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

public class CodisDAO implements IRedisDAO{

	private Jedis Jedis(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		
		JedisResourcePool jedisPool = new RoundRobinJedisPool("zkserver:2181", 30000, "/zk/codis/db_xxx/proxy",poolConfig);
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}
	
	@Override
	public Long lpush(String key, String value) {
		return Jedis().lpush(key, value);
	}

	@Override
	public String lpop(String key) {
		return Jedis().lpop(key);
	}

	@Override
	public Long rpush(String key, String value) {
		return Jedis().rpush(key, value);
	}

	@Override
	public String rpop(String key) {
		return Jedis().rpop(key);
	}

	@Override
	public Long llen(String key) {
		return Jedis().llen(key);
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		return Jedis().lrange(key, start, end);
	}

	@Override
	public String lindex(String key, long index) {
		return Jedis().lindex(key, index);
	}

	@Override
	public Long lrem(String key, long count, String value) {
		return Jedis().lrem(key, count, value);
	}

	@Override
	public String lset(String key, long index, String value) {
		return Jedis().lset(key, index, value);
	}

	@Override
	public String ltrim(String key, long start, long end) {
		return Jedis().ltrim(key, start, end);
	}

	@Override
	public String hmset(String key, Map<String, String> value) {
		return Jedis().hmset(key, value);
	}

	@Override
	public Long del(String... keys) {
		return Jedis().del(keys);
	}

	@Override
	public String set(byte[] key, byte[] value, long liveTime) {
		String result = Jedis().set(key, value);
		Jedis().expireAt(key, liveTime);
		return result;
	}

	@Override
	public String set(String key, String value, long liveTime) {
		String result = Jedis().set(key, value);
		Jedis().expireAt(key, liveTime);
		return result;
	}

	@Override
	public String set(String key, String value) {
		return Jedis().set(key, value);
	}

	@Override
	public String set(byte[] key, byte[] value) {
		return Jedis().set(key, value);
	}

	@Override
	public String get(String key) {
		return Jedis().get(key);
	}

	@Override
	public Set<String> Setkeys(String pattern) {
		return null;
	}

	@Override
	public Boolean exists(String key) {
		return Jedis().exists(key);
	}

	@Override
	public String flushDB() {
		return Jedis().flushDB();
	}

	@Override
	public long dbSize() {
		return Jedis().dbSize();
	}

	@Override
	public String ping() {
		return Jedis().ping();
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		return Jedis().hgetAll(key);
	}

	@Override
	public Boolean sismember(String key, String member) {
		return Jedis().sismember(key, member);
	}

	@Override
	public Long sadd(String key, String... member) {
		return Jedis().sadd(key, member);
	}

	@Override
	public Long incr(String key) {
		return Jedis().incr(key);
	}

	@Override
	public Long incr(byte[] key) {
		return Jedis().incr(key);
	}

	@Override
	public Long hset(String key, String field, String value) {
		return Jedis().hset(key, field, value);
	}

	@Override
	public Long hincrby(String key, String field, long increment) {
		return Jedis().hincrBy(key, field, increment);
	}

	@Override
	public Long decr(String key) {
		return Jedis().decr(key);
	}

	@Override
	public Long decr(byte[] key) {
		return Jedis().decr(key);
	}

	@Override
	public Boolean expireAt(String key, long seconds) {
		return Jedis().expireAt(key, seconds)>0;
	}

	@Override
	public Boolean expireAt(byte[] key, long seconds) {
		return Jedis().expireAt(key, seconds)>0;
	}

	@Override
	public Boolean expire(String key, int seconds) {
		return Jedis().expire(key, seconds)>0;
	}

	@Override
	public Boolean expire(byte[] key, int seconds) {
		return Jedis().expire(key, seconds)>0;
	}

	@Override
	public Long zadd(String key, double score, String member) {
		return Jedis().zadd(key, score, member);
	}

	@Override
	public Set<String> zrange(String key, int start, int stop) {
		return Jedis().zrange(key, start, stop);
	}

	@Override
	public Object hget(String key, String field) {
		return Jedis().hget(key, field);
	}

	@Override
	public Double zscore(String key, String member) {
		return Jedis().zscore(key, member);
	}

	@Override
	public Long zrem(String key, Object... member) {
		return Jedis().zrem(key, (String[])member);
	}

	@Override
	public Set<String> zrevrange(String key, int start, int stop) {
		return Jedis().zrevrange(key, start, stop);
	}

	@Override
	public Set<String> smembers(String key) {
		return Jedis().smembers(key);
	}

	@Override
	public Long sadd(String key, String member) {
		return Jedis().sadd(key, member);
	}

	@Override
	public Long sadd(byte[] key, byte[] member) {
		return Jedis().sadd(key, member);
	}

}
