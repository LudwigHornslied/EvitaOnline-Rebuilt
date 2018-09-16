package com.tistory.hornslied.evitaonline.koth;

import org.bukkit.Location;
import org.bukkit.World;

import com.tistory.hornslied.evitaonline.universe.BlockCoord;

public class KoTH {
	
	private String name;
	
	private World world;
	private BlockCoord min;
	private BlockCoord max;
	
	public KoTH(String name, World world, BlockCoord min, BlockCoord max) {
		this.world = world;
		this.min = min;
		this.max = max;
	}
	
	public String getName() {
		return name;
	}
	
	public World getWorld() {
		return world;
	}
	
	public BlockCoord getMin() {
		return min;
	}
	
	public BlockCoord getMax() {
		return max;
	}
	
	public boolean isIn(Location location) {
		if(!location.getWorld().equals(world))
			return false;
		
		return (min.getX() <= location.getBlockX() && location.getBlockX() <= max.getX()) &&
				(min.getY() <= location.getBlockY() && location.getBlockY() <= max.getY()) &&
				(min.getZ() <= location.getBlockZ() && location.getBlockZ() <= max.getZ());
	}
}
