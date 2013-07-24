package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.ui.RowCol;

public class PlayedTile {

	//key :p,   Integer, :default => -1 #board_position
	//key :l, Array #letters
	//key :t, Array #turn
	
	@SerializedName("p")
	private int boardPosition; 
	
	@SerializedName("l_")
	private List<PlayedLetter> letters = new ArrayList<PlayedLetter>();
	 
	private int tileIdAbove = -1;
	private int tileIdBelow = -1;
	private int tileIdToTheLeft = -1;
	private int tileIdToTheRight = -1;
	
	private boolean border = false;
	
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
	 
	public RowCol getRowCol(){
		return TileLayoutService.getRowCol(this.getBoardPosition());
		
	}
	
	public int getTileIdAbove() {
		 if (tileIdAbove == -1){
			 tileIdAbove = TileLayoutService.getTileIdAbove(boardPosition);
		 }
		return tileIdAbove;
	}

	public void setTileIdAbove(int tileIdAbove) {
		this.tileIdAbove = tileIdAbove;
	}

	public int getTileIdBelow() {
		if (tileIdBelow == -1){
			 tileIdBelow = TileLayoutService.getTileIdBelow(boardPosition);
		 }

		return tileIdBelow;
	}

	public void setTileIdBelow(int tileIdBelow) {
		this.tileIdBelow = tileIdBelow;
	}

	public int getTileIdToTheLeft() {
		if (tileIdToTheLeft == -1){
			 tileIdToTheLeft = TileLayoutService.getTileIdToTheLeft(boardPosition);
		 }

		return tileIdToTheLeft;
	}

	public void setTileIdToTheLeft(int tileIdToTheLeft) {
		this.tileIdToTheLeft = tileIdToTheLeft;
	}

	public int getTileIdToTheRight() {
		if (tileIdToTheRight == -1){
			 tileIdToTheRight = TileLayoutService.getTileIdToTheRight(boardPosition);
		 }

		return tileIdToTheRight;
	}

	public void setTileIdToTheRight(int tileIdToTheRight) {
		this.tileIdToTheRight = tileIdToTheRight;
	}
	
	public boolean isBorder() {
		if (TileLayoutService.isTileOnBottomRow(this.boardPosition)){
			this.border = true;
		}
		else if (TileLayoutService.isTileOnTopRow(this.boardPosition)){
			this.border = true;
		}
		else if (TileLayoutService.isTileOnLeftBorder(this.boardPosition)){
			this.border = true;
		}
		else if (TileLayoutService.isTileOnRightBorder(this.boardPosition)){
			this.border = true;
		}
		return border;
	}

	public void setBorder(boolean isBorder) {
		this.border = isBorder;
	}
	
	

}
