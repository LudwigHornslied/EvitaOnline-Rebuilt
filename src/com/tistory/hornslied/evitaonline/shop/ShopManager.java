package com.tistory.hornslied.evitaonline.shop;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.ConfigManager.ConfigType;

public class ShopManager implements Listener {
	
	private EvitaOnline plugin;

	public PurchasePage purchasePage;
	
	private HashMap<String, Shop> shops;
	
	public ShopManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		purchasePage = new PurchasePage();
		
		shops = new HashMap<>();
		
		load();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	private void load() {
		
	}
	
	public void saveShop(Shop shop) {
		FileConfiguration storage = plugin.getConfigManager().getConfig(ConfigType.SHOPS);
		
		storage.set("SHOPS." + shop.getName() + ".TITLE", shop.getTitle());
		
		for(int i = 0; i < 54; i++) {
			storage.set("SHOPS." + shop.getName() + ".GOODS." + i, shop.getGoods(i));
		}
		
		plugin.getConfigManager().saveConfig(ConfigType.SHOPS);
	}
	
	public void newShop(String name) {
		if(shops.containsKey(name.toLowerCase()))
			return;
		
		Shop shop = new Shop(name);
		shops.put(name.toLowerCase(), shop);
		
		saveShop(shop);
	}
	
	public boolean hasName(String name) {
		return shops.containsKey(name.toLowerCase());
	}
	
	public Shop getShop(String name) {
		return shops.get(name.toLowerCase());
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player))
			return;
		
		for(Shop shop : shops.values()) {
			if(shop.isOpening((Player) event.getWhoClicked())) {
				event.setCancelled(true);
				shop.onClick(event);
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(!(event.getPlayer() instanceof Player))
			return;
		
		for(Shop shop : shops.values()) {

			if(shop.isOpening((Player) event.getPlayer())) {
				shop.onClose((Player) event.getPlayer());
			}
		}
	}
}
