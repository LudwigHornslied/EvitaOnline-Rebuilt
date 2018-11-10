package com.tistory.hornslied.evitaonline.rank;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.permission.Perm;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;
import com.tistory.hornslied.evitaonline.universe.nation.NationRank;
import com.tistory.hornslied.evitaonline.universe.town.TownRank;

public class RankManager implements Listener {
	
	private EvitaOnline plugin;

	public RankManager(EvitaOnline plugin) {
		this.plugin = plugin;
	}

	public void update(Player player) {
		EvitaPlayer evitaPlayer = EvitaAPI.getEvitaPlayer(player);
		Rank rank = evitaPlayer.getRank();
		
		if(rank == Rank.ADMIN) {
			if(!player.isOp())
				player.setOp(true);
		} else {
			if (player.isOp())
				player.setOp(false);

			boolean dirty = false;
			for (PermissionAttachmentInfo attachInfo : player.getEffectivePermissions()) {
				if (attachInfo.getPermission().startsWith("evitaonline.")) {
					player.removeAttachment(attachInfo.getAttachment());
					dirty = true;
				}
			}

			if (dirty)
				player.recalculatePermissions();

			PermissionAttachment attachment = player.addAttachment(plugin);
			
			NationRank nationRank = evitaPlayer.getNationRank();
			TownRank townRank = evitaPlayer.getTownRank();
			
			if(nationRank == NationRank.LEADER) {
				attachment.setPermission(Perm.LEADER, true);
			} else if (nationRank == NationRank.VICELEADER) {
				attachment.setPermission(Perm.VICELEADER, true);
			}
			
			if(townRank == TownRank.MAYOR) {
				attachment.setPermission(Perm.MAYOR, true);
			} else if (townRank == TownRank.VICEMAYOR) {
				attachment.setPermission(Perm.VICEMAYOR, true);
			} else if (townRank == TownRank.BUILDER) {
				attachment.setPermission(Perm.BUILDER, true);
			} else if (townRank == TownRank.INVITER) {
				attachment.setPermission(Perm.INVITER, true);
			}
			
			for (String perm : rank.getPermissions()) {
				attachment.setPermission(perm, true);
			}
			player.recalculatePermissions();
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		update(player);
	}
}
