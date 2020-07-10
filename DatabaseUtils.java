package com.tbf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseUtils {
	
	/**
	 * This method creates and returns a connection to my cse database. The url, my username, and password are stored in 
	 * another class.
	 * @return
	 */
	public static Connection getConnection() {
		String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(DRIVER_CLASS).getDeclaredConstructor().newInstance();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		java.sql.Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.url, DatabaseInfo.username, DatabaseInfo.password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return conn;
	}
	
	/**
	 * This method checks that the given Connection, PreparedStatement, and ResultSet are not null, and then closes them. 
	 * It is intended for "get" methods that return something.
	 * @param conn
	 * @param ps
	 * @param rs
	 */
	public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if(rs != null && !rs.isClosed()) {
				rs.close();
			}
			if(ps != null && !ps.isClosed()) {
				ps.close();
			}
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method checks that the given Connection and PreparedStatement are not null, and then closes them. It is 
	 * intended for "insert" methods that don't return anything, these are not yet implemented.
	 * @param conn
	 * @param ps
	 */
	public static void close(Connection conn, PreparedStatement ps) {
		try {
			if(ps != null && !ps.isClosed()) {
				ps.close();
			}
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Checks that the given PreparedStatement and ResultSet are not null, and then closes them. It is used in methods
	 * that are given a Connection and can use that same connection after the method is used. 
	 * @param ps
	 * @param rs
	 */
	public static void close(PreparedStatement ps, ResultSet rs) {
		try {
			if(rs != null && !rs.isClosed()) {
				rs.close();
			}
			if(ps != null && !ps.isClosed()) {
				ps.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
