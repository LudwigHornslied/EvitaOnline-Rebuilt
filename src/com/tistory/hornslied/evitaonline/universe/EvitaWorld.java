package com.tistory.hornslied.evitaonline.universe;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.structure.Structure;
import com.tistory.hornslied.evitaonline.universe.plot.Plot;
import com.tistory.hornslied.evitaonline.universe.plot.PlotOwner;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class EvitaWorld {
	
	private String name;
	
	private ConcurrentHashMap<Coord, Plot> plots;
	private HashMap<BlockCoord, Structure> structureBlocks;
	
	private boolean claimable;
	private boolean endermanGrief;
	private boolean mobSpawning;
	
	private Set<EntityType> registeredMobs;
	
	public EvitaWorld(String name) {
		this.name = name;
		
		plots = new ConcurrentHashMap<>();
		structureBlocks = new HashMap<>();
		
		claimable = true;
		endermanGrief = false;
		mobSpawning = false;
		
		registeredMobs = new HashSet<>();
	}
	
	public void addPlot(Coord coord, Plot plot) {
		plots.put(coord, plot);
	}
	
	public void addRegisteredMob(EntityType type) {
		registeredMobs.add(type);
	}

	public void setClaimable(boolean claimable) {
		this.claimable = claimable;
	}
	
	public void setEndermanGrief(boolean endermanGrief) {
		this.endermanGrief = endermanGrief;
	}

	public void setMobSpawning(boolean mobSpawning) {
		this.mobSpawning = mobSpawning;
	}
	
	public void newPlot(Coord coord, PlotOwner plotOwner) {
		Plot plot = new Plot(this, coord.getX(), coord.getZ(), plotOwner);
		plots.put(coord, plot);
		plotOwner.addPlot(plot);
		
		try {
			EvitaOnline.getInstance().getUniverseManager().saveNewPlot(plot);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Set<EntityType> getRegisteredMobs() {
		return registeredMobs;
	}

	public String getName() {
		return name;
	}
	
	public Plot getPlot(Coord coord) {
		return plots.get(coord);
	}
	
	public int getMinDistanceFromOtherTown(Location location, Town homeTown) {
		return getMinDistanceFromOtherTown(location.getChunk().getX(), location.getChunk().getZ(), homeTown);
	}
	
	public int getMinDistanceFromOtherTown(int x, int z, Town homeTown) {

		double min = Integer.MAX_VALUE;
		for (Plot plot : plots.values()) {
				if (homeTown != null && homeTown.equals(plot.getPlotOwner()))
					continue;
					
				double dist = Math.sqrt(Math.pow(plot.getX() - x, 2) + Math.pow(plot.getZ() - z, 2));
				if (dist < min)
					min = dist;
		}

		return (int) Math.ceil(min);
	}

	public boolean isClaimable() {
		return claimable;
	}

	public boolean isEndermanGrief() {
		return endermanGrief;
	}

	public boolean isMobSpawning() {
		return mobSpawning;
	}
	
	public boolean isRegisteredMob(EntityType type) {
		return registeredMobs.contains(type);
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
