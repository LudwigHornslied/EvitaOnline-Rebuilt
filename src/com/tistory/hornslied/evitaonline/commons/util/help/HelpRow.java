package com.tistory.hornslied.evitaonline.commons.util.help;

import java.util.ArrayList;
import java.util.List;

public class HelpRow {
	
	private String command;
	private String description;
	private List<String> aliases;
	
	public HelpRow(String command, String description) {
		this.command = command;
		this.description = description;
		aliases = new ArrayList<String>();
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getDesc() {
		return description;
	}
	
	public List<String> getAliases() {
		return aliases;
	}
}
