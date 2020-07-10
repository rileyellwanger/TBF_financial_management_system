package com.tbf;

import java.util.List;

import com.list.MySortedList;
import com.objects.Asset;
import com.objects.Portfolio;

public class ReportPrinter {
	/**
	 * This method prints out a summary report of the given Portfolios. The report includes: portfolio code, 
	 * owner's full name, manager's full name, fees, commission, weighted risk, annual return, and total 
	 * value.
	 * @param portfolios
	 */
	public static void printPortfolioSummary(MySortedList<Portfolio> portfolios) {
		System.out.println("Portfolio Summary Report");
		System.out.println("===============================================================================================================================\r\n");
		System.out.printf("%-11s| %-21s| %-21s| %15s| %13s| %17s| %15s| %13s\n", 
						  "Portfolio", "Owner", "Manager", "Fees  ", "Commission  ", "Weighted Risk  ", 
						  "Return  ", "Total");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
		double totalFees = 0.0;
		double totalCommission = 0.0;
		double totalReturn = 0.0;
		double totalValue = 0.0;
		
		for(Portfolio p : portfolios) {
			System.out.printf("%-11s| %-21s| %-21s| $%12.2f  |$%10.2f   |%15.4f   |$%14.2f |$%13.2f\n", 
					p.getCode(), p.getOwner().getFullName(), p.getBroker().getFullName(), p.getAnnualFees(),
					p.getAnnualCommission(), p.getAgregateRisk(), p.getTotalAnnualReturn(), p.getPortfolioValue());
			
			totalFees += p.getAnnualFees();
			totalCommission += p.getAnnualCommission();
			totalReturn += p.getTotalAnnualReturn();
			totalValue += p.getPortfolioValue();
		}
		System.out.println("                                                         -------------------------------------------------------------------------------------");
		System.out.printf("%57s| $%12.2f  |$%10.2f   |%15s   |$%14.2f |$%13.2f\n", "Totals", totalFees, 
						  totalCommission, "", totalReturn, totalValue);
	}
	/**
	 * This method prints detailed information from the given Portfolio, including: Portfolio code, 
	 * owner's name, owner's address, owner's email(s), manager's name, beneficiary's name (optional), 
	 * benefitiary's address, and beneficiary's email(s).
	 * @param p
	 */
	public static void printPortfolioDetails(Portfolio p) {
		System.out.println("Portfolio " + p.getCode());
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("Owner:");
		System.out.println(p.getOwner().getFullName());
		System.out.println(p.getOwner().getEmails());
		System.out.println(p.getOwner().getAddress().getFullAddress());
		System.out.println("------------------------------------------");
		System.out.println("Manager:");
		System.out.println(p.getBroker().getFullName());
		System.out.println("------------------------------------------");
		System.out.println("Benefeciary:");
		if(p.getBeneficiary().getCode().contentEquals("empty")) {
			System.out.println("none");
		} else {
			System.out.println(p.getBeneficiary().getFullName());
			System.out.println(p.getBeneficiary().getEmails());
			System.out.println(p.getBeneficiary().getAddress().getFullAddress());
		}
		
	}
	/**
	 * This method prints detailed information of each asset in the given portfolio, including: code, 
	 *  label, return rate, risk, annual return, and total value.
	 * @param p
	 */
	public static void printAssetDetails(Portfolio p) {
		System.out.println("Assets");
		System.out.printf("%-11s |%-32s |%11s |%14s  %13s  %13s|\n", 
						  "Code", "Asset", "Return Rate", "Risk|", "Annual Return|", "Value");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
		List<Asset> assets = p.getAssets();
		double totalRisk = 0.0;
		double totalReturn = 0.0;
		double totalValue = 0.0;
		for(Asset a : assets) {
			System.out.printf("%-11s |%-32s |%11.2f |%13.2f|$%14.2f|$%14.2f|\n", 
							  a.getCode(), a.getLabel(), a.getAnnualRateOfReturn() * 100.0, a.getRisk(), 
							  a.getExpectedAnnualReturn(), a.getAssetValue());
			totalRisk += a.getRisk();
			totalReturn += a.getExpectedAnnualReturn();
			totalValue += a.getAssetValue();
		}
		System.out.printf("%107s", "------------------------------------------------------------\n");
		System.out.printf("%58s | %12.3f|$%14.2f|$%14.2f|\n", "Totals", totalRisk, totalReturn, totalValue);
	}
	/**
	 * This method prints out a full Details Report of the given Portfolios. For more information see 
	 * printPortfolioDetails() and printAssetDetails() (above).
	 * @param portfolios
	 */
	public static void printPortfolioDetailsReport(List<Portfolio> portfolios) {
		System.out.println("Portfolio Details Report");
		System.out.println("===============================================================================================================================\r\n");
		for(Portfolio p : portfolios) {
			printPortfolioDetails(p);
			System.out.println("------------------------------------------");
			printAssetDetails(p);
			System.out.printf("\n\n");
		}
	}
}
