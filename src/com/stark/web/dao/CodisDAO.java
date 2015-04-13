package com.stark.web.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.wandoulabs.jodis.JedisResourcePool;

import redis.clients.jedis.Jedis;

public class CodisDAO implements IRedisDAO{

	@Resource
	JedisResourcePool jedisPool;
	
	public JedisResourcePool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisResourcePool jedisPool) {
		this.jedisPool = jedisPool;
	}

	private Jedis Jedis(){
		//JedisPoolConfig poolConfig = new JedisPoolConfig();
		
		//JedisResourcePool jedisPool = new RoundRobinJedisPool("127.0.0.1:2181", 30000, "/zk/codis/db_test/proxy",poolConfig);
		return jedisPool.getResource();
	}
	
	@Override
	public Long lpush(String key, String value) {
		Jedis jedis =  Jedis();
		Long len = jedis.lpush(key, value); 
		close(jedis);
		return len;
	}

	@Override
	public String lpop(String key) {
		Jedis jedis = Jedis();
		String value = jedis.lpop(key);
		close(jedis);
		return value;
	}

	@Override
	public Long rpush(String key, String value) {
		Jedis jedis = Jedis();
		long len = jedis.rpush(key, value);
		close(jedis);
		return len;
	}

	@Override
	public String rpop(String key) {
		Jedis jedis = Jedis();
		String value = jedis.rpop(key);
		close(jedis);
		return value;
	}

	@Override
	public Long llen(String key) {
		Jedis jedis = Jedis();
		Long len = jedis.llen(key);
		close(jedis);
		return len;
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = Jedis();
		List<String> list = jedis.lrange(key, start, end);
		close(jedis);
		return list;
	}

	@Override
	public String lindex(String key, long index) {
		Jedis jedis = Jedis();
		String value = jedis.lindex(key, index);
		close(jedis);
		return value;
	}

	@Override
	public Long lrem(String key, long count, String value) {
		Jedis jedis = Jedis();
		Long len = jedis.lrem(key, count, value);
		close(jedis);
		return len;
	}

	@Override
	public String lset(String key, long index, String value) {
		Jedis jedis = Jedis();
		String val = jedis.lset(key, index, value);
		close(jedis);
		return val;
	}

	@Override
	public String ltrim(String key, long start, long end) {
		Jedis jedis = Jedis();
		String value =jedis.ltrim(key, start, end);
		close(jedis);
		return value;
	}

	@Override
	public String hmset(String key, Map<String, String> value) {
		Jedis jedis = Jedis();
		String val = jedis.hmset(key, value);
		close(jedis);
		return val;
	}

	@Override
	public Long del(String... keys) {
		Jedis jedis = Jedis();
		Long len = jedis.del(keys);
		close(jedis);
		return len;
	}

	@Override
	public String set(byte[] key, byte[] value, long liveTime) {
		Jedis jedis = Jedis();
		String result = jedis.set(key, value);
		jedis.expireAt(key, liveTime);
		close(jedis);
		return result;
	}

	@Override
	public String set(String key, String value, long liveTime) {
		Jedis jedis = Jedis();
		String result = jedis.set(key, value);
		jedis.expireAt(key, liveTime);
		close(jedis);
		return result;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = Jedis();
		String val = jedis.set(key, value);
		close(jedis);
		return val;
	}

	@Override
	public String set(byte[] key, byte[] value) {
		Jedis jedis = Jedis();
		String val = jedis.set(key, value);
		close(jedis);
		return val;
	}

	@Override
	public String get(String key) {
		Jedis jedis = Jedis();
		String value = jedis.get(key);
		close(jedis);
		return value;
	}

	@Override
	public Set<String> Setkeys(String pattern) {
		return null;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = Jedis();
		Boolean flag = jedis.exists(key);
		close(jedis);
		return flag;
	}

	@Override
	public String flushDB() {
		Jedis jedis = Jedis();
		String result = jedis.flushDB();
		close(jedis);
		return result;
	}

	@Override
	public long dbSize() {
		Jedis jedis = Jedis();
		long len = jedis.dbSize();
		close(jedis);
		return len;
	}

	@Override
	public String ping() {
		Jedis jedis = Jedis();
		String value = jedis.ping();
		close(jedis);
		return value;
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = Jedis();
		Map<String,String> map = jedis.hgetAll(key);
		close(jedis);
		return map;
	}

	@Override
	public Boolean sismember(String key, String member) {
		Jedis jedis = Jedis();
		Boolean result = jedis.sismember(key, member);
		close(jedis);
		return result;
	}

	@Override
	public Long sadd(String key, String... member) {
		Jedis jedis = Jedis();
		Long len = jedis.sadd(key, member);
		close(jedis);
		return len;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = Jedis();
		Long len = jedis.incr(key);
		close(jedis);
		return len;
	}

	@Override
	public Long incr(byte[] key) {
		Jedis jedis = Jedis();
		Long len = jedis.incr(key);
		close(jedis);
		return len;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = Jedis();
		Long len = jedis.hset(key, field, value);
		close(jedis);
		return len;
	}

	@Override
	public Long hincrby(String key, String field, long increment) {
		Jedis jedis = Jedis();
		Long len = jedis.hincrBy(key, field, increment);
		close(jedis);
		return len;
	}

	@Override
	public Long decr(String key) {
		Jedis jedis = Jedis();
		Long len = jedis.decr(key);
		close(jedis);
		return len;
	}

	@Override
	public Long decr(byte[] key) {
		Jedis jedis = Jedis();
		Long len = jedis.decr(key);
		close(jedis);
		return len;
	}

	@Override
	public Boolean expireAt(String key, long seconds) {
		Jedis jedis = Jedis();
		Boolean result = jedis.expireAt(key, seconds)>0;
		close(jedis);
		return result;
	}

	@Override
	public Boolean expireAt(byte[] key, long seconds) {
		Jedis jedis = Jedis();
		Boolean result = jedis.expireAt(key, seconds)>0;
		close(jedis);
		return result;
	}

	@Override
	public Boolean expire(String key, int seconds) {
		Jedis jedis = Jedis();
		Boolean result = jedis.expire(key, seconds)>0;
		close(jedis);
		return result;
	}

	@Override
	public Boolean expire(byte[] key, int seconds) {
		Jedis jedis = Jedis();
		Boolean result = jedis.expire(key, seconds)>0;
		close(jedis);
		return result;
	}

	@Override
	public Long zadd(String key, double score, String member) {
		Jedis jedis = Jedis();
		Long len = jedis.zadd(key, score, member);
		close(jedis);
		return len;
	}

	@Override
	public Set<String> zrange(String key, int start, int stop) {
		Jedis jedis = Jedis();
		Set<String> set = jedis.zrange(key, start, stop);
		close(jedis);
		return set;
	}

	@Override
	public Object hget(String key, String field) {
		Jedis jedis = Jedis();
		Object obj = jedis.hget(key, field);
		close(jedis);
		return obj;
	}

	@Override
	public Double zscore(String key, String member) {
		Jedis jedis = Jedis();
		Double value = jedis.zscore(key, member);
		close(jedis);
		return value;
	}

	@Override
	public Long zrem(String key, String ... member) {
		Jedis jedis = Jedis();
		Long len = jedis.zrem(key,member);
		close(jedis);
		return len;
	}

	@Override
	public Set<String> zrevrange(String key, int start, int stop) {
		Jedis jedis = Jedis();
		Set<String> set = jedis.zrevrange(key, start, stop);
		close(jedis);
		return set;
	}

	@Override
	public Set<String> smembers(String key) {
		Jedis jedis = Jedis();
		Set<String> set = jedis.smembers(key);
		close(jedis);
		return set;
	}

	@Override
	public Long sadd(String key, String member) {
		Jedis jedis = Jedis();
		Long len = jedis.sadd(key, member);
		close(jedis);
		return len;
	}

	@Override
	public Long sadd(byte[] key, byte[] member) {
		Jedis jedis = Jedis();
		Long len = jedis.sadd(key, member);
		close(jedis);
		return len;
	}

	private void close(Jedis jedis){
		jedis.close();
	}
}
