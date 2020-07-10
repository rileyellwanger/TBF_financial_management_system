package com.tbf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class PortfolioData {

	/**
	 * Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "delete from Email;";
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}	
		query = "delete from Portfolio;";
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}	
		query = "delete from Person;";
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}	
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Removes the person record from the database corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 */
	public static void removePerson(String personCode) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "delete from Email where personId = ?;" + 
				"delete from Portfolio where ownerId = ?;" + 
				"delete from Portfolio where beneficiaryId = ?;" + 
				"delete from Portfolio where brokerId = ?;" +
				"delete from Person where personId = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		query = "delete from Portfolio where ownerId = (select personId from Person where personCode = ?);";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		query = "delete from Portfolio where beneficiaryId = (select personId from Person where personCode = ?);";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		query = "delete from Portfolio where brokerId = (select personId from Person where personCode = ?);";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		query = "delete from Person where personCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Method to add a person record to the database with the provided data. The
	 * <code>brokerType</code> will either be "E" or "J" (Expert or Junior) or 
	 * <code>null</code> if the person is not a broker.
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @param brokerType
	 */
	public static void addPerson(String personCode, 
								 String firstName, 
								 String lastName, 
								 String street, 
								 String city, 
								 String state, 
								 String zip, 
								 String country, 
								 String brokerType, 
								 String secBrokerId) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String addressId = null;
		String query = "insert into Address (street, city, state, zipcode, country) values (?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			ps.setString(5, country);
			ps.executeUpdate();
			ps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			rs = ps.executeQuery();
			rs.next();
			addressId = rs.getString("LAST_INSERT_ID()");
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		query = "insert into Person (personCode, firstName, lastName, brokerType, secId, addressId) values (?,?,?,?,?,?);";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, brokerType);
			ps.setString(5, secBrokerId);
			ps.setString(6, addressId);
			ps.executeUpdate();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "insert into Email(personId, emailAddress) values" +
					   "((select personId from Person where personCode = ?), ?);";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.setString(2, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}

	/**
	 * Removes all asset records from the database
	 */
	public static void removeAllAssets() {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "delete from PortfolioAsset;";
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		query = "delete from Asset;";
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}

	/**
	 * Removes the asset record from the database corresponding to the
	 * provided <code>assetCode</code>
	 * @param assetCode
	 */
	public static void removeAsset(String assetCode) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "delete from PortfolioAsset where assetId = (select assetId from Asset where assetCode = ?);"; 	   
		try { 
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		query = "delete from Asset where assetCode = ?;";
		try { 
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Adds a deposit account asset record to the database with the
	 * provided data. 
	 * @param assetCode
	 * @param label
	 * @param apr
	 */
	public static void addDepositAccount(String assetCode, String label, double apr) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "insert into Asset (assetCode, assetType, label, annualPercentageRate) values (?, ?, ?, ?);";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "D");
			ps.setString(3, label);
			ps.setDouble(4, apr);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Adds a private investment asset record to the database with the
	 * provided data. 
	 * @param assetCode
	 * @param label
	 * @param quarterlyDividend
	 * @param baseRateOfReturn
	 * @param baseOmega
	 * @param totalValue
	 */
	public static void addPrivateInvestment(String assetCode, 
											String label, 
											Double quarterlyDividend, 
											Double baseRateOfReturn, 
											Double baseOmega, 
											Double totalValue) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "insert into Asset (assetCode, assetType, label, quarterlyDividend, baseRateOfReturn, baseOmegaMeasure, totalValue)" + 
					   "values (?, ?, ?, ?, ?, ?, ?);";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "P");
			ps.setString(3, label);
			ps.setDouble(4, quarterlyDividend);
			ps.setDouble(5, baseRateOfReturn);
			ps.setDouble(6, baseOmega);
			ps.setDouble(7, totalValue);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Adds a stock asset record to the database with the
	 * provided data. 
	 * @param assetCode
	 * @param label
	 * @param quarterlyDividend
	 * @param baseRateOfReturn
	 * @param beta
	 * @param stockSymbol
	 * @param sharePrice
	 */
	public static void addStock(String assetCode, 
								String label, 
								Double quarterlyDividend, 
								Double baseRateOfReturn, 
								Double beta, 
								String stockSymbol, 
								Double sharePrice) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "insert into Asset (assetCode, assetType, label, quarterlyDividend, baseRateOfReturn, betaMeasure, stockSymbol, sharePrice) " + 
					   "values (?, ?, ?, ?, ?, ?, ?, ?);";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "S");
			ps.setString(3, label);
			ps.setDouble(4, quarterlyDividend);
			ps.setDouble(5, baseRateOfReturn);
			ps.setDouble(6, beta);
			ps.setString(7, stockSymbol);
			ps.setDouble(8, sharePrice);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}

	/**
	 * Removes all portfolio records from the database
	 */
	public static void removeAllPortfolios() {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "delete from PortfolioAsset;";
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		query = "delete from Portfolio;";
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Removes the portfolio record from the database corresponding to the
	 * provided <code>portfolioCode</code>
	 * @param portfolioCode
	 */
	public static void removePortfolio(String portfolioCode) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "delete from PortfolioAsset where portfolioId = (select portfolioId from Portfolio where portfolioCode = ?);\r\n" + 
					   "delete from Portfolio where portfolioCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, portfolioCode);
			ps.setString(2, portfolioCode);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Adds a portfolio records to the database with the given data.  If the portfolio has no
	 * beneficiary, the <code>beneficiaryCode</code> will be <code>null</code>
	 * @param portfolioCode
	 * @param ownerCode
	 * @param managerCode
	 * @param beneficiaryCode
	 */
	public static void addPortfolio(String portfolioCode, String ownerCode, String managerCode, String beneficiaryCode) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		String query = "insert into Portfolio (portfolioCode, ownerId, brokerId, beneficiaryId) values" + 
					   "(?, (select personId from Person where personCode = ?), (select personId from Person where personCode = ?), (select personId from Person where personCode = ?));";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, portfolioCode);
			ps.setString(2, ownerCode);
			ps.setString(3, managerCode);
			ps.setString(4, beneficiaryCode);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn, ps);
	}
	
	/**
	 * Associates the asset record corresponding to <code>assetCode</code> with the 
	 * portfolio corresponding to the provided <code>portfolioCode</code>.  The third 
	 * parameter, <code>value</code> is interpreted as a <i>balance</i>, <i>number of shares</i>
	 * or <i>stake percentage</i> depending on the type of asset the <code>assetCode</code> is
	 * associated with.
	 * @param portfolioCode
	 * @param assetCode
	 * @param value
	 */
	public static void addAsset(String portfolioCode, String assetCode, double value) {
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String type = null;
		String query = "select assetType from Asset where assetCode = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				type = rs.getString("assetType");
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if(type.equals("D")) {
			query = "insert into PortfolioAsset (portfolioId, assetId, balance) values" +
					"((select portfolioId from Portfolio where portfolioCode = ?), (select assetId from Asset where assetCode = ?), ?);";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, portfolioCode);
				ps.setString(2, assetCode);
				ps.setDouble(3, value);
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else if(type.equals("P")) {
			query = "insert into PortfolioAsset (portfolioId, assetId, stake) values " +
					"((select portfolioId from Portfolio where portfolioCode = ?), (select assetId from Asset where assetCode = ?), ?);";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, portfolioCode);
				ps.setString(2, assetCode);
				ps.setDouble(3, value);
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			query = "insert into PortfolioAsset (portfolioId, assetId, shareCount) values" +
					"((select portfolioId from Portfolio where portfolioCode = ?), (select assetId from Asset where assetCode = ?), ?);";
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, portfolioCode);
				ps.setString(2, assetCode);
				ps.setInt(3, (int)value);
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		DatabaseUtils.close(conn, ps, rs);
	}
	
	
}

