package com.tbf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.objects.Asset;
import com.objects.DepositAccount;
import com.objects.Private;
import com.objects.Stock;

public class AssetDbUtils {
	/**
	 * Accesses my CSE database and returns a 'DepositAccount' associated with the given code.
	 * @param code
	 * @return
	 */
	public static DepositAccount getDepositAccount(String code) {
		DepositAccount d = null;
		Connection conn = DatabaseUtils.getConnection();
		String query = "select label, annualPercentageRate from Asset where assetCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String label = rs.getString("label");
				double apr = rs.getDouble("annualPercentageRate");
				d = new DepositAccount("D", code, label, apr);
			}
			rs.close();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn,ps,rs);
		return d;
	}
	
	/**
	 * This method has the same functionality as the above 'getDepositAccount' method, but is given 
	 * a Connection that is left open so it can continue to be used in another 'get' function. 
	 * @param code
	 * @param conn
	 * @return
	 */
	public static DepositAccount getDepositAccount(String code, Connection conn) {
		DepositAccount d = null;
		String query = "select label, annualPercentageRate from Asset where assetCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String label = rs.getString("label");
				double apr = rs.getDouble("annualPercentageRate");
				d = new DepositAccount("D", code, label, apr);
			}
			rs.close();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(ps,rs);
		return d;
	}
	
	/**
	 * Accesses my CSE database and returns a 'Private' investment associated with the given code. 
	 * @param code
	 * @return
	 */
	public static Private getPrivateInvestment(String code) {
		Private p = null;
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select label, quarterlyDividend, baseRateOfReturn, baseOmegaMeasure, totalValue from Asset where assetCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String label = rs.getString("label");
				double dividend = rs.getDouble("quarterlyDividend");
				double rate = rs.getDouble("baseRateOfReturn");
				double omega = rs.getDouble("baseOmegaMeasure");
				double value = rs.getDouble("totalValue");
				p = new Private("P", code, label, dividend, rate, omega, value);
			}
			rs.close();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn,ps,rs);
		return p;
	}
	
	/**
	 * This method has the same functionality as the above 'getPrivateInvestment' method, but is given 
	 * a Connection that is left open so it can continue to be used in another 'get' function. 
	 * @param code
	 * @param conn
	 * @return
	 */
	public static Private getPrivateInvestment(String code, Connection conn) {
		Private p = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select label, quarterlyDividend, baseRateOfReturn, baseOmegaMeasure, totalValue from Asset where assetCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String label = rs.getString("label");
				double dividend = rs.getDouble("quarterlyDividend");
				double rate = rs.getDouble("baseRateOfReturn");
				double omega = rs.getDouble("baseOmegaMeasure");
				double value = rs.getDouble("totalValue");
				p = new Private("P", code, label, dividend, rate, omega, value);
			}
			rs.close();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(ps,rs);
		return p;
	}
	
	/**
	 * Accesses my CSE database and returns a 'Stock' Investment associated with the given code. 
	 * @param code
	 * @return
	 */
	public static Stock getStock(String code) {
		Stock s = null;
		Connection conn = DatabaseUtils.getConnection();
		String query = "select label, quarterlyDividend, baseRateOfReturn, betaMeasure, stockSymbol, sharePrice from Asset where assetCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String label = rs.getString("label");
				double dividend = rs.getDouble("quarterlyDividend");
				double rate = rs.getDouble("baseRateOfReturn");
				double beta = rs.getDouble("betaMeasure");
				String symbol = rs.getString("stockSymbol");
				double price = rs.getDouble("sharePrice");
				s = new Stock("S", code, label, dividend, rate, beta, symbol, price);
			}
			rs.close();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(conn,ps,rs);
		return s;
	}
	
	/**
	 * This method has the same functionality as the above 'getStock' method, but is given 
	 * a Connection that is left open so it can continue to be used in another 'get' function. 
	 * @param code
	 * @param conn
	 * @return
	 */
	public static Stock getStock(String code, Connection conn) {
		Stock s = null;
		String query = "select label, quarterlyDividend, baseRateOfReturn, betaMeasure, stockSymbol, sharePrice from Asset where assetCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String label = rs.getString("label");
				double dividend = rs.getDouble("quarterlyDividend");
				double rate = rs.getDouble("baseRateOfReturn");
				double beta = rs.getDouble("betaMeasure");
				String symbol = rs.getString("stockSymbol");
				double price = rs.getDouble("sharePrice");
				s = new Stock("S", code, label, dividend, rate, beta, symbol, price);
			}
			rs.close();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		DatabaseUtils.close(ps,rs);
		return s;
	}
	
	/**
	 * Accesses my CSE SQL database and returns an 'Asset' associated with the given code.
	 * @param code
	 * @return
	 */
	public static Asset getAsset(String code) {
		Asset a = null;
		String type = null;
		Connection conn = DatabaseUtils.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		//get assetType (D, P, or S)
		String query = "select assetType from Asset where assetCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				type = rs.getString("assetType");
			}
			rs.close();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		//use the appropriate 'get' method depending on the type of 'Asset'
		if(type.equals("D")) {
			a = getDepositAccount(code, conn);
		} else if(type.equals("P")) {
			a = getPrivateInvestment(code, conn);
		} else if(type.equals("S")){
			a = getStock(code, conn);
		}
		DatabaseUtils.close(conn,ps,rs);
		return a;
	}
	
	/**
	 * This method has the same functionality as the above 'getAsset' method, but is given 
	 * a Connection that is left open so it can continue to be used in another 'get' function. 
	 * @param code
	 * @param conn
	 * @return
	 */
	public static Asset getAsset(String code, Connection conn) {
		Asset a = null;
		String type = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select assetType from Asset where assetCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				type = rs.getString("assetType");
			}
			rs.close();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		if(type.equals("D")) {
			a = getDepositAccount(code, conn);
		} else if(type.equals("P")) {
			a = getPrivateInvestment(code, conn);
		} else if(type.equals("S")){
			a = getStock(code, conn);
		}
		DatabaseUtils.close(ps,rs);
		return a;
	}
	
	/**
	 * Accesses my CSE SQL database and returns a list of all 'Assets' in the database.
	 * @return
	 */
	public static List<Asset> getAllAssets() {
		Connection conn = DatabaseUtils.getConnection();
		List<Asset> assets = new ArrayList<>();
		List<String> codes = new ArrayList<>();
		//get all assetCodes in the database and store them in a List
		String query = "select assetCode as code from Asset;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String s = rs.getString("code");
				codes.add(s);
			}
			rs.close();
		}catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//Create an 'Asset' for every record and add it to returned List
		for(String s : codes) {
			assets.add(getAsset(s, conn));
		}
		
		DatabaseUtils.close(conn,ps,rs);
		return assets;
	}
}
