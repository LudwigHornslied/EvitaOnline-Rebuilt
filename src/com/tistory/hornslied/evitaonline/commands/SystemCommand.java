package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class SystemCommand {

	@Command(aliases = { "system", "시스템" }, desc = "")
	@NestedCommand(SubCommand.class)
	@CommandPermissions("evitaonline.admin")
	public static void system(CommandContext args, CommandSender sender) {
		
	}
	
	public static class SubCommand {
		
		@Command(aliases = {"?", "help", "도움말"}, desc = "")
		public static void help(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = {"maintenance", "도움말"}, desc = "")
		public static void maintenance(CommandContext args, CommandSender sender) throws CommandException {
			
		}
	}
}
