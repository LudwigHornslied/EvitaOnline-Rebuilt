package com.tistory.hornslied.evitaonline.classes;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.ItemUtil;

public class Archer extends Class {

	public static ItemStack HELMET;
	public static ItemStack CHESTPLATE;
	public static ItemStack LEGGINGS;
	public static ItemStack BOOTS;
	
	static {
		HELMET = ItemUtil.create(Material.IRON_HELMET, C.BGreen + "궁수 모자");
		CHESTPLATE = ItemUtil.create(Material.IRON_CHESTPLATE, C.BGreen + "궁수 흉갑");
		LEGGINGS = ItemUtil.create(Material.IRON_LEGGINGS, C.BGreen + "궁수 하의");
		BOOTS = ItemUtil.create(Material.IRON_BOOTS, C.BGreen + "궁수 부츠");
	}
	
	private HashMap<Player, Long> rapidShots;
	
	public Archer() {
		super("궁수");
	}

	@Override
	public boolean checkEquipping(Player player) {
		PlayerInventory inv = player.getInventory();
		
		return ItemUtil.compareDisplayName(HELMET, inv.getHelmet()) &&
				ItemUtil.compareDisplayName(CHESTPLATE, inv.getChestplate()) &&
				ItemUtil.compareDisplayName(LEGGINGS, inv.getChestplate()) &&
				ItemUtil.compareDisplayName(BOOTS, inv.getBoots());
	}
	
	@EventHandler
	public void rapidShot(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(!equals(manager.getPlayerClass(player)))
			return;
		
		switch(event.getAction()) {
		case LEFT_CLICK_AIR:
		case LEFT_CLICK_BLOCK:
			break;
		case RIGHT_CLICK_AIR:
		case RIGHT_CLICK_BLOCK:
			if(!rapidShots.containsKey(player))
				break;
			
			break;
		default:
			break;
		}
	}
}
