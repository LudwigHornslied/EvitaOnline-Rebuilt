package com.tistory.hornslied.evitaonline.mine;

import org.bukkit.block.Block;

import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.universe.BlockCoord;
import com.tistory.hornslied.evitaonline.universe.EvitaWorld;

public class Mineral {

	private EvitaWorld world;
	
	private BlockCoord min;
	private BlockCoord max;
	
	public Mineral(EvitaWorld world, BlockCoord min, BlockCoord max) {
		this.world = world;
		this.min = min;
		this.max = max;
	}
	
	public boolean isMineral(Block block) {
		if(!world.equals(EvitaAPI.getEvitaWorld(block.getWorld())))
			return false;
		
		return (min.getX() <= block.getX() && block.getX() <= max.getX()) &&
				(min.getY() <= block.getY() && block.getY() <= max.getY()) &&
				(min.getZ() <= block.getZ() && block.getZ() <= max.getZ());
	}
}
