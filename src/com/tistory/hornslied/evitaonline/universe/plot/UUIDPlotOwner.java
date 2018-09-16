package com.tistory.hornslied.evitaonline.universe.plot;

import java.util.UUID;

public abstract class UUIDPlotOwner extends PlotOwner {

	protected UUID uuid;
	
	public UUIDPlotOwner(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}
}
