package com.tistory.hornslied.evitaonline.universe.nation;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class Nation {
	
	private String name;
	private UUID uuid;
	private long registered;
	private Color color;
	
	private Set<Town> towns;
	private Town capital;

	private Set<Nation> allies;
	
	public Nation(String name, UUID uuid, long registered) {
		this.name = name;
		this.uuid = uuid;
		this.registered = registered;
		color = Color.GRAY;
		
		towns = new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public UUID getUuid() {
		return uuid;
	}
	
	public long getRegistered() {
		return registered;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Town getCapital() {
		return capital;
	}
	
	public EvitaPlayer getLeader() {
		return capital.getMayor();
	}
	
	public Set<EvitaPlayer> getResidents() {
		HashSet<EvitaPlayer> content = new HashSet<>();
		for(Town town : towns) {
			content.addAll(town.getResidents());
		}
		return content;
	}
	
	public int getPopulation() {
		int out = 0;
		for(Town town : towns) {
			out += town.getResidentsNumber();
		}
		return out;
	}
	
	public int getPlotNumber() {
		int out = 0;
		for(Town town : towns) {
			out += town.getPlotNumber();
		}
		return out;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setCapital(Town town) {
		capital = town;
	}
	
	public void addTown(Town town) {
		towns.add(town);
	}
	
	public boolean isAlliedWith(Nation nation) {
		return allies.contains(nation);
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
