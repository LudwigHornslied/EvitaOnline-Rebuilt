package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
import com.tistory.hornslied.evitaonline.balance.BalanceManager;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.commons.util.help.Help;
import com.tistory.hornslied.evitaonline.commons.util.help.HelpRow;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

public class EconomyCommand {
	
	private static BalanceManager manager = EvitaOnline.getInstance().getBalanceManager();
	
	private static Help moneyHelp = new Help("돈 도움말", "/money help <index>");
	
	static {
		moneyHelp.addPage()
		.addRow(new HelpRow("/돈 정보", "자신의 소지금을 확인합니다."))
		.addRow(new HelpRow("/돈 정보 <플레이어>", "플레이어의 소지금을 확인합니다."))
		.addRow(new HelpRow("/돈 보내기 <플레이어> <액수>", "플레이어에게 돈을 보냅니다."))
		.addRow(new HelpRow("/돈 순위 <페이지>", "서버 내 돈 순위를 확인합니다."));
	}

	@Command(aliases = { "money", "balance", "bal", "돈" }, desc = "")
	@NestedCommand(value = MoneySubCommand.class, executeBody = true)
	public static void money(CommandContext args, CommandSender sender) {
		moneyHelp.send(sender, 0);
	}

	@Command(aliases = { "economy", "eco", "경제" }, desc = "")
	@NestedCommand(value = EcoSubCommand.class, executeBody = true)
	@CommandPermissions("evitaonline.mod")
	public static void economy(CommandContext args, CommandSender sender) throws CommandException {

	}
	
	public static class MoneySubCommand {
		
		@Command(aliases = { "help", "도움말" }, desc = "", max = 0)
		public static void help(CommandContext args, CommandSender sender) {
			moneyHelp.send(sender, 0);
		}
		
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
			
			manager.withdraw(evitaSender, amount);
			manager.deposit(evitaTarget, amount);
			
			sender.sendMessage(P.Economy + C.Green + amount + " 페론 을 " + target.getName() + " 님에게 송금하였습니다.");
			if(target.isOnline())
				Bukkit.getPlayer(target.getName()).sendMessage(
						P.Economy + C.Green + sender.getName() + " 님이 당신에게 " + amount + " 페론 을 송금하였습니다.");
		}
		
		@Command(aliases = { "top", "순위" }, desc = "", min = 0, max = 1)
		public static void top(CommandContext args, CommandSender sender) throws CommandException {
			if(args.argsLength() == 1) {
				int page = args.getInteger(0);
				if(manager.hasPage(page))
					throw new CommandException(P.NoPage);
				
				int start = 7 * (page -1);
				
				sender.sendMessage(P.Line);
				sender.sendMessage(C.Lime + "돈 순위 " + C.Gray + "(" + page + "/" + manager.getMaxPage() + ")");
				
				int i = 0;
				while(i < 7 && manager.getRanking(start + i) != null) {
					sender.sendMessage(C.Gray + (start + i + 1) + ". " + C.Yellow + manager.getRanking(start + i).getName() + " " + C.White + manager.getRanking(start + i).getBalance() + " 페론");
					i++;
				}
				sender.sendMessage(P.Line);
			} else {
				if(manager.hasPage(1))
					throw new CommandException(P.NoPage);
				
				sender.sendMessage(P.Line);
				sender.sendMessage(C.Lime + "돈 순위 " + C.Gray + "(" + 1 + "/" + manager.getMaxPage() + ")");
				
				int i = 0;
				while(i < 7 && manager.getRanking(i) != null) {
					sender.sendMessage(C.Gray + (i + 1) + ". " + C.Yellow + manager.getRanking(i).getName() + " " + C.White + manager.getRanking(i).getBalance() + " 페론");
					i++;
				}
				sender.sendMessage(P.Line);
			}
		}
	}
	
	public static class EcoSubCommand {

		@Command(aliases = { "help", "?", "도움말" }, desc = "", min = 0, max = 0)
		public static void help(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = { "set", "설정" }, desc = "", min = 1, max = 1)
		public static void set(CommandContext args, CommandSender sender) throws CommandException {
			
		}
		
		@Command(aliases = { "give", "deposit", "보내기", "입금" }, desc = "", min = 1, max = 1)
		public static void deposit(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = { "withdraw", "출금" }, desc = "", min = 1, max = 1)
		public static void withdraw(CommandContext args, CommandSender sender) throws CommandException {
			
		}
	}
}
