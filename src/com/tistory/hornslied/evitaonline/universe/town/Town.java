package com.tistory.hornslied.evitaonline.universe.town;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.balance.BalanceOwner;
import com.tistory.hornslied.evitaonline.structure.Structure;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.nation.Nation;
import com.tistory.hornslied.evitaonline.universe.plot.UUIDPlotOwner;

public class Town extends UUIDPlotOwner implements BalanceOwner {
	
	private String name;
	private long registered;
	
	private Set<EvitaPlayer> residents;
	private Nation nation;
	private EvitaPlayer mayor;
	private Location spawn;
	private String description;
	private int bonusBlocks;
	
	private long balance;
	
	private Set<Structure> structures;
	
	private boolean mobSpawning;
	private boolean buildable;
	
	public Town(String name, UUID uuid, long registered) {
		super(uuid);
		this.name = name;
		this.registered = registered;
		
		residents = new HashSet<>();
		
		description = "마을 설명(/마을 설정 설명 <메시지>)";
		bonusBlocks = 0;
		balance = 0;
		
		structures = new HashSet<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNation(Nation nation) {
		this.nation = nation;
	}
	
	public void setMayor(EvitaPlayer player) {
		this.mayor = player;
	}
	
	public void setSpawn(Location location) {
		this.spawn = location;
	}
	
	public void setDescription(String desc) {
		description = desc;
	}
	
	public void setBonusBlocks(int number) {
		this.bonusBlocks = number;
	}
	
	public void setMobSpawning(boolean mobSpawning) {
		this.mobSpawning = mobSpawning;
	}
	
	public void setBuildable(boolean buildable) {
		this.buildable = buildable;
	}
	
	public void addPlayer(EvitaPlayer player) {
		residents.add(player);
	}
	
	public String getName() {
		return name;
	}
	
	public long getRegistered() {
		return registered;
	}
	
	public Nation getNation() {
		return nation;
	}
	
	public EvitaPlayer getMayor() {
		return mayor;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getBonusBlocks() {
		return bonusBlocks;
	}
	
	public Set<EvitaPlayer> getResidents() {
		return residents;
	}
	
	public int getResidentsNumber() {
		return residents.size();
	}
	
	public Set<Player> getOnlinePlayers() {
		Set<Player> out = new HashSet<>();
		for(EvitaPlayer resident : residents) {
			Player player = Bukkit.getPlayer(resident.getUuid());
			
			if(player == null || !player.isOnline())
				continue;
			
			out.add(player);
		}
		return out;
	}
	
	public int getMaxPlot() {
		return residents.size() * 7 + bonusBlocks;
	}
	
	public boolean hasNation() {
		return nation != null;
	}
	
	public boolean hasResident(EvitaPlayer player) {
		return residents.contains(player);
	}

	public boolean isCapital() {
		return nation != null & equals(nation.getCapital());
	}
	
	public boolean isMobSpawning() {
		return mobSpawning;
	}
	
	public boolean isBuildable() {
		return buildable;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void setBalance(long balance) {
		this.balance = balance;
	}

	@Override
	public long getBalance() {
		return balance;
	}

	@Override
	public boolean hasBalance(long amount) {
		return amount >= balance;
	}

	@Override
	public void deposit(long balance) {
		this.balance += balance;
	}

	@Override
	public void withdraw(long balance) {
		this.balance -= balance;
	}

	@Override
	public void saveBalance() {
		try {
			EvitaOnline.getInstance().getUniverseManager().saveTown(this);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
