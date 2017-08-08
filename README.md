# sql-utils
java实现，数据库操作的封装。

1、普通sql查询转分页sql查询语句
调用方法：

SplitTableUtils.genOracleSplitSql("select * from users","1","10"); //oracle分页
SplitTableUtils.genMysqlSplitSql("select * from users","1","10"); //mysql分页
SplitTableUtils.genSqlserverSplitSql("select * from users","1","10"); //sqlserver分页

2、dao层的简单封装

