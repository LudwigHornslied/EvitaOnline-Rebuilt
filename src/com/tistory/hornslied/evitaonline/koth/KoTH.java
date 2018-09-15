package com.tistory.hornslied.evitaonline.koth;

import org.bukkit.Location;

import com.tistory.hornslied.evitaonline.universe.BlockCoord;
import com.tistory.hornslied.evitaonline.universe.EvitaWorld;

public class KoTH {
	
	private String name;
	
	private EvitaWorld world;
	private BlockCoord min;
	private BlockCoord max;
	
	public KoTH(String name, EvitaWorld world, BlockCoord min, BlockCoord max) {
		this.world = world;
		this.min = min;
		this.max = max;
	}
	
	public boolean isIn(Location location) {
		return false;
	}
}
