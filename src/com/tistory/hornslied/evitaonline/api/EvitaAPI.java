package com.tistory.hornslied.evitaonline.api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.universe.Coord;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.EvitaWorld;
import com.tistory.hornslied.evitaonline.universe.plot.Plot;

public class EvitaAPI {

	private static EvitaOnline plugin = EvitaOnline.getInstance();

	public static EvitaWorld getEvitaWorld(World world) {
		return getEvitaWorld(world.getName());
	}

	public static EvitaWorld getEvitaWorld(String name) {
		return plugin.getUniverseManager().getEvitaWorld(name);
	}

	public static EvitaPlayer getEvitaPlayer(UUID uuid) {
		return plugin.getUniverseManager().getEvitaPlayer(uuid);
	}

	@Deprecated
	public static EvitaPlayer getEvitaPlayer(String name) {
		return plugin.getUniverseManager().getEvitaPlayer(Bukkit.getOfflinePlayer(name).getUniqueId());
	}

	public static EvitaPlayer getEvitaPlayer(Player player) {
		return getEvitaPlayer(player.getUniqueId());
	}
	
	public static Plot getPlot(Location location) {
		return getEvitaWorld(location.getWorld()).getPlot(Coord.parseCoord(location));
	}
	
	public static Plot getPlot(World world, int x, int z) {
		return getEvitaWorld(world.getName()).getPlot(Coord.parseCoord(x, z));
	}
}
