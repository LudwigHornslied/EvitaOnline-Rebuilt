package com.tistory.hornslied.evitaonline.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Shop {

	private String name;
	private String title;
	
	private HashSet<Player> openedPlayers;
	private HashMap<Integer, Goods> goods;
	
	public Shop(String name) {
		this.name = name;
		
		openedPlayers = new HashSet<>();
		goods = new HashMap<>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void openShop(Player player) {
		Inventory gui = Bukkit.createInventory(null, 54, title);
		
		for(int index : goods.keySet()) {
			Goods arg = goods.get(index);
			ItemStack loreGoods = arg.getItem().clone();
			
			List<String> lore;
			if(loreGoods.hasItemMeta() && loreGoods.getItemMeta().hasLore()) {
				lore = loreGoods.getItemMeta().getLore();
			} else {
				lore = new ArrayList<>();
			}
			
			switch(arg.getType()) {
			case BOTH:
				break;
			case BUY:
				break;
			case SELL:
				break;
			}
			
			gui.setItem(index, loreGoods);
		}
	}
}
