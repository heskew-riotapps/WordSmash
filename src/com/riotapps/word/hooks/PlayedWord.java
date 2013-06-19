package com.riotapps.word.hooks;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class PlayedWord {
  
 
	private boolean isOpponentPlay = false; 

	@SerializedName("w")
	private String word = "";
	
	@SerializedName("t")
	private int turn = 0;

	@SerializedName("p")
	private int pointsScored = 0;
	
	@SerializedName("p_d")
	private Date playedDate;  
	
//	  key :player_id , ObjectId --how to handle
//	  key :turn_num,     Integer, :default => 0
//	  key :points_scored, Integer, :default => 0
//	  key :played_date, Time

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getTurn(){
		return turn;
	}

	public void setTurn(int turnNum) {
		this.turn = turnNum;
	}

	public int getPointsScored() {
		return pointsScored;
	}

	public void setPointsScored(int pointsScored) {
		this.pointsScored = pointsScored;
	}

	public Date getPlayedDate() {
		return playedDate;
	}

	public void setPlayedDate(Date playedDate) {
		this.playedDate = playedDate;
	}

	 

	public boolean isOpponentPlay() {
		return isOpponentPlay;
	}

	public void setOpponentPlay(boolean isOpponentPlay) {
		this.isOpponentPlay = isOpponentPlay;
	}
 
}
