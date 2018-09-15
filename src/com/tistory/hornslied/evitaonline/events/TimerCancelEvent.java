package com.tistory.hornslied.evitaonline.events;

import java.util.UUID;

import com.tistory.hornslied.evitaonline.timer.AbstractTimer;
import com.tistory.hornslied.evitaonline.timer.GlobalTimer;
import com.tistory.hornslied.evitaonline.timer.PlayerTimer;

public class TimerCancelEvent extends EvitaEvent {

	private UUID uuid;
	private AbstractTimer timer;
	
	public TimerCancelEvent(UUID uuid, PlayerTimer timer) {
		this.uuid = uuid;
		this.timer = timer;
	}
	
	public TimerCancelEvent(GlobalTimer timer) {
		this.timer = timer;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public AbstractTimer getTimer() {
		return timer;
	}
}
