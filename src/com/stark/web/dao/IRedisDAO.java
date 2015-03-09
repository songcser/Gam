package com.stark.web.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public interface IRedisDAO {
	
	public Long lpush(String key, String value);
	
	public String lpop(String key);
	
	public Long rpush(String key, String value);
	
	public String rpop(String key);
	
	public Long llen(String key);
	
	public List<String> lrange(String key, long start, long end) ;
	
	public String lindex(String key, long index);
	
	public Long lrem(String key, long i, String value) ;
	
	public String lset(String key, long index, String value);
	
	public String ltrim(String key, long start, long end);
	
	
	public String hmset(final String key,final Map<String, String> value);
	
    public Long del(String... keys);

    public String set(byte[] key, byte[] value, long liveTime);

    public String set(String key, String value, long liveTime);

    public String set(String key, String value);

    public String set(byte[] key, byte[] value);

    public String get(String key);
   
    public Set<String> Setkeys(String pattern);

    public Boolean exists(String key);

    public String flushDB();

    public long dbSize();

    public String ping();

	public Map<String, String> hgetAll(String key);
	
	public Boolean sismember(String key,String member);

	public Long sadd(String key, String ... member);
	
	public Long incr(final String key);
	
	public Long incr(final byte[] key);

	public Long hset(String key, String field, String value);

	public Long hincrby(String key, String field, long increment);

	public Long decr(String key);

	public Long decr(byte[] key);

	public Boolean expireAt(String key, long seconds);
	
	public Boolean expireAt(byte[] key, long seconds);
	
	public Boolean expire(String key, int seconds);
	
	public Boolean expire(byte[] key, int seconds);

	public Long zadd(String key, double score, String member);

	public Set<String> zrange(String key, int start, int stop);

	public Object hget(String key, String field);

	public Double zscore(String key, String member);

	public Long zrem(String key, Object ... member);

	public Set<String> zrevrange(String key, int start, int stop);
	
	public Set<String> smembers(String key);

	public Long sadd(String key, String member);

	Long sadd(byte[] key, byte[] member);
}
