package com.huangjq.db.pojo;

import java.util.HashMap;
import java.util.Map;

public class Annon {
	private String table; //数据库表名
	private Map<String, Object> columnMap; //字段
	private Map<String, String> column2FieldMap; //字段
	private Map<String, String> field2ColumnMap; //字段
	private Map<String, Object> idMap; //主键
	
	private Map<String, Object> oneMap; //一对一
	private Map<String, Object> manyMap; //一对多
	
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public Map<String, Object> getColumnMap() {
		return columnMap;
	}
	public void setColumnMap(Map<String, Object> columnMap) {
		this.columnMap = columnMap;
	}
	public Map<String, String> getColumn2FieldMap() {
		return column2FieldMap;
	}
	public void setColumn2FieldMap(Map<String, String> column2FieldMap) {
		this.column2FieldMap = column2FieldMap;
	}
	public Map<String, String> getField2ColumnMap() {
		return field2ColumnMap;
	}
	public void setField2ColumnMap(Map<String, String> field2ColumnMap) {
		this.field2ColumnMap = field2ColumnMap;
	}
	public Map<String, Object> getIdMap() {
		return idMap;
	}
	public void setIdMap(Map<String, Object> idMap) {
		this.idMap = idMap;
	}
	public Map<String, Object> getOneMap() {
		return oneMap;
	}
	public void setOneMap(Map<String, Object> oneMap) {
		this.oneMap = oneMap;
	}
	public Map<String, Object> getManyMap() {
		return manyMap;
	}
	public void setManyMap(Map<String, Object> manyMap) {
		this.manyMap = manyMap;
	}
	public void putColumn(String column,Object value){
		if(columnMap == null) columnMap = new HashMap<String,Object>();
		columnMap.put(column, value);
	}
	public void putColumn2Field(String column,String field){
		if(column2FieldMap == null) column2FieldMap = new HashMap<String,String>();
		if(field2ColumnMap == null) field2ColumnMap = new HashMap<String,String>();
		column2FieldMap.put(column, field);
		field2ColumnMap.put(field, column);
	}
	
	public void putId(String column,Object value){
		if(idMap == null) idMap = new HashMap<String,Object>();
		idMap.put(column, value);
	}
	public void put2One(String column,Object value){
		if(oneMap == null) oneMap = new HashMap<String,Object>();
		oneMap.put(column, value);
	}
	public void put2Many(String column,Object value){
		if(manyMap == null) manyMap = new HashMap<String,Object>();
		manyMap.put(column, value);
	}
	@Override
	public String toString() {
		return "Annon [table=" + table + ", columnMap=" + columnMap + ", column2FieldMap=" + column2FieldMap
				+ ", field2ColumnMap=" + field2ColumnMap + ", idMap=" + idMap + ", oneMap=" + oneMap + ", manyMap="
				+ manyMap + "]";
	}
	
}
