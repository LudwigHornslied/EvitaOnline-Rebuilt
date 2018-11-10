package com.tistory.hornslied.evitaonline.commons.util;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.EntityPlayer;

public class PlayerNMSResult {

	private Player player;
	private EntityPlayer entityPlayer;
	
	public PlayerNMSResult(Player player, EntityPlayer entityPlayer) {
		this.player = player;
		this.entityPlayer = entityPlayer;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public EntityPlayer getEntityPlayer() {
		return entityPlayer;
	}
}
