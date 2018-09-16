package com.tistory.hornslied.evitaonline.commons.util;

import java.util.Arrays;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {

	public static ItemStack create(Material type, String displayName, String... lore) {
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}
	
	public static boolean compareDisplayName(ItemStack o1, ItemStack o2) {
		if(o1 == null && o2 == null)
			return true;
		
		if(o1 == null && o2 != null)
			return false;
		
		if(o1 != null && o2 == null)
			return false;
		
		if(o1.getType() != o2.getType())
			return false;
		
		if(o1.hasItemMeta() != o2.hasItemMeta())
			return false;
		
		if(!o1.hasItemMeta())
			return true;
		
		ItemMeta meta1 = o1.getItemMeta();
		ItemMeta meta2 = o2.getItemMeta();

		return Objects.equals(meta1.getDisplayName(), meta2.getDisplayName());
	}
}
