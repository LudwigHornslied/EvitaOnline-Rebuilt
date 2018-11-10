package com.tistory.hornslied.evitaonline.death;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.RandomStorage;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

public class DeathManager implements Listener {
	
	private EvitaOnline plugin;

	public DeathManager(EvitaOnline plugin) {
		this.plugin = plugin;
	}
	
	public void drop(Player player, int chance) {
		PlayerInventory inv = player.getInventory();
		ItemStack[] contents = inv.getContents();
		
		for(int i = 0; i < contents.length; i++) {
			if(contents[i] == null)
				continue;

			if (RandomStorage.RANDOM.nextInt(100) < chance) {
				player.getWorld().dropItem(player.getLocation(), contents[i]);
				contents[i] = null;
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player victim = event.getEntity();
		Player killer = victim.getKiller();
		
		victim.getWorld().strikeLightningEffect(victim.getLocation());
		
		EvitaPlayer evitaVictim = EvitaAPI.getEvitaPlayer(victim);
		
		evitaVictim.setKill(evitaVictim.getKill() + 1);
		try {
			plugin.getUniverseManager().savePlayer(evitaVictim);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(killer != null) {
			EvitaPlayer evitaKiller = EvitaAPI.getEvitaPlayer(killer);
			
			evitaKiller.setKill(evitaKiller.getKill() + 1);
			try {
				plugin.getUniverseManager().savePlayer(evitaKiller);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		drop(victim, 50);
		
		EntityDamageEvent damageEvent = victim.getLastDamageCause();
		DamageCause cause = damageEvent.getCause();
		
		if(cause.equals(DamageCause.FALL)) {
			
		} else if(cause.equals(DamageCause.ENTITY_ATTACK) || cause.equals(DamageCause.ENTITY_SWEEP_ATTACK)) {
			
		} else if(cause.equals(DamageCause.FIRE) || cause.equals(DamageCause.FIRE_TICK)) {
			
		} else if(cause.equals(DamageCause.LAVA)) {
			
		} else if(cause.equals(DamageCause.PROJECTILE)) {
			
		}
	}
}
