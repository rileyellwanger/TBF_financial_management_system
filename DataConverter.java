package com.tbf;

import java.util.List;

import com.objects.Asset;
import com.objects.Person;

public class DataConverter {
	
	public static void main(String args[]) {
		
		List<Person> persons = FileReader.parsePersonFile();
		List<Asset> assets = FileReader.parseAssetFile();
		
		XMLConverter.convertPersons(persons);
		XMLConverter.convertAssets(assets);
		
	}
 
}
