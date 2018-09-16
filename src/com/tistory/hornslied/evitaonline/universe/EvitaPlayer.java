package com.tistory.hornslied.evitaonline.universe;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.balance.BalanceOwner;
import com.tistory.hornslied.evitaonline.rank.Rank;
import com.tistory.hornslied.evitaonline.universe.nation.Nation;
import com.tistory.hornslied.evitaonline.universe.nation.NationRank;
import com.tistory.hornslied.evitaonline.universe.town.Town;
import com.tistory.hornslied.evitaonline.universe.town.TownRank;

public class EvitaPlayer implements BalanceOwner {
	
	private UUID uuid;
	private Rank rank;
	private Town town;
	
	private NationRank nationRank;
	private TownRank townRank;
	
	private long balance;
	private int donation;
	private int credit;
	
	private int kill;
	private int death;
	private Set<EvitaPlayer> ignoredPlayers;
	
	public EvitaPlayer(UUID uuid) {
		this.uuid = uuid;
		rank = Rank.USER;
		town = null;
		
		nationRank = null;
		townRank = null;
		
		balance = 0;
		donation = 0;
		credit = 0;
		
		kill = 0;
		death = 0;
		
		ignoredPlayers = new HashSet<>();
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public void setTown(Town town) {
		this.town = town;
	}
	
	public void setNationRank(NationRank rank) {
		nationRank = rank;
	}
	
	public void setTownRank(TownRank rank) {
		townRank = rank;
	}
	
	public void setDonation(int amount) {
		donation = amount;
	}
	
	public void setCredit(int amount) {
		credit = amount;
	}
	
	public void setKill(int kill) {
		this.kill = kill;
	}
	
	public void setDeath(int death) {
		this.death = death;
	}
	
	public void addIgnoredPlayer(EvitaPlayer player) {
		ignoredPlayers.add(player);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public String getName() {
		return Bukkit.getOfflinePlayer(uuid).getName();
	}
	
	public Town getTown() {
		return town;
	}
	
	public Nation getNation() {
		return town.getNation();
	}
	
	public NationRank getNationRank() {
		return nationRank;
	}
	
	public TownRank getTownRank() {
		return townRank;
	}
	
	public int getDonation() {
		return donation;
	}
	
	public int getCredit() {
		return credit;
	}
	
	public int getKill() {
		return kill;
	}
	
	public int getDeath() {
		return death;
	}
	
	public Set<EvitaPlayer> getIgnoredPlayers() {
		return ignoredPlayers;
	}
	
	public boolean hasTown() {
		return town != null;
	}
	
	public boolean hasNation() {
		return hasTown() && town.hasNation();
	}
	
	public boolean isMayor() {
		return town != null && equals(town.getMayor());
	}
	
	public boolean isLeader() {
		return town != null && town.getNation() != null && equals(town.getNation().getLeader());
	}
	
	public boolean isPlayerIgnored(EvitaPlayer player) {
		return ignoredPlayers.contains(player);
	}

	@Override
	public void setBalance(long balance) {
		this.balance = balance;
		try {
			EvitaOnline.getInstance().getUniverseManager().savePlayer(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getBalance() {
		return balance;
	}
	
	@Override
	public boolean hasBalance(long amount) {
		return balance >= amount;
	}

	@Override
	public void deposit(long balance) {
		this.balance += balance;
		try {
			EvitaOnline.getInstance().getUniverseManager().savePlayer(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void withdraw(long balance) {
		this.balance -= balance;
		try {
			EvitaOnline.getInstance().getUniverseManager().savePlayer(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
