package com.tistory.hornslied.evitaonline.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandSenderTypeException;
import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.chat.ChannelGUI;
import com.tistory.hornslied.evitaonline.chat.ChatManager;

public class ChatCommand {
	
	private static ChatManager manager = EvitaOnline.getInstance().getChatManager();
	
	@Command(aliases = { "channel", "채널" }, desc = "", min = 0, max = 1)
	public static void channel(CommandContext args, CommandSender sender) throws CommandSenderTypeException {
		if(!(sender instanceof Player))
			throw new CommandSenderTypeException();
		
		if(args.argsLength() == 1) {
			
		} else {
			ChannelGUI.CONTENT.open((Player) sender);
		}
	}
	
	@Command(aliases = { "whisper", "message", "msg", "tell", "w", "m", "귓속말", "귓" }, desc = "", min = 2, max = -1)
	public static void whisper(CommandContext args, CommandSender sender) {
		
	}
	
	@Command(aliases = { "reply", "r", "대답" }, desc = "", min = 1, max = -1)
	public static void reply(CommandContext args, CommandSender sender) throws CommandSenderTypeException {
		if(!(sender instanceof Player))
			throw new CommandSenderTypeException();
		
	}
}
