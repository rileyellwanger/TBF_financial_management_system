package com.tbf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.objects.Address;

public class AddressDbUtils {
	/**
	 * Accesses my cse database and returns an 'Address' for the given person's ID code. 
	 * @param personCode
	 * @return
	 */
	public static Address getAddress(String personCode) {
		Address a = null;
		Connection conn = DatabaseUtils.getConnection();
		
		String query = "select a.street, a.city, a.state, a.zipcode, a.country from Address a " + 
					   "join Person p on a.addressId = p.addressId where personCode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zipcode = rs.getString("zipcode");
				String country = rs.getString("country");
				a = new Address(street, city, state, zipcode, country);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		DatabaseUtils.close(conn,ps,rs);
		
		return a;
	}
	
	/**
	 * This method has same functionality as the above, it returns an Address for the given person's 
	 * ID code. However, it is given a Connection and does not close it, so it can continue to be used within
	 * another "get" method. 
	 * @param personCode
	 * @param conn
	 * @return
	 */
	public static Address getAddress(String personCode, Connection conn) {
		Address a = null;
		
		String query = "select a.street, a.city, a.state, a.zipcode, a.country from Address a " + 
					   "join Person p on a.addressId = p.addressId where personCode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zipcode = rs.getString("zipcode");
				String country = rs.getString("country");
				a = new Address(street, city, state, zipcode, country);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		DatabaseUtils.close(ps,rs);
		
		return a;
	}
}
