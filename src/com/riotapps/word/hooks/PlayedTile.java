package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PlayedTile {

	//key :p,   Integer, :default => -1 #board_position
	//key :l, Array #letters
	//key :t, Array #turn
	
	@SerializedName("p")
	private int boardPosition; 

	@SerializedName("l_")
	private List<PlayedLetter> letters = new ArrayList<PlayedLetter>();
	 
	public int getBoardPosition() {
		return boardPosition;
	}

	public void setBoardPosition(int boardPosition) {
		this.boardPosition = boardPosition;
	}

	public List<PlayedLetter> getLetters() {
		return letters;
	}

	public void setLetters(List<PlayedLetter> letters) {
		this.letters = letters;
	}
  
	public void addLetter(PlayedLetter letter){
		this.letters.add(letter);
	}
	
	public PlayedLetter getLatestPlayedLetter(){
		return this.letters.get(this.letters.size() - 1);
	}
	 
}
