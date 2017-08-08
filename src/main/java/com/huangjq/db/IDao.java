package com.huangjq.db;

import java.util.List;
import java.util.Map;

public interface IDao {
	public <T> T insert(T t);
	public <T> T insert(String sql);
	public <T> T insert(String sql,Object[] params);
	public <T> T update(T t);
	public <T> T update(String sql);
	public <T> T update(String sql,Object[] params);
	public <T> int delete(T t);
	public <T> int delete(String sql);
	public <T> int delete(String sql,Object[] params);
	public <T> int delete(Class<T> bean,long id);
	public <T> List<T> queryForBean(String sql,Class<T> bean);
	public <T> List<T> queryForBean(String sql,Class<T> bean,Object[] params);
	public <T> List<T> queryForBean(T t,String field);
	public <T> List<T> queryForBean(Class<T> bean,String where);
	public <T> List<T> queryForBean(Class<T> bean,String field,String value);
	public <T> T fetch(Class<T> bean,long id);
	public <T> T fetch(Class<T> bean,String field,String value);
	public List<Map<String, Object>> queryForList(String sql);
	public List<Map<String, Object>> queryForList(String sql,Object[] params);
	public Map<String, Object> queryForMap(String sql);
	public Map<String, Object> queryForMap(String sql,Object[] params);
	public Object queryObject(String sql);
	public String queryString(String sql);
	public String queryString(String sql,Object[] params);
	public Integer queryInt(String sql);
	public Integer queryInt(String sql,Object[] params);
}
