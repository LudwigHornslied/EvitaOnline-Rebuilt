package com.tistory.hornslied.evitaonline.chat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.ItemUtil;
import com.tistory.hornslied.evitaonline.permission.Perm;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class ChannelGUI implements InventoryProvider {
	
	public static SmartInventory CONTENT = SmartInventory.builder()
			.id("channelGui")
			.provider(new ChannelGUI())
			.size(1, 9)
			.title("채팅 채널")
			.build();
	
	private static ClickableItem CHANNEL_LOCAL = ClickableItem.of(
			ItemUtil.create(
					Material.GRASS, 
					C.BYellow + "지역채팅",
					C.BDarkGray + "50블럭 이내의 거리에 있는 사람에게만 보이는 채팅입니다."),
			e -> {
				Bukkit.dispatchCommand(e.getWhoClicked(), "channel local");
				e.getWhoClicked().closeInventory();
			});
	
	private static ClickableItem CHANNEL_TOWN = ClickableItem.of(
			ItemUtil.create(
					Material.IRON_CHESTPLATE, 
					C.BYellow + "마을채팅", 
					C.BDarkGray + "같은 마을에 소속된 사람에게만 보이는 채팅입니다."),
			e -> {
				Bukkit.dispatchCommand(e.getWhoClicked(), "channel town");
				e.getWhoClicked().closeInventory();
			});
	
	private static ClickableItem CHANNEL_NATION = ClickableItem.of(
			ItemUtil.create(
					Material.SHIELD,
					C.BYellow + "국가채팅", 
					C.BDarkGray + "같은 국가에 소속된 사람에게만 보이는 채팅입니다."),
			e -> {
				Bukkit.dispatchCommand(e.getWhoClicked(), "channel nation");
				e.getWhoClicked().closeInventory();
			});
	
	private static ClickableItem CHANNEL_GLOBAL = ClickableItem.of(
			ItemUtil.create(
					Material.EYE_OF_ENDER,
					C.BYellow + "전체채팅", 
					C.BDarkGray + "서버에 접속한 모두에게 보이는 채팅입니다."),
			e -> {
				Bukkit.dispatchCommand(e.getWhoClicked(), "channel global");
				e.getWhoClicked().closeInventory();
			});
	
	private static ClickableItem CHANNEL_ADMIN = ClickableItem.of(
			ItemUtil.create(
					Material.IRON_SWORD,
					C.BYellow + "어드민채팅", 
					C.BDarkGray + "관리자 전용 채널."),
			e -> {
				Bukkit.dispatchCommand(e.getWhoClicked(), "channel global");
				e.getWhoClicked().closeInventory();
			});

	@Override
	public void init(Player player, InventoryContents contents) {
		refreshGUI(player, contents);
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		refreshGUI(player, contents);
	}
	
	private void refreshGUI(Player player, InventoryContents contents) {
		EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(player);
		
		contents.set(0, 0, CHANNEL_LOCAL);
		
		int i = 1;
		if(evitaPlayer.hasTown()) {
			contents.set(0, i, CHANNEL_TOWN);
			i++;
		}
		
		if(evitaPlayer.hasNation()) {
			contents.set(0, i, CHANNEL_NATION);
			i++;
		}
		
		if(player.hasPermission(Perm.MOD)) {
			contents.set(0, i, CHANNEL_GLOBAL);
			i++;
			contents.set(0, i, CHANNEL_ADMIN);
			i++;
		}
	}

}
