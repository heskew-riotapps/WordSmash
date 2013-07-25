package com.riotapps.word.hooks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.riotapps.word.R;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Check;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Storage;
import com.riotapps.word.utils.Utils;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.GameTile;
import com.riotapps.word.ui.GameTileComparator;
import com.riotapps.word.ui.PlacedResult;
import com.riotapps.word.ui.PlacedWord;
import com.riotapps.word.ui.RowCol;
import com.riotapps.word.data.GameData;
import com.riotapps.word.hooks.WordService;
 
 
@SuppressLint("NewApi")
public class GameService {
	private static final String TAG = GameService.class.getSimpleName();
	
	
	public static boolean checkGameAlertAlreadyShown(Context context, String gameId){
		SharedPreferences settings = Storage.getSharedPreferences();
		if (settings.getBoolean(String.format(Constants.USER_PREFS_GAME_ALERT_CHECK, gameId), false) == false) { 
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(String.format(Constants.USER_PREFS_GAME_ALERT_CHECK, gameId),true);
			// Check if we're running on GingerBread or above
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			     // If so, call apply()
			     editor.apply();
			 // if not
			 } else {
			     // Call commit()
			     editor.commit();
			 }
			return false;
		}
		else{
			return true;
		}
	}
	

	public static List<Game> getLocalActiveGames(){
		return GameData.getLocalActiveGames();	
	}

	
	public static Game getGame(String gameId){
		return GameData.getGame(gameId);
	}
	
	public static void removeGame(String gameId){
		 
		GameData.removeGame(gameId);
 	}

	public static Game createGame(Context ctx, Player contextPlayer, int opponentId) throws DesignByContractException{
		
		//Check.Require(PlayerService.getPlayerFromLocal().getId().equals(contextPlayer.getId()), ctx.getString(R.string.validation_incorrect_context_player));
    	Check.Require(contextPlayer.getActiveGameId().length() == 0, ctx.getString(R.string.validation_create_game_duplicate));
		
		
		Game game = new Game();
    	
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss", Locale.US);
		Date now = Calendar.getInstance().getTime();        
	 
		game.setId(df.format(now));
    	PlayerGame pg = new PlayerGame();
    	pg.setPlayerId(contextPlayer.getId());
    	pg.setScore(0);
    	pg.setStatus(1);
    	pg.setTurn(true);
    	pg.setTrayVersion(1);
    	pg.setPlayerOrder(1);
    	
  
    	PlayerGame pg2 = new PlayerGame();
    	pg2.setPlayerId(String.valueOf(opponentId));
    	pg2.setScore(0); 
    	pg2.setStatus(1);
    	pg2.setTurn(false);
    	pg2.setTrayVersion(1);
    	pg2.setPlayerOrder(2);
    	
    	
    	
    	//ported from rails
    	game.setCreateDate(new Date());
    	game.setStatus(1); //active
    	game.setTurn(1);
    	game.setOpponentId(opponentId);
    	game.setLastTurnDate(game.getCreateDate());
    	game.setRandomVowel(AlphabetService.getRandomVowel());
    	game.setRandomConsonants(AlphabetService.getRandomConsonants());
    	game.setHopper(AlphabetService.getHopper(game.getRandomVowel(), game.getRandomConsonants()));
    	
    	
    	//load the tile trays for the players
    	for (int i = 0; i < 14; i++){
    		//first 7 go to player
    		if (i < 7){
    			pg.addLetterToTray(game.getHopper().get(0));
    		}
    		//next 7 got to opponent
    		else {
    			pg2.addLetterToTray(game.getHopper().get(0));
    		}
    		
    		game.getHopper().remove(0);
    	}
    	
    	List<PlayerGame> pgs = new ArrayList<PlayerGame>();
    	pgs.add(pg);
    	pgs.add(pg2);
    	game.setPlayerGames(pgs);
    	
    	List<PlayedTurn> turns = new ArrayList<PlayedTurn>();
    	PlayedTurn turn = new PlayedTurn();
    	turn.setOpponentPlay(false);
    	turn.setTurn(0);
    	turn.setPoints(0);
    	turn.setAction(8);
    	turn.setPlayedDate(new Date());
    	turns.add(turn);
    	
    	 
    	game.setPlayedTurns(turns);
    	
    	//get hopper letters
    	//generate random consonants and vowels 
    	//save game as 
    	/*
    	#add a PlayedTurn record
		played_turn = PlayedTurn.new
		played_turn.player_id = current_player.id
		played_turn.t = 0 #turn_num (zero represents the starting of the game)
		played_turn.a = 8 #last turn action - started the game
		played_turn.p = 0 #points
		played_turn.p_d = nowDate #played_date
		@game.played_turns << played_turn
    	*/
    	
    	//just for logging
    	Logger.d(TAG, "random vowel=" + game.getRandomVowel());
    	String consonants = "";
    	String hopper = "";
    	for (String s : game.getRandomConsonants())
    	{
    		consonants += s + ",";
    	}
     	for (String s : game.getHopper())
    	{
    		hopper += s + ",";
    	}
    	Logger.d(TAG, "random consonants=" + consonants);
    	Logger.d(TAG, "random hopper=" + hopper);
    	
    	contextPlayer.setActiveGameId(game.getId());
    	PlayerService.savePlayer(contextPlayer);
    	saveGame(game);
    	
    	return game;
	}
	
	public static void saveGame(Game game){
		GameData.saveGame(game);
	}
	

	public static void cancel(Player player, Game game){
		
		//Check.Require(game.getTurn() == 1, "can only cancel game on first turn");
		removeGame(game.getId());
		player.setActiveGameId("");
		PlayerService.savePlayer(player);
	}
	
	
	public static void play(boolean isOpponent, Game game, PlacedResult placedResult){
		Logger.d(TAG, "GameService.play called.  isOpponent=" + isOpponent);		
		Date now = new Date();

		//add a new word to the game's word list for each word played
		for (PlacedWord placedWord : placedResult.getPlacedWords()){
			PlayedWord word = new PlayedWord();
			word.setPlayedDate(now);
			word.setOpponentPlay(isOpponent);
			word.setPointsScored(placedWord.getTotalPoints());
			word.setTurn(game.getTurn());
			word.setWord(placedWord.getWord());
			
			game.getPlayedWords().add(word);
		}
		

		for(GameTile placedTile : placedResult.getPlacedTiles()){
			//add or update a board tile to the list for each placed tile, 
			game.addPlayedTile(placedTile);
			
			//remove the played letters from the player's tray. 
			//make sure to only remove one letter per placedTile even if the same letter is repeated in the player's tray

			//the opponent is always the second playerGame
			game.getPlayerGames().get(isOpponent ? 1 : 0).removeFirstMatchingLetter(placedTile.getPlacedLetter());
		}

		//add letters from hopper into players tray to make up for played letters that were removed
		for (int i = 0; i < placedResult.getPlacedTiles().size(); i++){
			//take care since the hopper may be near the end
			if (game.getHopper().size() > 0){
				//add the first hopper letter to the player's tray
				game.getPlayerGames().get(isOpponent ? 1 : 0).getTrayLetters().add(game.getHopper().get(0));
				//remove hopper letter that was just added to the player's tray
				game.getHopper().remove(0);
			}
		}
			
		PlayedTurn turn = new PlayedTurn();
    	turn.setOpponentPlay(isOpponent);
    	turn.setTurn(game.getTurn());
    	turn.setPoints(placedResult.getTotalPoints());
    	turn.setAction(9); // #WORDS_PLAYED(9), action
    	turn.setPlayedDate(new Date());
    	game.getPlayedTurns().add(turn);
		
    	game.getPlayerGames().get(isOpponent ? 1 : 0).setScore(game.getPlayerGames().get(isOpponent ? 1 : 0).getScore() + placedResult.getTotalPoints());
 
    	//not needed for opponent, keeping for now though
    	game.getPlayerGames().get(isOpponent ? 1 : 0).setTrayVersion(game.getPlayerGames().get(isOpponent ? 1 : 0).getTrayVersion() + 1);		
    		 
    	game.setLastTurnDate(now);
		game.setTurn(game.getTurn() + 1);
		
		//are any letters left in the hopper??
		//if not (and the player's tray is empty) the game is over.  let's check
		if (game.getPlayerGames().get(isOpponent ? 1 : 0).getTrayLetters().size() == 0 && 
			game.getHopper().size() == 0) {
			//game is over	
			//figure out winner
			
			//send in opposite playerGame to calculate bonus
			int bonus = calculateBonusScore(game.getPlayerGames().get(isOpponent ? 0 : 1));
			game.getPlayerGames().get(isOpponent ? 1 : 0).setScore(game.getPlayerGames().get(isOpponent ? 1 : 0).getScore() + bonus); 
			 
			int playerScore = game.getPlayerGames().get(0).getScore();
			int opponentScore = game.getPlayerGames().get(1).getScore();
			
			// #WON(4), #LOSS(5), #DRAW(6)
			if (playerScore > opponentScore){
				//player wins!!
				game.getPlayerGames().get(0).setStatus(4);
				game.getPlayerGames().get(1).setStatus(5);
				
				PlayerService.addWinToPlayerRecord();
				game.getOpponent().addWinToRecord();
			}
			else if (playerScore < opponentScore){
				game.getPlayerGames().get(0).setStatus(5);
				game.getPlayerGames().get(1).setStatus(4);

				PlayerService.addLossToPlayerRecord();
				game.getOpponent().addLossToRecord();
			}
			else {
				game.getPlayerGames().get(0).setStatus(6);
				game.getPlayerGames().get(1).setStatus(6);	
				
				PlayerService.addDrawToPlayerRecord();
				game.getOpponent().addDrawToRecord();
			}
						
			 game.setStatus(3); // 3  # completed
			 game.setCompletionDate(now);
			 addGameToCompletedList(game);
			 OpponentService.saveOpponentRecord(game.getOpponentId(), game.getOpponent().getRecord());
		}
		else {
			//game is not over, let's keep going
			//set turns, just in case...more than likely this will not be necessary
			game.getPlayerGames().get(0).setTurn(isOpponent);
			game.getPlayerGames().get(1).setTurn(!isOpponent);	
		}
//		ApplicationContext.captureTime(TAG, "WordService.isWordIndexed starting");
 //		Logger.d(TAG, "is aaaabenn indexed? " + WordService.isWordIndexed("aaaabenn"));
//		ApplicationContext.captureTime(TAG, "WordService.isWordIndexed 1 ended");
	//	Logger.d(TAG, "is ehiinooopstz indexed? " + WordService.isWordIndexed("ehiinooopstz"));
//		ApplicationContext.captureTime(TAG, "WordService.isWordIndexed 2 ended");
		
		GameService.saveGame(game);
		//return game;	
	}
	
 
	
public static Game skip(boolean isOpponent, Game game){
		Logger.d(TAG, "GameService.skip called.  isOpponent=" + isOpponent);
		Date now = new Date();

		PlayedTurn turn = new PlayedTurn();
    	turn.setOpponentPlay(isOpponent);
    	turn.setTurn(game.getTurn());
    	turn.setPoints(0);
    	turn.setAction(10); // #SKIPPED(10), action
    	turn.setPlayedDate(new Date());
    	game.getPlayedTurns().add(turn);
		

    	//might not be needed, keeping for now though
    	game.getPlayerGames().get(isOpponent ? 1 : 0).setTrayVersion(game.getPlayerGames().get(isOpponent ? 1 : 0).getTrayVersion() + 1);		
    		 
    	game.setLastTurnDate(now);
		game.setTurn(game.getTurn() + 1);
		
		//if there has been 4 skips in a row, game is over, unlikely as it is too happen, let's check for it
		if (game.getNumConsecutiveSkips() == 4) {
			//game is over	
			//figure out winner (no bonus is awarded for game ending on skips)
			  
			int playerScore = game.getPlayerGames().get(0).getScore();
			int opponentScore = game.getPlayerGames().get(1).getScore();
			
			// #WON(4), #LOSS(5), #DRAW(6)
			if (playerScore > opponentScore){
				//player wins!!
				game.getPlayerGames().get(0).setStatus(4);
				game.getPlayerGames().get(1).setStatus(5);
				
				PlayerService.addWinToPlayerRecord();
				game.getOpponent().addWinToRecord();
 			}
			else if (playerScore < opponentScore){
				game.getPlayerGames().get(0).setStatus(5);
				game.getPlayerGames().get(1).setStatus(4);

				PlayerService.addLossToPlayerRecord();
				game.getOpponent().addLossToRecord();
			}
			else {
				game.getPlayerGames().get(0).setStatus(6);
				game.getPlayerGames().get(1).setStatus(6);	
				
				PlayerService.addDrawToPlayerRecord();
				game.getOpponent().addDrawToRecord();
				
			}
						
			 game.setStatus(3); // 3  # completed
			 game.setCompletionDate(now);
			 addGameToCompletedList(game);
			 OpponentService.saveOpponentRecord(game.getOpponentId(), game.getOpponent().getRecord());
		}
		else {
			//game is not over, let's keep going
			//set turns, just in case...more than likely this will not be necessary
			game.getPlayerGames().get(0).setTurn(isOpponent);
			game.getPlayerGames().get(1).setTurn(!isOpponent);	
		}

		GameService.saveGame(game);
		return game;	
	}
	
	public static Game swap(boolean isOpponent, Game game, List<String> swappedLetters){
		Logger.d(TAG, "GameService.swap called.  isOpponent=" + isOpponent);
		Date now = new Date();
	
		PlayedTurn turn = new PlayedTurn();
		turn.setOpponentPlay(isOpponent);
		turn.setTurn(game.getTurn());
		turn.setPoints(0);
		turn.setAction(swappedLetters.size()); // #SKIPPED(10), action
		turn.setPlayedDate(new Date());
		game.getPlayedTurns().add(turn);
		
		/*
		#ONE_LETTER_SWAPPED(1),
		#TWO_LETTERS_SWAPPED(2),
		#THREE_LETTERS_SWAPPED(3),
		#FOUR_LETTERS_SWAPPED(4),
		#FIVE_LETTERS_SWAPPED(5),
		#SIX_LETTERS_SWAPPED(6),
		#SEVEN_LETTERS_SWAPPED(7),
		*/
	
		//add letters from hopper into players tray to make up for played letters that were removed
		for (String letter : swappedLetters){
			//add back to hopper
			game.getHopper().add(letter);
			
			//remove from tray. 
			game.getPlayerGames().get(isOpponent ? 1 : 0).removeFirstMatchingLetter(letter);
		}
		
		//reshuffle since letters were added back to hopper
		game.shuffleHopper();
		
		//add letters from hopper into players tray to make up for swapped letters that were removed
		for (int i = 0; i < swappedLetters.size(); i++){
			//take care since the hopper may be near the end
			if (game.getHopper().size() > 0){
				//add the first hopper letter to the player's tray
				game.getPlayerGames().get(isOpponent ? 1 : 0).getTrayLetters().add(game.getHopper().get(0));
				//remove hopper letter that was just added to the player's tray
				game.getHopper().remove(0);
			}
		}
		
		//might not be needed, keeping for now though
		game.getPlayerGames().get(isOpponent ? 1 : 0).setTrayVersion(game.getPlayerGames().get(isOpponent ? 1 : 0).getTrayVersion() + 1);		
			 
		game.setLastTurnDate(now);
		game.setTurn(game.getTurn() + 1);
		
	
		//game is not over, let's keep going
		//set turns, just in case...more than likely this will not be necessary
		game.getPlayerGames().get(0).setTurn(isOpponent);
		game.getPlayerGames().get(1).setTurn(!isOpponent);	
	
		GameService.saveGame(game);
		return game;	
	}	
	
	public static int calculateBonusScore(PlayerGame playerGame){
		int bonus = 0;
		
		for (String letter : playerGame.getTrayLetters()){
			bonus = bonus + AlphabetService.getLetterValue(letter);
		}
		
		return bonus;

	}
	
	public static void autoPlay(Context context, Game game, List<GameTile> boardTiles){
		PlacedResult placedResult = new PlacedResult();
		
		Logger.d(TAG,"before getDefaultLayout");
		TileLayout defaultLayout = TileLayoutService.getDefaultLayout(context);
		//autoplay logic kicked off here
		//put results in placedResult object just like in normal play
		
		//game.getOpponentGame().getTrayLetters() are the letters you will be playing with
		
		//game.getPlayedTiles() contains the tiles that already have at least one letter played on them
		Logger.d(TAG,"before getPlayedTiles");
		List<PlayedTile> tiles = game.getPlayedTiles();
		
	
		
		//create collections for H/V tiles/sets. 
		//object in collection must contain letter(s),len , x, y
		
		
		//to get the neighboring playedTiles do this: (example...i assume it would be done in some AI loop for real)
		//if there is not a played tile, you can assume its a blank tile
		PlayedTile above = game.getPlayedTileAbove(game.getPlayedTiles().get(0));
		PlayedTile below = game.getPlayedTileBelow(game.getPlayedTiles().get(0));
		PlayedTile right = game.getPlayedTileToTheRight(game.getPlayedTiles().get(0));
		PlayedTile left = game.getPlayedTileToTheLeft(game.getPlayedTiles().get(0));
		
		Logger.d(TAG,"before played tile loop");
		//for each tile
		for  (PlayedTile tile : tiles) {
			PlayedTile[] tileArray = new PlayedTile[1];
			tileArray[0] = tile;
			
		//	Logger.d(TAG,"played tile loop" + tile.getBoardPosition());
			
			
		//CanAIPlayOnTile(tile);	
		//get left/right/up/down -
			//TileLayoutService.getTileIdAbove(tileId), Below, ToTheRight, ToTheLeft will help you alot
			//int uTile = TileLayoutService.getTileIdAbove(tile.getBoardPosition());
			PlayedTile nextTile;
			
			/*
			//have to have a null check here
			//wrong
			try{
				
				nextTile = tiles.get(TileLayoutService.getTileIdAbove(tile.getBoardPosition()));
			}
			catch (IndexOutOfBoundsException iEx){
				//played tile does not exist at that location
			}
			*/
			//??GameService.getLettersAlongOnAxis might help 
			//if nextTile null to another direction. if not, also go opposite direction
			//need to take precaution to not process multiple tiles of the same string in the same axis
		//append / get next
			//add to H or V collection at end
			//is it part of a H/V word that ive already hit? Ignore for axis (not for cross) see opposite
			// -- to this point need way to check existing list 
			//so for every tile check H then V .. if either continues then becomes part of one workd
			//while up then while right then while down then while left, but skip if already counted
		//add single tile to opposite h/v collection regardless for single cross build
		}
		
		//for each in V col 
			//look for another V on same row
				//check distance (playable together? otherwise prevent overwrite
		
		//for each in H col
			//look for another H on same col
				//check distance (playable together? otherwise prevent overwrite
		
		//with letter blob compares, still need to shift/rotate through playable tiles
		//this is to check for words of all length, combinations of the letters from 2 thru tray+board(row/col)
		
		//all possible plays added to list - 
			//check for distance/position
		//free standing plays added to a smash list
			//check for valid impacted words
		//calc scores of valid plays 
		//sort by score
		//apply AI difficulty rules and random failure rates
		
		//if getPlayedTiles.size() == 0, the player skipped his first turn, you can play any word
		//formed by the tray tiles but at least one letter must be placed on a starter tile
		
		//this will return row and column (15x15 board)
		//TileLayoutService.getRowCol(tileId) 
		//this is already wrapped by game.getPlayedTile.get(x).getRowCol()
		
		// you'll probably want to look at GameService.isWordConnectedToPlayedWords and GameService.getWords to 
		//study some of that logic
		
		
		//WordService.isWordValid(word) will tell you if the word is in the dictionary
		
		//for more advanced AI, potential bonus can be taken into account
		//TileLayoutService.getLetterMultiplier(tileId, layout)
		//TileLayoutService.getWordMultiplier(tileId, layout)
		
		//getTileValue returns the points for a specific played tile, helpful for more advanced AI
 
		//game.getPlayedTiles().get(0).getLatestPlayedLetter() will return the latest letter that has been played on that tile
		//note that game.getPlayedTiles will only return the tiles that have had a letter played on them, it will NOT return all 225 tiles from the board layout
		
		//defaultLayout will contain all of the bonus and starter tiles, you may not need it directly
		
		//TileLayoutService.getTileIdAbove(tileId), Below, ToTheRight, ToTheLeft will help you alot 
		//if any of them return 255 that means the tile position being requested is outside of the board boundaries
		
		 WordService wordService = new WordService(context);
	        
		 //put word lookup calls here
 	            
	     wordService.finish();
	     wordService = null;
 		
	     List<PlacedResult> placedResults = new ArrayList<PlacedResult>();
	     
	     //for each placedResult option use a new List<GameTile> based on boardTiles passed in
	     //call setPlacedLetter on each board tile played
	     //put all options in a list of PlacedResults 
	     //for each option, reset list of boardTiles 
	     /*
	     try {
	    	  	placedResults.add(GameService.checkPlayRules(context, defaultLayout, game, boardTiles, false));
		 }
	     catch (DesignByContractException e){

	    	 //there was a problem, do not add to list
	     }
	     */
	     
		
		if (placedResults.size() == 0){
			//we have a skip or swap or resign
			//how to determine which, perhaps a simple flag
			GameService.skip(true, game);

		}
		else {
			//we have a play to handle
			//for the moment, let's just take the first placedResult
			GameService.play(true, game, placedResults.get(0));
		}
			
		
		
		
		//derive points  and placedResult
	//	PlacedResult placedResult = GameService.checkPlayRules(parent, this.defaultLayout, this.parent.getGame(), this.tiles, this.trayTiles, true);
	 
      //  placedResult.setTotalPoints(totalPoints);
      //  placedResult.setPlacedTiles(placedTiles);
      //  placedResult.setPlacedWords(words);
        
      //  return placedResult;
		
		
		//for placedTiles, we just need Letter and BoardPosition
		//for playedWords, we just need word and points
		
		
		//just call this after determining played words, passing PlacedResults to it
		//return GameService.play(true, game, placedResult);
		//return GameService.skip(true, game);
	 	//return GameService.swap(true, game, placedResult);
	}
	
	
	//isOpponent = true means opponent is resigning
  	public static Game resign(boolean isOpponent, Game game){
  
    	PlayedTurn turn = new PlayedTurn();
    	turn.setOpponentPlay(isOpponent);
    	turn.setTurn(game.getTurn());
    	turn.setPoints(0);
    	turn.setAction(11); // #RESIGNED(11)
    	turn.setPlayedDate(new Date());
    	game.getPlayedTurns().add(turn);
    	 
    	if (isOpponent){
    		game.getPlayerGames().get(0).setStatus(4); //winner		
    		game.getPlayerGames().get(1).setStatus(7); //resigned
    	}
    	else{
    		game.getPlayerGames().get(0).setStatus(7); //resigned		
    		game.getPlayerGames().get(1).setStatus(4); 	//winner
    		
    		//in case player resigns while ahead
    		if (game.getPlayerGames().get(0).getScore() >= game.getPlayerGames().get(1).getScore()){
    			game.getPlayerGames().get(1).setScore(game.getPlayerGames().get(0).getScore() + 1);
    		}
    	}

    	Player player = PlayerService.getPlayer();
    	//add 1 to opponent's wins, save opponent 
    	//add 1 to player's losses, save player
    	player.setActiveGameId(Constants.EMPTY_STRING);
    	
    	if (isOpponent){
        	player.setNumWins(player.getNumWins() + 1);
        	game.getOpponent().addLossToRecord();   		
    	}
    	else{
        	player.setNumLosses(player.getNumLosses() + 1);
        	game.getOpponent().addWinToRecord();

    	}
    	PlayerService.savePlayer(player);
    	OpponentService.saveOpponentRecord(game.getOpponentId(), game.getOpponent().getRecord());
    	 
    	game.setStatus(3); //sets up enum for game status and playerGame status
    	game.setCompletionDate(new Date());
    	game.setTurn(game.getTurn() + 1);
    	
    	saveGame(game);
    	addGameToCompletedList(game);
    	
    	return game;
  	}
  	
  	
	public static void addGameToCompletedList(Game game){
		 List<GameListItem> games = GameData.getCompletedGameList();
		 
		 //put game at the beginning so that it saves in descending order
		 games.add(0, new  GameListItem(game.getId(), game.getCompletionDate()));
		 
		 //only store 10 games in this list.  clean out game storage and list for 11 and above
		 if (games.size() > Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE){
			 for (int i = games.size() - 1; i <= Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE - 1; i--){
				 
				 removeGame(games.get(i).getGameId());
				 games.remove(i);
			 }
			 
		 }
		 
		 GameData.saveCompletedGameList(games);
	}
	
	public static List<Game> getCompletedGames(){
		 List<GameListItem> games = GameData.getCompletedGameList();
		 List<Game> gameList = new ArrayList<Game>();
		 
		 for (GameListItem gli : games){
			 try{
				 gameList.add(getGame(gli.getGameId()));
			 }
			 catch (Exception e){
				 //game was likely missing for some reason
			 }
		 }
 		 
		 return gameList;
	}
	
	public static void loadScoreboard(final FragmentActivity context, Game game){
	
		 TextView tvPlayerScore = (TextView)context.findViewById(R.id.tvPlayerScore);
		 TextView tvOpponentName = (TextView)context.findViewById(R.id.tvOpponentName);
		 TextView tvOpponentScore = (TextView)context.findViewById(R.id.tvOpponentScore);
		 ImageView ivOpponent = (ImageView)context.findViewById(R.id.ivOpponent);
		 
		 TextView tvPlayerName = (TextView)context.findViewById(R.id.tvPlayerName);
		 TextView tvLettersLeft = (TextView)context.findViewById(R.id.tvLettersLeft);
		 TextView tvNumPoints = (TextView)context.findViewById(R.id.tvNumPoints);
		 
		 tvPlayerScore.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvOpponentName.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvOpponentScore.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvPlayerName.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvLettersLeft.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvNumPoints.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	 
		 tvPlayerScore.setText(String.valueOf(game.getPlayerGames().get(0).getScore()));
		 tvOpponentScore.setText(String.valueOf(game.getPlayerGames().get(1).getScore())); 
		 
		 Opponent o = OpponentService.getOpponent(game.getOpponentId());
		 
		 tvOpponentName.setText(o.getName());
		 
		 int opponentImageId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + o.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_MAIN), null, null);
		 ivOpponent.setImageResource(opponentImageId);

		 
		 if (game.getHopper().size() == 1){
			 tvLettersLeft.setText(R.string.scoreboard_1_letter_left);
		 }
		 else{
			 tvLettersLeft.setText(String.format(context.getString(R.string.scoreboard_letters_left), game.getHopper().size()));
		 }
 
	 }
	
	//return int that represents the display message
	public static PlacedResult checkPlayRules(Context context, TileLayout layout, Game game, List<GameTile> boardTiles, boolean bypassValidWordCheck) throws DesignByContractException{
				//	List<com.riotapps.word.ui.TrayTile> trayTiles, //AlphabetService alphabetService, // WordService wordService,
				//	boolean bypassValidWordCheck) throws DesignByContractException{
		
		long runningTime = System.nanoTime();
		
		PlacedResult placedResult = new PlacedResult();
		
		List<GameTile> placedTiles = getGameTiles(boardTiles);
	 
	     Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getGameTiles", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
		
		//check to see if user is skipping, if so just return empty placedResult
		if (placedTiles.size() == 0){
			return placedResult;
		}
		
		//check to determine that overlays did not happen on same letter
		for (GameTile tile : placedTiles){
			Logger.d(TAG, "tile original=" + tile.getOriginalLetter() + " placed=" + tile.getPlacedLetter());
			Check.Require(!tile.getOriginalLetter().equals(tile.getPlacedLetter()),  context.getString(R.string.game_play_invalid_overlay), Constants.ERROR_CODE_OVERLAY_PREVIOUS_LETTER);
		}
		 Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "placedTiles", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
		
		List<PlayedTile> playedTiles = game.getPlayedTiles();
		
		//let's get these collections in the tileId order for certain
		Collections.sort(placedTiles, new GameTileComparator());
		Collections.sort(game.getPlayedTiles(), new PlayedTileComparator());
		
		 Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "sorts", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
		//determine how to differentiate between rule checks that require action vs confirmation
		
		//is the player skipping this turn on purpose? let's confirm it with the player
	//	if(placedTiles.size() == 0) {
	//		return R.string.game_play_confirm_skip;
	//	}
		
		boolean isFirstPlayedWord = false;
		if (game.getTurn() == 1 || game.getPlayedTiles().size() == 0){
			isFirstPlayedWord = true;
		}
		 Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "check 1 starting", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
 
		//the first turn (that plays letters) must have more than one letter played (every word must be at least two letters long
	 	Check.Require(game.getTurn() > 1 || isFirstPlayedWord && placedTiles.size() > 1, context.getString(R.string.game_play_too_few_letters), Constants.ERROR_CODE_TOO_FEW_LETTERS);
	 	//Check.Require(game.getTurn() > 1 || game.getTurn() == 1 && placedTiles.size() > 1, context.getString(R.string.game_play_too_few_letters));
 
		 Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isMoveInValidStartPosition starting", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
	     
	 	Check.Require(isMoveInValidStartPosition(layout, game, placedTiles, isFirstPlayedWord), context.getString(R.string.game_play_invalid_start_position), Constants.ERROR_CODE_INVALID_START_POSITION);
	//	if (!this.isMoveInValidStartPosition(layout, game, placedTiles)){
	//		return R.string.game_play_invalid_start_position;
	//	}
		 Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isMoveInValidStartPosition ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
	 	//see which axis the tiles were played on, x = horizontal, y = vertical
	 	String axis = getPlacedAxis(placedTiles);
	 	
	 	 Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getPlacedAxis", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
	 	
	 	Check.Require(axis == "x" || axis == "y", context.getString(R.string.game_play_invalid_axis), Constants.ERROR_CODE_INVALID_AXIS);
	 	
	 	//create a sorted set of integers for easier comparison and locating in gap check
        SortedSet<Integer> playedSet = new TreeSet<Integer>();     
        for (PlayedTile tile : game.getPlayedTiles()){
       	 playedSet.add(tile.getBoardPosition());
        }
        
        SortedSet<Integer> placedSet = new TreeSet<Integer>();     
        for (GameTile tile : placedTiles){
       	 placedSet.add(tile.getId());
        }
	 	
	 	 Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isMoveFreeOfGaps starting", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
	     
        Check.Require(isMoveFreeOfGaps(axis, playedSet, placedSet), context.getString(R.string.game_play_invalid_gaps), Constants.ERROR_CODE_INVALID_GAPS);
        

	 	 Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isMoveFreeOfGaps ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
        
        Check.Require(isFirstPlayedWord || isWordConnectedToPlayedWords(placedTiles, playedTiles), context.getString(R.string.game_play_invalid_gaps_placed_words), Constants.ERROR_CODE_INVALID_PLACEMENT);
       // Check.Require(game.getTurn() == 1 || isWordConnectedToPlayedWords(placedTiles, playedTiles), context.getString(R.string.game_play_invalid_gaps_placed_words));
        
        Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isWordConnectedToPlayedWords ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
        
        
        //determine the words that have been played
        List<PlacedWord> words = getWords(layout, axis, placedTiles, playedTiles);
        
        Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getWords ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
	 	
        //make sure that at least one placedTiles is connected to played tiles
        Boolean isConnected = false;
        for(GameTile tile : placedTiles){
        	Logger.d(TAG, "checkPlayRules placedLetter" + tile.getPlacedLetter() + " " + tile.isConnected());
        	if (tile.isConnected()){
        		isConnected = true;
        	}
        }
        
  
        int totalPoints = 0;
        
        List<PlacedWord> invalidWords = new ArrayList<PlacedWord>();

		//ApplicationContext appContext = (ApplicationContext)context.getApplicationContext();
        
        WordService wordService = new WordService(context);
        
        
        for (PlacedWord word : words)
        {
            totalPoints += word.getTotalPoints();
            if (!bypassValidWordCheck){
            	if (!wordService.doesWordExist(word.getWord())){
            	 	Logger.d(TAG, "checkPlayRules invalid word=" + word.getWord());
            		invalidWords.add(word);
            	}
            }
        }
            
        wordService.finish();
        wordService = null;
        
        
        Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getTotalPoints ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
	 

        Check.Require(invalidWords.size() == 0, getInvalidWordsMessage(context, invalidWords), Constants.ERROR_CODE_INVALID_WORDS);

        Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getInvalidWordsMessage ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));
	     runningTime = System.nanoTime();
        
        //check for a smasher...its worth 40 bonus points
        if (placedTiles.size() == 7){
        	totalPoints += Constants.SMASHER_BONUS_POINTS;
        }
        
        placedResult.setTotalPoints(totalPoints);
        placedResult.setPlacedTiles(placedTiles);
        placedResult.setPlacedWords(words);
        
        return placedResult;
        
	}
	
	private static String getInvalidWordsMessage(Context context, List<PlacedWord> words){
 
		switch (words.size()){
		case 0:
			return "";
		case 1:
			return String.format(context.getString(R.string.game_play_1_invalid_word), words.get(0).getWord());
		case 2:
			return String.format(context.getString(R.string.game_play_2_invalid_words), words.get(0).getWord(), words.get(1).getWord());
		case 3:
			return String.format(context.getString(R.string.game_play_3_invalid_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord());
		case 4:
			return String.format(context.getString(R.string.game_play_4_invalid_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord());
		case 5:
			return String.format(context.getString(R.string.game_play_5_invalid_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord());
		case 6:
			return String.format(context.getString(R.string.game_play_6_invalid_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord());
		case 7:
			return String.format(context.getString(R.string.game_play_7_invalid_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord());
		case 8:
			return String.format(context.getString(R.string.game_play_8_invalid_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord());
		case 9:
			return String.format(context.getString(R.string.game_play_9_invalid_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(), words.get(8).getWord());
		default:
			return String.format(context.getString(R.string.game_play_10_invalid_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(), words.get(8).getWord(),
					words.get(9).getWord());
		}
	}
	
	private static boolean isMoveInValidStartPosition(TileLayout layout, Game game, List<GameTile> placedTiles, boolean isFirstPlayedWord){
		
		//this rule only affects the first played word
		if (!isFirstPlayedWord) {return true;}
	//	if (game.getTurn() > 1) {return true;}
		
		for(GameTile tile : placedTiles){
			if (TileLayoutService.getDefaultTile(tile.getId(), layout) == TileLayoutService.eDefaultTile.Starter){
				return true;
			}
		}
		return false;
	}
	
	private static boolean isMoveFreeOfGaps(String axis, SortedSet<Integer> playedSet, SortedSet<Integer> placedSet)
     {
         if (placedSet.size() == 1) { return true; }
         //in the direction of the axis, between the first placed tile and the last, there can be no gaps

         int increment = (axis == "x" ? 1 : 15);
         //if direction is horizontal, add 1 until we get to the last placed letter
         //if direction is vertical, add 15 until we get to the last placed letter
         //start at first and loop until the last...not looping each one because a previously played tile
         //might be in between

         int i = placedSet.first();
         int last = placedSet.last();

         do
         {
             if (placedSet.contains(i) != true && playedSet.contains(i) != true)
             {
                 return false;
             }
             i += increment;
         } while (i < last);  //i will max out at the highest placed tile

         return true;
 
    }
	
	private static boolean isWordConnectedToPlayedWords(List<GameTile> placedTiles, List<PlayedTile> playedTiles){
		  //if this letter is an incoming placed letter, mark is as connected
        //to the rest of the letters.  At the end, this will allow us to
        //determine if any incoming letters are on the same axis but separated 
        //from the main word by space(s)
        //"overlayed" counts as "connected"
		
		//if first moves are skips, then playedTiles will be empty.  this is ok
		if (playedTiles.size() == 0){
			return true;
		}
		
		for (GameTile tile : placedTiles){
			if (tile.getOriginalLetter().length() > 0){ return true;}
/*			
			int above = TileLayoutService.getTileIdAbove(tile.getId());
            int below = TileLayoutService.getTileIdBelow(tile.getId());
            int left = TileLayoutService.getTileIdToTheLeft(tile.getId());
            int right = TileLayoutService.getTileIdToTheRight(tile.getId());
            
            boolean isPlayedTileAbove = above == 255 ? false : (containsPlayedTileId(playedTiles, above) ? true : false);
            boolean isPlayedTileBelow = below == 255 ? false : (containsPlayedTileId(playedTiles, below) ? true : false);
            boolean isPlayedTileToTheLeft = left == 255 ? false : (containsPlayedTileId(playedTiles, left) ? true : false);
            boolean isPlayedTileToTheRight = right == 255 ? false : (containsPlayedTileId(playedTiles, right) ? true : false);
*/

            boolean isPlayedTileAbove = tile.getTileIdAbove() == 255 ? false : (containsPlayedTileId(playedTiles, tile.getTileIdAbove()) ? true : false);
            boolean isPlayedTileBelow = tile.getTileIdBelow() == 255 ? false : (containsPlayedTileId(playedTiles, tile.getTileIdBelow()) ? true : false);
            boolean isPlayedTileToTheLeft = tile.getTileIdToTheLeft() == 255 ? false : (containsPlayedTileId(playedTiles, tile.getTileIdToTheLeft()) ? true : false);
            boolean isPlayedTileToTheRight = tile.getTileIdToTheRight() == 255 ? false : (containsPlayedTileId(playedTiles, tile.getTileIdToTheRight()) ? true : false);

            
            if ( isPlayedTileAbove || isPlayedTileBelow || isPlayedTileToTheLeft || isPlayedTileToTheRight )
            {
            	return true;
            }
			
		}
		return false;
	}
	
	private static String getPlacedAxis(List<GameTile> placedTiles)
      {
          int row = 0;
          int col = 0;
          if (placedTiles.size() == 1) { return "x"; }
          String axis = "";
          int count = placedTiles.size();
          for (int i = 0; i < count; i++) 
          {
              RowCol rowCol = TileLayoutService.getRowCol(placedTiles.get(i).getId());
              
              Logger.d(TAG, "getPlacedAxis row=" + rowCol.getRow() + " col=" + rowCol.getColumn());
              if (i == 1)
              {
                  if (rowCol.getRow() != row && rowCol.getColumn() != col) { return ""; }
                  axis = (rowCol.getRow() == row) ? "x" : "y";
              }
              else if (i > 1)
              {
                  if (axis == "x" && rowCol.getRow() != row) { return ""; }
                  if (axis == "y" && rowCol.getColumn() != col) { return ""; }
              }
              else
              {
                  row = rowCol.getRow();
                  col = rowCol.getColumn();
              }

          }

          return axis;
      }
		
	private static List<GameTile> getGameTiles(List<GameTile> boardTiles){
		
		List<GameTile> tiles = new ArrayList<GameTile>();
		
		for(GameTile tile : boardTiles){
			if (tile.getPlacedLetter().length() > 0){
				tiles.add(tile);
			}
		}
		
		return tiles;
	}
	
	//placed tiles = tiles with letters that were placed on the board during this turn
	//played tiles = tiles with letters that were placed on the board during previous turns
	//placed word = words that were formed this turn by the placed tiles (in combination with previously played tiles_
	private static List<PlacedWord> getWords(TileLayout layout, String axis, List<GameTile> placedTiles, 
				List<PlayedTile> playedTiles) throws DesignByContractException {

		List<PlacedWord> words = new ArrayList<PlacedWord>();
     
        placedTiles.get(0).setConnected(true); //first letter is always "connected" to the rest of the chain since it is the starting point
  
        PlacedWord word = new PlacedWord();
  
        //we will be building the word and accumulating the points and multipliers as we go along        
        //let's start out by grabbing the first letter in the placed list
        word.setWord(placedTiles.get(0).getPlacedLetter());
        word.setPoints(getTileValue(placedTiles.get(0).getId(), placedTiles.get(0).getPlacedLetter(), playedTiles, layout));
        word.setMultiplier(getWordMultiplier(placedTiles.get(0).getId(), playedTiles, layout));


       // GameTile loopTile = placedTiles.get(0); 


        //multiply each wordWultiplier by this value in a loop,
        //then after the word value is calculated letter by letter,
        //multiply by word multiplier to get the final word value
        

        //first go forward (to the right or down)
        getLettersAlongOnAxis(word, axis, placedTiles.get(0).getId(), placedTiles, playedTiles, true, true, layout);

        //then go backward (to the left or up)
        getLettersAlongOnAxis(word, axis, placedTiles.get(0).getId(), placedTiles, playedTiles, true, false, layout);


        //it's possible to have a word that is only one letter long now
        //if the word is played vertically and the top placed letter has no letter to either side, this will be the case
     //   if (word.getWord().length() > 1)
     //   {// are all placed tiles connected.
     //       for (GameTile tile : placedTiles)
     //       {
     //       	Logger.d(TAG, "getWords placedLetter" + tile.getPlacedLetter() + " " + tile.isConnected());
     //           Check.Require(tile.isConnected() == true, context.getString(R.string.game_play_invalid_gaps_placed_words));
     //       }
     //       
     //      // Check.Require(word.getWord().length() > 1, context.getString(R.string.game_play_word_too_short));
     //   }

        
        //add word to the word  list
        if (word.getWord().length() > 1) {  words.add(word);}

        //ok, now we have discovered the main word, let's travel down the 
        //main word looking for words played in the opposite axis that hang off the main word
        //only look for words that hang off of placed (incoming) letters within the main word, not previously played letters
        for (GameTile tile : placedTiles)
        {
        	word = new PlacedWord();
            word.setWord(tile.getPlacedLetter());
            word.setPoints(getTileValue(tile.getId(), tile.getPlacedLetter(), playedTiles, layout));
            word.setMultiplier(getWordMultiplier(tile.getId(), playedTiles, layout));

            getLettersAlongOnAxis(word, axis, tile.getId(), placedTiles, playedTiles, false, true, layout);
            getLettersAlongOnAxis(word, axis, tile.getId(), placedTiles, playedTiles, false, false, layout);

            //add word to the word  list if it's longer than one letter
            if (word.getWord().length() > 1)
            {
            	 words.add(word);
            }
        }

        return words;
    }
	
	private static boolean containsPlayedTileId(List<PlayedTile> tiles, int tileId){
		for (PlayedTile tile : tiles){
			if (tile.getBoardPosition() == tileId) {return true;}
		}
		return false;
	}
	
	private static boolean containsGameTileId(List<GameTile> placedTiles, int tileId){
		for (GameTile tile : placedTiles){
			if (tile.getId() == tileId) {return true;}
		}
		return false;
	}
	
	private static GameTile getGameTile(List<GameTile> placedTiles, int tileId){
		for (GameTile tile : placedTiles){
			if (tile.getId() == tileId) {return tile;}
		}
		return null;
	}
	
	private static PlayedTile getPlayedTile(List<PlayedTile> tiles, int tileId){
		for (PlayedTile tile : tiles){
			if (tile.getBoardPosition() == tileId) {return tile;}
		}
		return null;
	}
	
	
	 private static void getLettersAlongOnAxis(PlacedWord word, String axis, int startingPosition, List<GameTile> placedTiles, 
	            List<PlayedTile> playedTiles, boolean onMainAxis, boolean proceedBackward, TileLayout layout)  {
		 
		 boolean loop = true;
		 int tilePosition = 0;
		 int loopPosition = startingPosition; // placedTiles[0].Id;
		 while (loop == true)
		 {
            if (proceedBackward == true)
            {
                //going backward on the axis

                //placedTiles are from this turn,  playedTiles are from previous turns
                //find position on the board to check, go to the left if axis is horizontal, go up if axis is vertical
                if (onMainAxis == true)
                {
                    tilePosition = (axis == "x" ? TileLayoutService.getTileIdToTheLeft(loopPosition) : TileLayoutService.getTileIdAbove(loopPosition));
                }
                else
                {
                    //when onMainAxis is false, we are wandering down the main word looking for connected words on the opposite
                    //axis from the main axis
                    tilePosition = (axis == "x" ? TileLayoutService.getTileIdAbove(loopPosition) : TileLayoutService.getTileIdToTheLeft(loopPosition));
                }
            }
            else
            {
                //going forward on the axis

                //placedTiles are from this turn,  playedTiles are from previous turns
                //find position on the board to check, go to the right if axis is horizontal, go down if axis is vertical 
                if (onMainAxis == true)
                {
                    tilePosition = (axis == "x" ? TileLayoutService.getTileIdToTheRight(loopPosition) : TileLayoutService.getTileIdBelow(loopPosition));
                }
                else
                {
                    tilePosition = (axis == "x" ? TileLayoutService.getTileIdBelow(loopPosition) : TileLayoutService.getTileIdToTheRight(loopPosition));
                }
            }
            if (tilePosition == 255 || (containsGameTileId(placedTiles, tilePosition) == false && containsPlayedTileId(playedTiles, tilePosition) == false))
            {
                //no letter was placed in this tile position and no previously played tile was in this tile position
                //or this position is off the board (tilePosition = 255)
                loop = false;
            }
            else
            {
                //add this letter to the partially constructed word
                String letter = (String) (containsGameTileId(placedTiles, tilePosition) == true ? getGameTile(placedTiles, tilePosition).getPlacedLetter() : getPlayedTile(playedTiles, tilePosition).getLatestPlayedLetter().getLetter()); 
                if (proceedBackward == true) { word.setWord(letter + word.getWord()); } else { word.setWord(word.getWord() + letter); }

                //keep track of the points as the word is being constructed
                word.setPoints(word.getPoints() + getTileValue(tilePosition, letter, playedTiles, layout));

                word.setMultiplier(word.getMultiplier() * getWordMultiplier(tilePosition, playedTiles, layout));

                //advance to previous (backwards or up) position
                loopPosition = tilePosition;

                //if this letter is an incoming placed letter, mark is as connected
                //to the rest of the letters.  At the end, this will allow us to
                //determine if any incoming letters are on the same axis but separated 
                //from the main word by space(s)
                //"overlayed" counts as "connected"
    //            if (containsGameTileId(placedTiles, tilePosition) && getGameTile(placedTiles, tilePosition).getOriginalLetter().length() > 0){
    //            	getGameTile(placedTiles, tilePosition).setConnected(true);
    //            }
    //            else{
	//                int above = TileLayoutService.getTileIdAbove(tilePosition);
	//                int below = TileLayoutService.getTileIdBelow(tilePosition);
	//                int left = TileLayoutService.getTileIdToTheLeft(tilePosition);
	//                int right = TileLayoutService.getTileIdToTheRight(tilePosition);
	//                
	//                boolean isPlayedTileAbove = above == 255 ? false : (containsPlayedTileId(playedTiles, above) ? true : false);
	//                boolean isPlayedTileBelow = below == 255 ? false : (containsPlayedTileId(playedTiles, below) ? true : false);
	//                boolean isPlayedTileToTheLeft = left == 255 ? false : (containsPlayedTileId(playedTiles, left) ? true : false);
	//                boolean isPlayedTileToTheRight = right == 255 ? false : (containsPlayedTileId(playedTiles, right) ? true : false);
	//  
	//                
	//                if ( isPlayedTileAbove || isPlayedTileBelow || isPlayedTileToTheLeft || isPlayedTileToTheRight )
	//             //   if (containsGameTileId(placedTiles, tilePosition) == true)
	//                {
	//                	getGameTile(placedTiles, tilePosition).setConnected(true);
	//                }
    //            }
            }
          }

 
        }

	 
	  public static int getTileValue(int tileId, String letter, List<PlayedTile> playedTiles, TileLayout layout)
      {
          int multiplier = 1;

          //if the tile has not already been played, count its multiplier
          if (containsPlayedTileId(playedTiles, tileId) == false)
          {
              multiplier = TileLayoutService.getLetterMultiplier(tileId, layout);
          }
          return AlphabetService.getLetterValue(letter) * multiplier;
      }

      public static int getWordMultiplier(int tileId, List<PlayedTile> playedTiles, TileLayout layout)
      {
          int multiplier = 1;

          //if the tile has not already been played, count its multiplier
          if (containsPlayedTileId(playedTiles, tileId) == false)
          {
              multiplier = TileLayoutService.getWordMultiplier(tileId, layout);
          }
          return multiplier;
      }

	 
  	public static String getPlacedWordsMessage(Context context, List<PlacedWord> words){
  		 
		switch (words.size()){
		case 0:
			return "";
		case 1:
			return String.format(context.getString(R.string.game_play_1_word), words.get(0).getWord());
		case 2:
			return String.format(context.getString(R.string.game_play_2_words), words.get(0).getWord(), words.get(1).getWord());
		case 3:
			return String.format(context.getString(R.string.game_play_3_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord());
		case 4:
			return String.format(context.getString(R.string.game_play_4_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord());
		case 5:
			return String.format(context.getString(R.string.game_play_5_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord());
		case 6:
			return String.format(context.getString(R.string.game_play_6_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord());
		case 7:
			return String.format(context.getString(R.string.game_play_7_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord());
		case 8:
			return String.format(context.getString(R.string.game_play_8_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord());
		case 9:
			return String.format(context.getString(R.string.game_play_9_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(),
					words.get(8).getWord());
		case 10:
			return String.format(context.getString(R.string.game_play_10_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(), 
					words.get(8).getWord(),	words.get(9).getWord());
		case 11:
			return String.format(context.getString(R.string.game_play_11_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(),
					words.get(8).getWord(), words.get(9).getWord(), words.get(10).getWord());
		case 12:
			return String.format(context.getString(R.string.game_play_12_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(), 
					words.get(8).getWord(), words.get(9).getWord(), words.get(10).getWord(), words.get(11).getWord());
		case 13:
			return String.format(context.getString(R.string.game_play_13_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(), 
					words.get(8).getWord(), words.get(9).getWord(), words.get(10).getWord(), words.get(11).getWord(), words.get(12).getWord());
		case 14:
			return String.format(context.getString(R.string.game_play_14_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(), words.get(8).getWord(),
					words.get(9).getWord(), words.get(10).getWord(), words.get(11).getWord(), words.get(12).getWord(), words.get(13).getWord());
		case 15:
			return String.format(context.getString(R.string.game_play_15_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(), words.get(8).getWord(),
					words.get(9).getWord(), words.get(10).getWord(), words.get(11).getWord(), words.get(12).getWord(), words.get(13).getWord(), 
					words.get(14).getWord());
		default:
			return String.format(context.getString(R.string.game_play_16_words), words.get(0).getWord(), words.get(1).getWord(), words.get(2).getWord(),
					words.get(3).getWord(), words.get(4).getWord(), words.get(5).getWord(), words.get(6).getWord(), words.get(7).getWord(), 
					words.get(8).getWord(), words.get(9).getWord(), words.get(10).getWord(), words.get(11).getWord(), words.get(12).getWord(), 
					words.get(13).getWord(), words.get(14).getWord(), words.get(15).getWord());
		}
	}
  	
  
  	/*
  	private static void addNewGameToActiveGames(Game game){
  		Logger.d(TAG, "addNewGameToActiveGames gameId=" + game.getId());
  		
  		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
  		
  		Logger.d(TAG, "addNewGameToActiveGames games before=" + appContext.getPlayer().getActiveGamesYourTurn().size());
	    appContext.getPlayer().getActiveGamesYourTurn().add(0, game);
  		Logger.d(TAG, "addNewGameToActiveGames games after=" + appContext.getPlayer().getActiveGamesYourTurn().size());
	    PlayerService.putPlayerToLocal(appContext.getPlayer()); 
  		
  	}
  	*/
  	/*
  	private static void moveActiveGameYourTurnToOpponentsTurn(Game game){
  		//in this scenario, player has just played a turn and game is not over, and we and updating the local game lists
  		//by removing the game from the player's turn list and moving it to opponent's turn list
  		
  		Player player = PlayerService.getPlayerFromLocal();
  		
  		int numActiveGames = player.getActiveGamesYourTurn().size();
  		for(int i = 0; i < numActiveGames; i++){
  			if (game.getId().equals(player.getActiveGamesYourTurn().get(i).getId())){
  				player.getActiveGamesYourTurn().remove(i);
  				break;
  			}
  		}

  		//remove it from opponents list just in case it was clicked on in that list and main
  		//landing had not been refreshed
  		int numOpponentGames = player.getActiveGamesOpponentTurn().size();
  		for(int i = 0; i < numOpponentGames; i++){
  			if (game.getId().equals(player.getActiveGamesOpponentTurn().get(i).getId())){
  				player.getActiveGamesOpponentTurn().remove(i);
  				break;
  			}
  		}
  		
  		//now add it (back) to the list
  		player.getActiveGamesOpponentTurn().add(0, game);

  		PlayerService.putPlayerToLocal(player);
  	 
	}
*/
	
}
