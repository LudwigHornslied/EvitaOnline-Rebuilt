package com.tistory.hornslied.evitaonline.universe.plot;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlotOwner {
	
	protected UUID uuid;

	protected Set<Plot> plots;

	protected boolean pvp;
	protected boolean forcePvp;
	
	public PlotOwner(UUID uuid) {
		this.uuid = uuid;
		
		plots = new HashSet<>();
		
		pvp = false;
		forcePvp = false;
	}
	
	public void setPvP(boolean pvp) {
		this.pvp = pvp;
	}
	
	public void setForcePvP(boolean forcePvp) {
		this.forcePvp = forcePvp;
	}
	
	public void addPlot(Plot plot) {
		plots.add(plot);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public Set<Plot> getPlots() {
		return plots;
	}
	
	public int getPlotNumber() {
		return plots.size();
	}

	public boolean isPvp() {
		return pvp;
	}
	
	public boolean isForcePvP() {
		return forcePvp;
	}
	
	public boolean isCombatable() {
		if(forcePvp) {
			return true;
		} else {
			return pvp;
		}
	}
}
