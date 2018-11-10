package com.tistory.hornslied.evitaonline.timer;

import java.util.UUID;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EnderPearlTimer extends PlayerTimer {

	public EnderPearlTimer() {
		super(15000);
	}
	
	@EventHandler
	public void onEnderpearlLaunch(ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		
		if(!(projectile instanceof EnderPearl))
			return;
		
		ProjectileSource source = ((EnderPearl) projectile).getShooter();
		
		if(!(source instanceof Player))
			return;
		Player player = ((Player) source);
		UUID uuid = player.getUniqueId();
		
		if(timers.containsKey(uuid)) {
			event.setCancelled(true);
		} else {
			timers.put(uuid, new TimerRunnable(this, uuid, defaultDuration));
		}
	}
}
