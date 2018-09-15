package com.tistory.hornslied.evitaonline.item;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import com.tistory.hornslied.evitaonline.ConfigManager.ConfigType;
import com.tistory.hornslied.evitaonline.EvitaOnline;

public class ItemManager {
	
	private EvitaOnline plugin;
	
	private HashMap<String, ItemStack> items;

	public ItemManager(EvitaOnline plugin) {
		this.plugin = plugin;
		items = new HashMap<>();
		
		load();
	}
	
	private void load() {
		FileConfiguration storage = plugin.getConfigManager().getConfig(ConfigType.ITEMS);
	
		Set<String> keys = storage.getKeys(false);
		
		if(keys == null)
			return;
		
		for(String key : keys) {
			items.put(key, storage.getItemStack(key));
		}
	}
	
	public void save(String name, ItemStack item) {
		
	}
}
