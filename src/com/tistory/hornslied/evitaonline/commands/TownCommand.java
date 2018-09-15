package com.tistory.hornslied.evitaonline.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
import com.tistory.hornslied.evitaonline.commons.util.help.Help;
import com.tistory.hornslied.evitaonline.commons.util.help.HelpRow;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.JsonMessage;
import com.tistory.hornslied.evitaonline.permission.Perm;
import com.tistory.hornslied.evitaonline.universe.Coord;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.EvitaWorld;
import com.tistory.hornslied.evitaonline.universe.UniverseManager;
import com.tistory.hornslied.evitaonline.universe.town.Town;

public class TownCommand {
	
	private static Help help = new Help("마을 명령어", "/town help <index>");
	
	private static EvitaOnline plugin = EvitaOnline.getInstance();
	
	static {
		help.addPage()
		.addRow(new HelpRow("/마을 정보", ""))
		.addRow(new HelpRow("/마을 귀환", "마을로 돌아옵니다."));
	}

	@Command(aliases = { "town", "마을" }, desc = "", max = -1)
	@NestedCommand(value = SubCommand.class, executeBody = true)
	public static void town(CommandContext args, CommandSender sender) {
		if (sender instanceof Player) {
			for (JsonMessage message : help.buildJson(0)) {
				message.sendToPlayer((Player) sender);
			}
		} else {
			sender.sendMessage(help.buildString(0));
		}
	}
	
	public static class SubCommand {
		@Command(aliases = {"?", "help", "도움말"}, desc = "", min = 0, max = 1)
		public static void help(CommandContext args, CommandSender sender) throws CommandException {
			if (args.argsLength() == 0) {
				if (sender instanceof Player) {
					for (JsonMessage message : help.buildJson(0)) {
						message.sendToPlayer((Player) sender);
					}
				} else {
					sender.sendMessage(help.buildString(0));
				}
			} else {
				int index = args.getInteger(0);
				if (index > help.size())
					throw new CommandException("해당 페이지가 존재하지 않습니다!");
				
				if (sender instanceof Player) {
					for (JsonMessage message : help.buildJson(index -1)) {
						message.sendToPlayer((Player) sender);
					}
				} else {
					sender.sendMessage(help.buildString(index -1));
				}
			}
		}
		
		@Command(aliases = { "info", "정보" }, desc = "", min = 0, max = 1)
		public static void info(CommandContext args, CommandSender sender) throws CommandException {
			if(args.argsLength() == 0) {
				if (!(sender instanceof Player))
					throw new CommandSenderTypeException();
				
				EvitaPlayer player = EvitaAPI.getEvitaPlayer((Player) sender);
				
				if(!player.hasTown())
					throw new CommandException("마을에 소속되어 있지 않습니다.");
				
				
			} else {
				
			}
		}
		
		@Command(aliases = { "spawn", "스폰", "귀환" }, desc = "", min = 0, max = 1)
		public static void spawn(CommandContext args, CommandSender sender) throws CommandException {
			if(args.argsLength() == 0) {
				if (!(sender instanceof Player))
					throw new CommandSenderTypeException();
				
				EvitaPlayer player = EvitaAPI.getEvitaPlayer((Player) sender);
				
				if(!player.hasTown())
					throw new CommandException("마을에 소속되어 있지 않습니다.");
				
				
			} else {
				
			}
		}
		
		@Command(aliases = { "create", "생성" }, desc = "", min = 1, max = 1)
		public static void create(CommandContext args, CommandSender sender) throws CommandException {
			if (!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			Player player = (Player) sender;
			EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(player);
			Location location = player.getLocation();
			EvitaWorld world = EvitaAPI.getEvitaWorld(location.getWorld());
			
			if(evitaPlayer.hasTown())
				throw new CommandException("이미 소속된 마을이 있습니다.");
			
			if (!world.isClaimable())
				throw new CommandException("영토를 취득할 수 없는 월드입니다.");
			
			if (EvitaAPI.getPlot(location) != null)
				throw new CommandException("이미 영토가 있는 지역입니다.");
			
			if (world.getMinDistanceFromOtherTown(location, null) < 6)
				throw new CommandException("다른 마을에서 너무 가깝습니다.");
			
			UniverseManager universeManager = plugin.getUniverseManager();
			
			String name = args.getString(0);
			if(universeManager.isBlackListedName(name))
				throw new CommandException("이미 존재하는 마을이름이거나 사용이 불가능한 이름입니다.");
			
			universeManager.newTown(name, evitaPlayer, location);
			try {
				universeManager.savePlayer(evitaPlayer);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Bukkit.broadcastMessage(P.Universe + C.Aqua + name + " 마을이 탄생하였습니다. 시장: " + sender.getName());
		}
		
		@Command(aliases = { "delete", "삭제" }, desc = "", min = 0, max = 1)
		@CommandPermissions("evitaonline.mayor")
		public static void delete(CommandContext args, CommandSender sender) throws CommandException {
			if(args.argsLength() == 0) {
				if (!(sender instanceof Player))
					throw new CommandSenderTypeException();
				
				Player player = (Player) sender;
				EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(player);
				
				if(!evitaPlayer.hasTown())
					throw new CommandException("소속된 마을이 없습니다.");
				
				Town town = evitaPlayer.getTown();
				
				if(town.isCapital())
					throw new CommandException("국가의 수도인 상태에서 마을을 해체할 수 없습니다.");
			} else {
				
			}
		}
		
		@Command(aliases = { "claim", "영토취득" }, desc = "", min = 0, max = 1)
		@CommandPermissions("evitaonline.vicemayor")
		public static void claim(CommandContext args, CommandSender sender) throws CommandException {
			if (!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			Player player = (Player) sender;
			EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(player);
			
			if(!evitaPlayer.hasTown())
				throw new CommandException("소속된 마을이 없습니다.");
			
			Town town = evitaPlayer.getTown();
			Location location = player.getLocation();
			EvitaWorld evitaWorld = EvitaAPI.getEvitaWorld(location.getWorld());

			if (!evitaWorld.isClaimable())
				throw new CommandException("영토를 취득할 수 없는 월드입니다.");
			
			if (EvitaAPI.getPlot(location) != null)
				throw new CommandException("이미 영토가 있는 지역입니다.");
			
			if (evitaWorld.getMinDistanceFromOtherTown(location, town) < 6)
				throw new CommandException("다른 마을에서 너무 가깝습니다.");
			
			if(town.getMaxPlot() == town.getPlotNumber())
				throw new CommandException("영토 취득 한계에 이르렀습니다.");
			
			World world = location.getWorld();
			int x = location.getChunk().getX();
			int z = location.getChunk().getZ();
			
			if(EvitaAPI.getPlot(world, x -1, z) == null &&
					EvitaAPI.getPlot(world, x, z -1) == null &&
					EvitaAPI.getPlot(world, x +1, z) == null &&
					EvitaAPI.getPlot(world, x, z +1) == null)
				throw new CommandException("마을 영토와 인접한 영역만 취득할 수 있습니다.");
			
			evitaWorld.newPlot(Coord.parseCoord(location), town);
		}
		
		@Command(aliases = { "set", "설정" }, desc = "", min = 0, max = 1)
		@CommandPermissions("evitaonline.vicemayor")
		@NestedCommand(Set.class)
		public static void set(CommandContext args, CommandSender sender) throws CommandException {
			
		}
		
		@Command(aliases = { "toggle", "토글" }, desc = "", min = 0, max = 1)
		@CommandPermissions("evitaonline.vicemayor")
		@NestedCommand(Toggle.class)
		public static void toggle(CommandContext args, CommandSender sender) throws CommandException {
			
		}
	}
	
	public static class Set {
		
		@Command(aliases = { "name", "이름" }, desc = "", min = 1, max = 1)
		public static void name(CommandContext args, CommandSender sender) throws CommandException {
			
		}
		
		@Command(aliases = { "spawn", "스폰" }, desc = "", min = 0, max = 0)
		public static void spawn() {
			
		}
		
		@Command(aliases = { "description", "desc", "설명" }, desc = "", min = 1, max = -1)
		public static void description() {
			
		}
	}
	
	public static class Toggle {
		
	}
	
	public static class Rank {
		
	}
}
