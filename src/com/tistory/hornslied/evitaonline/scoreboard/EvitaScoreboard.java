package com.tistory.hornslied.evitaonline.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.api.EvitaAPI;
import com.tistory.hornslied.evitaonline.commons.util.C;
import com.tistory.hornslied.evitaonline.commons.util.TimeUtil;
import com.tistory.hornslied.evitaonline.timer.TimerManager;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

public class EvitaScoreboard {
	
	private static TimerManager timerManager = EvitaOnline.getInstance().getTimerManager();
	
	private final Player viewer;
	private final EvitaPlayer evitaViewer;
	
	private final Scoreboard scoreboard;
	private final Objective obj;
	
	public String[] scoreboardRows = new String[16];
    public Team[] scoreboardTeams = new Team[16];
    public String[] scoreboardPlayers = new String[16];
    public int[] scoreboardScores = new int[16];

    protected final List<String> rows = new ArrayList<>();
	
	public EvitaScoreboard(Player player) {
		viewer = player;
		evitaViewer = EvitaAPI.getEvitaPlayer(viewer);
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = scoreboard.registerNewObjective("evita", "evita");
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(C.BGreen + "Evita Online");

        for(int i = 0; i < 16; i++) {
        	scoreboardTeams[i] = scoreboard.registerNewTeam("row-" + i);
        	scoreboardPlayers[i] = (String.valueOf(ChatColor.COLOR_CHAR) + (char) i);
        	scoreboardTeams[i].addEntry(scoreboardPlayers[i]);
            scoreboardScores[i] = -1;
            
            scoreboardRows[i] = null;
            scoreboardTeams[i].setPrefix("");
            scoreboardTeams[i].setSuffix("");
        }
        player.setScoreboard(scoreboard);
	}
	
	public void render() {
		rows.add("플레이어: " + C.Lime + viewer.getName());
		rows.add("마을: " + C.Lime + ((evitaViewer.hasTown()) ? evitaViewer.getTown().getName() : "없음"));
		rows.add("소지금: " + C.Lime + evitaViewer.getBalance() + " 페론");
		
		if(timerManager.enderPearlTimer.isContaining(viewer.getUniqueId()) ||
				timerManager.combatTimer.isContaining(viewer.getUniqueId()) ||
				timerManager.pvpProtTimer.isContaining(viewer.getUniqueId()) ||
				timerManager.teleportTimer.isContaining(viewer.getUniqueId()))
			rows.add(C.Aqua);

		if(timerManager.pvpProtTimer.isContaining(viewer.getUniqueId()))
			rows.add("PvP 보호: " + C.Lime + TimeUtil.formatTime(timerManager.pvpProtTimer.getRemaining(viewer.getUniqueId())));

		if(timerManager.combatTimer.isContaining(viewer.getUniqueId()))
			rows.add("컴뱃 태그: " + C.Lime + timerManager.combatTimer.getRemaining(viewer.getUniqueId())/1000 + " 초");
		
		if(timerManager.enderPearlTimer.isContaining(viewer.getUniqueId()))
			rows.add("엔더 진주: " + C.Lime + timerManager.enderPearlTimer.getRemaining(viewer.getUniqueId())/1000 + " 초");
		
		if(timerManager.teleportTimer.isContaining(viewer.getUniqueId()))
			rows.add("텔레포트: " + C.Lime + timerManager.teleportTimer.getRemaining(viewer.getUniqueId())/1000 + " 초");
		
		for(int i = 0; i < 16; i++) {
            if(i < rows.size())
                setRow(rows.size(), i, rows.get(i));
            else
                setRow(rows.size(), i, null);
        }
        
        this.rows.clear();
	}
	
	protected final void setRow(int maxScore, int row, @Nullable String text) {
        if(row < 0 || row >= 16)
            return;
    
        int score = text == null ? -1 : maxScore - row - 1;
        if(scoreboardScores[row] != score) {
            scoreboardScores[row] = score;
        
            if(score == -1)
                scoreboard.resetScores(scoreboardPlayers[row]);
            else
                obj.getScore(scoreboardPlayers[row]).setScore(score);
        }
    
        if(!Objects.equals(scoreboardRows[row], text)) {
            scoreboardRows[row] = text;
        
            if(text != null) {
                int split = 16 - 1;
                if(text.length() < 16 || text.charAt(split) != ChatColor.COLOR_CHAR)
                    split++;
            
                String prefix = StringUtils.substring(text, 0, split);
                String lastColors = ChatColor.getLastColors(prefix);
                String suffix = lastColors + StringUtils.substring(text, split, split + 16 - lastColors.length());
            
                scoreboardTeams[row].setPrefix(prefix);
                scoreboardTeams[row].setSuffix(suffix);
            }
        }
    }
}
