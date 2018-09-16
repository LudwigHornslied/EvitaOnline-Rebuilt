package com.tistory.hornslied.evitaonline.universe.plot;

import java.util.HashSet;
import java.util.Set;

public class PlotOwner {

	protected Set<Plot> plots;

	protected boolean pvp;
	protected boolean forcePvp;
	
	public PlotOwner() {
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
