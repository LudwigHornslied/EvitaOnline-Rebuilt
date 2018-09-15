package com.tistory.hornslied.evitaonline.commons.util.help;

import java.util.ArrayList;
import java.util.List;

public class HelpPage {
	
	private List<HelpRow> rows;
	
	private HelpPage() {
		rows = new ArrayList<>();
	}
	
	public static HelpPage newInstance() {
		return new HelpPage();
	}
	
	public HelpPage addRow(HelpRow row) {
		rows.add(row);
		return this;
	}
	
	public List<HelpRow> getRows() {
		return rows;
	}
}
