package com;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

class IndivisualPair {
	float price;
	String item;

	public IndivisualPair(float price, String item) {
		this.price = price;
		this.item = item;
	}
}

// Pair of price and Items (ItemList in case of combo)
public class ImprovedChallenge {

	public static void main(String[] args) throws IOException {
		while (true) {
			// Scanner for input of items
			Scanner sc = new Scanner(System.in);
			// Result res for storing the final result
			float res = Float.MAX_VALUE;
			int shopid = 0;

			// Map of ShopID and Items(Pair of price and item)
			Map<Integer, ArrayList<Pair>> shops = new HashMap<Integer, ArrayList<Pair>>();

			// CSV reader using opencsv-2.3.jar jar
			CSVReader reader = new CSVReader(new FileReader("newData.csv"),
					',', '"', 0);

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
			List<String> products = new ArrayList<String>(Arrays.asList(input))
					.subList(2, input.length);
			;

			// Iterate every shops for product
			for (Map.Entry<Integer, ArrayList<Pair>> shop : shops.entrySet()) {
				// create temporary SET tset i.e. set of products
				ArrayList<IndivisualPair> itemList = new ArrayList<IndivisualPair>();
				Set<String> ProductList = new HashSet<String>(products);
				float tres = 0;
				// Iterate every pair of List(pair of price and itemList(it may
				// be single or combo))
				for (Pair pl : shop.getValue()) {
					Set<String> tlist = new HashSet<String>(pl.list);
					// System.out.println(pl.list);
					// System.out.println(tlist);
					tlist.retainAll(ProductList);
					int n = tlist.size();
					if (n > 0) {
						for (String s : tlist) {
							itemList.add(new IndivisualPair(pl.price / n, s));
						}
					}
				}
				if (itemList.size() >= products.size()) {
					// Sort itemList on price in ascending order.
					Collections.sort(itemList,
							new Comparator<IndivisualPair>() {
								public int compare(IndivisualPair s1,
										IndivisualPair s2) {
									if (s1.price > s2.price)
										return 1;
									if (s1.price < s2.price)
										return -1;
									else
										return 0;

								}

							});

					for (int i = 0; i < itemList.size(); i++) {
						if (ProductList.contains(itemList.get(i).item)) {
							tres += itemList.get(i).price;
							ProductList.remove(itemList.get(i).item);
						}
						if (ProductList.size() == 0)
							break;
					}
					// Compare the tres with res for min price
					if (tres < res && ProductList.size() == 0) {
						res = tres;
						shopid = shop.getKey();
						flag = true;
					}
				}

			}
			if (flag)
				System.out.println(shopid + ", " + res);
			else
				System.out.println("none");
		}
	}
}