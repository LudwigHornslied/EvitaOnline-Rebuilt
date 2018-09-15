package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class SystemCommand {

	@Command(aliases = { "system", "시스템" }, desc = "", max = -1)
	@NestedCommand(SubCommand.class)
	public static void system(CommandContext args, CommandSender sender) {
		
	}
	
	public static class SubCommand {
		
	}
}
