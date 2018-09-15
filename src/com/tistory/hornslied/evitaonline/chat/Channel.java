package com.tistory.hornslied.evitaonline.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.permission.Perm;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

public enum Channel {
	GLOBAL("전체", ChatColor.YELLOW, ChatColor.WHITE) {
		
		@Override
		public boolean isShown(Player sender, Player receiver) {
			return true;
		}
	},
	LOCAL("지역", ChatColor.DARK_GRAY, ChatColor.WHITE) {
		@Override
		public boolean isShown(Player sender, Player receiver) {
			return sender.getLocation().distance(receiver.getLocation()) <= 50;
		}
	},
	TOWN("마을", ChatColor.AQUA, ChatColor.AQUA) {
		@Override
		public boolean isShown(Player sender, Player receiver) {
			EvitaPlayer evitaSender = EvitaAPI.getEvitaPlayer(sender);
			EvitaPlayer evitaReceiver = EvitaAPI.getEvitaPlayer(receiver);
			
			if(!evitaReceiver.hasTown() || !evitaSender.hasTown())
				return false;
			
			return evitaSender.getTown().equals(evitaReceiver.getTown());
		}
	},
	NATION("국가", ChatColor.GREEN, ChatColor.GREEN) {
		@Override
		public boolean isShown(Player sender, Player receiver) {
			EvitaPlayer evitaSender = EvitaAPI.getEvitaPlayer(sender);
			EvitaPlayer evitaReceiver = EvitaAPI.getEvitaPlayer(receiver);
			
			if(!evitaReceiver.hasNation() || !evitaSender.hasNation())
				return false;
			
			return evitaSender.getNation().equals(evitaReceiver.getNation());
		}
	},
	ADMIN("관리", ChatColor.RED, ChatColor.GOLD) {
		@Override
		public boolean isShown(Player sender, Player receiver) {
			return receiver.hasPermission(Perm.MOD);
		}
	};
	
	private String name;
	private ChatColor color;
	private ChatColor chatColor;
	
	Channel(String name, ChatColor color, ChatColor chatColor) {
		this.name = name;
		this.color = color;
		this.chatColor = chatColor;
	}
	
	public String getName() {
		return name;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public ChatColor getChatColor() {
		return chatColor;
	}
	
	public abstract boolean isShown(Player sender, Player receiver);
}
