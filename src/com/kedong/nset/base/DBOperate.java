package com.kedong.nset.base;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * 数据库操作类，包含数据库连接，断开连接，查询与增删改、提交操作
 *
 */
public class DBOperate {

	private Connection connection = null;
	private PreparedStatement ps = null;
	private ResultSet resultSet = null;
	private ResultSetMetaData metaData = null;
	private boolean isAutoCommit = false;
	private boolean isShowSQL = true;
	public Statement smt = null;

	Logger logger = Logger.getLogger(DBOperate.class);

	/**
	 * 获取连接信息
	 * @return
	 * Connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * 获取executeQuery方法的结果集
	 * @return
	 * Vector<Vector<Object>>类型的数据库查询结果集
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * 获取executeQuery方法的结果集对象中列的类型和属性信息的对象
	 * @return
	 * executeQuery方法的结果集的ResultSetMetaData类型的对象
	 */
	public ResultSetMetaData getMetaData() {
		return metaData;
	}

	/**
	 * 获取自动提交状态
	 * @return
	 * 如果自动提交则返回true，否则返回false
	 */
	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	/**
	 * 设置自动提交状态
	 * @param isAutoCommit
	 * boolean型参数，true设置为自动提交，false设置为不自动提交
	 */
	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

	/**
	 * 获取SQL执行语句打印状态
	 * @return
	 * 如果打印则返回true，否则返回false
	 */
	public boolean isShowSQL() {
		return isShowSQL;
	}

	/**
	 * 设置SQL执行语句打印状态
	 * @param isShowSQL
	 * boolean型参数，true设置为打印，false设置为不打印
	 */
	public void setShowSQL(boolean isShowSQL) {
		this.isShowSQL = isShowSQL;
	}

	/**
	 * 初始化一个DBOperate对象
	 */
	public DBOperate() {}

	/**
	 * 初始化一个DBOperate对象，并设置是否自动提交和是否打印SQL执行信息
	 * @param isAutoCommit
	 * 设置是否自动提交
	 * @param isShowSQL
	 * 设置是否打印SQL执行信息
	 */
	public DBOperate(boolean isAutoCommit, boolean isShowSQL) {
		this.isAutoCommit = isAutoCommit;
		this.isShowSQL = isShowSQL;
	}

	/**
	 * 数据库回滚操作，撤销对数据库的更新操作，当是否提交为false时有效
	 * @return
	 * 操作成功则返回true，否则抛出异常
	 * @throws SQLException
	 */
	public boolean rollback() throws SQLException {
		connection.rollback();
		return true;
	}

	/**
	 * 数据库提交操作，提交对数据库的更新操作，当是否提交为false时有效
	 * @return
	 * 操作成功则返回true，否则抛出异常
	 * @throws SQLException
	 */
	public boolean commit() throws SQLException {
		connection.commit();
		return true;
	}

	/**
	 * 数据库连接操作，根据配置文件中的database.driver，database.url，database.userid，database.password信息进行连接数据库
	 * @return
	 * 操作成功则返回true，否则抛出异常
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean connect(String dbname) throws ClassNotFoundException, SQLException {
		String driver = Env.getInstance().getProperty("database." + dbname + ".driver");
		String url = Env.getInstance().getProperty("database." + dbname + ".url");
		String userid = Env.getInstance().getProperty("database." + dbname + ".userid");
		String password = Env.getInstance().getProperty("database." + dbname + ".password");

		Class.forName(driver);
		connection = DriverManager.getConnection(url, userid, password);
		connection.setAutoCommit(isAutoCommit);

		return true;
	}

	/**
	 * 数据库连接操作，根据参数中的信息进行连接数据库
	 * @param driver
	 * String类型的数据库驱动描述
	 * @param url
	 * String类型的数据库连接地址描述
	 * @param userid
	 * String类型的数据库用户ID描述
	 * @param password
	 * String类型的数据库用户ID相应的登陆口令描述
	 * @return
	 * 操作成功则返回true，否则抛出异常
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean connect(String driver, String url, String userid, String password) throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		connection = DriverManager.getConnection(url, userid, password);
		connection.setAutoCommit(isAutoCommit);

		return true;
	}

	/**
	 * 数据库连接关闭操作
	 * @return
	 * 操作成功则返回true，否则抛出异常
	 * @throws SQLException
	 */
	public boolean close() throws SQLException {
		if (connection != null)
			connection.close();
		if (ps != null)
			ps.close();
		if (resultSet != null)
			resultSet.close();
		System.gc();
		return true;
	}

	/**
	 * 数据库批量更新操作
	 * @param sql
	 * String类型的带参数的SQL语句
	 * @param data
	 * Vector<Vector<Object>>类型的SQL参数
	 * @return
	 * 包含批中每个命令的一个元素的更新计数所组成的数组。数组的元素根据将命令添加到批中的顺序排序。 
	 * @throws SQLException
	 */
	public int [] executeBatchUpdate(String sql, Vector<Vector<Object>> data) throws SQLException {
		if (isShowSQL == true) logger.info(sql);

		ps = connection.prepareStatement(sql);
		for (int i = 0; i < data.size(); i++) {
			Vector<Object> tmpV = data.get(i);
			for (int j = 0; j < tmpV.size(); j++) {
				int index = j + 1;
				Object obj = tmpV.get(j);
				ps.setObject(index, obj);
			}
			ps.addBatch();
		}
		int [] executeUpdateRows = ps.executeBatch();

		return executeUpdateRows;
	}

	/**
	 * 数据库查询操作
	 * @param sql
	 * String类型的SQL查询语句
	 * @return
	 * 成功则返回Vector<Vector<Object>>类型的结果集，否则抛出异常
	 * @throws SQLException
	 */
	public Vector<Vector<Object>> executeQuery(String sql) throws SQLException {
		if (isShowSQL == true) logger.info(sql);

		Vector<Vector<Object>> resultVector = new Vector<Vector<Object>>();
		ps = connection.prepareStatement(sql);
		resultSet = ps.executeQuery (sql);
		metaData = resultSet.getMetaData();
		int numberOfColumns =  metaData.getColumnCount();

		while (resultSet.next()) {
			Vector<Object> newRow = new Vector<Object>();
			for (int i = 1; i <= numberOfColumns; i++) {
				newRow.addElement(resultSet.getObject(i));
			}
			resultVector.addElement(newRow);
		}

		return resultVector;
	}
	/**
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String,Object>> executeQueryMapList(String sql) throws SQLException {
		if (isShowSQL == true) logger.info(sql);

		List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
		ps = connection.prepareStatement(sql);
		resultSet = ps.executeQuery (sql);
		metaData = resultSet.getMetaData();
		int numberOfColumns =  metaData.getColumnCount();

		while (resultSet.next()) {
			Map<String,Object>oneRow = new HashMap<String,Object>();
			for (int i = 1; i <= numberOfColumns; i++) {
				String columnName =resultSet.getMetaData().getColumnName(i);
				oneRow.put(columnName, resultSet.getObject(i).toString());
			}
			retList.add(oneRow);
		}

		return retList;
	}
	
	/**
	 * 
	 * @param sql
	 * String类型的SQL查询语句
	 * @param data
	 * Vector<Object>类型的SQL参数
	 * @return
	 * 成功则返回Vector<Vector<Object>>类型的结果集，否则抛出异常
	 * @throws SQLException
	 */
	public Vector<Vector<Object>> executePreparedQuery(String sql, Vector<Object> data) throws SQLException {
		if (isShowSQL == true) logger.info(sql);

		Vector<Vector<Object>> resultVector = new Vector<Vector<Object>>();
		ps = connection.prepareStatement(sql);
		for (int j = 0; j < data.size(); j++) {
			int index = j + 1;
			Object obj = data.get(j);
			ps.setObject(index, obj);
		}
		resultSet = ps.executeQuery ();
		metaData = resultSet.getMetaData();
		int numberOfColumns =  metaData.getColumnCount();

		while (resultSet.next()) {
			Vector<Object> newRow = new Vector<Object>();
			for (int i = 1; i <= numberOfColumns; i++) {
				newRow.addElement(resultSet.getObject(i));
			}
			resultVector.addElement(newRow);
		}
		return resultVector;
	}
	/**
	 * 数据库更新操作
	 * @param sql
	 * String类型的SQL更新语句
	 * @return
	 * 成功则返回操作影响数据行数，否则抛出异常
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException {
		if (isShowSQL == true) logger.info(sql);

		ps = connection.prepareStatement(sql);
		int executeUpdateRowsNum = ps.executeUpdate(sql);
		return executeUpdateRowsNum;
	}
	/**
	 * 数据更新操作
	 * @param sql
	 * String类型的Sql更新语句，其中有参数占位符
	 * @param paras
	 * 传入Sql中的参数
	 * @return
	 * 返回影响的行数
	 * @throws SQLException
	 */
	public int executeUpdate(String sql,Vector<Object> paras) throws SQLException{
		if (isShowSQL == true) logger.info(sql);
		ps = connection.prepareStatement(sql);
		for(int i=0;i<paras.size();i++){
			ps.setObject(i+1, paras.get(i));
		}
		int executeUpdateRowsNum = ps.executeUpdate();
		return executeUpdateRowsNum;
	}
    public ResultSet executeQuery1(String sql) throws SQLException
    {
        ResultSet rs = null;
        rs =  smt.executeQuery(sql);
        return rs;
    }
}