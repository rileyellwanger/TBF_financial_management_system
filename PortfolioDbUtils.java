package com.tbf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.list.MySortedList;
import com.objects.Asset;
import com.objects.Broker;
import com.objects.DepositAccount;
import com.objects.Person;
import com.objects.Portfolio;
import com.objects.Private;
import com.objects.Stock;

public class PortfolioDbUtils {
	/**
	 * Accesses my CSE SQL database and returns a 'Portfolio' associated with the given ID code. 
	 * The maps are passed in so the method knows to create a 'Person' or 'Asset' if they do 
	 * not already exist in the program.
	 * @param code
	 * @param pMap
	 * @param aMap
	 * @return
	 */
	public static Portfolio getPortfolio(String code) {
		Portfolio p = null;
		Person owner = null;
		Broker broker = null;
		Person benefit = null;
		List<Asset> assets = new ArrayList<>();
		Connection conn = DatabaseUtils.getConnection();
		//get ID code for the owner of the portfolio
		String query = "select p.personCode from Person p join Portfolio po on po.ownerId = p.personId where po.portfolioCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String ownerCode = rs.getString("personCode");
				//if the owner is not yet created, create it. 
				owner = PersonDbUtils.getPerson(ownerCode, conn);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//get ID code for the broker/manager of the portfolio
		query = "select p.personCode from Person p join Portfolio po on po.brokerId = p.personId where po.portfolioCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String brokerCode = rs.getString("personCode");
				//if the broker is not yet created, create it. 
				broker = PersonDbUtils.getBroker(brokerCode, conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//get the ID code of the beneficiary of the portfolio, if there is one
		query = "select p.personCode from Person p join Portfolio po on po.beneficiaryId = p.personId where po.portfolioCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String benefitCode = rs.getString("personCode");
				//if the beneficiary is not yet created, create it
				benefit = PersonDbUtils.getPerson(benefitCode, conn);
			} else {
				benefit = new Person();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//get assetCodes associated with the portfolio and the balance/stake/shareCount 
		query = "select a.assetCode, pa.balance, pa.stake, pa.shareCount from Asset a join PortfolioAsset pa on a.assetId = pa.assetId" + 
				"	join Portfolio p on p.portfolioId = pa.portfolioId where p.portfolioCode = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			while(rs.next()) {
				String assetCode = rs.getString("assetCode");
				double balance = rs.getDouble("balance");
				double stake = rs.getDouble("stake");
				int shareCount = rs.getInt("shareCount");
				//if the Asset is not yet created, create it. 
				Asset a = AssetDbUtils.getAsset(assetCode, conn);
				if(a.getType().equals("D")) {
					((DepositAccount) a).setBalance(balance);
				} else if(a.getType().equals("P")) {
					((Private) a).setOwnership(stake);
				} else {
					((Stock) a).setShareCount(shareCount);
				}
				assets.add(a);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if(benefit.getCode().equals("empty")) {
			p = new Portfolio(code, owner, broker, assets);
		} else {
			p = new Portfolio(code,owner,broker,benefit,assets);
		}
		DatabaseUtils.close(conn,ps,rs);
		return p;
	}
	
	/**
	 * This method has the same functionality as the above 'getPortfolio' method, but is given 
	 * a Connection that is left open so it can continue to be used in another 'get' function. 
	 * @param code
	 * @param pMap
	 * @param aMap
	 * @param conn
	 * @return
	 */
	public static Portfolio getPortfolio(String code, Connection conn) {
		Portfolio p = null;
		Person owner = null;
		Broker broker = null;
		Person benefit = null;
		List<Asset> assets = new ArrayList<>();
		//get ID code for the owner of the portfolio
		String query = "select p.personCode from Person p join Portfolio po on po.ownerId = p.personId where po.portfolioCode = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String ownerCode = rs.getString("personCode");
				//if the owner is not yet created, create it. 
				owner = PersonDbUtils.getPerson(ownerCode, conn);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//get ID code for the broker/manager of the portfolio
		query = "select p.personCode from Person p join Portfolio po on po.brokerId = p.personId where po.portfolioCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String brokerCode = rs.getString("personCode");
				//if the broker is not yet created, create it. 
				broker = PersonDbUtils.getBroker(brokerCode, conn);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//get the ID code of the beneficiary of the portfolio, if there is one
		query = "select p.personCode from Person p join Portfolio po on po.beneficiaryId = p.personId where po.portfolioCode = ?;";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			if(rs.next()) {
				String benefitCode = rs.getString("personCode");
				//if the beneficiary is not yet created, create it
				benefit = PersonDbUtils.getPerson(benefitCode, conn);
			} else {
				benefit = new Person();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//get assetCodes associated with the portfolio and the balance/stake/shareCount 
		query = "select a.assetCode, pa.balance, pa.stake, pa.shareCount from Asset a join PortfolioAsset pa on a.assetId = pa.assetId" + 
				"	join Portfolio p on p.portfolioId = pa.portfolioId where p.portfolioCode = ?";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();
			while(rs.next()) {
				String assetCode = rs.getString("assetCode");
				double balance = rs.getDouble("balance");
				double stake = rs.getDouble("stake");
				int shareCount = rs.getInt("shareCount");
				//if the Asset is not yet created, create it. 
				Asset a = AssetDbUtils.getAsset(assetCode, conn);
				if(a.getType().equals("D")) {
					((DepositAccount) a).setBalance(balance);
				} else if(a.getType().equals("P")) {
					((Private) a).setOwnership(stake);
				} else {
					((Stock) a).setShareCount(shareCount);
				}
				assets.add(a);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if(benefit.getCode().equals("empty")) {
			p = new Portfolio(code, owner, broker, assets);
		} else {
			p = new Portfolio(code,owner,broker,benefit,assets);
		}
		DatabaseUtils.close(conn,ps,rs);
		return p;
	}
	
	/**
	 * Accesses my CSE SQL database and returns a List of 'Portfolios'. 
	 * @param pMap
	 * @param aMap
	 * @return
	 */
	public static MySortedList<Portfolio> getAllPortfolios(Comparator<Portfolio> cmp) {
		MySortedList<Portfolio> portfolios = new MySortedList<Portfolio>(cmp);
		List<String> codes = new ArrayList<>();
		Connection conn = DatabaseUtils.getConnection();
		//get all portfolioCodes and store them in a List
		String query = "select portfolioCode from Portfolio;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String code = rs.getString("portfolioCode");
				codes.add(code);
			}
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		//get the Portfolio associated with each code in the database
		for(String s : codes) {
			Portfolio p = getPortfolio(s);
			portfolios.insert(p);
		}
		
		return portfolios;
	}
}
