package com.tistory.hornslied.evitaonline;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.JsonMessage;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class Formatter {
	
	public static String getUserCard(Player player) {
		EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(player);
		
		return C.BYellow + "닉네임: " + C.White + player.getName() + "\n"
						+ C.BYellow + "국가: " + C.White + ((evitaPlayer.hasNation()) ? evitaPlayer.getNation().getName() : "없음")
						+ C.BYellow + "마을: " + C.White + ((evitaPlayer.hasTown()) ? evitaPlayer.getTown().getName() : "없음") + "\n"
						+ C.BYellow + "소지금: " + C.White + evitaPlayer.getBalance() + " 페론"; 
	}
	
	public static List<JsonMessage> getTownInfo(Town town) {
		ArrayList<JsonMessage> content = new ArrayList<>();
		
		content.add(new JsonMessage(P.Line));
		
		return content;
	}
}
