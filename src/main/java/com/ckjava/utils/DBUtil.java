package com.ckjava.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库的一些帮助类
 * @author chen_k
 *
 */
public class DBUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);

	public static void executeMutiSql(Connection conn, String sql) {
		int rowcount = 0;
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			String tempSql = sql.replaceAll(";(\\s*|\t|\r|\n)(?i)insert", "';'insert")
					.replaceAll(";(\\s*|\t|\r|\n)(?i)update", "';'update")
					.replaceAll(";(\\s*|\t|\r|\n)(?i)delete", "';'delete");
			String[] sqls = tempSql.split("\';\'");
			for (String sqlstr : sqls) {
				ps = conn.prepareStatement(sqlstr);
				rowcount = ps.executeUpdate();
				logger.info("执行sql：{},受影响行数：{}", new Object[]{sqlstr, rowcount});
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			logger.error("执行sql出现异常", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("回滚出现异常", e1);
			}
			rowcount = 0;
		} finally {
			closeResource(conn, ps, null);
		}
	}
	
	/**
	 * 关闭数据库相关资源
	 * @param Connection
	 * @param PreparedStatement
	 * @param ResultSet
	 */
	public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("关闭数据库资源 Connection 出现异常", e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				logger.error("关闭数据库资源 PreparedStatement 出现异常", e);
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("关闭数据库资源 ResultSet 出现异常", e);
			}
		}
	}
	
}
