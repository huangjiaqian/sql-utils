package com.huangjq.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Many {
	String value() default "";// 数据库字段名称
	String label() default ""; // 显示名称
	public Class<?> bean();
	public String foreignKey(); //外键
}