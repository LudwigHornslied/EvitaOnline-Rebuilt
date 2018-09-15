package com.tistory.hornslied.evitaonline.universe.plot;

import java.util.HashSet;
import java.util.Set;

import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.EvitaWorld;

public class Plot {
	
	private EvitaWorld world;
	private int x;
	private int z;
	
	private PlotOwner plotOwner;
	private PlotType type;
	
	private EvitaPlayer owner;
	private Set<EvitaPlayer> residents;
	
	private boolean pvp;
	private boolean mobSpawning;
	
	public Plot(EvitaWorld world, int x, int z, PlotOwner plotOwner) {
		this.world = world;
		this.x = x;
		this.z = z;
		
		this.plotOwner = plotOwner;
		type = PlotType.GENERAL;
		
		owner = null;
		residents = new HashSet<EvitaPlayer>();
		
		pvp = false;
		mobSpawning = false;
	}
	
	public EvitaWorld getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public PlotOwner getPlotOwner() {
		return plotOwner;
	}

	public PlotType getType() {
		return type;
	}
	
	public EvitaPlayer getOwner() {
		return owner;
	}
	
	public Set<EvitaPlayer> getResidents() {
		return residents;
	}
	
	public void setPlotOwner(PlotOwner plotOwner) {
		this.plotOwner = plotOwner;
	}

	public void setType(PlotType type) {
		this.type = type;
	}
	
	public void setOwner(EvitaPlayer owner) {
		this.owner = owner;
	}
	
	public void addResident(EvitaPlayer player) {
		residents.add(player);
	}
	
	public void setPvP(boolean pvp) {
		this.pvp = pvp;
	}
	
	public void setMobSpawning(boolean mobSpawning) {
		this.mobSpawning = mobSpawning;
	}
	
	public boolean isPvp() {
		return pvp;
	}
	
	public boolean isMobSpawning() {
		return mobSpawning;
	}
}
