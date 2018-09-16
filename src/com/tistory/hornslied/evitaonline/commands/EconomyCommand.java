package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandSenderTypeException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

public class EconomyCommand {

	@Command(aliases = { "money", "balance", "bal", "돈" }, desc = "")
	@NestedCommand(value = MoneySubCommand.class, executeBody = true)
	public static void money(CommandContext args, CommandSender sender) throws CommandException {

	}
	
	public static class MoneySubCommand {
		
		@SuppressWarnings("deprecation")
		@Command(aliases = { "info", "정보" }, desc = "", max = 1)
		public static void info(CommandContext args, CommandSender sender) throws CommandException {

			if (args.argsLength() == 0) {
				if (!(sender instanceof Player))
					throw new CommandSenderTypeException();
				
				EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer((Player) sender);
				sender.sendMessage(P.Economy + "소지금: " + C.Green + evitaPlayer.getBalance() + " 페론");
			} else {
				OfflinePlayer target = Bukkit.getOfflinePlayer(args.getString(0));
				
				if(target == null)
					throw new CommandException(P.PlayerNotExist);
				
				EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(target.getUniqueId());
				sender.sendMessage(P.Economy + C.Gold + target.getName() + C.White + " 님의 소지금: " + C.Green + evitaPlayer.getBalance() + " 페론");
			}
		}
		
		@SuppressWarnings("deprecation")
		@Command(aliases = { "give", "pay", "보내기" }, desc = "", min = 2, max = 2)
		public static void give(CommandContext args, CommandSender sender) throws CommandException {
			if (!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			OfflinePlayer target = Bukkit.getOfflinePlayer(args.getString(0));
			
			if(target == null)
				throw new CommandException(P.PlayerNotExist);
			
			long amount = args.getInteger(1);
			
			if(amount < 1)
				throw new CommandException("양수의 금액만 보낼 수 있습니다!");
			
			EvitaPlayer evitaSender = EvitaAPI.getEvitaPlayer((Player) sender);
			EvitaPlayer evitaTarget = EvitaAPI.getEvitaPlayer(target.getUniqueId());
			
			if(!evitaSender.hasBalance(amount))
				throw new CommandException("소지금이 부족합니다.");
			
			evitaSender.withdraw(amount);
			evitaTarget.deposit(amount);
			
			sender.sendMessage(P.Economy + C.Green + amount + " 페론 을 " + target.getName() + " 님에게 송금하였습니다.");
			if(target.isOnline())
				Bukkit.getPlayer(target.getName()).sendMessage(
						P.Economy + C.Green + sender.getName() + " 님이 당신에게 " + amount + " 페론 을 송금하였습니다.");
		}
	}
}
