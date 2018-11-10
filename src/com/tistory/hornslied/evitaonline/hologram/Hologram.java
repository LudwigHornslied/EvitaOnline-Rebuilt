package com.tistory.hornslied.evitaonline.hologram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.tistory.hornslied.evitaonline.EvitaOnline;

import net.minecraft.server.v1_12_R1.EntityHorse;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityWitherSkull;
import net.minecraft.server.v1_12_R1.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;

public class Hologram {
	private static final double distance = 0.23;
	private List<String> lines = new ArrayList<String>();
	private List<Integer> ids = new ArrayList<Integer>();
	private boolean showing = false;
	private Location location;

	public Hologram(Location location, String... lines) {
		this.lines.addAll(Arrays.asList(lines));
		this.location = location;
	}
	
	public void addLine(String line) {
		destroy();
		lines.add(line);
		show();
	}
	
	public void setLine(int index, String line) {
		destroy();
		lines.set(index, line);
		show();
	}
	
	public void removeLine(int index) {
		destroy();
		lines.remove(index);
		show();
	}
	
	public int getLineNumber() {
		return lines.size();
	}

	public void change(String... lines) {
		destroy();
		this.lines = Arrays.asList(lines);
		show();
	}

	public void show() {
		if (showing == true) {
			try {
				throw new Exception("Is already showing!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Location first = location.clone().add(0, (this.lines.size() / 2) * distance, 0);
		for (int i = 0; i < this.lines.size(); i++) {
			ids.addAll(showLine(first.clone(), this.lines.get(i)));
			first.subtract(0, distance, 0);
		}
		showing = true;
	}

	public void show(long ticks) {
		show();
		new BukkitRunnable() {
			@Override
			public void run() {
				destroy();
			}
		}.runTaskLater(EvitaOnline.getInstance(), ticks);
	}

	public void destroy() {
		if (showing == false) {
			try {
				throw new Exception("Isn't showing!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int[] ints = new int[this.ids.size()];
		for (int j = 0; j < ints.length; j++) {
			ints[j] = ids.get(j);
		}
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ints);
		for (Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
		showing = false;
	}

	private static List<Integer> showLine(Location loc, String text) {
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		EntityWitherSkull skull = new EntityWitherSkull(world);
		skull.setLocation(loc.getX(), loc.getY() + 1 + 55, loc.getZ(), 0, 0);
		PacketPlayOutSpawnEntity skull_packet = new PacketPlayOutSpawnEntity(skull, 85);

		EntityHorse horse = new EntityHorse(world);
		horse.setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
		horse.setAge(-1700000);
		horse.setCustomName(text);
		horse.setCustomNameVisible(true);
		PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horse);
		for (Player player : loc.getWorld().getPlayers()) {
			EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
			nmsPlayer.playerConnection.sendPacket(packedt);
			nmsPlayer.playerConnection.sendPacket(skull_packet);

			PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(horse, skull);
			nmsPlayer.playerConnection.sendPacket(pa);
		}
		return Arrays.asList(skull.getId(), horse.getId());
	}

}