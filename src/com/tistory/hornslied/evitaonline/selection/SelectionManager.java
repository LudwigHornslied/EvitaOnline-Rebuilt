package com.tistory.hornslied.evitaonline.selection;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.ItemUtil;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.permission.Perm;

public class SelectionManager implements Listener {

	private EvitaOnline plugin;
	
	private ItemStack wand = ItemUtil.create(
			Material.BLAZE_ROD,
			C.BCyan + "완드", 
			C.BDarkGray + "범위 지정용 완드");
	
	private HashMap<Player, Block> selection1;
	private HashMap<Player, Block> selection2;
	
	public SelectionManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		selection1 = new HashMap<>();
		selection2 = new HashMap<>();
		
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	public ItemStack getWand() {
		return wand;
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
	
	public Block getFirstPoint(Player player) {
		return selection1.get(player);
	}
	
	public Block getSecondPoint(Player player) {
		return selection2.get(player);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if(!event.hasBlock())
			return;
		
		Player player = event.getPlayer();
		
		if(!player.hasPermission(Perm.MOD))
			return;
		
		ItemStack item = player.getInventory().getItemInMainHand();
		
		if(!wand.isSimilar(item))
			return;
		
		event.setCancelled(true);
		
		Block block = event.getClickedBlock();
		
		switch(event.getAction()) {
		case LEFT_CLICK_BLOCK:
			if(block.equals(selection1.get(player)))
				return;
			
			selection1.put(player, block);
			player.sendMessage(P.Server + C.Yellow + "1번 선택 지점 지정.");
			break;
		case RIGHT_CLICK_BLOCK:
			if(block.equals(selection2.get(player)))
				return;
			
			player.sendMessage(P.Server + C.Yellow + "2번 선택 지점 지정.");
			selection2.put(player, event.getClickedBlock());
			break;
		default:
			break;
		}
	}
}
