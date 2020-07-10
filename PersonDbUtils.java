package com.tbf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.objects.Address;
import com.objects.Broker;
import com.objects.Person;

public class PersonDbUtils {
	/**
	 * Accesses my CSE database and returns a 'Person' associated with the given ID code.
	 * @param code
	 * @return
	 */
	public static Person getPerson(String code) {
		Person p = null;
		Address a = null;
		List<String> emails = new ArrayList<>();
		String type = null;
		Connection conn = DatabaseUtils.getConnection();
		
		emails = EmailDbUtils.getEmails(code, conn);
		a = AddressDbUtils.getAddress(code,conn);
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//Get personType to determine if the given code belongs to a 'Person' or the subclass, 'Broker'.
		String query = "select personType from Person where personCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				type = rs.getString("personType");
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//Create the appropriate object based on the personType.
		if(type.equals("B")) {
			p = getBroker(code, conn);
		} else {
			query = "select firstName, lastName from Person where personCode = ?;";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, code);
				rs = ps.executeQuery();
				if(rs.next()) {
					String firstName = rs.getString("firstName");
					String lastName = rs.getString("lastName");
					p = new Person(code, firstName, lastName, a, emails);
				}
				rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		DatabaseUtils.close(conn,ps,rs);
		return p;
	}

	/**
	 * This method has the same functionality as above, it returns a 'Person' for the given ID code. However,
	 * it is given a Connection and does not close it, so it can continue to be used within another "get" 
	 * method. 
	 * @param code
	 * @param conn
	 * @return
	 */
	public static Person getPerson(String code, Connection conn) {
		Person p = null;
		Address a = null;
		List<String> emails = new ArrayList<>();
		String type = null;
		
		emails = EmailDbUtils.getEmails(code, conn);
		a = AddressDbUtils.getAddress(code,conn);
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "select personType from Person where personCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				type = rs.getString("personType");
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		if(type.equals("B")) {
			p = getBroker(code, conn);
		} else {
			query = "select firstName, lastName from Person where personCode = ?;";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, code);
				rs = ps.executeQuery();
				if(rs.next()) {
					String firstName = rs.getString("firstName");
					String lastName = rs.getString("lastName");
					p = new Person(code, firstName, lastName, a, emails);
				}
				rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		DatabaseUtils.close(ps,rs);
		return p;
	}
	
	/**
	 * Accesses my CSE database and returns a 'Broker' associated with the given ID code. 
	 * @param code
	 * @return
	 */
	public static Broker getBroker(String code) {
		 Broker b = null;
		 List<String> emails = new ArrayList<>();
		 Address a = null;
		 Connection conn = DatabaseUtils.getConnection();
		 emails = EmailDbUtils.getEmails(code,conn);
		 a = AddressDbUtils.getAddress(code,conn);
		 
		 String query = "select firstName, lastName, brokerType, secId from Person where personCode = ?;";
		 PreparedStatement ps = null;
		 ResultSet rs = null;
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, code);
				rs = ps.executeQuery();
				if(rs.next()) {
					String firstName = rs.getString("firstName");
					String lastName = rs.getString("lastName");
					String brokerType = rs.getString("brokerType");
					String secId = rs.getString("secId");
					b = new Broker(code, firstName, lastName, a, emails, brokerType, secId);
				}
				rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		DatabaseUtils.close(conn,ps,rs);
		return b;
	}
	
	/**
	 * This method has the same functionality as the above 'getBroker', however it is given a
	 * Connection, and does not close it so it can continue to be used by another "get" method. 
	 * @param code
	 * @param conn
	 * @return
	 */
	public static Broker getBroker(String code, Connection conn) {
		 Broker b = null;
		 List<String> emails = new ArrayList<>();
		 Address a = null;
		 emails = EmailDbUtils.getEmails(code,conn);
		 a = AddressDbUtils.getAddress(code,conn);
		 
		 String query = "select firstName, lastName, brokerType, secId from Person where personCode = ?;";
		 PreparedStatement ps = null;
		 ResultSet rs = null;
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, code);
				rs = ps.executeQuery();
				if(rs.next()) {
					String firstName = rs.getString("firstName");
					String lastName = rs.getString("lastName");
					String brokerType = rs.getString("brokerType");
					String secId = rs.getString("secId");
					b = new Broker(code, firstName, lastName, a, emails, brokerType, secId);
				}
				rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		DatabaseUtils.close(ps,rs);
		return b;
	}

	/**
	 * Accesses my CSE database and returns a list of all 'Persons' in the database.
	 * @return
	 */
	public static List<Person> getAllPersons() {
		List<Person> people = new ArrayList<Person>();
		List<String> codes = new ArrayList<String>();
		Connection conn = DatabaseUtils.getConnection();
		//get a list of all 'personCodes' in the database.
		String query = "select personCode from Person;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String s = rs.getString("personCode");
				codes.add(s);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//use the getPerson method to create a 'Person' for every 'personCode', and 
		//add it to the returned list of people.
		for(String s : codes) {
			people.add(getPerson(s, conn));
		}
		DatabaseUtils.close(conn,ps,rs);
		return people;
	}
}
