package com.huangjq.db.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String value() default "";// 数据库字段名称
	String defaultVal() default ""; // 默认值
	String label() default ""; // 显示名称
	boolean foreignKey() default false; // 是否外建
}