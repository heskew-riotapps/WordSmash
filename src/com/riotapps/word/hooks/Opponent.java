package com.riotapps.word.hooks;

import android.content.Context;

import com.riotapps.word.R;
import com.riotapps.word.utils.Constants;

public class Opponent{
	
	private int id;
	private String name;
	private int numWins;
	private int numLosses;
	private int numDraws;
	private String imagePrefix;
	private int skillLevel;
	private int opponentGroupId;
	private OpponentRecord record = null;
	
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
					this.record =  OpponentService.getOpponentRecord(this.opponentGroupId);
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
	
	public String getBadgeDrawable(){
		return PlayerService.getBadgeDrawable(this.numWins);
	}

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
}
