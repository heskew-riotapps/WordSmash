package com.riotapps.word.hooks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.riotapps.word.utils.Constants;
import com.google.gson.annotations.SerializedName;

public class Player implements Parcelable{
	private static final String TAG = Player.class.getSimpleName();
	
	public Player(){}
	
	@SerializedName("id")
	private String id = "";
	
//	@SerializedName("n_c_g")
//	private int numCompletedGames = 0;
	
	@SerializedName("activeGameId")
	private String activeGameId = "";
	
	@SerializedName("n_w")
	private int numWins = 0; //num wins
	
	@SerializedName("n_l")
	private int numLosses = 0; //num losses
	
	@SerializedName("n_d")
	private int numDraws = 0; //num draws

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	
	public int getNumGames(){
		return this.numWins + this.numLosses + this.numDraws;
	}
	
	 public String getName(Context ctx){
		 return ctx.getString(com.riotapps.word.R.string.anonymous_player_name); 
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
	public String getActiveGameId() {
		return activeGameId;
	}
	public void setActiveGameId(String activeGameId) {
		this.activeGameId = activeGameId;
	}
 	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		
//		Logger.d(TAG, "parcelout");
		out.writeString(this.id);

		out.writeInt(this.numWins);
		out.writeInt(this.numLosses);
		out.writeInt(this.numDraws);
	//	out.writeString(this.badge_drawable);
//		Logger.d(TAG, "parcel out gravatar=" + this.gravatar);
	}
	
	public static final Parcelable.Creator<Player> CREATOR
    			= new Parcelable.Creator<Player>() {
			public Player createFromParcel(Parcel in) {
				return new Player(in);
			}

			public Player[] newArray(int size) {
				return new Player[size];
			}
	};

	private Player(Parcel in) {
		// same order as writeToParcel
	//	Logger.d(TAG, "parcelin");
		this.id = in.readString();

//		Logger.d(TAG, "parcel in authToken=" + this.authToken);
		this.numWins = in.readInt();
		this.numLosses = in.readInt();
		this.numDraws = in.readInt();

	}

	public void addWinToPlayerRecord(){
		this.numWins = this.numWins + 1;
	}
	
	public void addLossToPlayerRecord(){
		this.numLosses = this.numLosses + 1;
	}
	
	public void addDrawToPlayerRecord(){
		this.numDraws = this.numDraws + 1;
	}
	
}
