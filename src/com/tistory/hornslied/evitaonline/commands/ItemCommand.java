package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class ItemCommand {
	
	@Command(aliases = { "item", "아이템" }, desc = "", min = 0, max = -1)
	@CommandPermissions("evitaonline.mod")
	@NestedCommand(value = SubCommand.class, executeBody = true)
	public static void item(CommandContext args, CommandSender sender) {
		
	}
	
	public static class SubCommand {
		
		@Command(aliases = { "list", "목록" }, desc = "", max = 0)
		public static void list(CommandContext args, CommandSender sender) {
			
		}
	}
}
