package com.tistory.hornslied.evitaonline.universe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.PluginManager;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.db.*;
import com.tistory.hornslied.evitaonline.universe.listeners.*;
import com.tistory.hornslied.evitaonline.universe.nation.*;
import com.tistory.hornslied.evitaonline.universe.plot.*;
import com.tistory.hornslied.evitaonline.universe.town.*;

public class UniverseManager implements Listener {

	private EvitaOnline plugin;

	private ConcurrentHashMap<UUID, EvitaPlayer> players;
	private ConcurrentHashMap<String, EvitaWorld> worlds;
	private ConcurrentHashMap<UUID, Town> towns;
	private ConcurrentHashMap<UUID, AncientCity> ancientCities;
	private ConcurrentHashMap<UUID, Nation> nations;
	
	private Mine mine;
	
	private Set<String> blacklistedNames;
	private Set<String> blacklistedNationNames;

	public UniverseManager(EvitaOnline plugin) {
		this.plugin = plugin;

		players = new ConcurrentHashMap<>();
		worlds = new ConcurrentHashMap<>();
		towns = new ConcurrentHashMap<>();
		ancientCities = new ConcurrentHashMap<>();
		nations = new ConcurrentHashMap<>();
		
		mine = new Mine();
		
		blacklistedNames = new HashSet<>();
		blacklistedNationNames = new HashSet<>();

		try {
			load();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Bukkit.getPluginManager().registerEvents(this, plugin);
		registerListeners();
	}

	private void load() throws SQLException {
		DBManager db = plugin.getDBManager();
		
		//Create Tables
		db.query(SQL.WORLDS);
		db.query(SQL.NATIONS);
		db.query(SQL.TOWNS);
		db.query(SQL.ANCIENTCITIES);
		db.query(SQL.PLAYERS);
		db.query(SQL.PLOTS);
		
		convertDataFromTowny();
		
		//Load Worlds
		ResultSet worldSet = db.select("SELECT * FROM WORLDS");
		while (worldSet.next()) {
			EvitaWorld world = new EvitaWorld(worldSet.getString("name"));
			world.setClaimable(worldSet.getBoolean("claimable"));
			world.setEndermanGrief(worldSet.getBoolean("endermanGrief"));
			world.setMobSpawning(worldSet.getBoolean("mobSpawning"));
			
			String[] registeredMobs = worldSet.getString("registeredMobs").split("#");
			for (String name : registeredMobs)
				if (!name.equals(""))
					world.addRegisteredMob(EntityType.valueOf(name));

			worlds.put(world.getName(), world);
		}
		worldSet.close();
		
		//Load Nations
		ResultSet nationSet = db.select("SELECT * FROM NATIONS");
		while (nationSet.next()) {
			Nation nation = new Nation(
					nationSet.getString("name"),
					UUID.fromString(nationSet.getString("uuid")),
					nationSet.getLong("registered")
					);
			nations.put(nation.getUuid(), nation);
			blacklistedNationNames.add(nation.getName().toLowerCase());
		}
		nationSet.close();
		
		//Load Towns
		ResultSet townSet = db.select("SELECT * FROM TOWNS");
		while (townSet.next()) {
			Town town = new Town(
					townSet.getString("name"),
					UUID.fromString(townSet.getString("uuid")),
					townSet.getLong("registered")
					);
			
			town.setDescription(townSet.getString("description"));
			town.setBonusBlocks(townSet.getInt("bonusBlocks"));
			town.setPvP(townSet.getBoolean("pvp"));
			town.setMobSpawning(townSet.getBoolean("mobSpawning"));
			
			String[] spawn = townSet.getString("spawn").split("#");
			town.setSpawn(new Location(
					Bukkit.getWorld(spawn[0]), 
					Double.parseDouble(spawn[1]), 
					Double.parseDouble(spawn[2]), 
					Double.parseDouble(spawn[3]),
					Float.parseFloat(spawn[4]),
					Float.parseFloat(spawn[5])
					));
			
			if (townSet.getString("nation") != null) {
				Nation nation = nations.get(UUID.fromString(townSet.getString("nation")));
				town.setNation(nation);
				nation.addTown(town);
				
				if (townSet.getBoolean("isCapital"))
					nation.setCapital(town);
			}
			
			towns.put(town.getUuid(), town);
			blacklistedNames.add(town.getName().toLowerCase());
		}
		townSet.close();
		
		//Load AncientCities
		ResultSet acSet = db.select("SELECT * FROM ANCIENTCITIES");
		while (acSet.next()) {
			AncientCity ancientCity = new AncientCity(
					acSet.getString("name"),
					UUID.fromString(acSet.getString("uuid"))
					);
			ancientCities.put(ancientCity.getUuid(), ancientCity);
			blacklistedNames.add(ancientCity.getName().toLowerCase());
		}
		acSet.close();
		
		// Load Players
		ResultSet playerSet = db.select("SELECT * FROM PLAYERS");
		while (playerSet.next()) {
			EvitaPlayer player = new EvitaPlayer(UUID.fromString(playerSet.getString("uuid")));
			if (playerSet.getString("town") != null) {
				Town town = towns.get(UUID.fromString(playerSet.getString("town")));
				player.setTown(town);
				town.addPlayer(player);
				player.setTownRank(TownRank.valueOf(playerSet.getString("townRank")));
				
				if(player.getTownRank() == TownRank.MAYOR)
					town.setMayor(player);
			}
			players.put(player.getUuid(), player);
		}
		
		//Load Plots
		ResultSet plotSet = db.select("SELECT * FROM PLOTS");
		while (plotSet.next()) {
			PlotOwner plotOwner;
			switch (plotSet.getString("plotOwnerType")) {
			case "town":
				plotOwner = towns.get(UUID.fromString(plotSet.getString("plotOwner")));
				break;
			case "ancientcity":
				plotOwner = ancientCities.get(UUID.fromString(plotSet.getString("plotOwner")));
			case "mine":
				plotOwner = mine;
				break;
			default:
				continue;
			}
			
			EvitaWorld world = worlds.get(plotSet.getString("world"));
			Plot plot = new Plot(
					world,
					plotSet.getInt("x"),
					plotSet.getInt("z"),
					plotOwner
					);
			plot.setType(PlotType.valueOf(plotSet.getString("plotType")));
			if(plotSet.getString("owner") != null)
				plot.setOwner(players.get(UUID.fromString(plotSet.getString("owner"))));
			for (String uuid : plotSet.getString("residents").split("#")) {
				if(uuid != "")
					plot.addResident(players.get(UUID.fromString(uuid)));
			}
			plot.setPvP(plotSet.getBoolean("pvp"));
			plot.setMobSpawning(plotSet.getBoolean("mobSpawning"));
			world.addPlot(Coord.parseCoord(plotSet.getInt("x"), plotSet.getInt("z")), plot);
			plot.getPlotOwner().addPlot(plot);
		}
		plotSet.close();
	}
	
	private void convertDataFromTowny() {
		
	}
	
	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new ChunkListener(), plugin);
		pm.registerEvents(new TownBlockListener(), plugin);
		pm.registerEvents(new TownPvPListener(), plugin);
		pm.registerEvents(new WorldListener(), plugin);
	}
	
	public void saveNewWorld(String name) throws SQLException {
		saveNewWorld(worlds.get(name));
	}
	
	public void saveNewWorld(EvitaWorld world) throws SQLException {
		PreparedStatement ps = PSTable.save_new_world;
		
		ps.setString(1, world.getName());
		ps.setString(2, "");
		
		ps.executeUpdate();
	}
	
	public void saveNewTown(UUID uuid) throws SQLException {
		saveNewTown(towns.get(uuid));
	}
	
	public void saveNewTown(Town town) throws SQLException {
		Location spawn = town.getSpawn();
		
		PreparedStatement ps = PSTable.save_new_town;
		
		ps.setString(1, town.getName());
		ps.setString(2, town.getUuid().toString());
		ps.setLong(3, town.getRegistered());
		ps.setString(4, town.getMayor().getUuid().toString());
		ps.setString(5, spawn.getWorld().getName() + "#" + spawn.getX() + "#" + spawn.getY() + "#" + spawn.getZ() + "#" + spawn.getYaw() + "#" + spawn.getPitch());
		
		ps.executeUpdate();
	}
	
	public void saveNewPlayer(UUID uuid) throws SQLException {
		saveNewPlayer(players.get(uuid));
	}
	
	public void saveNewPlayer(EvitaPlayer player) throws SQLException {
		PreparedStatement ps = PSTable.save_new_player;
		
		ps.setString(1, player.getUuid().toString());
		ps.setString(2, "");
		
		ps.executeUpdate();
	}
	
	public void saveNewPlot(Plot plot) throws SQLException {
		PlotOwner plotOwner = plot.getPlotOwner();
		String type;
		if (plotOwner instanceof Town) {
			type = "town";
		} else if (plotOwner instanceof AncientCity) {
			type = "ancientCity";
		} else if (plotOwner instanceof Mine){
			type = "mine";
		} else {
			return;
		}
		
		PreparedStatement ps = PSTable.save_new_plot;
		
		ps.setString(1, plot.getWorld().getName());
		ps.setInt(2, plot.getX());
		ps.setInt(3, plot.getZ());
		ps.setString(4, type);
		ps.setString(5, (plotOwner instanceof UUIDPlotOwner) ? ((UUIDPlotOwner) plotOwner).getUuid().toString() : null);
		ps.setString(6, "");

		ps.executeUpdate();
	}
	
	public void saveWorld(EvitaWorld world) throws SQLException {
		StringBuilder registeredMobs = new StringBuilder();
		
		for(EntityType type : world.getRegisteredMobs()) {
			registeredMobs.append("#" + type.toString());
		}
		
		if(registeredMobs.length() > 0)
			registeredMobs.delete(0, 0);
		
		PreparedStatement ps = PSTable.save_world;
		
		ps.setBoolean(1, world.isClaimable());
		ps.setBoolean(2, world.isEndermanGrief());
		ps.setBoolean(3, world.isMobSpawning());
		ps.setString(4, registeredMobs.toString());
		ps.setString(5, world.getName());
		
		ps.executeUpdate();
	}
	
	public void saveNation(Nation nation) {
		
	}
	
	public void saveTown(Town town) throws SQLException {
		Location spawn = town.getSpawn();
		String spawnString = spawn.getX() + "#"
							+ spawn.getY() + "#"
							+ spawn.getZ() + "#"
							+ spawn.getYaw() + "#"
							+ spawn.getPitch();
		
		PreparedStatement ps = PSTable.save_town;
		
		ps.setString(1, town.getName());
		ps.setString(2, town.getMayor().getUuid().toString());
		ps.setString(3, spawnString);
		ps.setString(4, town.getDescription());
		ps.setInt(5, town.getBonusBlocks());
		ps.setString(6, (town.hasNation() ? town.getNation().getUuid().toString() : null));
		ps.setBoolean(7, town.isCapital());
		ps.setBoolean(8, town.isPvp());
		ps.setBoolean(9, town.isMobSpawning());

		ps.executeUpdate();
	}

	public void savePlayer(EvitaPlayer player) throws SQLException {
		StringBuilder ignoredPlayers = new StringBuilder();
		
		for(EvitaPlayer ignoredPlayer : player.getIgnoredPlayers()) {
			ignoredPlayers.append("#" + ignoredPlayer.getUuid().toString());
		}
		
		if(ignoredPlayers.length() > 0)
			ignoredPlayers.delete(0, 0);
		
		PreparedStatement ps = PSTable.save_player;
		
		ps.setString(1, player.getTown().getUuid().toString());
		ps.setString(2, player.getRank().toString());
		ps.setString(3, (player.hasNation()) ? player.getNationRank().toString() : null);
		ps.setString(4, (player.hasTown()) ? player.getTownRank().toString() : null);
		ps.setLong(5, player.getBalance());
		ps.setInt(6, player.getCredit());
		ps.setInt(7, player.getKill());
		ps.setInt(8, player.getDeath());
		ps.setInt(9, player.getDonation());
		ps.setString(10, ignoredPlayers.toString());
		ps.setString(11, player.getUuid().toString());

		ps.executeUpdate();
	}
	
	public void savePlot(Plot plot) throws SQLException {
		PlotOwner plotOwner = plot.getPlotOwner();
		String plotOwnerType;
		if (plotOwner instanceof Town) {
			plotOwnerType = "town";
		} else if (plotOwner instanceof AncientCity) {
			plotOwnerType = "ancientCity";
		} else if (plotOwner instanceof Mine) {
			plotOwnerType = "mine";
		} else {
			return;
		}
		
		StringBuilder residents = new StringBuilder();
		
		for(EvitaPlayer resident : plot.getResidents()) {
			residents.append("#" + resident.getUuid().toString());
		}
		
		if(residents.length() > 0)
			residents.delete(0, 0);
		
		PreparedStatement ps = PSTable.save_plot;
		
		ps.setString(1, plotOwnerType);
		ps.setString(2, (plotOwner instanceof UUIDPlotOwner) ? ((UUIDPlotOwner) plotOwner).getUuid().toString() : null);
		ps.setString(3, plot.getType().toString());
		ps.setString(4, plot.getOwner().getUuid().toString());
		ps.setString(5, residents.toString());
		ps.setBoolean(6, plot.isPvp());
		ps.setBoolean(7, plot.isMobSpawning());
		ps.setString(8, plot.getWorld().getName());
		ps.setInt(9, plot.getX());
		ps.setInt(10, plot.getZ());

		ps.executeUpdate();
	}
	
	public void newTown(String name, EvitaPlayer player, Location spawn) {
		Town town = new Town(name, UUID.randomUUID(), System.currentTimeMillis());
		town.addPlayer(player);
		town.setMayor(player);
		town.setSpawn(spawn);
		player.setTown(town);
		player.setTownRank(TownRank.MAYOR);
		towns.put(town.getUuid(), town);
		try {
			saveNewTown(town);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		blacklistedNames.add(name.toLowerCase());
		EvitaAPI.getEvitaWorld(spawn.getWorld()).newPlot(Coord.parseCoord(spawn.getChunk()), town);
	}
	
	public void deleteTown(Town town) {
		
	}

	public EvitaPlayer getEvitaPlayer(UUID uuid) {
		return players.get(uuid);
	}

	public EvitaWorld getEvitaWorld(String name) {
		return worlds.get(name);
	}
	
	public Town getTown(UUID uuid) {
		return towns.get(uuid);
	}
	
	public Town getTown(String name) {
		for(Town town : towns.values())
			if(town.getName().equalsIgnoreCase(name))
				return town;
		
		return null;
	}
	
	public Collection<Town> getTowns() {
		return towns.values();
	}
	
	public Nation getNation(UUID uuid) {
		return nations.get(uuid);
	}
	
	public Collection<Nation> getNations() {
		return nations.values();
	}
	
	public boolean isBlackListedName(String name) {
		return !Pattern.matches("^[a-zA-Z]*$", name) || blacklistedNames.contains(name.toLowerCase());
	}
	
	public boolean isBlackListedNationName(String name) {
		return !Pattern.matches("^[a-zA-Z]*$", name) || blacklistedNames.contains(name.toLowerCase());
	}
	
	// EventHandler

	@EventHandler
	public void onWorldInit(WorldInitEvent event) {
		String worldName = event.getWorld().getName();

		if (!worlds.containsKey(worldName)) {
			worlds.put(worldName, new EvitaWorld(worldName));
			try {
				saveNewWorld(worldName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		String worldName = event.getWorld().getName();

		if (!worlds.containsKey(worldName)) {
			worlds.put(worldName, new EvitaWorld(worldName));
			try {
				saveNewWorld(worldName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(!players.containsKey(player.getUniqueId())) {
			players.put(player.getUniqueId(), new EvitaPlayer(player.getUniqueId()));
			try {
				saveNewPlayer(player.getUniqueId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
