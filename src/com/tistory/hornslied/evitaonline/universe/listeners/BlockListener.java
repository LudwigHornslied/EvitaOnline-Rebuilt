package com.tistory.hornslied.evitaonline.universe.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.mine.Mine;
import com.tistory.hornslied.evitaonline.permission.Perm;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.plot.Plot;
import com.tistory.hornslied.evitaonline.universe.plot.PlotOwner;
import com.tistory.hornslied.evitaonline.universe.town.AncientCity;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(player);
		Block block = event.getBlock();
		Plot plot = EvitaAPI.getPlot(block.getLocation());
		
		if(plot == null) {
			//Wilderness
			if(!player.hasPermission(Perm.MOD)) {
				event.setCancelled(true);
			}
				
			return;
		}
		
		PlotOwner plotOwner = plot.getPlotOwner();
		
		if(plotOwner instanceof Town) {
			
			Town town = (Town) plotOwner;
			
			if(evitaPlayer.equals(plot.getOwner()) || plot.getResidents().contains(evitaPlayer))
				return;
			
			if(town.equals(evitaPlayer.getTown())) {
				
				if(plot.getOwner() != null && !plot.getResidents().contains(evitaPlayer)) {
					event.setCancelled(true);
					return;
				}
				
				if(!town.isBuildable()) {
					event.setCancelled(true);
					return;
				}
				
			} else {
				event.setCancelled(true);
			}
		} else if (plotOwner instanceof AncientCity) {

			if(!player.hasPermission(Perm.MOD)) {
				event.setCancelled(true);
			}
		} else if (plotOwner instanceof Mine) {

			if(!player.hasPermission(Perm.MOD)) {
				event.setCancelled(true);
			}
		}
	}
}
