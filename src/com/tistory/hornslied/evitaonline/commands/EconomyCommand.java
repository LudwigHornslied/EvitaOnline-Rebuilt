package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandSenderTypeException;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

public class EconomyCommand {

	@Command(aliases = { "money", "balance", "bal", "돈" }, desc = "", max = 1)
	public static void money(CommandContext args, CommandSender sender) throws CommandSenderTypeException {

		if (args.argsLength() == 0) {
			if (!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer((Player) sender);
			sender.sendMessage(P.Economy + "소지금: " + C.Green + evitaPlayer.getBalance() + " 페론");
		} else {
			
		}
	}
}
