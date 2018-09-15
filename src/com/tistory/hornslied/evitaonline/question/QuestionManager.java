package com.tistory.hornslied.evitaonline.question;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import com.tistory.hornslied.evitaonline.EvitaOnline;

public class QuestionManager {
	
	private HashMap<Player, Question> questions;
	private HashMap<Player, Timer> questionTimers;
	
	public QuestionManager(EvitaOnline plugin) {
		questions = new HashMap<>();
		questionTimers = new HashMap<>();
	}
	
	public void addQuestion(Question question) {
		Player player = question.getReceiver();
		
		if(questions.containsKey(player)) {
			questionTimers.get(player).cancel();
			questionTimers.remove(player);
			questions.get(player).decline();
			questions.remove(player);
		}
		
		questions.put(player, question);
		question.queue();
		questionTimers.put(player, new Timer());
		questionTimers.get(player).schedule(new QuestionExpireTask(player), 30000);
	}
	
	public class QuestionExpireTask extends TimerTask {
		
		private Player player;
		
		public QuestionExpireTask(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			questions.get(player).decline();
			questions.remove(player);
			questionTimers.remove(player);
		}
		
	}
}
