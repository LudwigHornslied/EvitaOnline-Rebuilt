package com.tistory.hornslied.evitaonline.moderation;

import java.util.HashMap;
import java.util.UUID;

public class Record {
	
	private UUID uuid;
	
	private String lastIP;
	private HashMap<String, Integer> ipCounts;
	
	public Record(UUID uuid) {
		this.uuid = uuid;
		
		ipCounts = new HashMap<>();
	}
}
