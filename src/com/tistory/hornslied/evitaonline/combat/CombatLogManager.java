package com.tistory.hornslied.evitaonline.combat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.commons.util.C;

public class CombatLogManager implements Listener {
	
	private HashSet<UUID> safeDisconnects;
	private HashMap<Player, Logout> logouts;
	private HashMap<UUID, CombatLog> combatLogs;

	public CombatLogManager(EvitaOnline plugin) {
		safeDisconnects = new HashSet<>();
		logouts = new HashMap<>();
		combatLogs = new HashMap<>();
	}
	
	public void safeDisconnect(Player player) {
		logouts.remove(player);
		safeDisconnects.add(player.getUniqueId());
		player.kickPlayer(C.Lime + "안전하게 서버에서 로그아웃 하였습니다.");
	}
	
	public void queueLogout(Player player) {
		
	}
	
	public void removeLogout(Player player) {
		logouts.remove(player);
	}
	
	public boolean isLogouting(Player player) {
		return logouts.containsKey(player);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(combatLogs.containsKey(player.getUniqueId())) {
			CombatLog log = combatLogs.remove(player.getUniqueId());
			
			CraftLivingEntity npc = (CraftLivingEntity) log.getNPC().getBukkitEntity();
			player.teleport(npc.getLocation());
			player.setFallDistance(npc.getFallDistance());
			player.setRemainingAir(npc.getRemainingAir());
			npc.remove();
			log.cancel();
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player damaged = (Player) event.getEntity();
		
		if(logouts.containsKey(damaged)) {
			logouts.remove(damaged).cancel();
		}
	}
}
