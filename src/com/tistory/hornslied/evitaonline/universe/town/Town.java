package com.tistory.hornslied.evitaonline.universe.town;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;

import com.tistory.hornslied.evitaonline.balance.BalanceOwner;
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
	
	private boolean mobSpawning;
	
	public Town(String name, UUID uuid, long registered) {
		super(uuid);
		this.name = name;
		this.registered = registered;
		
		residents = new HashSet<>();
		
		description = "마을 설명(/마을 설정 설명 <메시지>)";
		bonusBlocks = 0;
		balance = 0;
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
}
