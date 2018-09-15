package com.tistory.hornslied.evitaonline.combat;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class OldCombatManager implements Listener {
	
	private EvitaOnline plugin;
	
	public OldCombatManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	public void setPlayerAttackSpeed(Player player) {
		player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(24);
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		setPlayerAttackSpeed(event.getPlayer());
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
	}
}
