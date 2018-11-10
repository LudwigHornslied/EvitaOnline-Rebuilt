package com.tistory.hornslied.evitaonline.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.ItemUtil;
import com.tistory.hornslied.evitaonline.commons.util.NumberUtil;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

public class PurchasePage implements Listener {
	
	private HashMap<Player, PurchaseInfo> openedPlayers;
	
	private ItemStack blank = ItemUtil.create(Material.STAINED_GLASS_PANE, (short) 15, " ");
	private ItemStack confirm = ItemUtil.create(Material.STAINED_GLASS_PANE, (short) 5, C.Lime + "거래 확정");
	private ItemStack cancel = ItemUtil.create(Material.STAINED_GLASS_PANE, (short) 14, C.Red + "취소");

	private ItemStack plus1 = ItemUtil.create(Material.STAINED_GLASS_PANE, (short) 3, C.Yellow + "좌클릭: " + C.Lime + "+1", C.Yellow + "우클릭: " + C.Red + "-1");
	private ItemStack plus8 = ItemUtil.create(Material.STAINED_GLASS_PANE, (short) 3, C.Yellow + "좌클릭: " + C.Lime + "+8", C.Yellow + "우클릭: " + C.Red + "-8");
	private ItemStack plus16 = ItemUtil.create(Material.STAINED_GLASS_PANE, (short) 3, C.Yellow + "좌클릭: " + C.Lime + "+16", C.Yellow + "우클릭: " + C.Red + "-16");
	private ItemStack plus32 = ItemUtil.create(Material.STAINED_GLASS_PANE, (short) 3, C.Yellow + "좌클릭: " + C.Lime + "+32", C.Yellow + "우클릭: " + C.Red + "-32");
	private ItemStack plus64 = ItemUtil.create(Material.STAINED_GLASS_PANE, (short) 3, C.Yellow + "좌클릭: " + C.Lime + "+64", C.Yellow + "우클릭: " + C.Red + "-64");
	
	public PurchasePage() {
		openedPlayers = new HashMap<>();
	}
	
	private ItemStack getItemIcon(Goods goods, long balance, int amount, boolean buy) {
		ItemStack item = goods.getItem().clone();
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if(meta.hasLore()) {
			lore = meta.getLore();
		} else {
			lore = new ArrayList<>();
		}
		
		lore.add("");
		lore.add(C.Yellow + "수량: " + C.White + amount);
		lore.add(C.Gold + "소지금: " + C.White + NumberUtil.formatBalance(balance) + " 페론");
		lore.add((buy) ? C.Lime + "구매가: " + C.White + NumberUtil.formatBalance(goods.getBuyPrice() * amount) + " 페론" : C.Lime + "판매가: " + NumberUtil.formatBalance(goods.getSellPrice() * amount) + " 페론");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public void open(Player player, Goods goods, boolean buy) {
		Inventory gui = Bukkit.createInventory(null, 18, (buy) ? "아이템 구매" : "아이템 판매");
		
		gui.setItem(3, confirm);
		gui.setItem(4, getItemIcon(goods, EvitaAPI.getEvitaPlayer(player).getBalance(), 0, buy));
		gui.setItem(5, cancel);
		gui.setItem(11, plus1);
		gui.setItem(12, plus8);
		gui.setItem(13, plus16);
		gui.setItem(14, plus32);
		gui.setItem(15, plus64);
		
		while(gui.firstEmpty() >= 0) {
			gui.setItem(gui.firstEmpty(), blank);
		}
		
		openedPlayers.put(player, new PurchaseInfo(goods, buy));
		player.openInventory(gui);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(!(event.getWhoClicked() instanceof Player))
			return;
		
		if(openedPlayers.containsKey(event.getWhoClicked()))
			return;
		
		Player player = (Player) event.getWhoClicked();
		
		event.setCancelled(true);
		
		PurchaseInfo info = openedPlayers.get(player);
		
		EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(player);
		
		switch(event.getSlot()) {
		case 3:
			if(info.isBuy()) {
				if(!evitaPlayer.hasBalance(info.getGoods().getBuyPrice() * info.getAmount())) {
					player.closeInventory();
					player.sendMessage(P.NotEnoughBalance);
					return;
				}
				
			} else {
				if(!player.getInventory().contains(info.getGoods().getItem(), info.getAmount())) {
					return;
				}
			}
			break;
		case 5:
			Shop shop = info.getGoods().getShop();
			event.getWhoClicked().closeInventory();
			shop.openShop((Player) event.getWhoClicked());
			break;
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(openedPlayers.containsKey(event.getPlayer()))
			openedPlayers.remove(event.getPlayer());
	}
}
