package com.tistory.hornslied.evitaonline.rank;

import org.bukkit.ChatColor;

public enum Rank {
	ADMIN("어드민", ChatColor.DARK_RED),
	MOD("관리자", ChatColor.DARK_PURPLE),
	USER("유저", ChatColor.DARK_GRAY);
	
	private String name;
	private ChatColor color;
	private String[] permissions;
	
	Rank(String name, ChatColor color, String... permissions) {
		this.name = name;
		this.color = color;
		this.permissions = permissions;
	}
	
	public String getName() {
		return name;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	public String[] getPermissions() {
		return permissions;
	}
}
