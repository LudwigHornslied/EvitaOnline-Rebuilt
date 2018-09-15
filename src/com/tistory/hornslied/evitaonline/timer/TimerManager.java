package com.tistory.hornslied.evitaonline.timer;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class TimerManager {
	
	public CombatTimer combatTimer;
	public EnderPearlTimer enderPearlTimer;
	public PvPProtTimer pvpProtTimer;
	public TeleportTimer teleportTimer;
	
	public TimerManager(EvitaOnline plugin) {
		combatTimer = new CombatTimer();
		enderPearlTimer = new EnderPearlTimer();
		pvpProtTimer = new PvPProtTimer();
		teleportTimer = new TeleportTimer();
	}
}
