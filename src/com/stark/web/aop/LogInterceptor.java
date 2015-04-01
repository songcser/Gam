package com.stark.web.aop;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class LogInterceptor {

	private static final Logger logger = Logger.getLogger(LogInterceptor.class);
	public void before(JoinPoint jp) {
		Object[] args = jp.getArgs();
		String arg = jp.getSignature().getDeclaringTypeName()+"."+jp.getSignature().getName()+"(";
		for(int i=0;i<args.length;i++){
			arg +=args[i]+",";
		}
		if(arg.length()>0)
			arg = arg.substring(0,arg.length()-1);
		arg+=")";
		logger.info(arg);
	}
	
	public void beforeRedisDao(JoinPoint jp){
		Object[] args = jp.getArgs();
		String arg = "Redis: "+jp.getSignature().getName()+" ";
		for(int i=0;i<args.length;i++){
			Object o = args[i];
			if(o instanceof String[]){
				arg += Arrays.toString((String[])o);
			}
			else {
				arg +=o+" ";
			}
			
		}
		logger.info(arg);
	}
	
	public void beforeDao(JoinPoint jp){
		System.out.println("before==>");
		Class<?> clazz = jp.getTarget().getClass();
		try {
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields){
				System.out.println(field.getName());
				String fieldName = field.getName();
				if(fieldName.equals("redisDao")){
					String firstLetter = fieldName.substring(0, 1).toUpperCase();
					String getMethodName = "get" + firstLetter + fieldName.substring(1);
					Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
					Object value = getMethod.invoke(jp.getTarget(), new Object[]{});
					if(value==null){
						System.out.println("dddddddddddddddddddddddddd");
					}
				}
			}
		} catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generaDted catch block
			e.printStackTrace();
		}
	}
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		//Logger log = Logger.getLogger("custom");
		logger.info("===================================================");
		Object[] args = pjp.getArgs();
		String arg = pjp.getSignature().getDeclaringTypeName()+"."+pjp.getSignature().getName()+"(";
		for(int i=0;i<args.length;i++){
			arg +=args[i]+",";
		}
		if(arg.length()>0)
			arg = arg.substring(0,arg.length()-1);
		arg+=")";
		logger.info(arg);
		long startTime=System.currentTimeMillis();
		Object o = pjp.proceed();
		long endTime=System.currentTimeMillis();
		long time = endTime - startTime;
		logger.info("执行时间为："+time+"ms");
		//logger.info("===================================================");
		return o;
	}
	 public void afterThrowing(JoinPoint jp, Throwable  throwable)throws Exception {  
//	        System.out.println("产生异常的方法名称：  " + jp.getSignature().getName());  
//	          
//	        for(Object o:jp.getArgs()){  
//	            System.out.println("方法的参数：   " + o.toString());  
//	        }  
//	          
//	        System.out.println("代理对象：   " + jp.getTarget().getClass().getName());  
//	        System.out.println("抛出的异常:    " + throwable.getMessage()+">>>>>>>"  
//	                + throwable.getCause());  
	        logger.error(throwable.getMessage());
	    }  
}
