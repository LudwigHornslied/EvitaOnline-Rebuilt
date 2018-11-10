package com.tistory.hornslied.evitaonline.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.events.TimerExpireEvent;

public class CombatTimer extends PlayerTimer {

	public CombatTimer() {
		super(30000);
	}
	
	public void activateCombat(Player player) {
		if(timers.containsKey(player.getUniqueId()))
			player.sendMessage(P.Combat + C.Red + "전투상태에 돌입합니다!");
		
		timers.put(player.getUniqueId(), new TimerRunnable(this, player.getUniqueId(), defaultDuration));
	}

	@EventHandler
	public void onDamaged(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player damaged = (Player) event.getEntity();
		
		Player damager;
		if(event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
		} else if (event.getDamager() instanceof Projectile) {
			if(!(((Projectile) event.getDamager()).getShooter() instanceof Player))
				return;
			
			damager = (Player) ((Projectile) event.getDamager()).getShooter();
		} else {
			return;
		}

		activateCombat(damager);
		activateCombat(damaged);
	}
	
	@EventHandler
	public void onExpire(TimerExpireEvent event) {
		if(!equals(event.getTimer()))
			return;
		
		Player player = Bukkit.getPlayer(event.getUuid());
		
		if(player != null && player.isOnline())
			player.sendMessage(P.Combat + C.Lime + "당신은 전투상태에서 벗어났습니다!");
	}
}
