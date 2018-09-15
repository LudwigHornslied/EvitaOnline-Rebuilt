package com.tistory.hornslied.evitaonline.system;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class ServerAppearanceManager implements Listener {
	
	private EvitaOnline plugin;
	
	public ServerAppearanceManager(EvitaOnline plugin) {
		this.plugin = plugin;
	}
	
	// EventHandler
	@EventHandler
	public void onJoin() {
		
	}
}
