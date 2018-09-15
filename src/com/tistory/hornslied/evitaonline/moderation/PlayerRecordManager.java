package com.tistory.hornslied.evitaonline.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class PlayerRecordManager implements Listener {
	
	private EvitaOnline plugin;
	
	public PlayerRecordManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	// EventHandler
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
	}
}
