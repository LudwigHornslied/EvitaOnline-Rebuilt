package com.tistory.hornslied.evitaonline.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.NumberUtil;
import com.tistory.hornslied.evitaonline.shop.Goods.GoodsType;

public class Shop {
	
	private static PurchasePage purchasePage;

	private String name;
	private String title;
	
	private HashSet<Player> openedPlayers;
	private HashMap<Integer, Goods> goods;
	
	public Shop(String name) {
		this.name = name;
		this.title = name;
		
		goods = new HashMap<>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Goods getGoods(int index) {
		return goods.get(index);
	}
	
	public int firstEmpty() {
		for(int i = 0; i < 54; i++) {
			if(!goods.containsKey(i))
				return i;
		}
		
		return -1;
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

			lore.add("");
			
			switch(arg.getType()) {
			case BOTH:
				lore.add(C.Yellow + "구매가: " + C.White + NumberUtil.formatBalance(arg.getBuyPrice()) + " 페론");
				lore.add(C.Lime + "판매가: "+ C.White + NumberUtil.formatBalance(arg.getBuyPrice()) + " 페론");
				break;
			case BUY:
				lore.add(C.Yellow + "구매가: " + C.White + NumberUtil.formatBalance(arg.getBuyPrice()) + " 페론");
				break;
			case SELL:
				lore.add(C.Lime + "판매가: "+ C.White + NumberUtil.formatBalance(arg.getBuyPrice()) + " 페론");
				break;
			}
			
			gui.setItem(index, loreGoods);
		}
		
		openedPlayers.add(player);
		player.openInventory(gui);
	}
	
	public void addGoods(int index, Goods goods) {
		this.goods.put(index, goods);
	}
	
	public void removeGoods(int index) {
		goods.remove(index);
	}
	
	public void moveGoods(int index1, int index2) {
		if(!goods.containsKey(index1) || goods.containsKey(index2))
			return;
		
		goods.put(index2, goods.get(index1));
		goods.remove(index1);
	}
	
	public boolean isOpening(Player player) {
		return openedPlayers.contains(player);
	}
	
	public boolean hasGoods(int index) {
		return goods.containsKey(index);
	}
	
	public void onClick(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player))
			return;
		
		int slot = event.getSlot();
		
		if(!goods.containsKey(slot))
			return;
		
		Goods clickedGoods = goods.get(slot);
		
		switch(event.getClick()) {
		case LEFT:
			if(clickedGoods.getType() == GoodsType.SELL)
				return;
			
			event.getWhoClicked().closeInventory();
			purchasePage.open((Player) event.getWhoClicked(), clickedGoods, true);
			break;
		case RIGHT:
			if(clickedGoods.getType() == GoodsType.BUY)
				return;

			event.getWhoClicked().closeInventory();
			purchasePage.open((Player) event.getWhoClicked(), clickedGoods, false);
			break;
		default:
			break;
		}
	}
	
	public void onClose(Player player) {
		openedPlayers.remove(player);
	}
}
