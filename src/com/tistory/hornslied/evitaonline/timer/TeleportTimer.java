package com.tistory.hornslied.evitaonline.timer;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.events.TimerCancelEvent;
import com.tistory.hornslied.evitaonline.events.TimerExpireEvent;

public class TeleportTimer extends PlayerTimer {
	
	private HashMap<UUID, Location> queue;
	private HashMap<UUID, Location> originalLocation;

	public TeleportTimer() {
		super(10);
		
		queue = new HashMap<>();
		originalLocation = new HashMap<>();
	}
	
	public void queue(Player player, Location location) {
		timers.put(player.getUniqueId(), new TimerRunnable(this, player.getUniqueId(), defaultDuration));
		queue.put(player.getUniqueId(), location);
		originalLocation.put(player.getUniqueId(), player.getLocation());
		
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(timers.containsKey(player.getUniqueId())) {
			if(originalLocation.get(player.getUniqueId()).distance(event.getTo()) > 3) {
				timers.get(player.getUniqueId()).cancel();
			}
		}
	}
	
	@EventHandler
	public void onCancel(TimerCancelEvent event) {
		if(!equals(event.getTimer()))
			return;
		
		Player player = Bukkit.getPlayer(event.getUuid());

		if (player == null || !player.isOnline())
			return;
		
		player.sendMessage(P.Move + C.Red + "3블럭 초과 이동하여 텔레포트가 취소되었습니다.");
	}
	
	@EventHandler
	public void onExpire(TimerExpireEvent event) {
		if(!equals(event.getTimer()))
			return;
		
		Player player = Bukkit.getPlayer(event.getUuid());
		
		if(player == null || !player.isOnline())
			return;
		
		player.teleport(queue.get(player.getUniqueId()));
		queue.remove(player.getUniqueId());
		originalLocation.remove(player);
	}
}
