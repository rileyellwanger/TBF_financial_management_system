package com.tbf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.objects.Address;
import com.objects.Asset;
import com.objects.Broker;
import com.objects.DepositAccount;
import com.objects.Person;
import com.objects.Portfolio;
import com.objects.Private;
import com.objects.Stock;

public class FileReader {
	/**
	 * This method reads in a data file containing information on people in the format: 
	 * ID code;broker type;name;address;email
	 * 
	 * It then creates 'Person' objects and returns a List of Persons.
	 * @return
	 */
	public static List<Person> parsePersonFile() {
		List<Person> people = new ArrayList<Person>();
		File f = new File("data/Persons.dat");
		Scanner s;
		try {
			s = new Scanner(f);
		} catch(FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		int size = Integer.parseInt(s.nextLine());
		for(int i=0; i<size; i++) {
			Person p = null;
			String line = s.nextLine();
			String tokens[] = line.split(";");
			
			String code = tokens[0];
			String nameTokens[] = tokens[2].split(",");
			String lastName = nameTokens[0];
			String firstName = nameTokens[1];
			String addyTokens[] = tokens[3].split(",");
			Address addy = new Address(addyTokens[0], addyTokens[1], addyTokens[2], addyTokens[3], addyTokens[4]);
			List<String> emails = new ArrayList<String>();
			if(tokens.length > 4) {
				String emailTokens[] = tokens[4].split(",");
				for(String e : emailTokens) {
					emails.add(e);
				}
			}
			if(tokens[1].isEmpty()) {
				p = new Person(code, firstName, lastName, addy, emails);
			} else {
				String brokerTokens[] = tokens[1].split(",");
				p = new Broker(code, firstName, lastName, addy, emails, brokerTokens[0], brokerTokens[1]);
			}
			
			people.add(p);
		}
		
		
		s.close();
		return people;
	}
	
	/**
	 * This method reads in a data file containing information on assets in the format: 
	 * For deposit accounts: code; type; label; annual percentage rate
	 * For stocks: code;type;label;quarterlyDividend;baseRateOfReturn;betaMeasure;stockSymbol;sharePrice
	 * For private investments: code;P;label;quarterlyDividend;baseRateOfReturn;baseOmegaMeasure;totalValue
	 * 
	 * It then creates the appropriate 'Asset' object based on the type, and returns a List of Assets.
	 * 
	 * For more information see the design document.
	 * @return
	 */
	public static List<Asset> parseAssetFile() {
		List<Asset> assets = new ArrayList<Asset>();
		File f = new File("data/Assets.dat");
		Scanner s;
		try {
			s = new Scanner(f);
		} catch(FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		int size = Integer.parseInt(s.nextLine());
		for(int i=0; i<size; i++) {
			Asset a = null;
			String line = s.nextLine();
			String tokens[] = line.split(";");
			String code = tokens[0];
			String type = tokens[1];
			String label = tokens[2];
			if(tokens[1].equals("D")) {
				double apr = Double.parseDouble(tokens[3])/100.0;
				a = new DepositAccount(type, code, label, apr);
			} else {
				double quarterlyDividend = Double.parseDouble(tokens[3]);
				double baseRateOfReturn = Double.parseDouble(tokens[4]);
				if(baseRateOfReturn > 1.0) {
					baseRateOfReturn = baseRateOfReturn / 100.0;
				}
				if(tokens[1].equals("S")) {
					double betaMeasure = Double.parseDouble(tokens[5]);
					String stockSymbol = tokens[6];
					double sharePrice = Double.parseDouble(tokens[7]);
					a = new Stock(type, code, label, quarterlyDividend, baseRateOfReturn, betaMeasure, 
							      stockSymbol, sharePrice);
				} else if(tokens[1].equals("P")) {
					double baseOmegaMeasure = Double.parseDouble(tokens[5]);
					double value = Double.parseDouble(tokens[6]);
					a = new Private(type, code, label, quarterlyDividend, baseRateOfReturn, baseOmegaMeasure, value);
				}
			}
			assets.add(a);
		}
		
		
		
		s.close();
		return assets;
	}
	/**
	 * This method reads in a data file containing portfolio information in the format: 
	 * portfolio code; owner code; broker code; benefitiary code; asset information
	 * The asset information is listed in a particular format and is handled differently depending on the
	 * type of asset: 
	 * For deposit accounts: asset code:balance, 
	 * For stocks: asset code:share count,
	 * For private investments: code:percent owned,
	 * 
	 * 
	 * It then creates Portfolios using the information from the data file combined with the given Maps
	 * and returns a List of Portfolios. 
	 * 
	 * For more information see the design document. 
	 * @param personMap, assetMap
	 * @return
	 */
	public static List<Portfolio> parsePortfolioFile(Map<String, Person> personMap, Map<String, Asset> assetMap) {
		List<Portfolio> portfolios = new ArrayList<Portfolio>();
		File f = new File("data/Portfolios.dat");
		Scanner s;
		try {
			s = new Scanner(f);
		} catch(FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		int size = Integer.parseInt(s.nextLine());
		for(int i=0; i<size; i++) {
			Portfolio p = null;
			String line = s.nextLine();
			String tokens[] = line.split(";");
			String portCode = tokens[0];
			Person owner = personMap.get(tokens[1]);
			Broker broker = (Broker) personMap.get(tokens[2]);
			Person benefitiary = null;
			if(tokens.length > 3) {
				if(!tokens[3].isEmpty()) {
					benefitiary = personMap.get(tokens[3]);
				} else {
					benefitiary = new Person();
				}
			} else {
				benefitiary = new Person();
			}
			List<Asset> assets = new ArrayList<Asset>();
			if(tokens.length > 4) {
				String assetTokens[] = tokens[4].split(",");
				for(String n : assetTokens) {
					String assetInfo[] = n.split(":");
					Asset a = assetMap.get(assetInfo[0]);
					double value = Double.parseDouble(assetInfo[1]);
					a.setAssetValue(value);
					assets.add(a);
				}
			}
			p = new Portfolio(portCode, owner, broker, benefitiary, assets);
			
			
			portfolios.add(p);
		}
		
		s.close();
		return portfolios;
	}
	
 
}
