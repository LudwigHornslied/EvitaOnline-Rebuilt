package com.tistory.hornslied.evitaonline.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class TimerRunnable {
	
	private AbstractTimer abstractTimer;
	private UUID player;
	private Timer timer;
	
	private long expire;
	private long pausedRemainingTime;
	
	public TimerRunnable(AbstractTimer timer, UUID uuid, long duration) {
		abstractTimer = timer;
		player = uuid;
		setRemaining(duration);
		pausedRemainingTime = 0;
	}
	
	public TimerRunnable(AbstractTimer timer, long duration) {
		abstractTimer = timer;
		player = null;
		setRemaining(duration);
		pausedRemainingTime = 0;
	}
	
	public void setRemaining(long duration) {
		expire = System.currentTimeMillis() + duration;
		
		if(timer != null)
			timer.cancel();
		
		timer = new Timer();
		timer.schedule(new Expire(), duration);
	}
	
	public void pause() {
		if(isPaused())
			return;
		
		pausedRemainingTime = getRemaining();
		stop();
	}
	
	public void resume() {
		if(!isPaused())
			return;
		
		setRemaining(pausedRemainingTime);
		pausedRemainingTime = 0;
	}
	
	public UUID getUuid() {
		return player;
	}
	
	public long getRemaining() {
		return expire - System.currentTimeMillis();
	}
	
	public boolean isPaused() {
		return pausedRemainingTime != 0L;
	}
	
	public void stop() {
		timer.cancel();
	}
	
	public void cancel() {
		timer.cancel();
		if(abstractTimer instanceof PlayerTimer) {
			((PlayerTimer) abstractTimer).cancel(player);
		}
	}
	
	public class Expire extends TimerTask {
		
		public Expire() {
		}

		@Override
		public void run() {
			stop();
			if(abstractTimer instanceof PlayerTimer) {
				((PlayerTimer) abstractTimer).expire(player);
			}
		}
		
	}
}
