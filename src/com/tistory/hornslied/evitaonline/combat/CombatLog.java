package com.tistory.hornslied.evitaonline.combat;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class CombatLog {

	private LoggerNPC npc;
	private BukkitRunnable timer;
	
	public CombatLog(Player player) {
		npc = new LoggerNPC(player);
		
		timer = new BukkitRunnable() {

			@Override
			public void run() {
				npc.getBukkitEntity().remove();
			}
			
		};
		
		timer.runTaskLater(EvitaOnline.getInstance(), 600);
	}
	
	public LoggerNPC getNPC() {
		return npc;
	}
	
	public void cancel() {
		timer.cancel();
	}
}
