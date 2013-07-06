package com.riotapps.word.hooks;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class GameListItem {
	
	@SerializedName("g")
	private String gameId;
	
	@SerializedName("c_d")
	private Date completedDate;
	
	public GameListItem(String id, Date date){
		this.gameId = id;
		this.completedDate = date;
	}
	
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
}
