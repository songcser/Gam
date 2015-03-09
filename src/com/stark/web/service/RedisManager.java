package com.stark.web.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisManager implements IRedisManager{

    @Autowired  
    private RedisTemplate<Serializable, Serializable> redisTemplate;  
    
    
    
    public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
        return redisTemplate;
    }



    public void setRedisTemplate(
    	RedisTemplate<Serializable, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }



    @Override
    public void save() {
	redisTemplate.execute(new RedisCallback<Object>() {  
	        @Override  
	        public Object doInRedis(RedisConnection connection)  
	                throws DataAccessException {  
	            connection.set(  
	                    redisTemplate.getStringSerializer().serialize(  
	                            "user.uid." +"0"),  
	                    redisTemplate.getStringSerializer().serialize(  
	                            "stark pet"));  
	            return null;  
	        }  
	    });  
    
    }

}
