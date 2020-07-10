package com.tbf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailDbUtils {
	/**
	 * Accesses my database and returns a list of e-mails for the given person's ID code. 
	 * @param personCode
	 * @return
	 */
	public static List<String> getEmails(String personCode) {
		List<String> emails = new ArrayList<>();
		Connection conn = DatabaseUtils.getConnection();
		String query = "select emailAddress from Email e join Person p on e.personId = p.personId where " +
					   "personCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String s = rs.getString("emailAddress");
				emails.add(s);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} 
		DatabaseUtils.close(conn, ps, rs);
		return emails;
	}
	
	/**
	 * This method has same functionality as the above, it returns a list of e-mails for the given person's 
	 * ID code. However, it is given a Connection and does not close it, so it can continue to be used within
	 *  another "get" method. 
	 * @param personCode
	 * @param conn
	 * @return
	 */
	public static List<String> getEmails(String personCode, Connection conn) {
		List<String> emails = new ArrayList<>();
		String query = "select emailAddress from Email e join Person p on e.personId = p.personId where " +
					   "personCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String s = rs.getString("emailAddress");
				emails.add(s);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} 
		DatabaseUtils.close(ps, rs);
		return emails;
	}
}
