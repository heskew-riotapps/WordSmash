package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.R;
import com.riotapps.word.ui.GameTile;
import com.riotapps.word.ui.PlacedTile;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Utils;

 
public class Game implements Parcelable, Comparable<Game> {
	private static final String TAG = Game.class.getSimpleName();
	public Game(){}
	
	@SerializedName("id")
	private String id = "";
	
	@SerializedName("played_words")
	private List<PlayedWord> playedWords = new ArrayList<PlayedWord>();
 
	@SerializedName("player_games")
	private List<PlayerGame> playerGames = new ArrayList<PlayerGame>();
	
	@SerializedName("played_tiles")
	private List<PlayedTile> playedTiles = new ArrayList<PlayedTile>();
	
	@SerializedName("played_turns")
	private List<PlayedTurn> playedTurns = new ArrayList<PlayedTurn>();
/*	
	@SerializedName("l_t_a")
	private int lastTurnAction;
	
	@SerializedName("l_t_p")
	private String lastTurnPoints;
	
	@SerializedName("l_t_pl")
	private String lastTurnPlayerId;
*/	
	@SerializedName("l_t_d")
	private Date lastTurnDate;
	
 
	@SerializedName("r_v")
	private String randomVowel;

	@SerializedName("r_c")
	private List<String> randomConsonants;
	
	@SerializedName("hop")
	private List<String> hopper;
	
	@SerializedName("cr_d")
	private Date createDate = new Date(0);  
 
	
	@SerializedName("co_d")
	private Date completionDate = new Date(0); 

	@SerializedName("st")
	private int status = 0;   
	
	@SerializedName("t")
	private int turn = 0;  	
	
	@SerializedName("opp")
	private int opponentId;
//	private boolean showCompletionAlert;
 
	/*
	private List<PlayerGame> activePlayerGames = null;
	private List<PlayerGame> opponentPlayerGames = null;
	*/ 
	
//	private Player _lastTurnPlayer;
//	private PlayerGame _contextPlayerGame;
	

	
	public Opponent getOpponent(){
		return OpponentService.getOpponent(this.opponentId);
	}
 
	
	public int getOpponentId() {
		return opponentId;
	}

	public void setOpponentId(int opponentId) {
		this.opponentId = opponentId;
	}

	public String getRandomVowel() {
		return randomVowel;
	}

	public void setRandomVowel(String randomVowel) {
		this.randomVowel = randomVowel;
	}

	public List<String> getRandomConsonants() {
		return randomConsonants;
	}

	public void setRandomConsonants(List<String> randomConsonants) {
		this.randomConsonants = randomConsonants;
	}

	 /*
	//only used for new games, to add opponent to the player for "client-side joins"
	@SerializedName("opps")
	private List<Opponent> opponents_ = new ArrayList<Opponent>();
 
	public List<Opponent> getOpponents_() {
		return opponents_;
	}

	public void setOpponents_(List<Opponent> opponents_) {
		this.opponents_ = opponents_;
	}
	*/
	
/*
	private Player getLastTurnPlayer(){  //fix this
		if (this._lastTurnPlayer == null) {
			for (PlayerGame pg : this.getPlayerGames()){
				try{
				//Logger.d(TAG, "getLastTurnPlayer pg.name=" + pg.getPlayer().getName());
				}
				catch (Exception e){
					Logger.d(TAG, "getLastTurnPlayer e=" + e.toString());
				}
			//for (PlayerGame pg : this.getActivePlayerGames()){
			//	if (pg.get.equals(this.lastTurnPlayerId)){
			//	//	this._lastTurnPlayer = pg.getPlayer();
			//	}
			}
		}
		return this._lastTurnPlayer;
	}
	*/
	/*
	private Player getAdjustedLastTurnPlayer(){
		if (this._lastTurnPlayer == null) {
		    for (PlayerGame pg : this.getPlayerGames()){

			//for (PlayerGame pg : this.getActivePlayerGames()){
				if (pg.getPlayer().getId().equals(this.lastTurnPlayerId)){
					this._lastTurnPlayer = pg.getPlayer();
				}
			}
		}
		return this._lastTurnPlayer;
	}
	*/
	/*
	public PlayerGame getContextPlayerGame(String contextPlayerId){
		if (this._contextPlayerGame == null) {
			for (PlayerGame pg : this.getPlayerGames()){
				if (pg.getPlayerId().equals(contextPlayerId)){
					this._contextPlayerGame = pg;
				}
			}
		}
		return this._contextPlayerGame;
	}
	*/
	
	/*
	public Player getPlayerById(String playerId){
		Player player = null;
	 
		for (PlayerGame pg : this.getPlayerGames()){
			if (pg.getPlayerId().equals(playerId)){
				//player = pg.getPlayer();
				break;
			}
		}
 
		return player;
	}
	*/
//	@SerializedName("last_action_alert_text")
	//do not serialize
 //	private String lastActionText = "";
	
	public int getLastTurnPoints() {
		return this.getLastPlayedTurn().getPoints();
	}
 
	
/*
	public boolean isShowCompletionAlert() {
		return showCompletionAlert;
	}

	public void setShowCompletionAlert(boolean showCompletionAlert) {
		this.showCompletionAlert = showCompletionAlert;
	}
*/
	 
//	@SerializedName("d_c")
//	private String dupCheck = "";


	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PlayedWord> getPlayedWords() {
		return playedWords;
	}
	
	public List<PlayedWord> getLastPlayedWords() {
		List<PlayedWord> words = new ArrayList<PlayedWord>();
		
		for(PlayedWord word : this.playedWords){
			//Logger.d(TAG, "getLastPlayedWords word=" + word.getWord());
			if (word.getTurn() == (this.getTurn() - 1)){
				words.add(word);
			}
		}
		return words;
	}

	public void setPlayedWords(List<PlayedWord> playedWords) {
		this.playedWords = playedWords;
	}

	public List<PlayerGame> getPlayerGames() {
		return playerGames;
	}
	/*
	public List<PlayerGame> getActivePlayerGames() {
		if (this.activePlayerGames == null){
			this.activePlayerGames = new ArrayList<PlayerGame>();
		 	
			int x = 1;
			 for (PlayerGame pg : this.getPlayerGames()){ 
	        		Logger.d(TAG,"getActivePlayerGames pg.isACtive=" + pg.isActive() + " name=" + pg.getPlayer().getName());

				 if ( pg.isActive()){
	        		//adjust game order if needed, this is because the scoreboard needs the proper relative order, with no missing values
	        		//they should always from the server in playerOrder, so just readjust that order by bypassig the declined players
	        		pg.setPlayerOrder(x);
	        		this.activePlayerGames.add(pg);
	        		x += 1;
	        	}
			}
		}
	    return this.activePlayerGames;
	}
*/
	public List<PlayedTile> getPlayedTiles() {
		return playedTiles;
	}

	public void setPlayedTiles(List<PlayedTile> playedTiles) {
		this.playedTiles = playedTiles;
	}
	
 

	public PlayerGame[] getPlayerGameArray(){
		PlayerGame[] ret = new PlayerGame[this.getPlayerGames().size()];
		  for(int i = 0;i < ret.length;i++){
		    ret[i] = this.getPlayerGames().get(i);}
		  return ret;
		}
	
	public List<String> getHopper() {
		return hopper;
	}

	public void setHopper(List<String> hopper) {
		this.hopper = hopper;
	}

	public void shuffleHopper(){
		Collections.shuffle(this.hopper);
	}
	
	/*
	
	public List<PlayerGame> getOpponentPlayerGames(Player contextPlayer){ 
		//assume the context player is the first playergame
		if (this.opponentPlayerGames == null){
			this.opponentPlayerGames = new ArrayList<PlayerGame>();

			//for an active game we don't want any declined players in the mix
			if (this.isActive()){
				 for (PlayerGame pg : this.getPlayerGames()){ 
		         	if (!pg.getPlayer().getId().equals(contextPlayer.getId()) && 
		         			pg.isActive()){
		         		this.opponentPlayerGames.add(pg);
		         	}
				 }
			}
			else {
				//for completed games, only show those that did not decline
				for (PlayerGame pg : this.getPlayerGames()){ 
		         	if (!pg.getPlayer().getId().equals(contextPlayer.getId()) && 
		         			pg.isActive()){
		         	
		         		this.opponentPlayerGames.add(pg);
		         	}
				 }
			
				if (this.opponentPlayerGames.size() == 0){
					//for completed games, just in case all of the opponents declined, go ahead and return them,
					//just so the list has opponents in it 
					for (PlayerGame pg : this.getPlayerGames()){ 
			         	if (!pg.getPlayer().getId().equals(contextPlayer.getId())){
			         		this.opponentPlayerGames.add(pg);
			         	}
					 }
					
				}
			}
		}
		return this.opponentPlayerGames;
	}
	*/
	/*
	public boolean isContextPlayerStarter(Player contextPlayer){ 
		if (this.getContextPlayerGame(contextPlayer.getId()).getPlayerOrder() == 1) {
			return true;
		}
		//for (PlayerGame pg : this.getPlayerGames()){ 
        // 	if (pg.getPlayerOrder() == 1 && pg.getPlayer().getId().equals(contextPlayer.getId())){
        // 		return true;
        // 	}
		//}
		return false;
	}
	
	public int getContextPlayerOrder(Player contextPlayer){
		try{
			return this.getContextPlayerGame(contextPlayer.getId()).getPlayerOrder();
		}
		catch (Exception e){
			return 0;
		}
//		for (PlayerGame pg : this.getPlayerGames()){ 
 //        	if (pg.getPlayer().getId().equals(contextPlayer.getId())){
 //        		return pg.getPlayerOrder();
 //        	}
//		}
//		return 0;
	}
	*/
	public boolean isContextPlayerTurn(){
		return this.turn == 1 || this.getLastPlayedTurn().isOpponentPlay();	
	}
	
	/*
	public boolean isContextPlayerTurn(Player contextPlayer){
		try{
			return this.getContextPlayerGame(contextPlayer.getId()).isTurn();
		}
		catch (Exception e){
			return false;
		}
//		for (PlayerGame pg : this.getPlayerGames()){ 
 //        	if (pg.getPlayer().getId().equals(contextPlayer.getId())){
 //        		return pg.getPlayerOrder();
 //        	}
//		}
//		return 0;
	}
	*/
	/*
	public int getContextPlayerTrayVersion(Player contextPlayer){ 
		try{
			return this.getContextPlayerGame(0).getTrayVersion();
		}
		catch (Exception e){
			return 1;
		}
//		for (PlayerGame pg : this.getPlayerGames()){ 
 //        	if (pg.getPlayer().getId().equals(contextPlayer.getId())){
 //        		return pg.getTrayVersion();
  //       	}
//		}
//		return 1;
	}
	*/
	public PlayerGame getWinner(){ 
		 for (PlayerGame pg : this.getPlayerGames()){ 
         	if (pg.isWinner()){
         		return pg;
         	}
		}
		  return null;
	} 
	/*
	public List<Player> getOpponents(Player contextPlayer){ 
		//assume the context player is the first playergame
		List<Player> ret = new ArrayList<Player>();
		
		 for (PlayerGame pg : this.getPlayerGames()){ 
         	if (!pg.getPlayerId().equals(contextPlayer.getId()) && pg.getStatus() == 1){
         		//ret.add(pg.getPlayer());
         	}
		}
		  return ret;
	} 
	
	public List<Player> getAllOpponents(Player contextPlayer){ 
		//assume the context player is the first playergame
		List<Player> ret = new ArrayList<Player>();
		
		 for (PlayerGame pg : this.getPlayerGames()){ 
         	if (!pg.getPlayerId().equals(contextPlayer.getId())){
         		//ret.add(pg.getPlayer());
         	}
		}
		  return ret;
	}
	
	public int getNumActiveOpponents(){
		//refactor to take resigned/declined players out of the count
		return this.getPlayerGames().size() - 1;
	}
	
	public PlayerGame[] getPlayerGameOpponentsArray (){ 
		//assume the context player is the first playergame
		PlayerGame[] ret = new PlayerGame[this.getPlayerGames().size() - 1];
		
		  for(int i = 1;i < this.getPlayerGames().size();i++){
			  if (this.getPlayerGames().get(i).getPlayerOrder() > 1) {
				  ret[i - 1] = this.getPlayerGames().get(i);
			  }
		  }
		  return ret;
		}
		
		*/
	public void setPlayerGames(List<PlayerGame> playerGames) {
		this.playerGames = playerGames;
	}
 
	//public int getNumLettersLeft() {
	//	return this.hopper.size();
	//}

	 
 
	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

 
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
 
 
	public int getNumPlayerSlotsLeft(){
		return 4 - this.getPlayerGames().size(); 
	}

	public int getNumPlayers(){
		return this.getPlayerGames().size(); 
	}
	
 
	
	public boolean isCompleted(){
		return this.status == 3 || this.getStatus() == 4;
	}
	
	public boolean isActive(){
		return this.status == 1;
	}
	
	/*
	public int getLastTurnAction() {
		return lastTurnAction;
	}

	public void setLastTurnAction(int lastTurnAction) {
		this.lastTurnAction = lastTurnAction;
	}

	public String getLastTurnPlayerId() {
		return lastTurnPlayerId;
	}

	public void setLastTurnPlayerId(String lastTurnPlayerId) {
		this.lastTurnPlayerId = lastTurnPlayerId;
	}
*/
	public Date getLastTurnDate() {
		return lastTurnDate;
	}

	public void setLastTurnDate(Date lastTurnDate) {
		this.lastTurnDate = lastTurnDate;
	}
	
	

	public List<PlayedTurn> getPlayedTurns() {
		return playedTurns;
	}

	public void setPlayedTurns(List<PlayedTurn> playedTurns) {
		this.playedTurns = playedTurns;
	}

	public String getLastActionText(Context context){

		boolean isContext = this.isContextPlayerPerformedLastTurn();
		String opponentName = OpponentService.getOpponent(this.opponentId).getName();
		
		PlayerGame contextPlayerGame = this.playerGames.get(0); 
		PlayerGame opponentGame = this.playerGames.get(1); 


		Logger.d(TAG, "getLastActionText this.getStatus() ()=" + this.getStatus() );
		if (this.getStatus() == 3) { //game over
			if (this.getLastAction().equals(LastAction.TURN_SKIPPED)){
				//this means the game was called because of 4 consecutive skips
				 if (contextPlayerGame.isWinner()){
					 return String.format(context.getString(R.string.game_surface_game_over_win_after_skips),
							 contextPlayerGame.getScore(),
							 opponentGame.getScore(),
							 opponentName);
				 }
				 else if (contextPlayerGame.isDraw()){ 
					 return String.format(context.getString(R.string.game_surface_game_over_draw_after_skips),
							 contextPlayerGame.getScore(),
							 contextPlayerGame.getScore());
				 }
				 else { 
					 return String.format(context.getString(R.string.game_surface_game_over_loss_after_skips),
							 opponentGame.getScore(),
							 contextPlayerGame.getScore(),
							 opponentName);
				 }
				
			}
			else {
		  		 if (contextPlayerGame.isWinner()){
					 return String.format(context.getString(R.string.game_surface_game_over_win),
							 contextPlayerGame.getScore(),
							 opponentGame.getScore(),
							 opponentName);
				 }
				 else if (contextPlayerGame.isDraw()){ 
					 return String.format(context.getString(R.string.game_surface_game_over_draw),
							 contextPlayerGame.getScore(),
							 contextPlayerGame.getScore());
				 }
				 else { 
					 return String.format(context.getString(R.string.game_surface_game_over_loss),
							 opponentGame.getScore(),
							 contextPlayerGame.getScore(),
							 opponentName);
				 }
			}	 //handle draw
		 
		}
		else{
			Logger.d(TAG, "getLastActionText this.getLastAction()=" + this.getLastAction());
			switch (this.getLastAction()){
				case ONE_LETTER_SWAPPED:
					if (isContext){
						return context.getString(R.string.game_last_action_swapped_1_context);
					}
					else{
						return String.format(context.getString(R.string.game_last_action_swapped_1), opponentName);				
					}
				
				case TWO_LETTERS_SWAPPED:	
				case THREE_LETTERS_SWAPPED:
				case FOUR_LETTERS_SWAPPED:
				case FIVE_LETTERS_SWAPPED:
				case SIX_LETTERS_SWAPPED:
				case SEVEN_LETTERS_SWAPPED:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_swapped_context), this.getLastPlayedTurn().getAction());
						}
						else{
							return String.format(context.getString(R.string.game_last_action_swapped), opponentName, this.getLastPlayedTurn().getAction());				
						}
				case STARTED_GAME:
					if (isContext){
						return context.getString(R.string.game_last_action_started_context);
					}
					else{
						return String.format(context.getString(R.string.game_last_action_started), opponentName);				
					}
					
				case WORDS_PLAYED:
					
					List<PlayedWord> words = this.getLastPlayedWords();
					int numWordsPlayed = words.size();
			
					//for now limit display to 2
					switch (numWordsPlayed){
					case 1:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_word_played_context), this.getLastTurnPoints(), words.get(0).getWord());
						}
						else {
							return String.format(context.getString(R.string.game_last_action_word_played), this.getLastTurnPoints(), opponentName, words.get(0).getWord());						
						}
					default:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_2_words_played_context), this.getLastTurnPoints(), 
									words.get(0).getWord(), words.get(1).getWord());
						}
						else {
							return String.format(context.getString(R.string.game_last_action_2_words_played), this.getLastTurnPoints(), opponentName, 
									words.get(0).getWord(), words.get(1).getWord());						
						}
					
					}
				case TURN_SKIPPED:
					if (isContext){
						return context.getString(R.string.game_last_action_skipped_context);
					}
					else{
						return String.format(context.getString(R.string.game_last_action_skipped), opponentName);				
					}		
				
				case RESIGNED:
					if (isContext){
						return context.getString(R.string.game_last_action_resigned_context);
					}
					else{
						return String.format(context.getString(R.string.game_last_action_resigned), opponentName);				
					}
				case CANCELLED:
					return "cancelled"; ///probably not associated with game action, more of a pg status
					
				default:
					return context.getString(R.string.game_last_action_undetermined);
					
			}
 
		}

 	}

	public int getNumConsecutiveSkips(){
		int skips = 0;
		
		//have to go backwards
		for (int i = this.playedTurns.size() - 1; i >= 0; i--){
			if (this.playedTurns.get(i).getAction() == 10){
				skips = skips + 1;
			}
			else{
				break;
			}
		}
	/*	for(PlayedTurn turn : this.playedTurns){
			if (turn.getAction() == 10){
				skips = skips + 1;
			}
			else{
				break;
			}
		}
		*/
		return skips;

	}
	
	public String getWinnerAlertText(Context context, PlayerGame contextPlayerGame){
		String message = "";
		
	 
			PlayerGame singleOpponent = this.getPlayerGames().get(1);
			 if (contextPlayerGame.isWinner()){
				 message = String.format(context.getString(R.string.game_alert_game_over_2_player_context),
						 contextPlayerGame.getScore(),
						 singleOpponent.getScore(),
						 contextPlayerGame.getWinNum());
			 }
			 else if (contextPlayerGame.isDraw()){ 
				 message = String.format(context.getString(R.string.game_alert_game_over_2_player_draw),
						 contextPlayerGame.getScore(),
						 contextPlayerGame.getScore());
			 }
			 else { 
				 message = String.format(context.getString(R.string.game_alert_game_over_2_player),
						 singleOpponent.getScore(),
						 contextPlayerGame.getScore(),
						 singleOpponent.getPlayerName());
			 }
			 //handle draw
		 
		
		return message;
	}
	
	public PlayedTurn getLastPlayedTurn(){
		return this.playedTurns.get(this.playedTurns.size() - 1);
	}
	
	private boolean isContextPlayerPerformedLastTurn(){
		
		try{
			return !this.getLastPlayedTurn().isOpponentPlay();
		}
		catch(Exception e){
			// Logger.d(TAG, "isContextPlayerPerformedLastTurn  this.lastTurnPlayerId=" +  this.lastTurnPlayerId == null ? "null" : this.lastTurnPlayerId  + " contextPlayerId= " + contextPlayerId == null ? "null" : contextPlayerId + " error=" + e.getMessage());
			
			return false;
		}
	}
	
	public String getLastActionTextForList(Context context){

		String timeSince = Utils.getTimeSinceString(context, this.getLastTurnDate());
		String opponentName = OpponentService.getOpponent(this.opponentId).getName();
		
		PlayerGame contextPlayerGame = this.playerGames.get(0); 
		PlayerGame opponentGame = this.playerGames.get(1); 

		 if (contextPlayerGame.isWinner()){ 
			 return String.format(context.getString(R.string.game_list_game_over_win), timeSince,
					 contextPlayerGame.getScore(),
					 opponentGame.getScore(),
					 opponentName);
		 }
		 else if (contextPlayerGame.isDraw()){ 
			 return String.format(context.getString(R.string.game_list_game_over_draw), timeSince,
					 contextPlayerGame.getScore());
		 }
		 else { 
			 return String.format(context.getString(R.string.game_list_game_over_loss), timeSince,
					 opponentGame.getScore(),
					 contextPlayerGame.getScore(),
					 opponentName);
		 }
	}
	
	public PlayerGame getOpponentGame(){
		return this.playerGames.get(1);
		
	}

	public enum LastAction{
		NO_TRANSLATION(0),
		ONE_LETTER_SWAPPED(1),
		TWO_LETTERS_SWAPPED(2),
		THREE_LETTERS_SWAPPED(3),
		FOUR_LETTERS_SWAPPED(4),
		FIVE_LETTERS_SWAPPED(5),
		SIX_LETTERS_SWAPPED(6),
		SEVEN_LETTERS_SWAPPED(7),
		STARTED_GAME(8),
		WORDS_PLAYED(9),
		TURN_SKIPPED(10),
		RESIGNED(11),
		CANCELLED(12),
		DECLINED(13);;	
		
		private final int value;
		private LastAction(int value) {
		    this.value = value;
		 }
		
	  public int value() {
		    return value;
		  }
		
	  private static TreeMap<Integer, LastAction> _map;
	  static {
		_map = new TreeMap<Integer, LastAction>();
	    for (LastAction num: LastAction.values()) {
	    	_map.put(Integer.valueOf(num.value()), num);
	    }
	    //no translation
	    if (_map.size() == 0){
	    	_map.put(Integer.valueOf(0), NO_TRANSLATION);
	    }
	  }
	  
	  public static LastAction lookup(int value) {
		  return _map.get(Integer.valueOf(value));
	  	}
	}
	
	public LastAction getLastAction(){
		return LastAction.lookup(this.getLastPlayedTurn().getAction());
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
 
		out.writeString(this.id);
 
		out.writeList(this.playedWords);
		out.writeList(this.playerGames);
	 
		//out.writeSerializable(this.createDate);
		//out.writeSerializable(this.completionDate);
		out.writeLong(this.createDate == null ? 0 : this.createDate.getTime());
		out.writeLong(this.completionDate == null ? 0 : this.completionDate.getTime());
		out.writeInt(this.status);
 
	}

	public static final Parcelable.Creator<Game> CREATOR
		    = new Parcelable.Creator<Game>() {
		public Game createFromParcel(Parcel in) {
		    return new Game(in);
		}
		
		public Game[] newArray(int size) {
		    return new Game[size];
		}
	};
	
	 private Game(Parcel in) {
	 
         this.id = in.readString();
         in.readList(this.playedWords,PlayedWord.class.getClassLoader());
         in.readList(this.playerGames,PlayerGame.class.getClassLoader());
 
      
 
         this.createDate = new Date(in.readLong());
 
         this.completionDate = new Date(in.readLong());
      
         this.status = in.readInt();
      
       	 
     }

	@Override
	public int compareTo(Game game) {
        if (this.getCompletionDate().after(game.getCompletionDate()))
            return -1;
        else if (this.getCompletionDate().equals(game.getCompletionDate()))
            return 0;
        else
            return 1;
    }
	
	public void addPlayedTile(GameTile placedTile){
		boolean found = false;
		for(PlayedTile tile : this.playedTiles){
			if (tile.getBoardPosition() == placedTile.getId()){
				tile.addLetter(new PlayedLetter(this.turn, placedTile.getPlacedLetter()));
				found = true;
				break;
			}
		}
		if (!found){
			PlayedTile tile1 = new PlayedTile();
			tile1.setBoardPosition(placedTile.getId());
			tile1.addLetter(new PlayedLetter(this.turn, placedTile.getPlacedLetter()));
			this.playedTiles.add(tile1);
		}
	}
	
	public PlayedTile getPlayedTileAbove(PlayedTile playedTile){
		for (PlayedTile tile : this.playedTiles){
			if (tile.getBoardPosition() == playedTile.getTileIdAbove()) {return tile;}
		}
		return null;
	}
	
	public PlayedTile getPlayedTileBelow(PlayedTile playedTile){
		for (PlayedTile tile : this.playedTiles){
			if (tile.getBoardPosition() == playedTile.getTileIdBelow()) {return tile;}
		}
		return null;
	}
	
	public PlayedTile getPlayedTileToTheRight(PlayedTile playedTile){
		for (PlayedTile tile : this.playedTiles){
			if (tile.getBoardPosition() == playedTile.getTileIdToTheRight()) {return tile;}
		}
		return null;
	}
	
	public PlayedTile getPlayedTileToTheLeft(PlayedTile playedTile){
		for (PlayedTile tile : this.playedTiles){
			if (tile.getBoardPosition() == playedTile.getTileIdToTheLeft()) {return tile;}
		}
		return null;
	}
	
	public boolean isBoardTilePlayed(int boardPosition){
		for (PlayedTile tile : this.playedTiles){
			if (tile.getBoardPosition() == boardPosition) {return true;}
		}
		return false;
	}
	
	public int getNumConsecutivePlayableEmptyTilesInADirection(PlayedTile playedTile, String direction){
		int i = 0;
		int loopRegulator = 1;
		boolean loop = true;
//		PlayedTile loopTile = playedTile;
		int loopId = playedTile.getBoardPosition();
		
		while(loop){
			loopRegulator += 1;
			if (loopRegulator > 15) { loop = false; break; }
				
			if (direction.equals(Constants.DIRECTION_BELOW)){
				loopId = TileLayoutService.getTileIdBelow(loopId); //loopTile.getTileIdBelow(); 
			}
			else if (direction.equals(Constants.DIRECTION_ABOVE)){
				loopId = TileLayoutService.getTileIdAbove(loopId); //loopTile.getTileIdAbove(); 
			}
			else if (direction.equals(Constants.DIRECTION_LEFT)){
				loopId = TileLayoutService.getTileIdToTheLeft(loopId); //loopTile.getTileIdToTheLeft(); 
			}
			else if (direction.equals(Constants.DIRECTION_RIGHT)){
				loopId = TileLayoutService.getTileIdToTheRight(loopId); //loopTile.getTileIdToTheRight(); 
			}
			
			if (loopId == 255){ //border
				loop = false; //don't add any more to i
			}
			else if (this.isBoardTilePlayed(loopId)){ //we have come to a played tile, remove 1 from i because we are looking for empty playable tiles in an axis
				loop = false;
				if ( i > 0 ) {i -= 1;}
			}
			else { i += 1; }
		}
	 
		return i;

	}
	
	
	public int getNumLettersLeftInHopperAndOpponentTray(String letter){
		int count = 0;
		
		for (String l : this.hopper){
			if (l.equals(letter)){
				count += 1;
			}
		}
		for (String l : this.playerGames.get(1).getTrayLetters()){
			if (l.equals(letter)){
				count += 1;
			}
		}
		return count;
	}
	
	public int getTotalNumLetterCountLeftInHopperAndOpponentTray(){
		return this.hopper.size() + this.playerGames.get(1).getTrayLetters().size() ;
	}
}
