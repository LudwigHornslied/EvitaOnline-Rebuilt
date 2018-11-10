package com.tistory.hornslied.evitaonline.scoreboard;

import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class ScoreboardManager implements Listener {
	
	private EvitaOnline plugin;
	
	private WeakHashMap<Player, EvitaScoreboard> scoreboards;
	
	public ScoreboardManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		scoreboards = new WeakHashMap<>();
		
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				update();
			}
		}.runTaskTimer(plugin, 0, 20);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		scoreboards.put(player, new EvitaScoreboard(player));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		scoreboards.remove(event.getPlayer());
	}
	
	
    public void update() {
		for(EvitaScoreboard scoreboard : scoreboards.values())
			scoreboard.render();
	}
}
