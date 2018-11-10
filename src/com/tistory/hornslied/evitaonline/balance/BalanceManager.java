package com.tistory.hornslied.evitaonline.balance;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.tistory.hornslied.evitaonline.EvitaOnline;
import com.tistory.hornslied.evitaonline.universe.EvitaPlayer;

public class BalanceManager {
	
	private EvitaOnline plugin;
	
	private List<EvitaPlayer> balanceRanking;

	public BalanceManager(EvitaOnline plugin) {
		this.plugin = plugin;
		
		balanceRanking = new ArrayList<>();
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				refreshRanking();
			}
		}, 0, 600000);
	}
	
	public void deposit(BalanceOwner owner, long amount) {
		owner.deposit(amount);
		owner.saveBalance();
	}
	
	public void withdraw(BalanceOwner owner, long amount) {
		if(!owner.hasBalance(amount))
			return;
		
		owner.withdraw(amount);
		owner.saveBalance();
	}
	
	public void refreshRanking() {
		List<EvitaPlayer> rankings = new ArrayList<>(balanceRanking);
		rankings.clear();
		rankings.addAll(plugin.getUniverseManager().getEvitaPlayers());
		
		rankings.sort((o1, o2) ->
        {
            if(o1 == null || o2 == null)
                return 0;
            
            if(o2.getBalance() > o1.getBalance())
            	return 1;
            else if(o2.getBalance() == o1.getBalance())
            	return 0;
            else
            	return -1;
        });
		
		balanceRanking = rankings;
	}
	
	public EvitaPlayer getRanking(int index) {
		if(index >= balanceRanking.size())
			return null;
		
		return balanceRanking.get(index);
	}
	
	public boolean hasPage(int index) {
		if(index < 1)
			return false;
		
		return index > getMaxPage();
	}
	
	public int getMaxPage() {
		return (int) Math.ceil(balanceRanking.size());
	}
}
