package com.huangjq.db;

import java.lang.reflect.Proxy;

import com.huangjq.db.proxy.MethodProxy;


public class Dao {
	public static <T> Object getInstance(Class<T> clazz){
		MethodProxy invocationHandler = new MethodProxy();
		Object newProxyInstance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, invocationHandler);
		return newProxyInstance;
	}
}
