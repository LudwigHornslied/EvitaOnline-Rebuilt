package com.tistory.hornslied.evitaonline.timer;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.tistory.hornslied.evitaonline.events.TimerCancelEvent;
import com.tistory.hornslied.evitaonline.events.TimerExpireEvent;

public abstract class PlayerTimer extends AbstractTimer {
	
	protected HashMap<UUID, TimerRunnable> timers;
	
	public PlayerTimer(long defaultDuration) {
		super(defaultDuration);
		
		timers = new HashMap<>();
	}
	
	public void expire(UUID uuid) {
		timers.remove(uuid);
		Bukkit.getPluginManager().callEvent(new TimerExpireEvent(uuid, this));
	}
	
	public void cancel(UUID uuid) {
		timers.remove(uuid);
		Bukkit.getPluginManager().callEvent(new TimerCancelEvent(uuid, this));
	}
	
	public boolean isContaining(UUID uuid) {
		return timers.containsKey(uuid);
	}
}
