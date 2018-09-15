package com.tistory.hornslied.evitaonline.chat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.Formatter;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.P;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.ChildJsonMessage;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.HoverEvent;
import com.tistory.hornslied.evitaonline.commons.util.jsonchat.JsonMessage;
import com.tistory.hornslied.evitaonline.rank.Rank;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.nation.Nation;

public class ChatManager implements Listener {
	
	private WeakHashMap<Player, Channel> channels;
	private HashSet<Player> spies;
	
	private HashMap<Player, String> lastWhisperUser;
	
	public ChatManager(EvitaOnline plugin) {
		channels = new WeakHashMap<>();
		spies = new HashSet<>();
		lastWhisperUser = new HashMap<>();
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public boolean isSpy(Player player) {
		return spies.contains(player);
	}
	
	public void chatProcess(Player sender, Channel channel, String mention) {
		Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());
		Set<Player> recipients = new HashSet<Player>();
		EvitaPlayer evitaSender = EvitaAPI.getEvitaPlayer(sender);
		Rank rank = evitaSender.getRank();
		
		Nation nation = null;
		String nationInfo = null;
		
		if(evitaSender.hasNation()) {
			nation = evitaSender.getNation();
			nationInfo = C.BYellow + "국가명: " + C.White + nation.getName() + "\n"
						+ C.BYellow + "수도: " + C.White + nation.getCapital().getName() + "\n"
						+ C.BYellow + "인구: " + C.White + nation.getPopulation() + "\n";
		}
		
		for(Player player : players) {
			EvitaPlayer evitaReceiver = EvitaAPI.getEvitaPlayer(player);
			
			if (evitaReceiver.isPlayerIgnored(evitaSender))
				continue;
			else if (spies.contains(player))
				recipients.add(player);
			else if (channel.isShown(sender, player))
				recipients.add(player);
		}
		
		ChildJsonMessage message = new JsonMessage("").extra("");
		
		switch(channel) {
		case GLOBAL:
			message.add("[" + channel.getColor() + channel.getName() + C.White + "]");
		case LOCAL:
			if(evitaSender.hasNation()) {
				message.add("<-");
				message.add(nation.getName()).hover(HoverEvent.SHOW_TEXT, nationInfo);
				message.add(" ");
			} else {
				message.add("<");
			}
			
			message.add("[" + rank.getColor() + rank.getName() + C.White + "] ");
			message.add(sender.getName()).hover(HoverEvent.SHOW_TEXT, Formatter.getUserCard(sender));
			message.add("> " + channel.getChatColor() + mention);
			
			break;
		case ADMIN:
		case TOWN:
		case NATION:
			message.add("[" + channel.getColor() + channel.getName() + C.White + "]<");
			message.add("[" + rank.getColor() + rank.getName() + C.White + "] ");
			message.add(sender.getName()).hover(HoverEvent.SHOW_TEXT, Formatter.getUserCard(sender));
			message.add("> " + channel.getChatColor() + mention);
			break;
		default:
			break;
		}
		
		for(Player player : recipients) {
			message.sendToPlayer(player);
		}
	}
	
	public void whisper(Player player, String target, String message) {
		Player targetPlayer = Bukkit.getPlayer(target);
		
		if(targetPlayer == null || !targetPlayer.isOnline()) {
			player.sendMessage(P.Server + C.Red + P.PlayerNotOnline);
			return;
		}
		
		lastWhisperUser.put(player, targetPlayer.getName());
		
		EvitaPlayer evitaSender = EvitaAPI.getEvitaPlayer(player);
		EvitaPlayer evitaTarget = EvitaAPI.getEvitaPlayer(targetPlayer);
		
		if(evitaTarget.isPlayerIgnored(evitaSender)) {
			player.sendMessage(P.Chat + ChatColor.RED + targetPlayer.getName() + " 님이 당신을 차단하셨기 때문에 귓속말을 보낼 수 없습니다.");
			return;
		}
		
		ChildJsonMessage senderMessage = new JsonMessage("").extra("");
		ChildJsonMessage receiverMessage = new JsonMessage("").extra("");
		
		senderMessage.add("[" + C.Gold + "귓속말" + C.White+ "]<" + C.Yellow + "나"
				+ C.Gray + " -> " + C.White + "[" + evitaTarget.getRank().getColor()
				+ evitaTarget.getRank().getName() + C.White + "] ");
		senderMessage.add(targetPlayer.getName()).hover(HoverEvent.SHOW_TEXT, Formatter.getUserCard(targetPlayer));
		senderMessage.add(C.White + "> " + message);

		receiverMessage.add("[" + C.Gold + "귓속말" + C.White+ "]<[" + evitaTarget.getRank().getColor()
				+ evitaTarget.getRank().getName() + C.White + "] ");
		receiverMessage.add(player.getName()).hover(HoverEvent.SHOW_TEXT, Formatter.getUserCard(player));
		receiverMessage.add(C.Gray + " -> " + C.Yellow + "나" + C.White + "> " + message);
		
		senderMessage.sendToPlayer(player);
		receiverMessage.sendToPlayer(targetPlayer);
	}
	
	public void reply(Player player, String message) {
		if(!lastWhisperUser.containsKey(player)) {
			
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		chatProcess(event.getPlayer(), channels.get(event.getPlayer()), event.getMessage());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		channels.put(event.getPlayer(), Channel.LOCAL);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		channels.remove(event.getPlayer());
		lastWhisperUser.remove(event.getPlayer());
	}
}
