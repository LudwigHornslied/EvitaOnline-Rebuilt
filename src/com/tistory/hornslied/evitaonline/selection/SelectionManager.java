package com.tistory.hornslied.evitaonline.selection;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.commons.util.C;

public class SelectionManager implements Listener {

	private EvitaOnline plugin;
	
	private Material wandMaterial = Material.DIAMOND_HOE;
	private String wandName = C.BYellow + "";
	private List<String> wandLore;
	
	private HashMap<Player, Block> selection1;
	private HashMap<Player, Block> selection2;
	
	private HashMap<Player, Block[]> fakeBlocks;
	
	public SelectionManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		selection1 = new HashMap<>();
		selection2 = new HashMap<>();
		
		runCheckHoldingWand();
	}
	
	private void runCheckHoldingWand() {
		new BukkitRunnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				
				for(Player player : Bukkit.getOnlinePlayers()) {
					ItemStack item = player.getInventory().getItemInMainHand();
					
					if(item == null)
						return;
					
					if(item.getType() != wandMaterial || !item.hasItemMeta() 
							|| !item.getItemMeta().hasDisplayName() || !item.getItemMeta().getDisplayName().equals(wandName)) {
						if(fakeBlocks.containsKey(player)) {
							for(Block block : fakeBlocks.get(player)) {
								
								
								player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
							}
						}
					} else {
						if(!fakeBlocks.containsKey(player)) {
							for(Block block : fakeBlocks.get(player)) {
								player.sendBlockChange(block.getLocation(), Material.GLOWSTONE, (byte) 0);
							}
						}
					}
				}
			}
			
		}.runTaskTimerAsynchronously(plugin, 0, 1);
	}
	
	public Selection getSelection(Player player) {
		if(!selection1.containsKey(player) || !selection2.containsKey(player))
			return null;
		
		Block corner1 = selection1.get(player);
		Block corner2 = selection2.get(player);
		
		if(!corner1.getWorld().equals(corner2.getWorld()))
			return null;
		
		World world = corner1.getWorld();
		
		int minx = Math.min(corner1.getX(), corner2.getX());
		int miny = Math.min(corner1.getY(), corner2.getY());
		int minz = Math.min(corner1.getZ(), corner2.getZ());
		
		int maxx = Math.max(corner1.getX(), corner2.getX());
		int maxy = Math.max(corner1.getY(), corner2.getY());
		int maxz = Math.max(corner1.getZ(), corner2.getZ());
		
		return new Selection(world, world.getBlockAt(minx, miny, minz), world.getBlockAt(maxx, maxy, maxz));
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if(!event.hasBlock())
			return;
		
		Player player = event.getPlayer();
		
		switch(event.getAction()) {
		case LEFT_CLICK_BLOCK:
			selection1.put(player, event.getClickedBlock());
			break;
		case RIGHT_CLICK_BLOCK:
			selection2.put(player, event.getClickedBlock());
			break;
		default:
			break;
		}
	}
}
