package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class HologramCommand {

	@Command(aliases = { "hologram", "hd", "홀로그램" }, desc = "")
	@NestedCommand(value = SubCommand.class, executeBody = true)
	public static void hologram(CommandContext args, CommandSender sender) {
		
	}
	
	public static class SubCommand {
		
		@Command(aliases = { "create", "생성" }, desc = "", min = 2)
		@CommandPermissions("evitaonline.admin")
		public static void create(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = { "remove", "제거" }, desc = "", min = 1, max = 1)
		@CommandPermissions("evitaonline.admin")
		public static void remove(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = { "line", "" }, desc = "", min = 1, max = 1)
		@CommandPermissions("evitaonline.admin")
		public static void line(CommandContext args, CommandSender sender) throws CommandException {
			
		}
	}
}
