package com.stark.web.dao;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisDAO implements IRedisDAO {
	private static String redisCode = "utf-8";
	
	private static Logger logger=  Logger.getLogger(RedisDAO.class); 
	
	@Autowired
	// private StringRedisTemplate stringRedisTemplate;
	private RedisTemplate<String, String> redisTemplate;

	public RedisSerializer<String> getRedisSerializer() {
		return redisTemplate.getStringSerializer();
	}
	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public Long del(final String... keys) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				for (int i = 0; i < keys.length; i++) {
					result = connection.del(keys[i].getBytes());
				}
				return result;
			}
		});
		
		return result;
	}

	/**
	 * 添加key value 并且设置存活时间(byte)
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	public String set(final byte[] key, final byte[] value, final long liveTime) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return "";
			}
		});
		
		return "";
	}

	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 *            单位秒
	 */
	public String set(String key, String value, long liveTime) {
		return this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param value
	 */
	public String set(String key, String value) {
		return this.set(key, value, 0L);
	}

	/**
	 * 添加key value (字节)(序列化)
	 * 
	 * @param key
	 * @param value
	 */
	public String set(byte[] key, byte[] value) {
		return this.set(key, value, 0L);
	}

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					byte bytes[]= connection.get(key.getBytes());
					if(bytes==null){
						return null;
					}
					return new String(bytes, redisCode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	public Long incr(final String key){
		return incr(key.getBytes());
	}
	
	public Long incr(final byte[] key){
		//System.out.println("Redis: incr "+key);
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.incr(key);
			}
		});
	}
	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @return
	 */
	public Set<String> Setkeys(String pattern) {
		return redisTemplate.keys(pattern);

	}

	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	public Boolean exists(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(key.getBytes());
			}
		});
	}

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	public String flushDB() {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	/**
	 * 查看redis里有多少数据
	 */
	public long dbSize() {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.dbSize();
			}
		});
	}

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	public String ping() {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {

				return connection.ping();
			}
		});
	}

	/**
	 * 压栈
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public Long lpush(String key, String value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 出栈
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String lpop(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 入队
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public Long rpush(String key, String value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 出队
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String rpop(String key) {
		return redisTemplate.opsForList().rightPop(key);
	}

	/**
	 * 栈/队列长
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Long llen(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 范围检索
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public List<String> lrange(String key, long start, long end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 移除
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	@Override
	public Long lrem(String key, long i, String value) {
		return redisTemplate.opsForList().remove(key, i, value);
	}

	/**
	 * 检索
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	@Override
	public String lindex(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 置值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	@Override
	public String lset(String key, long index, String value) {
		redisTemplate.opsForList().set(key, index, value);
		return "";
	}

	/**
	 * 裁剪
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	@Override
	public String ltrim(String key, long start, long end) {
		redisTemplate.opsForList().trim(key, start, end);
		return "";
	}

	public String hmset(final BoundHashOperations<String, byte[], byte[]> boundHashOperations) {
		Object result = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] key = getRedisSerializer().serialize(boundHashOperations.getKey());
				// System.out.println("Key: "+key);
				
				connection.hMSet(key, boundHashOperations.entries());
				return connection;
			}
		});
		return result+"";
	}
	
	@Override
	public String hmset(final String key,final Map<String, String> value) {
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key); 
		if(boundHashOperations==null){
          	return 0+"";
        }
		boundHashOperations.putAll(value);  
	   
		return 1+"";
	}
	
	@Override
	public Long hset(String key,String field, String value) {
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key); 
		if(boundHashOperations==null){
          	return 0L;
        }
		boundHashOperations.put(field, value);
		
		return 1L;
	}
	
	@Override
	public Map<String, String> hgetAll(String key) {
		BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
		return  boundHashOperations.entries();
	}
	
	@Override
	public Boolean sismember(String key, String member) {
		return redisTemplate.opsForSet().isMember(key, member);
	}
	
	@Override
	public Long sadd(String key, String ... members) {
		return redisTemplate.opsForSet().add(key, members);
	}
	@Override
	public Long hincrby(String key, String field, long increment) {
		return redisTemplate.opsForHash().increment(key, field, increment);
	}
	@Override
	public Long decr(final byte[] key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.decr(key);
			}
		});
	}
	
	@Override
	public Long decr(String key){
		return decr(key.getBytes());
	}
	@Override
	public Boolean expireAt(String key, long seconds) {
		return expireAt(key.getBytes(), seconds);
	}
	@Override
	public Boolean expireAt(final byte[] key, final long seconds) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.expireAt(key, seconds);
			}
		});
	}
	@Override
	public Long zadd(String key, double score, String member) {
		if(redisTemplate.opsForZSet().add(key, member, score)){
			return 1L;
		}
		else 
			return 0L;
	}
	@Override
	public Set<String> zrange(String key, int start, int stop) {
		return redisTemplate.opsForZSet().range(key, start, stop);
	}
	@Override
	public Object hget(String key, String field) {
		return redisTemplate.opsForHash().get(key, field);
	}
	@Override
	public Double zscore(String key, String member) {
		return redisTemplate.opsForZSet().score(key, member);
	}
	@Override
	public Long zrem(String key, String ... members) {
		return redisTemplate.opsForZSet().remove(key, members);
	}
	@Override
	public Boolean expire(final String key, final int seconds) {
		return expire(key.getBytes(),seconds);
	}
	@Override
	public Boolean expire(final byte[] key, final int seconds) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.expire(key, seconds);
			}
		});
	}
	@Override
	public Set<String> zrevrange(String key, int start, int stop) {
		return redisTemplate.opsForZSet().reverseRange(key, start, stop);
	}
	@Override
	public Set<String> smembers(String key) {
		return redisTemplate.opsForSet().members(key);
	}
	@Override
	public Long sadd(String key, String member) {
		String[] members = {member};
		return redisTemplate.opsForSet().add(key, members);
	}
	@Override
	public Long sadd(byte[] key, byte[] member) {
		return redisTemplate.opsForSet().add(new String(key),new String(member));
	}
	
	public long incrBy(final byte[] key,final long size) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.incrBy(key, size);
			}
		});
	}
	@Override
	public Long incrBy(String key, long size) {
		return incrBy(key.getBytes(),size);
	}
	@Override
	public Long decrBy(String key, long size) {
		return decrBy(key.getBytes(),size);
	}
	public Long decrBy(final byte[] key,final long size) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.decrBy(key, size);
			}
		});
	}
}
