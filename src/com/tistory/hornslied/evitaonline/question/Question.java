package com.tistory.hornslied.evitaonline.question;

import org.bukkit.entity.Player;

public abstract class Question {
	
	protected Player receiver;
	
	public Question(Player receiver) {
		this.receiver = receiver;
	}
	
	public void queue() {
		
	}
	
	abstract public void accept();
	abstract public void decline();

	public Player getReceiver() {
		return receiver;
	}
}
