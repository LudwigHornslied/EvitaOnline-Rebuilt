package com.tistory.hornslied.evitaonline.system;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;

import com.tistory.hornslied.evitaonline.ConfigManager.ConfigType;
import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.permission.Perm;

public class SystemManager implements Listener{
	
	private EvitaOnline plugin;
	
	private Status status;
	private Set<UUID> allowedPlayers;
	
	private String stableMotd;
	private String maintenanceMotd;
	
	private long reboot;
	private Timer rebootTimer;
	
	public SystemManager(EvitaOnline plugin) {
		this.plugin = plugin;
		status = Status.STABLE;
		allowedPlayers = new HashSet<>();
		
		loadAllowedPlayers();
		loadMotd();
		scheduleReboot();
	}
	
	private void loadAllowedPlayers() {
		FileConfiguration allowedPlayers = plugin.getConfigManager().getConfig(ConfigType.ALLOWEDPLAYERS);
		
		if(allowedPlayers.getStringList("players") != null)
			for(String line : allowedPlayers.getStringList("players"))
				this.allowedPlayers.add(UUID.fromString(line));
	}
	
	private void loadMotd() {
		FileConfiguration config = plugin.getConfigManager().getConfig(ConfigType.DEFAULT);

		stableMotd = ChatColor.translateAlternateColorCodes('&',
				config.getString("MOTD.STABLE.LINE1") + "\n" + config.getString("MOTD.STABLE.LINE2"));
		maintenanceMotd = ChatColor.translateAlternateColorCodes('&',
				config.getString("MOTD.MAINTENANCE.LINE1") + "\n" + config.getString("MOTD.MAINTENANCE.LINE2"));
	}
	
	public long getRebootRemaining() {
		return reboot - System.currentTimeMillis();
	}
	
	public void scheduleReboot() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) + 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		reboot = now.getTimeInMillis();
		
		rebootTimer = new Timer();
		rebootTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					player.kickPlayer(C.BYellow + "서버를 재시작합니다.");
				}
				Bukkit.shutdown();
			}
		}, reboot);
	}
	
	public void toggleMaintenance() {
		if(status == Status.MAINTENANCE) {
			status = Status.STABLE;
		} else {
			Bukkit.getOnlinePlayers().forEach(player -> {
				if (!player.hasPermission(Perm.MOD) && !allowedPlayers.contains(player.getUniqueId()))
					player.kickPlayer(C.BYellow + "서버 점검을 시작합니다.");
			});
			
			status = Status.MAINTENANCE;
		}
	}
	
	// EventHandler
	
	@EventHandler
	public void onServerList(ServerListPingEvent event) {
		switch(status) {
		case MAINTENANCE:
			event.setMotd(maintenanceMotd);
			break;
		case STABLE:
		default:
			event.setMotd(stableMotd);
			break;
		}
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if(status == Status.MAINTENANCE && !(event.getPlayer().hasPermission(Perm.MOD) 
				|| allowedPlayers.contains(event.getPlayer().getUniqueId())))
			event.disallow(Result.KICK_WHITELIST, C.BYellow + "서버가 점검중입니다.");
	}
	
	public enum Status {
		STABLE,
		MAINTENANCE
	}
}
