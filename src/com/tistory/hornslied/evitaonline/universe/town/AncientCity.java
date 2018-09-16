package com.tistory.hornslied.evitaonline.universe.town;

import java.util.UUID;

import com.tistory.hornslied.evitaonline.universe.plot.UUIDPlotOwner;

public class AncientCity extends UUIDPlotOwner {
	
	private String name;
	
	public AncientCity(String name, UUID uuid) {
		super(uuid);
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
