package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandSenderTypeException;
import com.tistory.hornslied.evitaonline.commons.util.P;

public class UserUtilCommand {
	
	@Command(aliases = { "ping", "핑" }, desc = "", max = 1)
	public static void ping(CommandContext args, CommandSender sender) throws CommandException {
		if(args.argsLength() == 0) {
			if(!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			sender.sendMessage(P.Server + "핑: " + ChatColor.GREEN + ((CraftPlayer) (Player) sender).getHandle().ping + "ms");
		} else {
			Player player = Bukkit.getServer().getPlayer(args.getString(0));

			if (player == null)
				throw new CommandException("존재하지 않거나 접속중이지 않은 플레이어입니다.");
			
			sender.sendMessage(P.Server + ChatColor.GREEN + player.getName() + ChatColor.WHITE + " 님의 핑: " + ChatColor.GREEN + ((CraftPlayer) player).getHandle().ping + "ms");
		}
	}
}
