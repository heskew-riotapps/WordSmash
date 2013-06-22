package com.riotapps.word.hooks;

import java.util.Date;

public class GameListItem {
	private String gameId;
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
