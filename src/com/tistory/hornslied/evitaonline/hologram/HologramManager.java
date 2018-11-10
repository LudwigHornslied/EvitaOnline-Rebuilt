package com.tistory.hornslied.evitaonline.hologram;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.ConfigManager.ConfigType;

public class HologramManager {
	
	private EvitaOnline plugin;

	private HashMap<String, Hologram> holograms;
	
	public HologramManager(EvitaOnline plugin) {
		this.plugin = plugin;
	}
	
	public void saveHologram(String name) {
		FileConfiguration storage = plugin.getConfigManager().getConfig(ConfigType.HOLOGRAMS);
		
		Hologram hologram = holograms.get(name);
		
		storage.set("holograms." + name + ".location", null);
	}
	
	public void newHologram(String name, Location location) {
		if(holograms.containsKey(name))
			return;
		
		
	}
}
