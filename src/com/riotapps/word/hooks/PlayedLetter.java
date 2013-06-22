package com.riotapps.word.hooks;

public class PlayedLetter {

	private int turn;
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
