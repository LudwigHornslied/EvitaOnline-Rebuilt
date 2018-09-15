package com.tistory.hornslied.evitaonline.rank;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;

public class RankManager implements Listener {
	
	private EvitaOnline plugin;

	public RankManager(EvitaOnline plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		for(String perm : EvitaAPI.getEvitaPlayer(player).getRank().getPermissions()) {
			player.addAttachment(plugin, perm, true);
		}
	}
}
