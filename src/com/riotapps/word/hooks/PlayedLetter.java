package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class PlayedLetter {

	@SerializedName("t")
	private int turn;
	
	@SerializedName("l")
	private String letter;
	
	public PlayedLetter(int turn, String letter){
		this.turn = turn;
		this.letter = letter;
	}
	
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	
	
}
