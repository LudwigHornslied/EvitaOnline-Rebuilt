package com.tistory.hornslied.evitaonline.structure;

public class RelativeBlockCoord {

	private int relativeX;
	private int relativeY;
	private int relativeZ;
	
	private RelativeBlockCoord(int x, int y, int z) {
		relativeX = x;
		relativeY = y;
		relativeZ = z;
	}
	
	public static RelativeBlockCoord parse(int x, int y, int z) {
		return new RelativeBlockCoord(x, y, z);
	}
	
	public int getRelativeX() {
		return relativeX;
	}
	
	public int getRelativeY() {
		return relativeY;
	}
	
	public int getRelativeZ() {
		return relativeZ;
	}
	
	@Override
	public int hashCode() {
		int result = 20;
		
		result = 34 * result + relativeX;
		result = 34 * result + relativeY;
		result = 34 * result + relativeZ;
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		
		if(!(obj instanceof RelativeBlockCoord))
			return false;
		
		RelativeBlockCoord coord = (RelativeBlockCoord) obj;
		
		return coord.getRelativeX() == relativeX &&
				coord.getRelativeY() == relativeY &&
				coord.getRelativeZ() == relativeZ;
	}
}
