package com.riotapps.word.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlacedResult {

	private List<PlacedWord> placedWords = new ArrayList<PlacedWord>();
	private List<GameTile> placedTiles = new ArrayList<GameTile>();
	
	private List<GameTile> boardTiles = new ArrayList<GameTile>();
	
	private int matchType = 0;
	
	private int totalPoints = 0;
	
	public List<GameTile> getPlacedTiles() {
		return placedTiles;
	}
	public void setPlacedTiles(List<GameTile> placedTiles) {
		this.placedTiles = placedTiles;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	public List<PlacedWord> getPlacedWords() {
		return placedWords;
	}
	public void setPlacedWords(List<PlacedWord> placedWords) {
		this.placedWords = placedWords;
	}
	public List<GameTile> getBoardTiles() {
		return boardTiles;
	}
	public void setBoardTiles(List<GameTile> boardTiles) {
		this.boardTiles = boardTiles;
	}
	
	
	public boolean isAnyTileIdConnected(Set<Integer> ids){
		boolean isConnected = false;
	
		for (Integer i : ids){
			if (this.isTileIdConnected(i)){
				return true;
			}
		}
		
		return isConnected;
	}
	
	
	//used to determine if primed autoplay needs to be invalidated
	public boolean isTileIdConnected(int id){
		boolean isConnected = false;
		for (GameTile tile : this.placedTiles){
			//determine if the tile is touching
			
			if (tile.getId() == id ||
				tile.getTileIdAbove() == id || 
				tile.getTileIdBelow() == id ||
				tile.getTileIdToTheRight() == id ||
				tile.getTileIdToTheLeft() == id){
			return true;
			}
		}
		
		return isConnected;
	}
	
	public int getMatchType() {
		return matchType;
	}
	public void setMatchType(int matchType) {
		this.matchType = matchType;
	}
	
}