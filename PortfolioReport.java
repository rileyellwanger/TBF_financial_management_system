package com.tbf;


import com.list.MySortedList;
import com.objects.Portfolio;

public class PortfolioReport {
	public static void main(String args[]) {
		MySortedList<Portfolio> portfoliosByOwner = PortfolioDbUtils.getAllPortfolios(Portfolio.cmpByOwnerName);
		System.out.println("Sorted by Owner Name");
		ReportPrinter.printPortfolioSummary(portfoliosByOwner);
		MySortedList<Portfolio> portfoliosByValue = PortfolioDbUtils.getAllPortfolios(Portfolio.cmpByValueDesc);
		System.out.println("Sorted by Portfolio Value");
		ReportPrinter.printPortfolioSummary(portfoliosByValue);
		MySortedList<Portfolio> portfoliosByBroker = PortfolioDbUtils.getAllPortfolios(Portfolio.cmpByBroker);
		System.out.println("Sorted by Broker");
		ReportPrinter.printPortfolioSummary(portfoliosByBroker);
		
	}
}
