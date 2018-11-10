package com.tistory.hornslied.evitaonline.mine;

import java.sql.ResultSet;
import java.util.HashSet;

import org.bukkit.block.Block;
import org.bukkit.event.Listener;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.db.DBManager;
import com.tistory.hornslied.evitaonline.db.SQL;

public class MineManager implements Listener {

	private EvitaOnline plugin;
	
	private HashSet<Mineral> minerals;
	
	public MineManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		minerals = new HashSet<>();
		
		load();
	}
	
	private void load() {
		DBManager db = plugin.getDBManager();
		
		db.query(SQL.MINERALS);
		
		ResultSet rs = db.select("SELECT * FROM MINERALS");
		
		
	}
	
	public boolean isMineralBlock(Block block) {
		for(Mineral mineral : minerals) {
			if(mineral.isMineral(block))
				return true;
		}
		return false;
	}
}
