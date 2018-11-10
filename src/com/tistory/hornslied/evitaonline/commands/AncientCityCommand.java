package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandSenderTypeException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.universe.Coord;
import com.tistory.hornslied.evitaonline.universe.UniverseManager;
import com.tistory.hornslied.evitaonline.universe.town.AncientCity;

public class AncientCityCommand {
	
	private static UniverseManager manager = EvitaOnline.getInstance().getUniverseManager();

	@Command(aliases = { "ancientcity", "ac" }, desc = "", max = -1)
	@NestedCommand(value = SubCommand.class, executeBody = true)
	public static void ancientcity(CommandContext args, CommandSender sender) {
		
	}
	
	public static class SubCommand {
		
		@Command(aliases = { "info", "정보" }, desc = "", min = 1, max = 1)
		public static void info(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = { "list", "목록" }, desc = "", min = 0, max = 1)
		public static void list(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = { "create", "생성" }, desc = "", min = 1, max = 1)
		@CommandPermissions("evitaonline.admin")
		public static void create(CommandContext args, CommandSender sender) throws CommandException {
			String name = args.getString(0);
			
			if(manager.isBlackListedName(name))
				throw new CommandException("이미 존재하거나 사용이 불가능한 이름입니다.");
			
			manager.newAC(name);
		}

		@Command(aliases = { "claim", "영토취득" }, desc = "", min = 1, max = 1)
		@CommandPermissions("evitaonline.admin")
		public static void claim(CommandContext args, CommandSender sender) throws CommandException {
			if(!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			Player player = (Player) sender;
			AncientCity ac = manager.getAC(args.getString(0));
			Location location = player.getLocation();
			
			if(EvitaAPI.getPlot(location) != null)
				throw new CommandException("이미 영토가 있는 지역입니다.");
			
			if(ac == null)
				throw new CommandException("존재하지 않는 고대 도시입니다.");
			
			EvitaAPI.getEvitaWorld(location.getWorld()).newPlot(Coord.parseCoord(location), ac);
			sender.sendMessage(P.Universe + C.Aqua + "고대 도시 영토를 취득하였습니다. (" + location.getChunk().getX() + ", " + location.getChunk().getZ() + ")");
		}
	}
}
