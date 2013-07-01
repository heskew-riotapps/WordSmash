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
	
	private String id = "";
	
	@SerializedName("n_c_g")
	private int numCompletedGames = 0;
	
	private String activeGameId = "";
	
	@SerializedName("n_w")
	private int numWins = 0; //num wins
	
	@SerializedName("n_l")
	private int numLosses = 0; //num losses
	
	@SerializedName("n_d")
	private int numDraws = 0; //num draws
	
	//@SerializedName("alerts")
	//private List<Alert> latestAlerts = new ArrayList(); //the latest communication alert 
	
	private List<Purchase> purchases = new ArrayList<Purchase>();


//	@SerializedName("game_")
//	private Game notificationGame = null; //the game associated with notification via gcmIntent
	
	@SerializedName("o_n_i_a")
	private boolean noInterstitialAdsOption = false; //a paid upgrade to not display interstitial ads 
 
	/*
	@SerializedName("a_games")
	private List<Game> activeGames= new ArrayList<Game>();

	@SerializedName("c_games")
	private List<Game> completedGames = new ArrayList<Game>();
	
	@SerializedName("l_rf_d") 
	private Date lastRefreshDate = null; //new GregorianCalendar("10/6/2012"); //last time a game status changed that the player was involved in
	
	private List<Game> activeGamesYourTurn = null; //new ArrayList<Game>();
	private List<Game> activeGamesOpponentTurn = null; //new ArrayList<Game>();
*/
	//private String badge_drawable = "";

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
 
	/*
	public List<Alert> getLatestAlerts() {
		return latestAlerts;
	}
	public void setLatestAlerts(List<Alert> latestAlerts) {
		this.latestAlerts = latestAlerts;
	}
	*/
 
	public List<Purchase> getPurchases() {
		return purchases;
	}
	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}
	public boolean isNoInterstitialAdsOption() {
		return noInterstitialAdsOption;
	}
	
	public void setNoInterstitialAdsOption(boolean noInterstitialAdsOption) {
		this.noInterstitialAdsOption = noInterstitialAdsOption;
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

	
	public int getNumCompletedGames() {
		return numCompletedGames;
	}
	public void setNumCompletedGames(int numCompletedGames) {
		this.numCompletedGames = numCompletedGames;
	}
 
	
	public String getActiveGameId() {
		return activeGameId;
	}
	public void setActiveGameId(String activeGameId) {
		this.activeGameId = activeGameId;
	}
	public String getBadgeDrawable(){
		return PlayerService.getBadgeDrawable(this.numWins);
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
