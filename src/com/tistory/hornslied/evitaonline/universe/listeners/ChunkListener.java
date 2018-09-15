package com.tistory.hornslied.evitaonline.universe.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.events.PlayerMovePlotEvent;
import com.tistory.hornslied.evitaonline.universe.plot.Plot;
import com.tistory.hornslied.evitaonline.universe.plot.PlotOwner;
import com.tistory.hornslied.evitaonline.universe.town.AncientCity;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class ChunkListener implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if (from.getChunk() != to.getChunk()) {
			PlayerMovePlotEvent pmpe = new PlayerMovePlotEvent(event.getPlayer(), EvitaAPI.getPlot(from), EvitaAPI.getPlot(to), event);
			Bukkit.getPluginManager().callEvent(pmpe);
		}
	}
	
	@EventHandler
	public void onPlayerMovePlot(PlayerMovePlotEvent event) {
		Player player = event.getPlayer();
		Plot from = event.getFrom();
		Plot to = event.getTo();
		
		if (from == null & to == null) {
			return;
		} else if (from == null & to != null) {
			sendChunkNotification(player, to);
		} else if (to == null) {
			sendChunkNotification(player, to);
		} else if (from.getPlotOwner() != to.getPlotOwner()){
			sendChunkNotification(player, to);
		}
	}
	
	private void sendChunkNotification(Player player, Plot target) {
		
		if(target == null) {
			player.sendTitle(C.Lime + "야생", C.BRed + "PvP, 약탈 가능", 10, 70, 10);
		} else {
			PlotOwner plotOwner = target.getPlotOwner();
			
			if(plotOwner instanceof Town) {
				player.sendTitle(
						C.BYellow + ((Town) plotOwner).getName(), 
						C.BGray + ((Town) plotOwner).getDescription(), 
						10, 70, 10
						);
			} else if (plotOwner instanceof AncientCity) {
				player.sendTitle(
						C.BGold + ((AncientCity) plotOwner).getName(), 
						(plotOwner.isCombatable() ? C.BRed + "전쟁 중(PvP)" : C.BYellow + "고대 도시"), 
						10, 70, 10
						);
			}
		}
	}
}
