package com.tistory.hornslied.evitaonline.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public abstract class Class implements Listener {
	
	protected String name;
	
	public Class(String name) {
		this.name = name;
		
		Bukkit.getPluginManager().registerEvents(this, EvitaOnline.getInstance());
	}
	
	abstract public boolean checkEquipping(Player player);
}