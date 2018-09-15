package com.tistory.hornslied.evitaonline.commons.util.help;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.ChildJsonMessage;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.ClickEvent;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.Color;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.HoverEvent;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.JsonMessage;

public class Help {
	
	private String title;
	private List<HelpPage> pages;
	
	private String helpCommand;
	
	public Help(String title, String helpCommand) {
		this.title = title;
		this.helpCommand = helpCommand;
		
		pages = new ArrayList<>();
	}
	
	public HelpPage addPage() {
		HelpPage page = HelpPage.newInstance();
		pages.add(page);
		return page;
	}
	
	public String[] buildString(int index) {
		if (pages.size() == 0)
			return null;
		
		List<HelpRow> rows = pages.get(index).getRows();
		String[] content = new String[rows.size() + 2];
		content[0] = P.Line;
		content[1] = C.BLime + title + " " + C.Gray + "(" + (index+1) + "/" + pages.size() + ")";
		content[content.length -1] = P.Line;
		
		int i = 2;
		for(HelpRow row : pages.get(index).getRows()) {
			content[i] = C.Yellow + row.getCommand() + C.DarkGray + " - " + C.Gray + row.getDesc();
			i++;
		}
		
		return content;
	}
	

	public JsonMessage[] buildJson(int index) {
		if (pages.size() == 0)
			return null;
		
		List<HelpRow> rows = pages.get(index).getRows();
		JsonMessage[] content = new JsonMessage[rows.size() + ((pages.size() == 1) ? 3 : 4)];
		content[0] = new JsonMessage(P.Line);
		content[1] = new JsonMessage(C.BLime + title + " " + C.Gray + "(" + (index+1) + "/" + pages.size() + ")");
		content[content.length -1] = new JsonMessage(P.Line);
		
		int i = 2;
		for(HelpRow row : rows) {
			content[i] = getCommandRow(row);
			i++;
		}
		
		if (pages.size() != 1) {
			ChildJsonMessage pagination = new JsonMessage("").extra("");
			
			if (index == 0) {
				pagination.add("[다음]")
						.color(Color.GRAY)
						.click(ClickEvent.RUN_COMMAND, helpCommand.replace("<index>", "2"));
			} else if (index == pages.size() -1) {
				pagination.add("[이전]")
						.color(Color.GRAY)
						.click(ClickEvent.RUN_COMMAND, helpCommand.replace("<index>", Integer.toString(pages.size() - 1)));
			} else {
				pagination.add("[이전]")
						.color(Color.GRAY)
						.click(ClickEvent.RUN_COMMAND, helpCommand.replace("<index>", Integer.toString(index)));
				pagination.add(" / ")
						.color(Color.DARK_GRAY);
				pagination.add("[다음]")
						.color(Color.GRAY)
						.click(ClickEvent.RUN_COMMAND, helpCommand.replace("<index>", Integer.toString(index + 2)));
			}
			
			content[content.length -2] = pagination;
		}
		
		return content;
	}
	
	private JsonMessage getCommandRow(HelpRow row) {
		ChildJsonMessage content = new JsonMessage("").extra("");
		
		content.add(row.getCommand())
				.color(Color.YELLOW)
				.hover(HoverEvent.SHOW_TEXT, "")
				.click(ClickEvent.SUGGEST_COMMAND, row.getCommand());
		content.add(" - ")
				.color(Color.DARK_GRAY);
		content.add(row.getDesc())
				.color(Color.GRAY);
		
		return content;
	}
	
	public int size() {
		return pages.size();
	}
	
	public void send(Player player, int index) {
		for(JsonMessage message : buildJson(index))
			message.send(player);
	}
}
