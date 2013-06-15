package com.riotapps.word.hooks;

import java.sql.Date;

public class PlayedTurn {
	/*
	key :player_id , ObjectId
	key :t, Integer #turn
	key :a, Integer, :default => 0 #action,  
			#NO_TRANSLATION(0), (no action yet)
			#ONE_LETTER_SWAPPED(1),
			#TWO_LETTERS_SWAPPED(2),
			#THREE_LETTERS_SWAPPED(3),
			#FOUR_LETTERS_SWAPPED(4),
			#FIVE_LETTERS_SWAPPED(5),
			#SIX_LETTERS_SWAPPED(6),
			#SEVEN_LETTERS_SWAPPED(7),
			#STARTED_GAME(8),
			#WORDS_PLAYED(9),
			#TURN_SKIPPED(10),
			#RESIGNED(11),
			#CANCELLED(12),
			#DECLINED(13),
			#WON(14),
			#LOST(15),
			#DRAW(16);
	key :p, Integer, :default => 0 #points for this turn
	key :p_d, Time #played_date
	*/
	
	private String playerId;
	private int turn;
	private int action;
	
	//create enum for action
	
	private int points;
	private Date playedDate;
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public Date getPlayedDate() {
		return playedDate;
	}
	public void setPlayedDate(Date playedDate) {
		this.playedDate = playedDate;
	}
	
	
	
	
}
