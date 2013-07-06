package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class OpponentRecord {

//	private int opponentId;
	
	@SerializedName("w")
	private int numWins = 0;
	
	@SerializedName("l")
	private int numLosses = 0;
	
	@SerializedName("d")
	private int numDraws = 0;
	
//	public int getOpponentId() {
//		return opponentId;
//	}
//	public void setOpponentId(int opponentId) {
//		this.opponentId = opponentId;
//	}
	public int getNumWins() {
		return numWins;
	}
	public void setNumWins(int numWins) {
		this.numWins = numWins;
	}
	public int getNumLosses() {
		return numLosses;
	}
	public void setNumLosses(int numLosses) {
		this.numLosses = numLosses;
	}
	public int getNumDraws() {
		return numDraws;
	}
	public void setNumDraws(int numDraws) {
		this.numDraws = numDraws;
	}
	
	
}
