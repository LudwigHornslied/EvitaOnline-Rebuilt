package com.tistory.hornslied.evitaonline.commons.util.help;

public class HelpRow {
	
	private String command;
	private String description;
	
	public HelpRow(String command, String description) {
		this.command = command;
		this.description = description;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getDesc() {
		return description;
	}
}
