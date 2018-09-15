package com.tistory.hornslied.evitaonline.selection;

import org.bukkit.World;
import org.bukkit.block.Block;

public class Selection {

	private World world;
	private Block min;
	private Block max;
	
	public Selection(World world, Block min, Block max) {
		this.world = world;
		this.min = min;
		this.max = max;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Block getMin() {
		return min;
	}
	
	public Block getMax() {
		return max;
	}
	
	public long size() {
		return (max.getX() - min.getX() +1) * (max.getY() - min.getY() +1) * (max.getZ() - min.getZ() +1);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj instanceof Selection))
			return false;
		
		Selection selection = (Selection) obj;
		
		return world.equals(selection.getWorld()) && min.equals(selection.getMin()) && max.equals(selection.getMax());
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = result * 8 + world.hashCode();
		result = result * 13 + min.getX();
		result = result * 13 + min.getY();
		result = result * 13 + min.getZ();
		result = result * 16 + max.getX();
		result = result * 16 + max.getY();
		result = result * 16 + max.getZ();
		
		return result;
	}
}
