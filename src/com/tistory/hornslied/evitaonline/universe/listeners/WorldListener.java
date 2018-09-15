package com.tistory.hornslied.evitaonline.universe.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.universe.EvitaWorld;

public class WorldListener implements Listener {
	
	@EventHandler
	public void onEndermanGrief(EntityChangeBlockEvent event) {
		if (event.getEntityType() == EntityType.ENDERMAN) {
			EvitaWorld world = EvitaAPI.getEvitaWorld(event.getBlock().getWorld());
			
			if (!world.isEndermanGrief())
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.CUSTOM)
			return;
		
		EvitaWorld world = EvitaAPI.getEvitaWorld(event.getLocation().getWorld());
		
		if (world.isMobSpawning()) {
			if (world.isRegisteredMob(event.getEntityType()))
				event.setCancelled(true);
		} else {
			if (!world.isRegisteredMob(event.getEntityType()))
				event.setCancelled(true);
		}
	}
}
