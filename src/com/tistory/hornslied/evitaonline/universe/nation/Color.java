package com.tistory.hornslied.evitaonline.universe.nation;

public enum Color {
	BLACK("§0", 0x000000),
	DARK_BLUE("§1", 0x00002A),
	DARK_GREEN("§2", 0x002A00),
	DARK_AQUA("§3", 0x002A2A),
	DARK_RED("§4", 0x2A0000),
	DARK_PURPLE("§5", 0x2A002A),
	GOLD("§6", 0x2A2A00),
	GRAY("§7", 0x2A2A2A),
	DARK_GRAY("§8", 0x151515),
	BLUE("§9", 0x15153F),
	GREEN("§a", 0x153F15),
	AQUA("§b", 0x153F3F),
	RED("§c", 0x3F1515),
	LIGHT_PURPLE("§d", 0x3F153F),
	YELLOW("§e", 0x3F3F15);
	
	private String chatColor;
	private int hexColor;
	
	Color(String chatColor, int hexColor) {
		this.chatColor = chatColor;
		this.hexColor = hexColor;
	}
	
	public String getChatColor() {
		return chatColor;
	}
	
	public int getHexColor() {
		return hexColor;
	}
}
