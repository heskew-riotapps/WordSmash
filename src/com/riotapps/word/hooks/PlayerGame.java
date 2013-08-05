package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class PlayerGame implements Parcelable{
	private static final String TAG = PlayerGame.class.getSimpleName();
//	  key :player_id , ObjectId
//	  key :score,     Integer, :default => 0
//	  key :last_turn_date, Time
//	  key :last_alert_date, Time
//	  key :last_reminder_date, Time
//	  key :last_chatter_received_date, Time
//	  key :last_viewed_date, Time
//	  key :win_num,    Integer, :default => 0
//	  key :is_turn, Boolean, :default => false 
//	  key :is_winner, Boolean, :default => false 
//	  key :has_been_alerted_to_end_of_game, Boolean, :default => false 
	
	public PlayerGame(){}
	
	@SerializedName("player_id")
	private String playerId; 
	
	//private Player player; 

	private List<String> standaloneWords = new ArrayList<String>();
	
	@SerializedName("sc")
	private int score = 0;
	
	@SerializedName("st")
	private int status = 0;
	
/*	@SerializedName("l_t")
	private int lastTurn;

	@SerializedName("l_t_a")
	private int lastTurnAction;
	
	@SerializedName("l_t_p")
	private int lastTurnPoints;
	
	@SerializedName("l_t_d")
	private Date lastTurnDate;
*/		
	@SerializedName("l_a_d")
	private Date lastAlertDate;

	@SerializedName("l_r_d")
	private Date lastReminderDate;

	
	@SerializedName("w_n")
	private int winNum = 0;
	
	@SerializedName("i_t")
	private boolean isTurn = false;
	
  	@SerializedName("a_e_g")
	private boolean hasBeenAlertedToEndOfGame = false;
	
	@SerializedName("o")
	private int playerOrder = 0;

	@SerializedName("t_v")
	private int trayVersion = 1;
	
	@SerializedName("t_l")
	private List<String> trayLetters = new ArrayList<String>();
	
	
	public List<String> getTrayLetters() {
		return trayLetters;
	}

	public void setTrayTiles(List<String> trayLetters) {
		this.trayLetters = trayLetters;
	}
	
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public boolean isOpponent(){
		return this.playerOrder == 2;
	}
	
	public String getPlayerName(){
		//make sure this stuff is cached well
		
		if (this.isOpponent()){
			//get opponent name
		}
		else{
			//get context player name
		}
		return "XX";	
	}
	 
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	public Date getLastReminderDate() {
		return lastReminderDate;
	}

	public void setLastReminderDate(Date lastReminderDate) {
		this.lastReminderDate = lastReminderDate;
	}


	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	public boolean isTurn() {
		return isTurn;
	}

	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}

	public boolean isWinner() {
		return this.status == 4;
	}
	
	public boolean isDraw() {
		return this.status == 6;
	}

 
	public boolean isHasBeenAlertedToEndOfGame() {
		return hasBeenAlertedToEndOfGame;
	}

	public void setHasBeenAlertedToEndOfGame(boolean hasBeenAlertedToEndOfGame) {
		this.hasBeenAlertedToEndOfGame = hasBeenAlertedToEndOfGame;
	}

	 public boolean isTrayFull(){
		 
		 return this.trayLetters.size() == 7; 
	 }
 
	 public List<List<String>> getSortedTrayLetterSets(int setLength){
		 //grab all unique sets of tray letters in batches of "setLength"
		 //so if the tray letters are P, R, T, A, L, B, Y and the setLength is 4
		 //return P, R, T, A...P, R, T, L,...P, R, T, B...etc
		 List<List<String>> letterSets = new ArrayList<List<String>>();
	
		 //we only need 6 indexes here because if a set length of 7 is requested
		 //all letters are returned as the only eligible letter set
		 int x1 = 0;
		 int x2 = 1;
		 int x3 = 2;
		 int x4 = 3;
		 int x5 = 4;
		 int x6 = 5;
	 
		 
		 int traySize = this.trayLetters.size();
		 int maxIndex = traySize - 1;

		 
		 if (traySize < setLength){
			return letterSets; 
		 }
		 else if (this.trayLetters.size() == setLength){
			 //return all x letters as the single set to be returned
			 List<String> letterSet = new ArrayList<String>();
				
				for (int j = 0; j < setLength; j ++){
					letterSet.add(this.trayLetters.get(j));	
				}
			letterSets.add(letterSet);
			return letterSets;
		 }
		 else if (setLength == 1){
			 //return all x letters as the x number of sets 

				for (int j = 0; j <= maxIndex; j ++){
					List<String> letterSet = new ArrayList<String>();
					letterSet.add(this.trayLetters.get(j));
					letterSets.add(letterSet);
				}
 
			return letterSets;
		 }
		 else{
			 boolean notAtEnd = true;
			 while (notAtEnd){
				 	//the idea here is to gather every combination of positions (not every single permutation of letters)
				 	//later we can ensure that duplicate tile sets are not added amd sort them for index lookup
				 
					List<String> letterSet = new ArrayList<String>();
						 
					letterSet.add(this.trayLetters.get(x1));	
					if (setLength >= 2) { letterSet.add(this.trayLetters.get(x2)); }
					if (setLength >= 3) { letterSet.add(this.trayLetters.get(x3)); }
					if (setLength >= 4) { letterSet.add(this.trayLetters.get(x4)); }
					if (setLength >= 5) { letterSet.add(this.trayLetters.get(x5)); }
					if (setLength == 6) { letterSet.add(this.trayLetters.get(x6)); }
					
					if (setLength == 6 && x6 < (maxIndex - (setLength - 6))){
						x6 += 1;
					}
					else if(setLength >= 5 && x5 < (maxIndex - (setLength - 5))){
						x5 += 1;
					}
					else if(setLength >= 4 && x4 < (maxIndex - (setLength - 4))){
						x4 += 1;
					}	
					else if(setLength >= 3 && x3 < (maxIndex - (setLength - 3))){
						x3 += 1;
					}
				 	else if(setLength >= 2 && x2 < (maxIndex - (setLength - 2))){
				 		x2 += 1;
				 	}
				 	else if(x1 < (maxIndex - (setLength - 1))){
				 		x1 += 1;
				 		//reset other indexes
				 		x2 = x1+1;
						x3 = x1+2;
						x4 = x1+3;
						x5 = x1+4;
						x6 = x1+5;
				 	}
				 	else {
				 		//nothing more to do, let's get out of this loopy logic
				 		notAtEnd = false;
				 	}
					
					//we have filled up this letterSet
					if (letterSet.size() == setLength){
						//check for dupes here
						
						//sort for index lookup
						Collections.sort(letterSet);
						
						String newSet = "";
						for (String s : letterSet){
							newSet += s;
						}
						
						boolean exactSetMatch = false; //letterSets.size() > 0; //this will make exactSetMatch false for the first set
						for (int i = 0; i < letterSets.size(); i++){ // 
							String set = "";
							for (String s : letterSets.get(i)){
								set += s;
							}
							
							if (newSet.equals(set)){
								exactSetMatch = true;
								break;
							}
							
							/*
							boolean loopMatch = false;
							for (int j = 0; j < setLength; j++){ // 
		 						
								if (letterSets.get(i).get(j).equals(letterSet.get(j))){ 
								}
								else{
									exactSetMatch = false; 
									break;
								}	
				 			}
				 			*/
						}
						
						//only add it to collection if its not a complete duplicate of another sorted set
						//these sets will be used for index lookup, which is why they are sorted
						if (!exactSetMatch) {letterSets.add(letterSet);}
					}
			 }
			 
			 return letterSets;
		 }
		 
		 //set length of 6
		 //1,2,3,4,5,6
		 //1,3,4,5,6,7
		 //2,3,4,5,6,7
			 //max for x4 for 6 setLength = 4  maxIndex - (setlength - position) 6-(6-4) = 4
			 //max for x4 for 5 setLength = 5  maxIndex - (setlength - position) 6-(5-4) = 5
			 //max for x4 for 4 setLength = 6  maxIndex - (setlength - position) 6-(4-4) = 6

			 //max for x4 for 4 setLength = 6  maxIndex - (setlength - position) 5-(4-4) = 5

			 
	 		 
		 //set length of 4 end results
		 //1,2,3,4
		 //1,2,3,5
		 //1,2,3,6
		 //1,2,3,7
		 //1,2,4,5
		 //1,2,4,6
		 //1,2,4,7
		 //1,2,5,6
		 //1,2,5,7
		 //1,3,4,5
		 //1,3,4,6
		 //1,3,4,7
		 //1,3,5,6
		 //1,3,5,7
		 //1,3,6,7
		 //1,4,5,6
		 //1,4,5,7
		 //1,5,6,7
		 //2,3,4,5
		 //2,3,4,6
		 //2,3,4,7
		 //2,4,5,6
		 //2,4,5,7
		 //2,5,6,7
		 //3,4,5,6
		 //3,4,5,7
		 //3,5,6,7
		 //4,5,6,7	 
		 
		 
	 }
	 
	public boolean isActive(){
		//declined and cancelled
		//NO_TRANSLATION(0), (no action yet)
		//ACTIVE(1),
		//CANCELLED(2),
		//DECLINED(3),
		//WON(4),
		//LOST(5),
		//DRAW(6),
		//RESIGNED(7) //a temp status, will be changed to LOST after the game is over????
		//DECLINED_BY_INVITEES(8),
		if (this.status == 1 || this.status == 4 || this.status == 5 || this.status == 6 || this.status == 7){
			return true;
		}
		return false;
	}

	public int getPlayerOrder() {
		return playerOrder;
	}

	public void setPlayerOrder(int playerOrder) {
		this.playerOrder = playerOrder;
	}

	public int getTrayVersion() {
		return trayVersion;
	}

	public void setTrayVersion(int trayVersion) {
		this.trayVersion = trayVersion;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
//		Logger.d(TAG, "parcel out");
		out.writeString(this.playerId); 
	 
//		Logger.d(TAG, "parcel out playerId=" + this.player.getId());
		out.writeInt(this.score);
	//	out.writeLong(this.lastTurnDate == null ? 0 : this.lastTurnDate.getTime());
		out.writeLong(this.lastAlertDate == null ? 0 : this.lastAlertDate.getTime());
		out.writeLong(this.lastReminderDate == null ? 0 : this.lastReminderDate.getTime());
		out.writeInt(this.winNum);
		out.writeByte((byte) (this.isTurn ? 1 : 0));
	 
		out.writeByte((byte) (this.hasBeenAlertedToEndOfGame ? 1 : 0)); 
		out.writeInt(this.playerOrder);
 		out.writeList(this.trayLetters);
 	//	out.writeInt(this.lastTurn);
 	//	out.writeInt(this.lastTurnAction);
 	 	out.writeInt(this.status);
 		out.writeInt(this.trayVersion);
 		
	}
	
	public static final Parcelable.Creator<PlayerGame> CREATOR
			= new Parcelable.Creator<PlayerGame>() {
			public PlayerGame createFromParcel(Parcel in) {
				return new PlayerGame(in);
			}

			public PlayerGame[] newArray(int size) {
				return new PlayerGame[size];
			}
	};
	
	private PlayerGame(Parcel in) {
//		Logger.d(TAG, "parcel in");
		// same order as writeToParcel
	 	this.playerId = in.readString();
	 
	// 	Logger.d(TAG, "parcel in playerId=" + this.playerId);
	 	this.score = in.readInt();
	// 	this.lastTurnDate = new Date();
	// 	this.lastTurnDate.setTime(in.readLong());
		this.lastAlertDate = new Date();
	 	this.lastAlertDate.setTime(in.readLong()); 	
	 	this.lastReminderDate = new Date();
	 	this.lastReminderDate.setTime(in.readLong());
	  	this.winNum = in.readInt();
	  	this.isTurn = in.readByte() == 1;
	 
	    this.hasBeenAlertedToEndOfGame  = in.readByte() == 1;
	    this.playerOrder = in.readInt();
	    this.trayLetters = new ArrayList<String>();
	    in.readStringList(this.trayLetters);
//	    this.lastTurn = in.readInt();
//	    this.lastTurnAction = in.readInt();
	    this.status = in.readInt();
	    this.trayVersion = in.readInt();
	  //  Logger.d(TAG, "parcel in playerOrder=" + this.playerOrder);
	
	}
	
	public void removeFirstMatchingLetter(String letter){
		List<String> letters = this.trayLetters;
		
		for (int i = 0; i < this.trayLetters.size(); i++){
			if (this.trayLetters.get(i).equals(letter)){
				this.trayLetters.remove(i);
				break;
			}
		}
	}
 
	public void addLetterToTray(String letter){
		this.trayLetters.add(letter);
 
	}
	
	public boolean doesTrayContainAVowel(){
		String vowels[] = AlphabetService.getVowels();
		
		for (String trayLetter : this.trayLetters){
			for (String vowel : vowels){
				if (trayLetter.equals(vowel)){
					return true;
				}
			}
		}
		return false;
 
	}

	public boolean doesTrayContainAConsonant(){
		String consonants[] = AlphabetService.getConsonants();
		
		for (String trayLetter : this.trayLetters){
			for (String consonant : consonants){
				if (trayLetter.equals(consonant)){
					return true;
				}
			}
		}
		return false;
 
	}
}
