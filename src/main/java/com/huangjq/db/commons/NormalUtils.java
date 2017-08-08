package com.huangjq.db.commons;

import java.lang.reflect.Field;

import com.hjq.util.Util;
import com.hjq.util.bean.BeanUtil;
import com.huangjq.db.annotation.Column;
import com.huangjq.db.annotation.Id;
import com.huangjq.db.annotation.Many;
import com.huangjq.db.annotation.One;
import com.huangjq.db.annotation.Table;
import com.huangjq.db.pojo.Annon;

public class NormalUtils {
	public static <T> Annon getAnnn(Class<T> clazz){
		return getAnnn(getBean(clazz));
	}
	public static <T> Annon getAnnn(T t){
		Class<? extends Object> clazz = t.getClass();
		Annon annon = new Annon();
		annon.setTable(NormalUtils.getTableName(clazz));
		
		Field[] Fields = clazz.getDeclaredFields();
		for (Field field : Fields) {
			if(field.getAnnotation(Id.class) != null){
				String idName = field.getAnnotation(Id.class).value();
				if(idName == null || idName.equals("")){
					idName = field.getName();
				}
				Object value = null;
				try {
					value = BeanUtil.getProperty(t, field.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				annon.putId(idName, value);
				annon.putColumn2Field(idName, field.getName());
			}
			if(field.getAnnotation(Column.class) != null){
				String columnName = field.getAnnotation(Column.class).value();
				if(columnName == null || columnName.equals("")){
					columnName = field.getName();
				}
				Object value = null;
				try {
					value = BeanUtil.getProperty(t, field.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				annon.putColumn(columnName, value);
				annon.putColumn2Field(columnName, field.getName());
			}
			if(field.getAnnotation(One.class) != null){
				String columnName = field.getAnnotation(One.class).value();
				if(columnName == null || columnName.equals("")){
					columnName = field.getName();
				}
				Object value = null;
				try {
					value = BeanUtil.getProperty(t, field.getName());
				} catch (Exception e) {
				}
				annon.put2One(columnName, value);
				annon.putColumn2Field(columnName, field.getName());
			}
			/*
			if(field.getAnnotation(Many.class) != null){
				String columnName = field.getAnnotation(Many.class).value();
				annon.put2Many(field.getName(), null);
			}
			*/
			
		}
		return annon;
	}
	
	public static <T> String getTableName(Class<T> clazz){
		String name = clazz.getSimpleName();
		if(clazz.getAnnotation(Table.class) != null){
			name = clazz.getAnnotation(Table.class).value();
			if(name == null || name.equals("")) name = clazz.getSimpleName();
		}
		return name;
	}
	public static <T> T getBean(Class<T> bean){
		T t = null;
		try {
			t = bean.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static <T> String selectSql(Class<T> clazz,String table,String where){
		StringBuffer sql = new StringBuffer();
		try {
			sql.append("select * from ").append(table).append(where);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	
	public static <T> String selectSql(Class<T> clazz,long id){
		StringBuffer where = new StringBuffer();
		Annon annon = null;
		try {
			annon = getAnnn(clazz.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		String idfield = "";
		for(String key: annon.getIdMap().keySet()){
			idfield = key;
		}
		if("".equals(idfield)) return "";
		where.append(" where ").append(idfield).append("='").append(id).append("'");
		return selectSql(clazz, annon.getTable(), where.toString());
	}
	
	public static <T> String insertSql(T t) {
		return insertSql(getAnnn(t));
	}
	public static <T> String updateSql(T t) {
		return updateSql(getAnnn(t));
	}
	/**
	 * 获取插入语句sql
	 * @return
	 */
	public static String insertSql(Annon annon) {
		if (annon.getColumnMap() == null) return "";
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ").append(annon.getTable()).append("(");
		StringBuffer colNames = new StringBuffer();
		StringBuffer valNames = new StringBuffer();
		for (String key : annon.getColumnMap().keySet()) {
			colNames.append(key).append(",");
			valNames.append("?,");
		}
		sql.append(colNames.toString().replaceAll("^,*|,*$", "") + ")");
		sql.append(" values(");
		sql.append(valNames.toString().replaceAll("^,*|,*$", "") + ")");
		return sql.toString();
	}

	/**
	 * 获取更新sql
	 * @return
	 */
	public static String updateSql(Annon annon) {
		if (annon.getColumnMap() == null) return "";
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(annon.getTable()).append(" set ");
		
		StringBuffer colNames = new StringBuffer();
		for (String key : annon.getColumnMap().keySet()) {
			colNames.append(key).append("=?,");
		}
		StringBuffer where = new StringBuffer(" where ");
		for (String key : annon.getIdMap().keySet()) {
			where.append(key).append("=?").append(" and ");
		}
		if(annon.getIdMap().keySet().size() < 1){
			where = new StringBuffer("");
		}else{
			where = new StringBuffer(where.substring(0,where.length()-5));
		}
		sql.append(colNames.toString().replaceAll("^,*|,*$", "")).append(where.toString().replaceAll("^,*|,*$", ""));
		return sql.toString();
	}
}
