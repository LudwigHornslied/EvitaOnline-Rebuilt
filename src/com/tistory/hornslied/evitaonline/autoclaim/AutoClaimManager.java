package com.tistory.hornslied.evitaonline.autoclaim;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.events.PlayerMovePlotEvent;
import com.tistory.hornslied.evitaonline.universe.plot.PlotOwner;

public class AutoClaimManager implements Listener {

	private Map<Player, PlotOwner> autoclaims;
	
	public AutoClaimManager(EvitaOnline plugin) {
		autoclaims = new HashMap<>();
	}
	
	@EventHandler
	public void onMovePlot(PlayerMovePlotEvent event) {
		
	}
}
