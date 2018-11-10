package com.tistory.hornslied.evitaonline.timer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.db.DBManager;
import com.tistory.hornslied.evitaonline.db.PSTable;
import com.tistory.hornslied.evitaonline.db.SQL;
import com.tistory.hornslied.evitaonline.events.TimerExpireEvent;

public class PvPProtTimer extends PlayerTimer {
	
	private DBManager db;

	public PvPProtTimer() {
		super(18000000);
		
		db = EvitaOnline.getInstance().getDBManager();
		db.query(SQL.PVPPROTS);
	}
	
	public void initPvPProt(Player player, long duration) {
		timers.put(player.getUniqueId(), new TimerRunnable(this, player.getUniqueId(), duration));
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(!player.hasPlayedBefore()) {
			initPvPProt(player, defaultDuration);
			player.sendMessage(P.Combat + C.Lime + "처음 3시간 동안은 PvP 보호가 적용됩니다. /pvp보호 해제 로 해제할 수 있습니다.");
		}
			
		ResultSet rs = db.select("SELECT time FROM PVPPROTS WHERE uuid = '" + player.getUniqueId().toString() + "'");
		
		try {
			if(rs.next()) {
				initPvPProt(player, rs.getLong("time"));
			}
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		db.query("DELETE FROM PVPPROTS WHERE uuid = '" + player.getUniqueId().toString() + "'");
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if(!timers.containsKey(player.getUniqueId()))
			return;
		
		PreparedStatement ps = PSTable.save_pvpprot;

		try {
			ps.setString(1, player.getUniqueId().toString());
			ps.setLong(2, getRemaining(player.getUniqueId()));
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		cancel(player.getUniqueId());
	}
	
	@EventHandler
	public void onExpire(TimerExpireEvent event) {
		if(!equals(event.getTimer()))
			return;
		
		Player player = Bukkit.getPlayer(event.getUuid());
		
		if(player != null && player.isOnline())
			player.sendMessage(P.Combat + C.Lime + "PvP 보호가 종료되었습니다! 이제 다른 유저들에게 공격받을 수 있습니다.");
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
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
		
		if(timers.containsKey(damaged.getUniqueId())) {
			event.setCancelled(true);
			damager.sendMessage(P.Combat + C.Red + "PvP 보호가 걸린 유저입니다.");
		} else if(timers.containsKey(damager.getUniqueId())) {
			event.setCancelled(true);
			damager.sendMessage(P.Combat + C.Red + "PvP 보호중에는 타인을 공격할 수 없습니다.");
			
		}
	}
}
