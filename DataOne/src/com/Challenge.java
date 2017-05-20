package com;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

//Pair of price and Items (ItemList in case of combo)
class Pair {
	float price;
	Set<String> list = new HashSet<String>();
}

public class Challenge {

	public static void main(String[] args) throws IOException {
		// Scanner for input of items
		Scanner sc = new Scanner(System.in);
		// Result res for storing the final result
		float res = Float.MAX_VALUE;
		int shopid = 0;

		// Map of ShopID and Items(Pair of price and item)
		Map<Integer, ArrayList<Pair>> shops = new HashMap<Integer, ArrayList<Pair>>();

		// CSV reader using opencsv-2.3.jar jar
		CSVReader reader = new CSVReader(new FileReader("data.csv"), ',', '"',
				0);

		String[] nextLine;
		// Read line by line CSV file
		while ((nextLine = reader.readNext()) != null) {

			// Create pair of price and itemList
			Pair p = new Pair();
			p.price = Float.parseFloat(nextLine[1]);
			for (int i = 2; i < nextLine.length; i++)
				p.list.add(nextLine[i].trim());

			int shopID = Integer.parseInt(nextLine[0]);
			ArrayList<Pair> ItemList = shops.get(shopID);
			if (ItemList == null) {
				ItemList = new ArrayList<Pair>();
			}
			ItemList.add(p);
			// Add ShopID and ItemList to Map(shops)
			shops.put(Integer.parseInt(nextLine[0]), ItemList);
		}

		// list of products, input form user
		String input[] = sc.nextLine().split("\\s+");
		boolean flag = false;
		List<String> products = new ArrayList<String>(Arrays.asList(input));

		// Iterate every shops for product
		for (Map.Entry<Integer, ArrayList<Pair>> shop : shops.entrySet()) {
			// create temporary SET tset i.e. set of products
			Set<String> tset = new HashSet<String>(products.subList(2, products
					.size()));
			float tres = 0;
			// Iterate every pair of List(pair of price and itemList(it may be
			// single or combo))
			for (Pair pl : shop.getValue()) {

				if (tset.removeAll(pl.list)) {
					tres += pl.price;
				}
			}
			// Compare the tres with res for min price
			if (tres < res && tset.size() == 0) {
				res = tres;
				shopid = shop.getKey();
				flag = true;
			}

		}
		if (flag)
			System.out.println(shopid + ", " + res);
		else
			System.out.println("none");
	}
}
