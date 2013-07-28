package com.riotapps.word.ui;

import com.riotapps.word.hooks.TileLayoutService;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameTile {
	private int id;
	
	private int xPosition = 0;
	private int yPosition = 0;
	private Bitmap currentBitmap;
	private Bitmap originalBitmap;
	private Bitmap originalBitmapZoomed;
	private int xPositionZoomed = 0;
	private int yPositionZoomed = 0;
	private String originalText = "";
	private String originalLetter = "";
	private String draggingLetter = "";
	private int xPositionDragging = 0;
	private int yPositionDragging = 0;
	private boolean dragPending = false;
	private boolean dragPositionSet = false;
	//private String placedText = "";

	private int row;
	private int column;
    private boolean isLastPlayed = false;
    private boolean isOverlay = false;
    private boolean isPlacement = false;
    private String placedLetter = "";
    private boolean isDraggable = false;
 
    private boolean isDragging = false;
    private int xPositionCenter = 0;
    private int yPositionCenter = 0;
    private int xPositionCenterRelativeZoomed = 0;
    private int yPositionCenterRelativeZoomed = 0;
	private boolean isConnected = false;

//	private GameTile tileToLeft = null;
//	private GameTile tileToRight = null;
//	private GameTile tileAbove = null;
//	private GameTile tileBelow = null;
	
	private int tileIdAbove = -1;
	private int tileIdBelow = -1;
	private int tileIdToTheLeft = -1;
	private int tileIdToTheRight = -1;
	
	
	
	public GameTile(){
		
	}
//	public GameTile(Canvas canvas){
//		this.canvas = canvas; //???is canvas needed?
//	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getxPosition() {
		return xPosition;
	}


	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}


	public int getyPosition() {
		return yPosition;
	}


	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}


	public Bitmap getCurrentBitmap() {
		return currentBitmap;
	}


	public void setCurrentBitmap(Bitmap currentBitmap) {
		this.currentBitmap = currentBitmap;
	}


	public Bitmap getOriginalBitmap() {
		return originalBitmap;
	}


	public void setOriginalBitmap(Bitmap originalBitmap) {
		this.originalBitmap = originalBitmap;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getColumn() {
		return column;
	}


	public void setColumn(int column) {
		this.column = column;
	}


	public boolean isLastPlayed() {
		return isLastPlayed;
	}


	public void setLastPlayed(boolean isLastPlayed) {
		this.isLastPlayed = isLastPlayed;
	}


	public boolean isOverlay() {
		return isOverlay;
	}


	public void setOverlay(boolean isOverlay) {
		this.isOverlay = isOverlay;
	}


	public boolean isPlacement() {
		return isPlacement;
	}


	public void setPlacement(boolean isPlacement) {
		this.isPlacement = isPlacement;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	public Bitmap getOriginalBitmapZoomed() {
		return originalBitmapZoomed;
	}
	public void setOriginalBitmapZoomed(Bitmap originalBitmapZoomed) {
		this.originalBitmapZoomed = originalBitmapZoomed;
	}
	public int getxPositionZoomed() {
		return xPositionZoomed;
	}
	public void setxPositionZoomed(int xPositionZoomed) {
		this.xPositionZoomed = xPositionZoomed;
	}
	public int getyPositionZoomed() {
		return yPositionZoomed;
	}
	public void setyPositionZoomed(int yPositionZoomed) {
		this.yPositionZoomed = yPositionZoomed;
	}
	
	public String getDisplayLetter(){
		return (this.placedLetter.length() > 0 ? this.placedLetter : this.originalLetter);
	}

	
	public boolean isDragPositionSet() {
		return dragPositionSet;
	}

	public void setDragPositionSet(boolean dragPositionSet) {
		this.dragPositionSet = dragPositionSet;
	}

	public int getTileIdAbove() {
		 if (tileIdAbove == -1){
			 tileIdAbove = TileLayoutService.getTileIdAbove(id);
		 }
		return tileIdAbove;
	}

	public void setTileIdAbove(int tileIdAbove) {
		this.tileIdAbove = tileIdAbove;
	}

	public int getTileIdBelow() {
		if (tileIdBelow == -1){
			 tileIdBelow = TileLayoutService.getTileIdBelow(id);
		 }

		return tileIdBelow;
	}

	public void setTileIdBelow(int tileIdBelow) {
		this.tileIdBelow = tileIdBelow;
	}

	public int getTileIdToTheLeft() {
		if (tileIdToTheLeft == -1){
			 tileIdToTheLeft = TileLayoutService.getTileIdToTheLeft(id);
		 }

		return tileIdToTheLeft;
	}

	public void setTileIdToTheLeft(int tileIdToTheLeft) {
		this.tileIdToTheLeft = tileIdToTheLeft;
	}

	public int getTileIdToTheRight() {
		if (tileIdToTheRight == -1){
			 tileIdToTheRight = TileLayoutService.getTileIdToTheRight(id);
		 }

		return tileIdToTheRight;
	}

	public void setTileIdToTheRight(int tileIdToTheRight) {
		this.tileIdToTheRight = tileIdToTheRight;
	}

/*
	public GameTile getTileToLeft() {
		return tileToLeft;
	}

	public void setTileToLeft(GameTile tileToLeft) {
		this.tileToLeft = tileToLeft;
	}

	public GameTile getTileToRight() {
		return tileToRight;
	}

	public void setTileToRight(GameTile tileToRight) {
		this.tileToRight = tileToRight;
	}

	public GameTile getTileAbove() {
		return tileAbove;
	}

	public void setTileAbove(GameTile tileAbove) {
		this.tileAbove = tileAbove;
	}

	public GameTile getTileBelow() {
		return tileBelow;
	}

	public void setTileBelow(GameTile tileBelow) {
		this.tileBelow = tileBelow;
	}
*/
public String getOriginalText() {
		return originalText;
	}
	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}
	public String getOriginalLetter() {
		return originalLetter;
	}
	//	public String getPlacedText() {
//		return placedText;
//	}
//	public void setPlacedText(String placeText) {
//		this.placedText = placedText;
//	}
	public void setPlacedLetter(String placedLetter) {
		this.placedLetter = placedLetter;
		if (placedLetter.length() > 0) {
			this.isDraggable = true;
		}
		else{
			this.isDraggable = false;
		}
	}
	
	public String getPlacedLetter() {
		return placedLetter;
	}
	public void setOriginalLetter(String originalLetter) {
		this.originalLetter = originalLetter;
	}
//	public String getCurrentLetter() {
//		return placedLetter.length() > 0 ? placedLetter : originalLetter;
//	}
	public boolean isDraggable() {
		return isDraggable;
	}
	public void setDraggable(boolean isDraggable) {
		this.isDraggable = isDraggable;
	}
	public boolean isDragging() {
		return isDragging;
	}
	public void setDragging(boolean isDragging) {
		this.isDragging = isDragging;

	}
	public String getDraggingLetter() {
		return draggingLetter;
	}
	public void setDraggingLetter(String draggingLetter) {
		this.draggingLetter = draggingLetter;
	}
	
	public void recallLetter(){
		this.placedLetter = "";
		this.draggingLetter = "";
		this.isDragging = false;
		this.isDraggable = false;
	}
	public int getxPositionCenter() {
		return xPositionCenter;
	}
	public void setxPositionCenter(int xPositionCenter) {
		this.xPositionCenter = xPositionCenter;
	}
	public int getyPositionCenter() {
		return yPositionCenter;
	}
	public void setyPositionCenter(int yPositionCenter) {
		this.yPositionCenter = yPositionCenter;
	}

	public int getxPositionCenterRelativeZoomed() {
		return xPositionCenterRelativeZoomed;
	}

	public void setxPositionCenterRelativeZoomed(int xPositionCenterRelativeZoomed) {
		this.xPositionCenterRelativeZoomed = xPositionCenterRelativeZoomed;
	}

	public int getyPositionCenterRelativeZoomed() {
		return yPositionCenterRelativeZoomed;
	}

	public void setyPositionCenterRelativeZoomed(int yPositionCenterRelativeZoomed) {
		this.yPositionCenterRelativeZoomed = yPositionCenterRelativeZoomed;
	}

	public boolean isDroppable() {
		return this.placedLetter.length() == 0;
	}

	public void removePlacement(){
		this.placedLetter = "";
	}
	 
	public void removeDrag(){
		this.isDragging = false;
		if (this.draggingLetter.length() > 0){
			this.placedLetter =  this.draggingLetter;
		}
		this.draggingLetter = "";
		this.xPositionDragging = 0;
		this.yPositionDragging = 0;
	}
	
	public void setDrag(){
		this.isDragging = true;
		this.draggingLetter = this.placedLetter;
		this.placedLetter = "";
		this.dragPending = false;
		this.dragPositionSet = false;
	}
	
	public void removeDragAndPlacement(){
		this.isDragging = false;
		this.placedLetter = "";
		this.draggingLetter = "";
		this.xPositionDragging = 0;
		this.yPositionDragging = 0;
		this.dragPositionSet = false;
	}

	public int getxPositionDragging() {
		return xPositionDragging;
	}

	public void setxPositionDragging(int xPositionDragging) {
		this.xPositionDragging = xPositionDragging;
		this.dragPositionSet = true;
	}

	public int getyPositionDragging() {
		return yPositionDragging;
	}

	public void setyPositionDragging(int yPositionDragging) {
		this.yPositionDragging = yPositionDragging;
		this.dragPositionSet = true;
	}

	public boolean isDragPending() {
		return dragPending;
	}

	public void setDragPending(boolean dragPending) {
		this.dragPending = dragPending;
		this.dragPositionSet = false;
	}
	
	
}


