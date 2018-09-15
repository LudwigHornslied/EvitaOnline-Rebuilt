package com.tistory.hornslied.evitaonline.db;

public class SQL {
	
	public static String PLAYERS = "CREATE TABLE IF NOT EXISTS PLAYERS ("
								+ "uuid VARCHAR(36) NOT NULL,"
								+ "town VARCHAR(36) DEFAULT NULL,"
								+ "rank VARCHAR(16) DEFAULT 'USER',"
								+ "nationRank VARCHAR(16) DEFAULT NULL,"
								+ "townRank VARCHAR(16) DEFAULT NULL,"
								+ "balance BIGINT NOT NULL DEFAULT 0,"
								+ "credit INT NOT NULL DEFAULT 0,"
								+ "killCount INT NOT NULL DEFAULT 0,"
								+ "deathCount INT NOT NULL DEFAULT 0,"
								+ "donation INT NOT NULL DEFAULT 0,"
								+ "ignoredPlayers MEDIUMTEXT NOT NULL,"
								+ "PRIMARY KEY(uuid)"
								+ ")";
	
	public static String WORLDS = "CREATE TABLE IF NOT EXISTS WORLDS ("
								+ "name VARCHAR(32) NOT NULL,"
								+ "claimable BOOL NOT NULL DEFAULT 1,"
								+ "endermanGrief BOOL NOT NULL DEFAULT 0,"
								+ "mobSpawning BOOL NOT NULL DEFAULT 0,"
								+ "registeredMobs MEDIUMTEXT NOT NULL,"
								+ "PRIMARY KEY(name)"
								+ ")";

	public static String TOWNS = "CREATE TABLE IF NOT EXISTS TOWNS (" 
								+ "name VARCHAR(16) NOT NULL,"
								+ "uuid VARCHAR(36) NOT NULL,"
								+ "registered BIGINT DEFAULT NULL,"
								+ "mayor VARCHAR(36) NOT NULL,"
								+ "spawn MEDIUMTEXT NOT NULL,"
								+ "description VARCHAR(25) NOT NULL DEFAULT '마을 설명(/마을 설정 설명 <메시지>)',"
								+ "bonusBlocks INT NOT NULL DEFAULT 0,"
								+ "nation VARCHAR(36) DEFAULT NULL,"
								+ "isCapital BOOL NOT NULL DEFAULT 0,"
								+ "pvp BOOL NOT NULL DEFAULT 0,"
								+ "mobSpawning BOOL NOT NULL DEFAULT 0,"
								+ "PRIMARY KEY(uuid)"
								+ ")";
	
	public static String ANCIENTCITIES = "CREATE TABLE IF NOT EXISTS ANCIENTCITIES ("
								+ "name VARCHAR(16) NOT NULL,"
								+ "uuid VARCHAR(36) NOT NULL,"
								+ "PRIMARY KEY(uuid)"
								+ ")";
	
	public static String PLOTS = "CREATE TABLE IF NOT EXISTS PLOTS ("
								+ "world VARCHAR(32) NOT NULL,"
								+ "x INT NOT NULL,"
								+ "z INT NOT NULL,"
								+ "plotOwnerType VARCHAR(16) NOT NULL,"
								+ "plotOwner VARCHAR(36),"
								+ "plotType VARCHAR(16) NOT NULL DEFAULT 'GENERAL',"
								+ "owner VARCHAR(36) DEFAULT NULL,"
								+ "residents MEDIUMTEXT NOT NULL,"
								+ "pvp BOOL NOT NULL DEFAULT 0,"
								+ "mobSpawning BOOL NOT NULL DEFAULT 0,"
								+ "PRIMARY KEY(world, x, z)"
								+ ")";
	
	public static String NATIONS = "CREATE TABLE IF NOT EXISTS NATIONS ("
								+ "name VARCHAR(16) NOT NULL,"
								+ "uuid VARCHAR(36) NOT NULL,"
								+ "registered BIGINT DEFAULT NULL,"
								+ "capital VARCHAR(36) NOT NULL,"
								+ "color VARCHAR(16) NOT NULL,"
								+ "PRIMARY KEY(uuid)"
								+ ")";
	
	//ELSE
	
	public static String DONATIONS = "CREATE TABLE IF NOT EXISTS DONATIONS ("
								+ "uuid VARCHAR(36) NOT NULL,"
								+ "amount INT NOT NULL DEFAULT 0,";
}
