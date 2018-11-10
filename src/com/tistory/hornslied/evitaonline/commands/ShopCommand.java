package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.tistory.hornslied.evitaonline.shop.ShopManager;

public class ShopCommand {
	
	private static ShopManager manager;

	@Command(aliases = { "shop", "상점" }, desc = "")
	@NestedCommand(value = SubCommand.class, executeBody = true)
	public static void shop(CommandContext args, CommandSender sender) {
		
	}
	
	public static class SubCommand {
		
		@Command(aliases = { "create", "생성" }, desc = "", min = 1, max = 1)
		@CommandPermissions("evitaonline.admin")
		public static void create(CommandContext args, CommandSender sender) throws CommandException {
			String name = args.getString(0);
			
			if(manager.hasName(name))
				throw new CommandException("이미 존재하는 상점 이름입니다.");
			
			manager.newShop(name);
		}

		@Command(aliases = { "title", "" }, desc = "", min = 2, max = 2)
		@CommandPermissions("evitaonline.admin")
		public static void title(CommandContext args, CommandSender sender) throws CommandException {
			
		}
		
		@Command(aliases = { "add", "추가" }, desc = "", min = 3, max = 4)
		@CommandPermissions("evitaonline.admin")
		public static void add(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = { "remove", "제거" }, desc = "", min = 2, max = 2)
		@CommandPermissions("evitaonline.admin")
		public static void remove(CommandContext args, CommandSender sender) throws CommandException {
			
		}

		@Command(aliases = { "move", "이동" }, desc = "", min = 3, max = 3)
		@CommandPermissions("evitaonline.admin")
		public static void move(CommandContext args, CommandSender sender) throws CommandException {
			
		}
	}
}
