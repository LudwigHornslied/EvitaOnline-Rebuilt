package com.tistory.hornslied.evitaonline.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.tistory.hornslied.evitaonline.universe.plot.Plot;

public class PlayerMovePlotEvent extends EvitaEvent {
	
	private Player player;
	private Plot from;
	private Plot to;
	private PlayerMoveEvent pme;
	
	public PlayerMovePlotEvent(Player player, Plot from, Plot to, PlayerMoveEvent pme) {
		this.player = player;
		this.from = from;
		this.to = to;
		this.pme = pme;
	}
    
    public Player getPlayer() {
    	return player;
    }
    
    public Plot getFrom() {
    	return from;
    }
    
    public Plot getTo() {
    	return to;
    }
    
    public PlayerMoveEvent getPME() {
    	return pme;
    }

}
