package com.huangjq.db.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hjq.util.FileUtil;
import com.hjq.util.StringUtil;
import com.hjq.util.Util;
import com.hjq.util.bean.BeanUtil;
import com.hjq.util.prop.Prop;
import com.hjq.utils.DateUtil;
import com.huangjq.hr.commons.BaseDao;
import com.huangjq.hr.commons.pojo.StWhere;
import com.huangjq.hr.models.SplitTable;

import net.sf.json.JSONObject;
import net.sf.jsqlparser.JSQLParserException;


/***
 * 通用分页类，
 * @author hjq
 *
 */
public class SplitTableUtils {
	private static BaseDao dao = BaseDao.getDao();
	class DbType{
		public final static String ORACLE="oracle";
		public final static String MSSQL="sqlserver";
		public final static String MYSQL="mysql";
	}
	/**
	 * 将普通sql转oracle分页语句
	 * @param sql
	 * @param start
	 * @param end
	 * @return
	 */
	public static String genOracleSplitSql(String sql,String start,String end){
		return genOracleSplitSql(sql, Util.getIntValue(start), Util.getIntValue(end));
	}
	public static String genOracleSplitSql(String sql,int start,int end){
		StringBuffer br = new StringBuffer();
		br.append("select * from (select rownum rm,a.* from (").append(sql).append(")  a) where rm between ").append(start).append(" and ").append(end);
		return br.toString();
	}
	
	/**
	 * 将普通sql转Mysql分页语句
	 * @param sql
	 * @param start
	 * @param end
	 * @return
	 */
	public static String genMysqlSplitSql(String sql,String start,String end){
		return genMysqlSplitSql(sql, Util.getIntValue(start), Util.getIntValue(end));
	}
	public static String genMysqlSplitSql(String sql,int start,int end){
		start = start - 1;
		end = end - start;
		StringBuffer br = new StringBuffer();
		br.append(sql).append(" limit ").append(start).append(",").append(end);
		return br.toString();
	}
	/**
	 * 将普通sql转Sqlserver分页语句
	 * @param sql
	 * @param start
	 * @param end
	 * @return
	 */
	public static String genSqlserverSplitSql(String sql,String start,String end){
		return genMysqlSplitSql(sql, Util.getIntValue(start), Util.getIntValue(end));
	}
	public static String genSqlserverSplitSql(String sql,int start,int end){
		start = start - 1;
		end = end - start;
		return new SqlServerParse().convertToPageSql(sql, start, end).toLowerCase();
	}
	
	/**
	 * 获取sql语句总条数
	 * @param sql
	 * @return
	 */
	public static String genCountSql(String sql){
		sql = "select count(1) as count from ("+sql+") a";
		return sql;
	}
	
	/**
	 * 获取总记录数
	 * @param sql
	 * @return
	 */
	public static Long getCount(String sql){
		return (Long) dao.queryForObject(genCountSql(sql));
	}
	
	/**
	 * 返回分页后集合
	 * @param sql
	 * @param start
	 * @param end
	 * @param dbType (sqlserver、mysql、oracle)
	 * @return
	 */
	public static List<Map<String,Object>> getPageList(String sql,int start,int end,String dbType){
		sql = genMysqlSplitSql(sql, start, end);
		return dao.queryForList(sql);			
	}
	
	/**
	 * 为sql增加 where
	 * @param sql
	 * @return
	 */
	public static String addWhere(String sql,String where){
		if(StringUtil.isEmpty(sql)) return sql;
		if(sql.indexOf("where") == -1){ //不存在 where
			sql += " where "+where;
		}else{
			sql += " and "+where;
		}
		return sql;
	}
	public static String addWhere(String sql,List<StWhere> stWheres){
		return addWhere(sql,stWhere2Str(stWheres));
	}
	public static String stWhere2Str(List<StWhere> stWheres){
		StringBuffer where = new StringBuffer();
		for (int i = 0;i < stWheres.size();i++) {
			StWhere stWhere =stWheres.get(i);
			
			where.append(stWhere.getName())
				.append(" ")
				.append(stWhere.getOp());
			if(stWhere.getOp().equals("like")){
				where.append(" '%").append(stWhere.getValue()).append("%' ");
			}else{
				where.append("'").append(stWhere.getValue()).append("' ");				
			}
			
			if(i != stWheres.size() -1) where .append(" and ");
		}
		return where.toString();
	}
	
	
	/**
	 * 获取页面 
	 * @param sql
	 * @param dbType 数据库类型(oracle、mysql、sqlserver)
	 * @param page 第几页
	 * @param rows 每页条数
	 * @return 如，{"records":20,rows:[{id:1,name:""},{id:2,name:""}]}
	 */
	public static String getPage(String sql,String dbType,int page,int rows){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("records", getCount(sql)); //总记录数
		
		int end = page * rows;
		int start = end - rows + 1;
		resultMap.put("rows", getPageList(sql, start, end, dbType));
		return JSONObject.fromObject(resultMap).toString();
	}
	
	public static String getPage(String sql,String dbType,int page,int rows,Map<String,String> dateFormatMap){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("records", getCount(sql)); //总记录数
		
		int end = page * rows;
		int start = end - rows + 1;
		List<Map<String, Object>> dataList = getPageList(sql, start, end, dbType);
		List<Map<String, Object>> dataList2 = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : dataList) {
			if(dateFormatMap != null){
				
				for(String key: dateFormatMap.keySet()){
					Object value = map.get(key);
					String formate = dateFormatMap.get(key);
					if(value != null){
						value = DateUtil.date2Str(formate, (Date)value);
						map.put(key, value);
					}
				}				
			}
			dataList2.add(map);
		}
		resultMap.put("rows", dataList2);
		return JSONObject.fromObject(resultMap).toString();
	}
	
	public static List<Map<String,Object>> getDataList(String sql,String dbType,int page,int rows){
		int end = page * rows;
		int start = end - rows + 1;
		return getPageList(sql, start, end, dbType);
	}
	
	@SuppressWarnings("unchecked")
	public static List<SplitTable> getSplitTables(){
		File file = new File(Prop.getRootPath()+"/WEB-INF/config/services/split-table.json");
		String jsonStr = FileUtil.loadFile(file, "UTF-8");
		
		List<Map<String, Object>> values = (List<Map<String, Object>>) com.alibaba.fastjson.JSONArray.parse(jsonStr);
		
		List<SplitTable> splitTables = new ArrayList<SplitTable>();
		
		for (Map<String, Object> map : values) {
			SplitTable splitTable = new SplitTable();
			BeanUtil.transMap2Bean(map, splitTable);
			splitTables.add(splitTable);
		}
		
		return splitTables;
	}
	
	public static SplitTable getSplitTable(String id){
		SplitTable browMain = new SplitTable();
		List<SplitTable> browMains = getSplitTables();
		for (SplitTable browMain2 : browMains) {
			if((browMain2.getId()+"").equals(id)){
				browMain = browMain2;
				break;
			}
		}
		return browMain;
	}
	
	public static void main(String[] args) throws JSQLParserException {
		String sql = "select workflow_currentoperator.workflowtype,workflow_currentoperator.workflowid,  currentnodetype, count(distinct workflow_requestbase.requestid) workflowcount from workflow_currentoperator,workflow_requestbase  where workflow_requestbase.requestid=workflow_currentoperator.requestid and workflow_currentoperator.workflowtype>1  and exists (select 1 from hrmresource where id=workflow_currentoperator.userid and hrmresource.status in (0,1,2,3)) group by workflow_currentoperator.workflowtype, workflow_currentoperator.workflowid,currentnodetype  order by workflow_currentoperator.workflowtype, workflow_currentoperator.workflowid";
		//sql = genMysqlSplitSql(sql,21,40);
		//sql = genSplitSql(sql);
		//sql = genCountSql(sql);
		sql = genSqlserverSplitSql(sql, 4, 5);
		System.out.println(sql);
		
	}
}
