package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandSenderTypeException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.selection.Selection;
import com.tistory.hornslied.evitaonline.selection.SelectionManager;

public class SelectionCommand {

	private static SelectionManager manager = EvitaOnline.getInstance().getSelectionManager();

	@Command(aliases = { "selection", "선택" }, desc = "")
	@NestedCommand(value = SubCommand.class, executeBody = true)
	public static void selection(CommandContext args, CommandSender sender) {
		
	}
	
	public static class SubCommand {
		
		@Command(aliases = { "info", "정보" }, desc = "", max = 0)
		@CommandPermissions("evitaonline.mod")
		public static void info(CommandContext args, CommandSender sender) throws CommandException {
			if(!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			Selection selection = manager.getSelection((Player) sender);
			
			if(selection == null)
				throw new CommandException("완드로 선택 영역을 먼저 지정해야 합니다.");
			
			Block coord1 = manager.getFirstPoint((Player) sender);
			Block coord2 = manager.getSecondPoint((Player) sender);
			
			sender.sendMessage(P.Line);
			sender.sendMessage(C.Gold + "월드: " + C.White + selection.getWorld().getName());
			sender.sendMessage(C.Gold + "지점1: " + C.White + coord1.getX() + ", " + coord1.getY() + ", " + coord1.getZ());
			sender.sendMessage(C.Gold + "지점2: " + C.White + coord2.getX() + ", " + coord2.getY() + ", " + coord2.getZ());
			sender.sendMessage(C.Gold + "크기: " + C.White + selection.size());
			sender.sendMessage(P.Line);
		}

		@Command(aliases = { "wand", "완드" }, desc = "")
		@CommandPermissions("evitaonline.mod")
		public static void wand(CommandContext args, CommandSender sender) throws CommandSenderTypeException {
			if(!(sender instanceof Player))
				throw new CommandSenderTypeException();
			
			Player player = (Player) sender;
			
			player.getInventory().addItem(manager.getWand());
		}

		@Command(aliases = { "help", "?", "도움말" }, desc = "", max = 0)
		@CommandPermissions("evitaonline.mod")
		public static void help(CommandContext args, CommandSender sender) {
			
		}
	}
}
