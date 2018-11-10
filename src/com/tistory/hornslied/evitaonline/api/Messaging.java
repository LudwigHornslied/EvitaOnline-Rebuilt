package com.tistory.hornslied.evitaonline.api;

import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.universe.town.Town;

public class Messaging {

	public static void sendTownMessage(Town town, String message) {
		for(Player player : town.getOnlinePlayers()) {
			player.sendMessage(message);
		}
	}
}
