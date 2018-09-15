package com.tistory.hornslied.evitaonline.classes;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.ItemUtil;

public class Cataphract extends Class {

	public static ItemStack HELMET;
	public static ItemStack CHESTPLATE;
	public static ItemStack LEGGINGS;
	public static ItemStack BOOTS;
	
	static {
		HELMET = ItemUtil.create(Material.DIAMOND_HELMET, C.BGreen + "중기병 모자");
		CHESTPLATE = ItemUtil.create(Material.DIAMOND_CHESTPLATE, C.BGreen + "중기병 흉갑");
		LEGGINGS = ItemUtil.create(Material.DIAMOND_LEGGINGS, C.BGreen + "중기병 하의");
		BOOTS = ItemUtil.create(Material.DIAMOND_BOOTS, C.BGreen + "중기병 부츠");
	}

	public Cataphract() {
		super("중기병");
	}

	@Override
	public boolean checkEquipping(Player player) {
		if(!player.isInsideVehicle())
			return false;
		
		if(!(player.getVehicle() instanceof Horse))
			return false;
		
		Horse horse = (Horse) player.getVehicle();
		
		if(horse.getInventory().getArmor() == null || horse.getInventory().getArmor().getType() != Material.DIAMOND_BARDING)
			return false;
		
		PlayerInventory inv = player.getInventory();
		
		return ItemUtil.compareDisplayName(HELMET, inv.getHelmet()) &&
				ItemUtil.compareDisplayName(CHESTPLATE, inv.getChestplate()) &&
				ItemUtil.compareDisplayName(LEGGINGS, inv.getChestplate()) &&
				ItemUtil.compareDisplayName(BOOTS, inv.getBoots());
	}
}
