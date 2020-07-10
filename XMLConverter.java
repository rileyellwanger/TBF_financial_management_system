package com.tbf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.objects.Address;
import com.objects.Asset;
import com.objects.Broker;
import com.objects.DepositAccount;
import com.objects.Investment;
import com.objects.Person;
import com.objects.Portfolio;
import com.objects.Private;
import com.objects.Stock;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XMLConverter {
	/**
	 * This method, given a List of Person objects, creates an XML file titled "data/Persons.xml", 
	 * converts and writes the information to Persons.xml.
	 * 
	 * @param persons
	 */
	public static void convertPersons(List<Person> persons) {
		XStream xstream = new XStream();
		xstream.alias("address", Address.class);
		xstream.alias("person", Person.class);
		xstream.alias("broker", Broker.class);
		
		List<String> personsXml = new ArrayList<String>();
		for(Person p : persons) {
			String xml = xstream.toXML(p);
			personsXml.add(xml);
		}
		
		try {
			File personOutput = new File("data/Persons.xml");
			PrintWriter pw = new PrintWriter (personOutput);
			for(String s : personsXml) {
				pw.print(s);
			}
			pw.close();
		} catch (FileNotFoundException fnfe){
			throw new RuntimeException(fnfe);
		}
	}
	
	/**
	 * This method, given a List of 'Asset' objects, creates an XML file titled "data/Assets.xml", 
	 * converts and writes the information to Assets.xml.
	 * @param assets
	 */
	public static void convertAssets(List<Asset> assets) {
		XStream xstream = new XStream(new StaxDriver());
		
		xstream.alias("asset", Asset.class);
		xstream.alias("deposit account", DepositAccount.class);
		xstream.alias("invsetment", Investment.class);
		xstream.alias("portfolio", Portfolio.class);
		xstream.alias("private", Private.class);
		xstream.alias("stock", Stock.class);
		
		List<String> assetsXml = new ArrayList<String>();
		for(Asset a : assets) {
			String xml = xstream.toXML(a);
			assetsXml.add(xml);
		}
		
		try {
			File assetOutput = new File("data/Assets.xml");
			PrintWriter aw = new PrintWriter(assetOutput);
			for(String s : assetsXml) {
				aw.print(s);
			}
			aw.close();
		} catch (FileNotFoundException fnfe){
			throw new RuntimeException(fnfe);
		}
	}
	
}
