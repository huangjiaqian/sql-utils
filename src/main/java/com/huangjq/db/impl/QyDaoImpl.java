package com.huangjq.db.impl;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.hjq.util.Util;
import com.huangjq.db.IDao;
import com.huangjq.db.commons.NormalUtils;
import com.huangjq.db.pojo.Annon;

public class QyDaoImpl implements IDao{
	
	private boolean autoClose = true; //自动关闭连接
	
	private DataSource ds;
	public QyDaoImpl(DataSource ds){
		this.ds = ds;
	}
	private Connection getConn(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T insert(T t) {
		Annon annon = NormalUtils.getAnnn(t);
		String sql = NormalUtils.insertSql(annon);
		
		Connection conn = getConn();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Map<String, Object> columns = annon.getColumnMap();
			ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			if(columns != null && columns.keySet().size() > 0){
				int i = 0;
				for (String key : columns.keySet()) {
					ps.setObject(i+1, columns.get(key));
					i++;
				}
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs.next()){
				t = (T) fetch(t.getClass(), Util.getIntValue(Util.null2String(rs.getObject(1))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, null, ps ,null, conn);
		}
		return t;
	}

	public <T> T insert(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T insert(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T update(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T update(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T update(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> int delete(T t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public <T> int delete(String sql) {
		// TODO Auto-generated method stub
		return 0;
	}

	public <T> int delete(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return 0;
	}

	public <T> int delete(Class<T> bean, long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	public <T> List<T> queryForBean(String sql, Class<T> bean) {
		return queryForBean(sql, bean, null);
	}

	public <T> List<T> queryForBean(String sql, Class<T> bean, Object[] params) {
		Annon annon = NormalUtils.getAnnn(bean);
		System.out.println(annon);
		Connection conn = getConn();
		List<T> resultList = new ArrayList<T>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			if(params != null && params.length > 0){
				for (int i = 0;i < params.length;i++) {
					Object param = params[i];
					ps.setObject(i+1, param);
				}
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				T t = bean.newInstance();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					Object value = rs.getObject(i + 1);
					String key = rsmd.getColumnLabel(i + 1);
					try{
						String fieldName = annon.getColumn2FieldMap().get(key); //字段名
						if(fieldName != null){
							Field field = bean.getDeclaredField(fieldName);
							if(annon.getColumnMap().containsKey(key) || annon.getIdMap().containsKey(key)){
								
							}else if(annon.getOneMap().containsKey(key)){
								value = fetch(field.getType(), Util.getIntValue(value));
							}
							try{
								if(value != null){
									BeanUtils.copyProperty(t, fieldName, value);									
								}
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
					}catch (Exception e) {}
				}
				resultList.add(t);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, null, ps, null, conn);
		}
		return resultList;
	}

	public <T> List<T> queryForBean(T t, String field) {
		// TODO Auto-generated method stub
		return null;
	}


	public <T> List<T> queryForBean(Class<T> bean, String where) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> queryForBean(Class<T> bean, String field, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T fetch(Class<T> bean, long id) {
		String sql = NormalUtils.selectSql(bean, id);
		List<T> list = queryForBean(sql, bean);
		if(list != null && list.size() > 0) return list.get(0);
		return null;
	}

	public <T> T fetch(Class<T> bean, String field, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> queryForList(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> queryForList(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> queryForMap(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> queryForMap(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object queryObject(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public String queryString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public String queryString(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer queryInt(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer queryInt(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return null;
	}
	private void closeAll(ResultSet rs,Statement stat,PreparedStatement ps,CallableStatement callStat ,Connection conn){
		try {
			if (rs != null) rs.close();
			if (stat != null) stat.close();
			if (ps != null) ps.close();
			if (callStat != null) callStat.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
