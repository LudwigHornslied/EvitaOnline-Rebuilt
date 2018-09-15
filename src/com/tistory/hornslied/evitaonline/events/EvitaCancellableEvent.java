package com.tistory.hornslied.evitaonline.events;

import org.bukkit.event.Cancellable;

public abstract class EvitaCancellableEvent extends EvitaEvent implements Cancellable {

	private boolean cancelled;
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
