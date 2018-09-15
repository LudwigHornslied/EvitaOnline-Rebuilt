package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.tistory.hornslied.evitaonline.commons.util.P;

public class RecordCommand {

	@SuppressWarnings("deprecation")
	@Command(aliases = { "record", "lookup", "기록" }, desc = "", min = 1, max = 1)
	@CommandPermissions("evitaonline.mod")
	public static void record(CommandContext args, CommandSender sender) throws CommandException {
		OfflinePlayer player = Bukkit.getOfflinePlayer(args.getString(0));
		
		if(player == null)
			throw new CommandException(P.PlayerNotExist);
		
		
	}
}
