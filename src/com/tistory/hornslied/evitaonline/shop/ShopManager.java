package com.tistory.hornslied.evitaonline.shop;

import java.util.HashMap;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class ShopManager {

	private HashMap<String, Shop> shops;
	
	public ShopManager(EvitaOnline plugin) {
		shops = new HashMap<>();
	}
}
