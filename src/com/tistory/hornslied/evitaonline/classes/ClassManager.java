package com.tistory.hornslied.evitaonline.classes;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class ClassManager implements Listener {
	
	private HashMap<Player, Class> classes;
	
	public Archer archer;
	public Cataphract cataphract;
	
	public ClassManager(EvitaOnline plugin) {
		classes = new HashMap<>();
		
		archer = new Archer();
		cataphract = new Cataphract();
	}
	
	public Class getPlayerClass(Player player) {
		return classes.get(player);
	}
}
