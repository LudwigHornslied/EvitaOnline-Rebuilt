package com.tistory.hornslied.evitaonline.structure;

import org.bukkit.Material;

public class BlockData {
	
	private Material type;
	private byte data;
	
	public BlockData(Material type, int data) {
		this.type = type;
		this.data = (byte) data;
	}
	
	public Material getType() {
		return type;
	}
	
	public byte getData() {
		return data;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int hashCode() {
		int result = 29;
		
		result = 29 * result + type.getId();
		result = 29 * result + (int) data;
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		
		if(!(obj instanceof BlockData))
			return false;
		
		BlockData blockData = (BlockData) obj;
		
		return blockData.getType() == type && blockData.getData() == data;
	}
}
