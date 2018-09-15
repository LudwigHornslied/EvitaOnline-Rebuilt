package com.tistory.hornslied.evitaonline.universe;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class Coord {
	
	private int x;
	private int z;
	
	private Coord(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	public static Coord parseCoord(Location location) {
		return parseCoord(location.getChunk());
	}
	
	public static Coord parseCoord(Chunk chunk) {
		return new Coord(chunk.getX(), chunk.getZ());
	}
	
	public static Coord parseCoord(int x, int z) {
		return new Coord(x, z);
	}
	
	@Override
	public int hashCode() {

		int result = 17;
		result = 27 * result + x;
		result = 27 * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this)
			return true;
		if (!(obj instanceof Coord))
			return false;

		Coord o = (Coord) obj;
		return this.x == o.x && this.z == o.z;
	}
}
