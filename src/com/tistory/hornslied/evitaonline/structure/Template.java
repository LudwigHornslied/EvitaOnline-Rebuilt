package com.tistory.hornslied.evitaonline.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.material.MaterialData;

public class Template {

	private String name;
	private StructureType type;
	private HashMap<RelativeBlockCoord, MaterialData> datas;
	
	public Template(String name, FileConfiguration file) {
		this.name = name;
		datas = new HashMap<>();
		
		load(file);
	}
	
	@SuppressWarnings("deprecation")
	private void load(FileConfiguration file) {
		this.type = StructureType.valueOf(file.getString("TYPE"));
		
		ConfigurationSection blocks = file.getConfigurationSection("BLOCKS");
		Set<String> keys = blocks.getKeys(false);
		
		if(keys == null)
			return;
		
		for(String key : keys) {
			String[] coordData = key.split(":");
			RelativeBlockCoord coord = RelativeBlockCoord.parse(
					Integer.parseInt(coordData[0]), 
					Integer.parseInt(coordData[1]), 
					Integer.parseInt(coordData[2]));
			
			String[] typeData = blocks.getString(key).split(":");
			MaterialData blockData = new MaterialData(
					Material.valueOf(typeData[0]),
					(byte) Integer.parseInt(typeData[1]));
			
			datas.put(coord, blockData);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public StructureType getType() {
		return type;
	}
	
	public Map<RelativeBlockCoord, MaterialData> getDatas() {
		return datas;
	}
}
