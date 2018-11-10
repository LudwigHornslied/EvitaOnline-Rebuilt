package com.tistory.hornslied.evitaonline.crates;

import java.util.Set;

import org.bukkit.material.MaterialData;

public class Crate {
	
	private String name;
	private String displayName;
	private MaterialData block;
	
	private Set<CrateItem> crateItem;
	
	public Crate() {
		
	}
}
