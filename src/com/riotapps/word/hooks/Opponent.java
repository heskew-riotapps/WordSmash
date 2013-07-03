package com.riotapps.word.hooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.riotapps.word.R;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;

public class Opponent{
	
	private int id;
	private String name;
 
	private String imagePrefix;
	private int skillLevel;
	private int opponentGroupId;
	private OpponentRecord record = null;
	private Bitmap smallImage = null;
	
	private OpponentGroup opponentGroup = null;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumWins() {
		return this.getRecord().getNumWins();
	}
	 
	public int getNumLosses() {
		return this.getRecord().getNumLosses();
	}
	 
	public int getNumDraws() {
		return this.getRecord().getNumDraws();
	}
	 
	public String getImagePrefix() {
		return imagePrefix;
	}
	public void setImagePrefix(String imagePrefix) {
		this.imagePrefix = imagePrefix;
	}
	public int getSkillLevel() {
		return skillLevel;
	}
	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}
	
	
	public int getOpponentGroupId() {
		return opponentGroupId;
	}
	public void setOpponentGroupId(int opponentGroupId) {
		this.opponentGroupId = opponentGroupId;
	}
	
	public OpponentRecord getRecord() {
		//store in local variable
		if (this.record == null) {
			this.record =  OpponentService.getOpponentRecord(this.id);
		}
		return this.record;
	}
	
 
	
	public OpponentGroup getOpponentGroup(){
		
		//store in local variable
		if (this.opponentGroup == null) {
			this.opponentGroup =  OpponentGroupService.getOpponentGroup(this.opponentGroupId);
		}
		return this.opponentGroup;
	
	}
	
//	public String getBadgeDrawable(){
//		return PlayerService.getBadgeDrawable(this.get);
//	}

	public String getDrawableByMode(String mode){
		return this.imagePrefix + Constants.UNDERSCORE + mode;
	}
	
	public String getSkillLevelText(Context context){	
		switch (this.skillLevel){
		case 1:
			return context.getString(R.string.skill_level_1);
		case 2:
			return context.getString(R.string.skill_level_2);
		case 3:
			return context.getString(R.string.skill_level_3);
		}
		return Constants.EMPTY_STRING;
	}

	public Bitmap getSmallBitmap(){
		if (this.smallImage == null){
		 this.preloadBitmaps();
		}
		
		return smallImage;
	
	}
	
	public void preloadBitmaps(){
		if (this.smallImage == null){
		  Context context = ApplicationContext.getAppContext();
			
		  int opponentImageId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + this.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_SMALL), null, null);
		  this.smallImage = BitmapFactory.decodeResource(context.getResources(), opponentImageId);
		}
	}
	
	public void addLossToRecord(){
		this.getRecord().setNumLosses(this.getRecord().getNumLosses() + 1);
	}
	
	public void addWinToRecord(){
		this.getRecord().setNumWins(this.getRecord().getNumWins() + 1);
	}
	
	public void addDrawToRecord(){
		this.getRecord().setNumDraws(this.getRecord().getNumDraws() + 1);
	}
}
