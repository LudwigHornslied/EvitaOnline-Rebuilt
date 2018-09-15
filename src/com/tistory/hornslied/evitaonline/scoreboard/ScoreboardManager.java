package com.tistory.hornslied.evitaonline.scoreboard;

import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.commons.util.repeater.RepeatHandler;
import com.tistory.hornslied.evitaonline.commons.util.repeater.Repeatable;
import com.tistory.hornslied.evitaonline.commons.util.repeater.Time;

public class ScoreboardManager implements Listener {
	
	private EvitaOnline plugin;
	
	private WeakHashMap<Player, EvitaScoreboard> scoreboards;
	
	public ScoreboardManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		scoreboards = new WeakHashMap<>();
		
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
		RepeatHandler.Instance.registerRepeatables(this);
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
	
	@Repeatable(interval = @Time(seconds = 1))
    public void tick() {
		for(EvitaScoreboard scoreboard : scoreboards.values())
			scoreboard.render();
	}
}
