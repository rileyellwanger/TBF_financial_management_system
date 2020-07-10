package com.tbf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.objects.Asset;
import com.objects.Person;

public class Mapper {
	/**
	 * This method takes the given List of Persons and returns a Map that maps each Person code to the 
	 * matching Person object.
	 * @param people
	 * @return
	 */
	public static Map<String, Person> mapPersons(List<Person> people) {
		Map<String, Person> personMap = new HashMap<>();
		for(Person p : people) {
			personMap.put(p.getCode(), p);
		}
		return personMap;
	}
	/**
	 * This method takes the given List of Assets and returns a Map that maps each Asset code to the 
	 * matching Asset object. 
	 * @param assets
	 * @return
	 */
	public static Map<String, Asset> mapAssets(List<Asset> assets) {
		Map<String, Asset> assetMap = new HashMap<>(); 
		for(Asset a : assets) {
			assetMap.put(a.getCode(), a);
		}
		return assetMap;
	}
}
