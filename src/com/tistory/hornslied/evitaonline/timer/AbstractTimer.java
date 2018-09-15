package com.tistory.hornslied.evitaonline.timer;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public abstract class AbstractTimer implements Listener {
	
	protected long defaultDuration;
	
	public AbstractTimer(long defaultDuration) {
		this.defaultDuration = defaultDuration;
		
		Bukkit.getPluginManager().registerEvents(this, EvitaOnline.getInstance());
	}
}
