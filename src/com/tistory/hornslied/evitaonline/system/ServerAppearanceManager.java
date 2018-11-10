package com.tistory.hornslied.evitaonline.system;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class ServerAppearanceManager implements Listener {
	
	private EvitaOnline plugin;
	
	public ServerAppearanceManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	// EventHandler
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
	}
}
