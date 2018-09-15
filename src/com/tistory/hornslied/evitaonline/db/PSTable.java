package com.tistory.hornslied.evitaonline.db;

import java.sql.PreparedStatement;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class PSTable {
	
	private static DBManager db = EvitaOnline.getInstance().getDBManager();
	
	public static PreparedStatement save_new_world = db.getPreparedStatement(
			"INSERT INTO WORLDS (name, registeredMobs) VALUES (?, ?)"
			);
	
	public static PreparedStatement save_new_nation = db.getPreparedStatement(
			""
			);
	
	public static PreparedStatement save_new_town = db.getPreparedStatement(
			"INSERT INTO TOWNS (name, uuid, registered, mayor, spawn) VALUES (?, ?, ?, ?, ?)"
			);
	
	public static PreparedStatement save_new_player = db.getPreparedStatement(
			"INSERT INTO PLAYERS (uuid, ignoredPlayers) VALUES (?, ?)"
			);
	
	public static PreparedStatement save_new_plot = db.getPreparedStatement(
			"INSERT INTO PLOTS (world, x, z, plotOwnerType, plotOwner, residents) VALUES (?, ?, ?, ?, ?, ?)"
			);
	
	public static PreparedStatement save_world = db.getPreparedStatement(
			"UPDATE WORLDS SET claimable = ?, endermanGrief = ?, mobSpawning = ?, registeredMobs = ? WHERE = name = ?"
			);
	
	public static PreparedStatement save_nation = db.getPreparedStatement(
			"UPDATE NATIONS SET name = ? WHERE uuid = ?"
			);
	
	public static PreparedStatement save_town = db.getPreparedStatement(
			"UPDATE TOWNS SET name = ?, mayor = ?, spawn = ?, description = ?, bonusBlocks = ?, nation = ?, isCapital = ?, pvp = ?, mobSpawning = ? WHERE uuid = ?"
			);
	
	public static PreparedStatement save_player = db.getPreparedStatement(
			"UPDATE PLAYERS SET town = ?, rank = ?, nationRank = ?, townRank = ?, balance = ?, credit = ?, killCount = ?, deathCount = ?, donation = ?, ignoredPlayers = ? WHERE uuid = ?"
			);
	
	public static PreparedStatement save_plot = db.getPreparedStatement(
			"UPDATE PLOTS SET plotOwnerType = ?, plotOwner = ?, plotType = ?, owner = ?, residents = ?, pvp = ?, mobSpawning = ? WHERE world = ?, x = ?, z = ?"
			);
}
