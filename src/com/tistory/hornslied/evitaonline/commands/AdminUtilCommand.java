package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandSenderTypeException;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;

public class AdminUtilCommand {
	
	@Command(aliases = { "alert", "알림", "공지" }, desc = "", min = 1, max = -1)
	@CommandPermissions("evitaonline.mod")
	public static void alert(CommandContext args, CommandSender sender) {
		Bukkit.broadcastMessage("[" + C.DarkRed + "알림" + C.White + "] " + args.getJoinedStrings(0));
	}
	
	@Command(aliases = { "teleport", "tp", "티피", "텔레포트" }, desc = "", min = 1, max = 2)
	@CommandPermissions("evitaonline.mod")
	public static void teleport(CommandContext args, CommandSender sender) throws CommandException {
		if(args.argsLength() == 1) {
			if(!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			Player target = Bukkit.getPlayer(args.getString(0));
			
			if(target == null)
				throw new CommandException(P.PlayerNotOnline);
			
			((Player) sender).teleport(target, TeleportCause.PLUGIN);
			sender.sendMessage(P.Move + ChatColor.AQUA + target.getName() + " 에게 텔레포트합니다.");
		} else {
			Player player = Bukkit.getPlayer(args.getString(0));
			Player target = Bukkit.getPlayer(args.getString(1));

			if(player == null || target == null)
				throw new CommandException(P.PlayerNotOnline);
			
			player.teleport(target, TeleportCause.PLUGIN);
			sender.sendMessage(P.Move + ChatColor.AQUA + player.getName() + " 를 " + target.getName() + " 에게 텔레포트 시킵니다.");
		}
	}
}
