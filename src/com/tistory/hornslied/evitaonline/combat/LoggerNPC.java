package com.tistory.hornslied.evitaonline.combat;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.mojang.authlib.GameProfile;
import com.tistory.hornslied.evitaonline.commons.util.PlayerNMSResult;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityVillager;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;

public class LoggerNPC extends EntityVillager {

	public LoggerNPC(Player player) {
		super(((CraftWorld) player.getWorld()).getHandle());
		lastDamager = ((CraftPlayer) player).getHandle().lastDamager;
		
		Location location = player.getLocation();
		
		setPosition(location.getX(), location.getY(), location.getZ());
		
		if(((CraftWorld) player.getWorld()).getHandle().addEntity(this, SpawnReason.CUSTOM)) {
			setCustomName(ChatColor.AQUA + player.getName());
			setCustomNameVisible(true);
			setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		}
	}
	
	private static PlayerNMSResult getResult(World world, UUID playerUUID) {
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
		if (offlinePlayer.hasPlayedBefore()) {
			WorldServer worldServer = ((CraftWorld) world).getHandle();
			EntityPlayer entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), worldServer,
					new GameProfile(playerUUID, offlinePlayer.getName()), new PlayerInteractManager(worldServer));
			CraftPlayer player = entityPlayer.getBukkitEntity();
			if (player != null) {
				player.loadData();
				return new PlayerNMSResult(player, entityPlayer);
			}
		}

		return null;
	}

	@Override
	public boolean damageEntity(DamageSource damageSource, float amount) {
		return false;
	}
	
	@Override
	public boolean a(EntityHuman entityHuman) {
		return false;
	}
	
	@Override
	public void die(DamageSource damageSource) {
		
	}

}
