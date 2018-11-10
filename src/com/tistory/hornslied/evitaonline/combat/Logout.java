package com.tistory.hornslied.evitaonline.combat;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class Logout {

	private Player player;
	private Location ogLocation;
	private long expire;
	private BukkitRunnable timer;
	
	public Logout(Player player) {
		this.player = player;
		ogLocation = player.getLocation();
		expire = System.currentTimeMillis() + 30000;
		
		timer = new BukkitRunnable() {

			@Override
			public void run() {
				if(getRemaining() < 0) {
					cancel();
					EvitaOnline.getInstance().getCombatLogManager().safeDisconnect(player);
					return;
				}
				
				if(!player.getWorld().equals(ogLocation.getWorld())) {
					cancel();
					EvitaOnline.getInstance().getCombatLogManager().removeLogout(player);
					return;
				}
				
				if(player.getLocation().distance(ogLocation) > 0.5) {
					cancel();
					EvitaOnline.getInstance().getCombatLogManager().removeLogout(player);
					return;
				}
			}
			
		};
		timer.runTaskTimerAsynchronously(EvitaOnline.getInstance(), 0, 1);
	}
	
	public void cancel() {
		timer.cancel();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public long getRemaining() {
		return expire - System.currentTimeMillis();
	}
}
