package com.riotapps.word.hooks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.Timer;
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
import com.riotapps.word.ui.PlacedResultComparator;
import com.riotapps.word.ui.PlacedTile;
import com.riotapps.word.ui.PlacedWord;
import com.riotapps.word.ui.RowCol;
import com.riotapps.word.data.GameData;
import com.riotapps.word.hooks.Game.LastAction;
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
		//game.getPlayerGames().get(1).getPlacedResults().clear();
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
		
		Logger.d(TAG, "GameService.play placedTile loop starting");	
		
		for(GameTile placedTile : placedResult.getPlacedTiles()){
			//add or update a board tile to the list for each placed tile, 
			game.addPlayedTile(placedTile);
			
			//remove the played letters from the player's tray. 
			//make sure to only remove one letter per placedTile even if the same letter is repeated in the player's tray

			//the opponent is always the second playerGame
			game.getPlayerGames().get(isOpponent ? 1 : 0).removeFirstMatchingLetter(placedTile.getPlacedLetter());
		}
		Logger.d(TAG, "GameService.play placedTile loop ending");	
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
			
		Logger.d(TAG, "GameService.play add played turn loop starting");	
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
				game.getOpponent().addLossToRecord();
			}
			else if (playerScore < opponentScore){
				game.getPlayerGames().get(0).setStatus(5);
				game.getPlayerGames().get(1).setStatus(4);

				PlayerService.addLossToPlayerRecord();
				game.getOpponent().addWinToRecord();
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
			Logger.d(TAG, "GameService.play setTurn starting");	
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
		
		Logger.d(TAG, "GameService.play saveGame starting ");	
		
		GameService.saveGame(game);
		//return game;	
	}
	
 
	
public static void skip(boolean isOpponent, Game game){
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
				game.getOpponent().addLossToRecord();
 			}
			else if (playerScore < opponentScore){
				game.getPlayerGames().get(0).setStatus(5);
				game.getPlayerGames().get(1).setStatus(4);

				PlayerService.addLossToPlayerRecord();
				game.getOpponent().addWinToRecord();
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
		//return game;	
	}
	
	public static void swap(boolean isOpponent, Game game, List<String> swappedLetters){
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
		//return game;	
	}	
	
	public static int calculateBonusScore(PlayerGame playerGame){
		int bonus = 0;
		
		for (String letter : playerGame.getTrayLetters()){
			bonus = bonus + AlphabetService.getLetterValue(letter);
		}
		
		return bonus;

	}
	
	public static void autoPlay(Context context, Game game, List<GameTile> boardTiles, boolean followThroughWithPlay, List<PlacedResult> tempPlacedResults){
		//remove previously placed tiles from boardTiles...this is usually done in resetGameAfterRefresh
  
		ApplicationContext.captureTime(TAG, "autoplay started starting");
		for(GameTile tile : boardTiles){
			if (tile.isPlacement()){
				tile.removePlacementBeforeAutoplay();
			}
		}
		
	    List<PlacedResult> placedResults = new ArrayList<PlacedResult>();
		PlayerGame opponentGame = game.getPlayerGames().get(1);
		
		//following first play, will not be able to used pre autoplay moves.
		if (followThroughWithPlay && game.getPlayedWords().size() == 1 && game.getLastAction() == LastAction.WORDS_PLAYED){
			//opponentGame.getPlacedResults().clear();
			tempPlacedResults.clear();
		}
		
		long runningTime = 0;  
		if (followThroughWithPlay){
			runningTime = System.currentTimeMillis();
		}
		
		Logger.d(TAG, "autoplay - num derived placed results=" + tempPlacedResults.size() );
		Logger.d(TAG, "autoplay = followThroughWithPlay=" + followThroughWithPlay + " opponentGame.getPlacedResults().size()=" + tempPlacedResults.size());

	    //concede if losing by 250? perhaps randomly

	 
	    List<String> swappedLetters = new ArrayList<String>();
	    
		Logger.d(TAG,"before getDefaultLayout");
		TileLayout defaultLayout = TileLayoutService.getDefaultLayout(context);
		//autoplay logic kicked off here
		//put results in placedResult object just like in normal play
		
		//game.getOpponentGame().getTrayLetters() are the letters you will be playing with
		
		//game.getPlayedTiles() contains the tiles that already have at least one letter played on them
		Logger.d(TAG,"before getPlayedTiles");
		List<PlayedTile> tiles = game.getPlayedTiles();
		
		
		if (followThroughWithPlay){
			//just to get a little low percentage logic out of the way
			//if the player skips to start the game and the opponent has no vowels in his tray
			//nothing can be done except to skip or swap.  in this case, let's swap 3 letters
			if (game.getPlayedWords().size() ==0 && !opponentGame.doesTrayContainAVowel()){
	
				Logger.d(TAG, "Autoplay, no vowels at beginning of game after a skip or swap.  swapping 3 letters");
				//just grab first 3 letters for now
				swappedLetters.add(opponentGame.getTrayLetters().get(0));
				swappedLetters.add(opponentGame.getTrayLetters().get(1));
				swappedLetters.add(opponentGame.getTrayLetters().get(2));
				GameService.swap(true, game, swappedLetters);
				
				return;
			}
		/*	
			if (!opponentGame.doesTrayContainAVowel()){
				Logger.d(TAG, "autoplay, tray contains NO VOWELS");
			}
			if (!opponentGame.doesTrayContainAConsonant()){
				Logger.d(TAG, "autoplay, tray contains NO CONSONANTS");
			}
			*/
			//randomly swap if there are no vowels or no consonants
			if ((!opponentGame.doesTrayContainAVowel() || !opponentGame.doesTrayContainAConsonant())
					&& game.getHopper().size() > 0 && Utils.coinFlip() == Constants.COIN_FLIP_HEADS){
				Logger.d(TAG, "Autoplay, no vowels or no consonants.  swapping x letters, depending on hopper size");
				
				int randomVal = Utils.getRandomNumberFromRange(1, 7);
				
				swappedLetters.add(opponentGame.getTrayLetters().get(0));
				if (randomVal > 1 && game.getHopper().size() > 1) { swappedLetters.add(opponentGame.getTrayLetters().get(1)); }
				else if (randomVal > 2 && game.getHopper().size() > 2) { swappedLetters.add(opponentGame.getTrayLetters().get(2)); }
				else if (randomVal > 3 && game.getHopper().size() > 3) { swappedLetters.add(opponentGame.getTrayLetters().get(3)); }
				else if (randomVal > 4 && game.getHopper().size() > 4) { swappedLetters.add(opponentGame.getTrayLetters().get(4)); }
				else if (randomVal > 5 && game.getHopper().size() > 5) { swappedLetters.add(opponentGame.getTrayLetters().get(5)); }
				else if (randomVal > 6 && game.getHopper().size() > 6) { swappedLetters.add(opponentGame.getTrayLetters().get(6)); }
	
				GameService.swap(true, game, swappedLetters);
				
				return;
			}
		}
		
		//create collections for H/V tiles/sets. 
		//object in collection must contain letter(s),len , x, y
		
		
		//to get the neighboring playedTiles do this: (example...i assume it would be done in some AI loop for real)
		//if there is not a played tile, you can assume its a blank tile
//		PlayedTile above = game.getPlayedTileAbove(game.getPlayedTiles().get(0));
//		PlayedTile below = game.getPlayedTileBelow(game.getPlayedTiles().get(0));
//		PlayedTile right = game.getPlayedTileToTheRight(game.getPlayedTiles().get(0));
//		PlayedTile left = game.getPlayedTileToTheLeft(game.getPlayedTiles().get(0));
		
		
		//dont forget logic to start game if game.getPlayedWords.size() == 0) , this means the player skipped his turn
		//and you will have to play on one or more of the starter tiles, using only the letters in your tray.
		//if you can't form a 2 letter word (no vowels), you will have to swap or skip
		
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
		
		//the layout 
		//https://docs.google.com/spreadsheet/ccc?key=0AsYnYHEXRYMndElrQzVrdFh6Sm5aaERqX1U2ajlSU2c#gid=0
	   	WordService wordService = new WordService(context);
		 
		try{ 
			//if there is only one word played, that means the first word was just laid and all of the letters on the board are new and 
			//the pre-determined autoplay should be cleared in total
			if (followThroughWithPlay && game.getPlayedWords().size() == 1 && game.getLastAction().equals(LastAction.WORDS_PLAYED)){
				tempPlacedResults.clear();
			}
			
			if (!followThroughWithPlay || (followThroughWithPlay && tempPlacedResults.size() == 0)) {
	
		
					 if (game.getPlayedWords().size() == 0){
						 //player skipped first turn, 
						 //determine full words from tray and make a play on a random starter tile
			
					
			
						 List<String> sortedTray = GameService.getSortedTrayLetters(opponentGame.getTrayLetters());
						 
						 String trayLettersSorted = ""; 
						 for (String s : sortedTray){
							 trayLettersSorted += s;
						 }
						 
						//Logger.d(TAG, "autoplay, player skipped firt turn. TRAY LETTERS=" + trayLettersSorted);
							
						//get all the words that match the index
						//List<String> matches = wordService.getMatchingWordsFromIndex(trayLettersSorted);
						
						//if (matches.size() > 0){
							//for each match
						GameService.addWordsToStartGame( boardTiles, defaultLayout, context, game, placedResults, wordService, runningTime );
			 
						//}
						if (placedResults.size() == 0) {
							//Logger.d(TAG, "autoplay, no full words in tray, swapping letters");
							//swap letters 
							//just grab first 3 letters for now
							swappedLetters.add(opponentGame.getTrayLetters().get(0));
							if (game.getHopper().size() > 1) { swappedLetters.add(opponentGame.getTrayLetters().get(1)); }
							if (game.getHopper().size() > 1) { swappedLetters.add(opponentGame.getTrayLetters().get(2)); }
							GameService.swap(true, game, swappedLetters);
							
							wordService.finish();
						    wordService = null;
						     
							return;
						}
				 
					 }
					 else{
						//time to find some words to play 

							//find standalone words (made up from letters only in the tray) in the tray for each combination between 2 and 7
							//these can be used for placement at the end or beginning of already played words
							//these words can potentially be found and stored earlier
						 
							findWordsToPlay(game, game.getPlayedTiles(), placedResults, wordService, context, boardTiles, defaultLayout, runningTime);
	
						
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
			 
			
		     //for each placedResult option use a new List<GameTile> based on boardTiles passed in
		     //call setPlacedLetter on each board tile played
		     //put all options in a list of PlacedResults 
		     //for each option, reset list of boardTiles 
		     
		/*     try {
		    
		    	 	//this is the real one
		    	 	//placedResults.add(GameService.checkPlayRules(context, defaultLayout, game, boardTiles, false)); 	
		    	 
		    	 	//this is the placeholder
		    	 	PlacedResult pr = GameService.checkPlayRules(context, defaultLayout, game, boardTiles, true);
			 }
		     catch (DesignByContractException e){
	
		    	 //there was a problem, do not add to list
		     }
		  */    
			 }//end of 
			else{
				//if last turn was a skip or swap, we don't need to do anything else.
				//the pump is already primed.  if the last moved was a play, we need to determine if any
				//pre-primed plays were invalidated
				Logger.d(TAG, "autoplay tempPlacedResults (game.getLastAction()=" + game.getLastAction());
				switch (game.getLastAction()){
					case TURN_SKIPPED:
					case TWO_LETTERS_SWAPPED:	
					case THREE_LETTERS_SWAPPED:
					case FOUR_LETTERS_SWAPPED:
					case FIVE_LETTERS_SWAPPED:
					case SIX_LETTERS_SWAPPED:
					case SEVEN_LETTERS_SWAPPED:
						Logger.d(TAG, "autoplay - tempPlacedResults not changed because of skip or swap");
						pushPlacedResults(tempPlacedResults, placedResults );
						break;
					default:
						Logger.d(TAG, "autoplay - clear tempPlacedResults that were connected to tiles associated with last turn tempPlacedResults=" + tempPlacedResults.size());
						//check all placedResults to determine which ones need to be invalidated
						
						 
						Set<Integer> involvedPositions = new HashSet<Integer>();
						invalidateConnectedPlacedResults(game, tempPlacedResults, involvedPositions);
						
						//now go back through subset of played letters (based on tiles affected by last turn) and determine what can be played there
						//this subset will be added to placedResults
						List<PlayedTile> involvedTiles =  new ArrayList<PlayedTile>();
						
						for (Integer i : involvedPositions){
							 PlayedTile tile = game.getPlayedTile(i);
								 
							 if (tile != null){
								 involvedTiles.add(tile);
							 }
						}
						
						Logger.d(TAG, "autoplay - tempPlacedResults after clearing invalidations tempPlacedResults=" + tempPlacedResults.size());
						
						findWordsToPlay(game, involvedTiles, tempPlacedResults, wordService, context, boardTiles, defaultLayout, runningTime);
						
						Logger.d(TAG, "autoplay - tempPlacedResults after adding words tempPlacedResults=" + tempPlacedResults.size());
						
						 
						//move tempPlacedResults to official list for final step
						pushPlacedResults(tempPlacedResults, placedResults );
						break;
				}
				
				Logger.d(TAG, "autoplay - clear tempPlacedResults");
				tempPlacedResults.clear();
				
			}
		}
		
		 finally {
		        
			 //put word lookup calls here
	 	            
		     wordService.finish();
		     wordService = null;
		 }
		
		
		
			Logger.d(TAG, "autoplay - play NUMBER OF OPTIONS =" + placedResults.size());
		if (followThroughWithPlay){
			
			if (placedResults.size() == 0){
				//we have a skip or swap or resign
				//how to determine which, perhaps a simple flag
				//put logic here to determine which placedResult to play based on opponent's skill level
		
				
				GameService.skip(true, game);
	
			}
			else {
				//we have a play to handle
				Collections.sort(placedResults, new PlacedResultComparator());
				
				int numOptions = placedResults.size();
				int randomIndex = 0;
				int start = 0;
				int base = 1;
				int randomBetterPlay = 0;
				
				if (numOptions > 9){
					
					switch (game.getOpponent().getSkillLevel()){
						case Constants.SKILL_LEVEL_NOVICE:
							//from bottom 50%
							base = Math.round(numOptions / 2);
							
							start = 0;
							//with a coin flip, determine if we grab from top half of range only
							if (Utils.coinFlip() == Constants.COIN_FLIP_HEADS){
								start = Math.round(base / 2); 
							}
							
							//every so often (1 in 15 chance) let novice get lucky and play a better word from upper half
							randomBetterPlay = Utils.getRandomNumberFromRange(1, 15);  //make these constants
							if (randomBetterPlay == 7){
								//grab from top 50%
								start = base;
								base = numOptions - 1;
							}
							
							randomIndex = Utils.getRandomNumberFromRange(start, base);
							break;
						case Constants.SKILL_LEVEL_AMATEUR:
							//from 25% to 75%
							start = Math.round(numOptions / 4);
							base = numOptions - start;
							
							//with a coin flip, determine if we grab from top half of range only
							if (Utils.coinFlip() == Constants.COIN_FLIP_HEADS){
								start = Math.round(base / 2); 
							}
							
							//every so often (1 in 10 chance) let amateur get lucky and play a better word from upper half
							randomBetterPlay = Utils.getRandomNumberFromRange(1, 10);  //make these constants
							if (randomBetterPlay == 7){
								//grab from top 75%
								start = base;
								base = numOptions - 1;
							}
							
							randomIndex = Utils.getRandomNumberFromRange(start, base);					
							break;
						case Constants.SKILL_LEVEL_SEMI_PRO:
							//from 50% to 90%
							start = Math.round(numOptions / 2);
							base = Math.round(numOptions / 10);
							
							if (game.getPlayerGames().get(0).getScore() > opponentGame.getScore() + Constants.SCORE_DIFFERENCE_TRIGGER_SEMI_PRO){
								//from 75% to 90%
								start = numOptions - Math.round(numOptions / 4); 
								Logger.d(TAG, "autoplay SKILL_LEVEL_SEMI_PRO losing...start=" + start + " end=" + (numOptions - base));
								
							}
							//every so often (1 in 10 chance) let novice get lucky and play a better word from upper half
							randomBetterPlay = Utils.getRandomNumberFromRange(1, 10);  //make these constants
							if (randomBetterPlay == 7){
								//grab from top 75%
								start = base;
								base = numOptions - 1;
							}
							randomIndex = Utils.getRandomNumberFromRange(start, numOptions - base);					
							break;
						case Constants.SKILL_LEVEL_PROFESSIONAL:
							//top 25% unless losing by 100
							base = Math.round(numOptions / 4); 
							
							//if opponent is losing by 100 or more, play from the top 5% 
							if (game.getPlayerGames().get(0).getScore() > opponentGame.getScore() + Constants.SCORE_DIFFERENCE_TRIGGER_PROFESSIONAL){
								base = Math.round(numOptions / 20); 
								Logger.d(TAG, "autoplay SKILL_LEVEL_PROFESSIONAL losing...start=" + (numOptions - base) + " end=" + (numOptions - 1));
								
							}

							randomIndex = Utils.getRandomNumberFromRange(numOptions - base, numOptions - 1);
							
							break;
						case Constants.SKILL_LEVEL_EXPERT:
							//top 20% (or top 10%) unless losing by 80
							
							base = Math.round(numOptions / 5); 
								
							if (Utils.coinFlip() == Constants.COIN_FLIP_HEADS){
								base = Math.round(numOptions / 10); 
							}

							randomIndex = Utils.getRandomNumberFromRange(numOptions - base, numOptions - 1);
							
							//if opponent is losing by 100 or more, play the highest score or second highest score 
							if (game.getPlayerGames().get(0).getScore() > opponentGame.getScore() + Constants.SCORE_DIFFERENCE_TRIGGER_EXPERT){
	 							randomIndex = (Utils.coinFlip() == Constants.COIN_FLIP_HEADS ? numOptions - 1 : numOptions - 2);
							}
							break;
						case Constants.SKILL_LEVEL_MASTER:
							//grab from top 2 choices with a coin toss
 							randomIndex = (Utils.coinFlip() == Constants.COIN_FLIP_HEADS ? numOptions - 1 : numOptions - 2);
 							
 							//if opponent is losing, play the highest score  
							if (game.getPlayerGames().get(0).getScore() > opponentGame.getScore()){
								randomIndex = numOptions - 1;
							}
							break;
					}
				}
				else if (numOptions == 1){
					randomIndex = 0;
				}
				else {
					//can tighten this into skill levels later if needed
					randomIndex = Utils.getRandomNumberFromRange(0, numOptions - 1);
				}
				
				Logger.d(TAG, "autoplay HIGHEST SCORED OPTION POINTS=" + placedResults.get(numOptions - 1).getTotalPoints());
				//this can be enhanced based on the skill level of the opponent perhaps
	
				try{
					Logger.d(TAG, "autoplay index=" + randomIndex);
					GameService.play(true, game, placedResults.get(randomIndex));
				}
				catch (IndexOutOfBoundsException e){
					Logger.d(TAG, "autoplay IndexOutOfBoundsException index=0");
					//just in case we are out of index, though this should not be an issue
					GameService.play(true, game, placedResults.get(0));
				}
			
				
			}
		}
		else{
			//save placedResult list in opponentGame, this will be used to speed things up
			pushPlacedResults(placedResults, tempPlacedResults);
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
	private static void invalidateConnectedPlacedResults(Game game, List<PlacedResult> placedResults, Set<Integer> involvedPositions){
		
		List<PlacedResult> placedResultsToRemove = new ArrayList<PlacedResult>();
		//List<PlayedTile> involvedTiles = new ArrayList<PlayedTile>();
		
		//List<Integer> involvedPositions = new ArrayList<Integer>();
	
		//find all board positions involved in the last play
		
		//we only need to go along main axis one time (later, for now duplicates will be found but not added)
		for (PlayedTile tile : game.getPlayedTiles()){
			//this tile was part of last play, find all tile ids connected to it in each direction
			if (tile.getLatestPlayedLetter().getTurn() == game.getTurn() - 1){ 
				involvedPositions.add(tile.getBoardPosition());
				
				Logger.d(TAG, "autoplay - invalidateConnectedPlacedResults tile letter=" + tile.getLatestPlayedLetter().getLetter());
				
				findConnectedPositionsByDirection(game, involvedPositions, Constants.DIRECTION_LEFT, tile.getBoardPosition());
				findConnectedPositionsByDirection(game, involvedPositions, Constants.DIRECTION_RIGHT, tile.getBoardPosition());
				findConnectedPositionsByDirection(game, involvedPositions, Constants.DIRECTION_ABOVE, tile.getBoardPosition());
				findConnectedPositionsByDirection(game, involvedPositions, Constants.DIRECTION_BELOW, tile.getBoardPosition());
			
			}
		}
		
		for (PlacedResult placedResult : placedResults){
			if (placedResult.isAnyTileIdConnected(involvedPositions)){
				placedResultsToRemove.add(placedResult);
			}
		}
		
		Logger.d(TAG, "autoplay - invalidateConnectedPlacedResults removing " + placedResultsToRemove.size() + " placed results");
		
		placedResults.removeAll(placedResultsToRemove);
		
		
	}
	
	private static void findWordsToPlay(Game game, List<PlayedTile> playedTiles, List<PlacedResult> placedResults, WordService wordService, 
			Context context, List<GameTile> boardTiles, TileLayout defaultLayout, long runningTime){
		//these lists can be done ahead of time as well
 
		
		PlayerGame opponentGame = game.getPlayerGames().get(1);
		
		List<List<String>> letterSets_7 = opponentGame.getSortedTrayLetterSets(7);
		List<List<String>> letterSets_6 = opponentGame.getSortedTrayLetterSets(6);
		List<List<String>> letterSets_5 = opponentGame.getSortedTrayLetterSets(5);
		List<List<String>> letterSets_4 = opponentGame.getSortedTrayLetterSets(4);
		List<List<String>> letterSets_3 = opponentGame.getSortedTrayLetterSets(3);
		List<List<String>> letterSets_2 = opponentGame.getSortedTrayLetterSets(2);
		List<List<String>> letterSets_1 = opponentGame.getSortedTrayLetterSets(1);
		
	//	Logger.d(TAG, "findWordsToPlay !!! letterSets_7 num=" + letterSets_7.size());
	//	Logger.d(TAG, "findWordsToPlay !!! letterSets_6 num=" + letterSets_6.size());
	//	Logger.d(TAG, "findWordsToPlay !!! letterSets_5 num=" + letterSets_5.size());
	//	Logger.d(TAG, "findWordsToPlay !!! letterSets_4 num=" + letterSets_4.size());
	//	Logger.d(TAG, "findWordsToPlay !!! letterSets_3 num=" + letterSets_3.size());
	//	Logger.d(TAG, "findWordsToPlay !!! letterSets_2 num=" + letterSets_2.size());
	//	Logger.d(TAG, "findWordsToPlay !!! letterSets_1 num=" + letterSets_1.size());
		
		
		for (PlayedTile playedTile : playedTiles){
			//easy checks first
			if (maxAutoplayTimeElapsed(runningTime)) { Logger.d(TAG, "findWordsToPlay TIME ELAPSED" ); break; }
			
			int numTilesAbove = game.getNumConsecutivePlayableEmptyTilesInADirection(playedTile, Constants.DIRECTION_ABOVE);
			int numTilesBelow = game.getNumConsecutivePlayableEmptyTilesInADirection(playedTile, Constants.DIRECTION_BELOW);
			int numTilesToTheRight = game.getNumConsecutivePlayableEmptyTilesInADirection(playedTile, Constants.DIRECTION_RIGHT);
			int numTilesToTheLeft = game.getNumConsecutivePlayableEmptyTilesInADirection(playedTile, Constants.DIRECTION_LEFT);
			
		//	Logger.d(TAG, "autoplay boardPosition=" + playedTile.getBoardPosition() + " letter=" +  playedTile.getLatestPlayedLetter().getLetter() + " numTilesAbove=" + numTilesAbove + " numTilesBelow=" + numTilesBelow);
		//	if (numTilesAbove > 0 && numTilesBelow > 0 ) {
		
			
			int successfulAdditions = 0;
			for (PlacedResult result : placedResults){
				if (result.getMatchType() == Constants.AUTOPLAY_MATCH_ACROSS){
					successfulAdditions += 1;
				}
			}
			 
			findWordsUpOrDownSingleTile(Constants.AXIS_VERTICAL, playedTile, placedResults, wordService, letterSets_7,
				letterSets_6, letterSets_5, letterSets_4, letterSets_3, letterSets_2, letterSets_1,
				numTilesAbove, numTilesBelow, boardTiles, context, game, defaultLayout, successfulAdditions, runningTime);
 				
 			if (successfulAdditions < Constants.MAX_WORD_MATCHES_ACROSS && !maxAutoplayTimeElapsed(runningTime)) {
 			 
				findWordsUpOrDownSingleTile(Constants.AXIS_HORIZONTAL, playedTile, placedResults, wordService, letterSets_7,
					letterSets_6, letterSets_5, letterSets_4, letterSets_3, letterSets_2, letterSets_1,
					numTilesToTheLeft, numTilesToTheRight, boardTiles, context, game, defaultLayout, successfulAdditions, runningTime);
			}
	 
			//will make this tighter and smarter with side by side perpendicular lookups
			
			List<List<String>> letterSets = getAllLetterSetCombinations(opponentGame);
			
			String[] lettersetArray = new String[letterSets.size()];
			
			//create an array of strings so we can look up all valid words at once
			for (int i = 0; i < letterSets.size(); i++){
				String index = "";
				//put the letters into a string to use as a parameter to look up valid matches by index
				for (String s : letterSets.get(i)){
					index += s;
				}
				lettersetArray[i] = index.toLowerCase();
			}
	 
			List<String> matches = wordService.getMatchingWordsFromIndexArray(lettersetArray);
			
			if (matches.size() > 0 && !maxAutoplayTimeElapsed(runningTime)) 
				findWordsPerpendicularToPlayedWords(matches, playedTile, placedResults, wordService, 
						boardTiles, context, game, defaultLayout, runningTime);
			}
			
		 
	}
	
	private static void findConnectedPositionsByDirection(Game game, Set<Integer> involvedPositions, String direction, int boardPosition){
		//now find any tiles touching this one in x and y axis until empty tile is hit
		boolean connect = true;
		int brakes = 0;
		int loopId = boardPosition;
		int numEmptyTiles = 0;
		
		while (connect){
			//making sure we don't go off the rails
			if (brakes >= 15) {connect = false; break; }
			//let go left first
			
			int connectedId = 255;
			
			if (direction.equals(Constants.DIRECTION_LEFT)){
				connectedId = TileLayoutService.getTileIdToTheLeft(loopId);
			}
			else if (direction.equals(Constants.DIRECTION_RIGHT)){
				connectedId = TileLayoutService.getTileIdToTheRight(loopId);
			}
			else if (direction.equals(Constants.DIRECTION_ABOVE)){
				connectedId =TileLayoutService.getTileIdAbove(loopId);
			}
			else if (direction.equals(Constants.DIRECTION_BELOW)){
				connectedId = TileLayoutService.getTileIdBelow(loopId);
			}
			
			if (connectedId == 255){
				//we've hit the border, stop this loop
				connect = false;
			}
			else{
				//does connectedId contain a playedTile at this position
				if (game.isBoardTilePlayed(connectedId)){
					//yes, its connected, add position to the this
					involvedPositions.add(connectedId);	
					loopId = connectedId;
				}
				else{
					//we've hit an empty, unplayed tile, stop this loop after finding second empty tile, first one is still "connected"
					if (numEmptyTiles == 0){ 
						involvedPositions.add(connectedId);	
						loopId = connectedId;
						numEmptyTiles += 1;
					}
					else{	
						connect = false;
					}
				}
			}

			brakes += 1;
		}
	}
	
	private static void pushPlacedResults(List<PlacedResult> source, List<PlacedResult> target){
		//placedTiles, placedWords, totalPoints
		
		target.clear();
		
		for (PlacedResult sourceResult : source){
			PlacedResult targetResult = new PlacedResult();
			
			List<GameTile> placedTiles = new ArrayList<GameTile>();
			for (GameTile tile : sourceResult.getPlacedTiles()){
				GameTile targetTile = new GameTile();
				targetTile.setPlacedLetter(tile.getPlacedLetter());
				targetTile.setId(tile.getId());
				placedTiles.add(targetTile);
			}
			targetResult.setPlacedTiles(placedTiles);			
			targetResult.setTotalPoints(sourceResult.getTotalPoints());
		
			List<PlacedWord> placedWords = new ArrayList<PlacedWord>();
			for (PlacedWord sourceWord : sourceResult.getPlacedWords()){
				PlacedWord targetWord = new PlacedWord();
				
				targetWord.setWord(sourceWord.getWord());
				targetWord.setPoints(sourceWord.getPoints());
				targetWord.setMultiplier(sourceWord.getMultiplier());
				
				placedWords.add(targetWord);
			}
			
			targetResult.setPlacedWords(placedWords);		
			target.add(targetResult);
		}
 
	}
	
	private static void findWordsPerpendicularToPlayedWords(List<String> matches, PlayedTile playedTile, List<PlacedResult> placedResults, WordService wordService,  
			List<GameTile> boardTiles, Context context, Game game, TileLayout defaultLayout, long runningTime)
	{
 
		int successfulAdditions = 0;
		for (PlacedResult result : placedResults){
			if (result.getMatchType() == Constants.AUTOPLAY_MATCH_PERPENDICULAR){
				successfulAdditions += 1;
			}
		}
		
		//find all words that can be played next to a previously played word, perpendicular to the word...ie, connecting to the beginning or ending of word and either
		//placing just a single tile to extend that word, or play another word off of that placed letter, on the opposite asix of the previous word
		//later we can allow these perpendicular words to attach to other existing letters, this will provide better, realistic logic
		
		//first find all tiles that are connected to played tiles that contain at least a single empty tile on their other 3 sides
		// this will allow each letter in the tray to be tested against that previous tile to determine if a real word can be made,
		//if so continue in the opposite axis of the previous letter, trying to expand that word
		
		
		if (!maxAutoplayTimeElapsed(runningTime)){
			findPerpendicularMatchesBesidePlayedTile(matches, Constants.DIRECTION_LEFT, playedTile, placedResults, wordService,  
				boardTiles, context, game, defaultLayout, successfulAdditions, runningTime);
		}
		 if (successfulAdditions < Constants.MAX_WORD_MATCHES_PERPENDICULAR && !maxAutoplayTimeElapsed(runningTime)){
		
			 findPerpendicularMatchesBesidePlayedTile(matches, Constants.DIRECTION_RIGHT, playedTile, placedResults, wordService,  
						boardTiles, context, game, defaultLayout, successfulAdditions, runningTime);
		 }
	
		 if (successfulAdditions < Constants.MAX_WORD_MATCHES_PERPENDICULAR && !maxAutoplayTimeElapsed(runningTime)){
			 
			 findPerpendicularMatchesBesidePlayedTile(matches, Constants.DIRECTION_ABOVE, playedTile, placedResults, wordService,  
						boardTiles, context, game, defaultLayout, successfulAdditions, runningTime);
		 }
		 
		 if (successfulAdditions < Constants.MAX_WORD_MATCHES_PERPENDICULAR && !maxAutoplayTimeElapsed(runningTime)){
			 findPerpendicularMatchesBesidePlayedTile(matches, Constants.DIRECTION_BELOW, playedTile, placedResults, wordService,  
						boardTiles, context, game, defaultLayout, successfulAdditions, runningTime);
				
		 }

	}
	
	private static List<List<String>> getAllLetterSetCombinations(PlayerGame playerGame){
		List<List<String>> letterSets = new ArrayList<List<String>>();
		
		//these lists can be done ahead of time as well
		letterSets.addAll(playerGame.getSortedTrayLetterSets(7));
		letterSets.addAll(playerGame.getSortedTrayLetterSets(6));
		letterSets.addAll(playerGame.getSortedTrayLetterSets(5));
		letterSets.addAll(playerGame.getSortedTrayLetterSets(4));
		letterSets.addAll(playerGame.getSortedTrayLetterSets(3));
		letterSets.addAll(playerGame.getSortedTrayLetterSets(2));
		letterSets.addAll(playerGame.getSortedTrayLetterSets(1));
		
		return letterSets;

	}
	
	private static void findPerpendicularMatchesBesidePlayedTile(List<String> matches, String direction, PlayedTile playedTile, 
			List<PlacedResult> placedResults, WordService wordService,  
			List<GameTile> boardTiles, Context context, Game game, TileLayout defaultLayout, int successfulAdditions, long runningTime){
				
			ApplicationContext.captureTime(TAG, "starting findPerpendicular direction=" + direction + " letter=" + playedTile.getLatestPlayedLetter().getLetter() + " position=" + playedTile.getBoardPosition() );
			//find empty tiles (we need 2 at least) to extend this word
			int tileNextDoorPosition = TileLayoutService.getTileIdNextDoorByDirection(playedTile.getBoardPosition(), direction);
			//PlayedTile tileToLeft = game.getPlayedTileToTheLeft(playedTile);
			//PlayedTile tileToLeft_Left = null;
			String beforeDirection = "";
			String afterDirection = "";
			String perpendicularAxis = Constants.AXIS_HORIZONTAL;
			if ( direction.equals(Constants.DIRECTION_LEFT) || direction.equals(Constants.DIRECTION_RIGHT) ){
				perpendicularAxis = Constants.AXIS_VERTICAL;
			}
			
			if (direction.equals(Constants.DIRECTION_LEFT) || direction.equals(Constants.DIRECTION_RIGHT)){
				beforeDirection = Constants.DIRECTION_ABOVE;
				afterDirection = Constants.DIRECTION_BELOW;
			}
			else {
				beforeDirection = Constants.DIRECTION_LEFT;
				afterDirection = Constants.DIRECTION_RIGHT;
			}
		
			int tileOutsideNextDoorPosition = 0;
			int tileBeforeNextDoorPosition = 0;
			int tileAfterNextDoorPosition = 0;

			//is the tile next door on the border or an empty tile??
			//if so let's continue
			if (tileNextDoorPosition != Constants.TILE_POSITION_ON_BORDER && !game.isBoardTilePlayed(tileNextDoorPosition)){
				
				//is the position beside the next door position also open? this ensures that 2 positions are open so
				//that a letter can be safely placed in the next door position
				tileOutsideNextDoorPosition = TileLayoutService.getTileIdNextDoorByDirection(tileNextDoorPosition, direction);
				
				//in order to determine if all 3 directions are open for the tileNextDoor, check the tiles "before" and "after"
				tileBeforeNextDoorPosition = TileLayoutService.getTileIdNextDoorByDirection(tileNextDoorPosition, beforeDirection);
				tileAfterNextDoorPosition = TileLayoutService.getTileIdNextDoorByDirection(tileNextDoorPosition, afterDirection);
			}
			else {
				//this tile won't work, lets get out of this logic
				return;
			}
			
			ApplicationContext.captureTime(TAG, "starting findPerpendicular determine validity 1" );
			
			//now that we have the 3 positions to check, let's check them
			if (
				(tileOutsideNextDoorPosition ==  Constants.TILE_POSITION_ON_BORDER  || !game.isBoardTilePlayed(tileOutsideNextDoorPosition)) && 
				(tileBeforeNextDoorPosition == Constants.TILE_POSITION_ON_BORDER ||  !game.isBoardTilePlayed(tileBeforeNextDoorPosition)) &&
				(tileAfterNextDoorPosition == Constants.TILE_POSITION_ON_BORDER ||  !game.isBoardTilePlayed(tileAfterNextDoorPosition))
				) {
				//we will continue below
			}
			else {
				//this tile won't work, lets get out of this logic
				return;
			}
			ApplicationContext.captureTime(TAG, "starting findPerpendicular determine validity 2" );	
			//ok let's keep going
			Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords VALID " + direction + "  letter=" + playedTile.getBoardPosition() + " " + playedTile.getLatestPlayedLetter().getLetter());
				
			//find played word based on direction and playedTile boardPosition
			
			PlayerGame opponentGame = game.getOpponentGame();
			
			//this is a valid option in which to attempt to extend the word
			//now find all letters to the right so we can test the new word for validity
			String word = game.getPlayedWordByDirectionStartingAtPlayedTile(playedTile, Utils.getOppositeDirection(direction));
 
			ApplicationContext.captureTime(TAG, "starting findPerpendicular after getPlayedWordByDirectionStartingAtPlayedTile" );
			
			Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords CURRENT WORD " + word);
			
					
			//loop through distinct letters and search for all words at once
			//then loop through valid word list that is returned
			
			List<String> distinctTrayLetters = new ArrayList<String>();
			distinctTrayLetters.addAll(opponentGame.getDistinctTrayLetters());
			
			//build array to look for all words at once
			String[] lookupWords = new String[distinctTrayLetters.size()];
			for (int i = 0; i < distinctTrayLetters.size(); i++ ){ 
				String baseWord = distinctTrayLetters.get(i) + word;
				 
				if (direction == Constants.DIRECTION_BELOW || direction == Constants.DIRECTION_RIGHT){
					//add letter to end of word
					baseWord = word + distinctTrayLetters.get(i);
				}
				lookupWords[i] = baseWord.toLowerCase();
			}
			
			ApplicationContext.captureTime(TAG, "before getWordsFromWordArray" );
			List<String> validBaseWords = wordService.getWordsFromWordArray(lookupWords);
			ApplicationContext.captureTime(TAG, "after getWordsFromWordArray" );			
			
			//skip duplicate letters since results will be too close to worry about differences
			for (String validBaseWord : validBaseWords){ 

				if (maxAutoplayTimeElapsed(runningTime)) { break; }  
				//pull the tray letter back out
				String trayLetter = validBaseWord.substring(0,1);
				if (direction == Constants.DIRECTION_BELOW || direction == Constants.DIRECTION_RIGHT){
					//grab letter from end of word
					trayLetter = validBaseWord.substring(validBaseWord.length() - 1);
				}

				//baseWord is valid, add this option to placedResults...
				try{
					List<GameTile> gameBoardTiles = getBoardBaseTiles(boardTiles); 
					
					//add tray letter to tileNextDoorPosition
					gameBoardTiles.get(tileNextDoorPosition).setPlacedLetter(trayLetter.toUpperCase());
					
					PlacedResult placedResult = GameService.checkPlayRules(context, defaultLayout, game, gameBoardTiles, false); 

					ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile checkPlayRules ended");

					placedResult.setMatchType(Constants.AUTOPLAY_MATCH_PERPENDICULAR);
					placedResults.add(placedResult);
					successfulAdditions += 1;
				 
					Logger.d(TAG, "autoplay word match points=" + word + " " + placedResult.getTotalPoints());
				}
				catch (DesignByContractException e){
					//the base word is valid but the placement causes a rule failure, let's get out of this iteration
					 Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords DesignByContractException=" + e.getMessage());
					 continue;
				}
			
				//now that we have added the base word to the results, let's see if we can form a valid word
				//in the perpendicular direction using the letter in tileNextDoorPosition 
			
				//let's try to determine how many tiles we have to deal with in the perpendicular axis
				//for a left/right word, the axis to look for is vertical, for a up/down word, the axis to look for is horizontal

				int numTilesBefore = game.getNumConsecutivePlayableEmptyTilesInADirection(tileNextDoorPosition, beforeDirection);
				int numTilesAfter = game.getNumConsecutivePlayableEmptyTilesInADirection(tileNextDoorPosition, afterDirection);
	
				 Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords num matches=" + matches.size());
	
				//we need all valid words from the tray that contain the letter in context
				for (String match : matches){
					ApplicationContext.captureTime(TAG, "match loop match=" + match);
					//let's make sure we are not going over the limit
					if (maxAutoplayTimeElapsed(runningTime)) { break; }  
					if (successfulAdditions >= Constants.MAX_WORD_MATCHES_PERPENDICULAR) { break; }  
					
					int positionInWord = match.indexOf(trayLetter.toLowerCase());
					//does this letter occur in this word?
					if (positionInWord == -1){ 
						//the trayLetter is not in this word
						 Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords the trayLetter is not in this word match=" + match + " trayLetter=" + trayLetter);

						continue;
					}
						
					//	boolean ok = true;
					String[] wordSplit = match.trim().split(""); 
					
					//now loop through valid word match seeing if it will fit in the spot
					//if so, run through rules to see if we can add as legitimate result option
					if ((match.length() - 1 - positionInWord) <= numTilesAfter && numTilesBefore >= (match.length() - positionInWord) ){
						//will continue below
					}
					else {
						//word won't fit based on position of open tiles.  let's move on
						 Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords invalid position for matched word match=" + match + " positionInWord=" + positionInWord + " numTileAfter=" + numTilesAfter + " numTilesBefore=" + numTilesBefore);
						continue;
					}
					
					int anchorPosition = tileNextDoorPosition;  
					boolean ok = true;	
					
					//reset the boardTile list
			 		List<GameTile> gameBoardTiles = getBoardBaseTiles(boardTiles); 
			 		
					//a positive difference means go up/left
					//a negative difference means go down/right
					//because java adds empty string as the first element ( its a bit annoying )
					//use 1 to get the first  element in this case 
					for (int y = 1; y < wordSplit.length; y ++) {
						int boardPosition = 0;
						
						int difference = positionInWord - y + 1;
						
						if (difference == 0){
							boardPosition = anchorPosition;
						}
						else if (difference > 0){
							if (perpendicularAxis.equals(Constants.AXIS_HORIZONTAL)){
								boardPosition = TileLayoutService.getTileIdXToTheLeft(anchorPosition, difference);											
							}
							else{
								boardPosition = TileLayoutService.getTileIdXAbove(anchorPosition, difference);
							}	
						}
						else{
							if (perpendicularAxis.equals(Constants.AXIS_HORIZONTAL)){
								boardPosition = TileLayoutService.getTileIdXToTheRight(anchorPosition, Math.abs(difference));
							}
							else{
								boardPosition = TileLayoutService.getTileIdXBelow(anchorPosition, Math.abs(difference));
							}
						}
									
		//				Logger.d(TAG, "autoplay word match loop boardPosition()=" + boardPosition + " difference=" + difference);

						//just a precaution, should not hit this logic
						if (boardPosition == 255){
							//we've reached a border and cant go any further, abort this word
							ok = false;
							break;
						}
						
						String letter = wordSplit[y];
						//set letter on this version of the board, we will have to get the boardTile positions back out after choosing placedResults
						//perhaps add List<GameTile> to placedResult just for convenience
						gameBoardTiles.get(boardPosition).setPlacedLetter(letter.toUpperCase());
					}
					
					if (!ok){
						//something weird happened, let's get out of this iteration/match
						continue;
					}
						
					
					//now that we have the letters placed on the board, lets check the rules to validate and assign points
					try{
		 
						//add to the placedResults list if the word is good
						//later we will decide which placedResult to play
						ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTilecheckPlayRules starting");

						 PlacedResult placedResult = GameService.checkPlayRules(context, defaultLayout, game, gameBoardTiles, false); 

						 ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile checkPlayRules ended");
						 boolean distinctScore = true;
						 
						 //check for already played score, doing this should provide a better spread across the sill levels
						 //since we won't get 42 "4 point" results, skewing the spread
						 for(PlacedResult loopResult : placedResults){
							 if (placedResult.getTotalPoints() == loopResult.getTotalPoints()){
								 distinctScore = false;
								 Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords score not distinct, word =" + match + " " + placedResult.getTotalPoints());
							 }
						 }
						 if (distinctScore){
							 placedResult.setMatchType(Constants.AUTOPLAY_MATCH_PERPENDICULAR);
							 placedResults.add(placedResult);
							 successfulAdditions += 1;
							
							 Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords word match points=" + match + " " + placedResult.getTotalPoints());
						 }
				 
					}
					catch (DesignByContractException e){
					    //there was a problem, do not add to list				
						Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords DesignByContractException=" + e.getMessage());

					} //end catch

				}// end (String match : matches){
		}// end for (String trayLetter : opponentGame.getDistinctTrayLetters()){ 	
			
		ApplicationContext.captureTime(TAG, "ending findPerpendicular direction=" + direction + " letter=" + playedTile.getLatestPlayedLetter().getLetter() + " position=" + playedTile.getBoardPosition() );

	}
	
	
	private static void findWordsUpOrDownSingleTile(String axis, PlayedTile playedTile, List<PlacedResult> placedResults, WordService wordService,  
		List<List<String>> letterSets_7, List<List<String>> letterSets_6, List<List<String>> letterSets_5, List<List<String>> letterSets_4,
		List<List<String>> letterSets_3, List<List<String>> letterSets_2, List<List<String>> letterSets_1, int numTilesBehind,
		int numTilesAhead, List<GameTile> boardTiles, Context context, Game game, TileLayout defaultLayout, int successfulAdditions, long runningTime)
	{
		ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile starting");
		if (numTilesBehind > 0 && numTilesAhead > 0 ) {
			//here we have the opportunity to play a word down or up across a single tile  
			
//			if (opponentGame.getTrayLetters().size() <= numTilesBelow){
//				
//			}
			Logger.d(TAG, "autoplay, going to check words now");
			
			for (int x = ((numTilesAhead + numTilesBehind) > 7 ? 7 : (numTilesAhead + numTilesBehind)); x > 0; x--){
				if (maxAutoplayTimeElapsed(runningTime))  { break; }
				if (successfulAdditions >= Constants.MAX_WORD_MATCHES_ACROSS) { break; }
				
		//		ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile outer for loop starting");
				List<List<String>> letterSets = new ArrayList<List<String>>();
				switch (x){
					case 7:
						letterSets = getLetterSetBase(letterSets_7);
					//	Logger.d(TAG, "autoplay, going to check letter sets 7 in length now - items=" + letterSets_7.size());
						break;
					case 6:
						letterSets = getLetterSetBase(letterSets_6);
					//	Logger.d(TAG, "autoplay, going to check letter sets 6 in length now - items=" + letterSets_6.size());
						break;
					case 5:
						letterSets = getLetterSetBase(letterSets_5);
				//		Logger.d(TAG, "autoplay, going to check letter sets 5 in length now - items=" + letterSets_5.size());
						break;
					case 4:
						letterSets = getLetterSetBase(letterSets_4);
					//	Logger.d(TAG, "autoplay, going to check letter sets 4 in length now - items=" + letterSets_4.size());
						break;
					case 3:
						letterSets = getLetterSetBase(letterSets_3);
				//		Logger.d(TAG, "autoplay, going to check letter sets 3 in length now - items=" + letterSets_3.size());
						break;
					case 2:
						letterSets = getLetterSetBase(letterSets_2);
					//	Logger.d(TAG, "autoplay, going to check letter sets 2 in length now - items=" + letterSets_2.size());
						break;
					case 1:
						letterSets = getLetterSetBase(letterSets_1);
				//		Logger.d(TAG, "autoplay, going to check letter sets 1 in length now - items=" + letterSets_1.size());
						break;
				}
				
				String[] lettersetArray = new String[letterSets.size()];
				
				//create an array of index strings to feed the sqlite query "in clause"
				for (int i = 0; i < letterSets.size(); i++) {
					//add tile letter to index before searching
					letterSets.get(i).add(playedTile.getLatestPlayedLetter().getLetter());
					
					//resort the list to get the index in order
					Collections.sort(letterSets.get(i));
					
					String index = "";
					//put the letters into a string to use as a parameter to look up valid matches by index
					for (String s : letterSets.get(i)){
						index += s;
					}
					
					lettersetArray[i] = index.toLowerCase();
				}
				
				
			//	ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile getMatchingWordsFromIndexArray starting- amount=" + lettersetArray.length);
				List<String> matches = new ArrayList<String>();
				
				if(lettersetArray.length > 0){
					matches = wordService.getMatchingWordsFromIndexArray(lettersetArray);
				}
			//	ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile getMatchingWordsFromIndexArray starting");
				/*
				for (List<String> letterSet : letterSets){
					ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile inner for loop starting - placedResults.size()=" + placedResults.size());
					
					if (placedResults.size() >= Constants.MAX_WORD_MATCHES_ACROSS) { break; }
					
					String set = "";
					
					for (String s : letterSet){
						set += s + " ";
					}	
					Logger.d(TAG, "autoplay inside check for words loop letterSet length=" + letterSet.size() + " " + set);
					
					letterSet.add(playedTile.getLatestPlayedLetter().getLetter());

					set = "";
						
					for (String s : letterSet){
						set += s + " ";
					}	
					
					Logger.d(TAG, "autoplay inside check for words loop letterSet length after adding letter=" + letterSet.size() + " " + set);
					//resort since we just add a letter to the mix
					Collections.sort(letterSet);
					
					String index = "";
					//put the letters into a string to use as a parameter to look up valid matches by index
					for (String s : letterSet){
						index += s;
					}
					
					Logger.d(TAG, "autoplay, going to check index " + index);
					
					//get all the words that match the index
					List<String> matches = wordService.getMatchingWordsFromIndex(index);

					Logger.d(TAG, "autoplay, num matching words for index " + index + "=" + matches.size());
					*/
					//loop through the matches looking for the first letter that is the same as the played letter
					//this will be enhanced
					for (String match : matches){
						//find the position of the already played tile (the first instance of it)
				//		ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile match loop starting - match=" + match);
						if (maxAutoplayTimeElapsed(runningTime))  { break; }
						if (successfulAdditions >= Constants.MAX_WORD_MATCHES_ACROSS) { break; }
						
						int playedTilePositionInWord = match.indexOf(playedTile.getLatestPlayedLetter().getLetter().toLowerCase());
						
				//		Logger.d(TAG, "autoplay word match=" + match + " playedLetter=" + playedTile.getLatestPlayedLetter().getLetter() + " playedTilePosition=" + playedTilePositionInWord);
					//	if (match.startsWith(playedTile.getLatestPlayedLetter().getLetter().toLowerCase())){
							//set the placed tiles and then called check rules
							boolean ok = true;
							
					//		Logger.d(TAG, "autoplay word actual match=" + match);
							
							//let's split the word back into strings
							String[] wordSplit = match.trim().split("");  
								
							//set the initial boardPosition based on the tile in context
							//int boardPosition = playedTile.getBoardPosition(); 
							
						//	Logger.d(TAG, "autoplay word match playedTile.getBoardPosition()=" + playedTile.getBoardPosition());
							//int boardPosition = TileLayoutService.getTileIdBelow(playedTile.getBoardPosition());

							
							//reset the boardTiles each time through
							List<GameTile> gameBoardTiles = getBoardBaseTiles(boardTiles); 
				 
							//this is temp, for logging/debugging only
							int numplacedtiles = 0;
							for(GameTile tile : gameBoardTiles){
								if (tile.getPlacedLetter().length() > 0){
									numplacedtiles += 1;
								}
							}
							
							//establish anchor position from already played tile
							int anchorPosition = playedTile.getBoardPosition();
							
				//			Logger.d(TAG, "autoplay word num placed tiles from boardtiles=" + numplacedtiles + " num placed in this turn=" + String.valueOf(wordSplit.length - 1));
							
							//handle if board position is 255
							//let's traverse down the letters, placing them on a board tile
							//for (String letter : wordSplit){
							//because java adds empty string as the first element, its a bit annoying
							//use 1 to get the first  element  
							for (int y = 1; y < wordSplit.length; y ++) {
								ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile match inner loop adding letter to board - letter=" + wordSplit[y].toUpperCase());
								//if our loop variable is equal to the playedTilePositionInWord, do not add this letter to the board
								//it is the position of the already played letter
								if (y == playedTilePositionInWord  + 1){
								 
								}
								else{
									//place board tiles based on the relative position of the already played letter
									int difference = playedTilePositionInWord - y + 1;
									
									//a positive difference means go up/left
									//a negative difference means go down/right
									
									int boardPosition = 0;
									if (difference > 0){
										if (axis.equals(Constants.AXIS_HORIZONTAL)){
											boardPosition = TileLayoutService.getTileIdXToTheLeft(anchorPosition, difference);											
										}
										else{
											boardPosition = TileLayoutService.getTileIdXAbove(anchorPosition, difference);
										}	
									}
									else{
										if (axis.equals(Constants.AXIS_HORIZONTAL)){
											boardPosition = TileLayoutService.getTileIdXToTheRight(anchorPosition, Math.abs(difference));
										}
										else{
											boardPosition = TileLayoutService.getTileIdXBelow(anchorPosition, Math.abs(difference));
										}
									}
												
					//				Logger.d(TAG, "autoplay word match loop boardPosition()=" + boardPosition + " difference=" + difference);

									if (boardPosition == 255){
										//we've reached a border and cant go any further, abort this word
										ok = false;
						//				Logger.d(TAG, "autoplay, border hit trying to play board tiles");
										break;
									}
									
									//skip first letter
									String letter = wordSplit[y];
									//set letter on this version of the board, we will have to get the boardTile positions back out after choosing placedResults
									//perhaps add List<GameTile> to placedResult just for convenience
									gameBoardTiles.get(boardPosition).setPlacedLetter(letter.toUpperCase());
									
						//			Logger.d(TAG, "autoplay, add letter " + letter + " on position " + boardPosition);
									
									//this was temp
									////advance the board position		
									///boardPosition = TileLayoutService.getTileIdBelow(boardPosition);
								}
							}
							
							//now that we have the letters placed on the board, lets check the rules to validate and assign points
							try{
								if (ok){
									//add to the placedResults list if the word is good
									//later we will decide which placedResult to play
									ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTilecheckPlayRules starting");

									 PlacedResult placedResult = GameService.checkPlayRules(context, defaultLayout, game, gameBoardTiles, false); 

									 ApplicationContext.captureTime(TAG, "findWordsUpOrDownSingleTile checkPlayRules ended");

									 boolean distinctScore = true;
									 
									 //add distinct score
									 for(PlacedResult loopResult : placedResults){
										 if (placedResult.getTotalPoints() == loopResult.getTotalPoints()){
											 distinctScore = false;
											 Logger.d(TAG, "autoplay findWordsPerpendicularToPlayedWords score not distinct, word =" + match + " " + placedResult.getTotalPoints());
										 }
									 }
									 if (distinctScore){
										 placedResult.setMatchType(Constants.AUTOPLAY_MATCH_ACROSS);
										 placedResults.add(placedResult);
										 successfulAdditions += 1;
										 Logger.d(TAG, "autoplay word match points=" + match + " " + placedResult.getTotalPoints());
									 }
								}
								else{
									
								}
							}
							catch (DesignByContractException e){

						    	 //there was a problem, do not add to list
								
								 Logger.d(TAG, "autoplay DesignByContractException=" + e.getMessage());
						    
								
							}
						}
			//	}	
			}
		}	
	}
	
	private static void addWordsToStartGame(List<GameTile> boardTiles, 
			TileLayout defaultLayout, Context context, Game game, List<PlacedResult> placedResults, WordService wordService, long runningTime) {
		
		PlayerGame opponentGame = game.getPlayerGames().get(1);
		
		List<List<String>> letterSets = getAllLetterSetCombinations(opponentGame);
		
		//these lists can be done ahead of time as well
	//	letterSets.addAll(opponentGame.getSortedTrayLetterSets(7));
	//	letterSets.addAll(opponentGame.getSortedTrayLetterSets(6));
	//	letterSets.addAll(opponentGame.getSortedTrayLetterSets(5));
	//	letterSets.addAll(opponentGame.getSortedTrayLetterSets(4));
	//	letterSets.addAll(opponentGame.getSortedTrayLetterSets(3));
	//	letterSets.addAll(opponentGame.getSortedTrayLetterSets(2));
		//dont add single letter sets because starting word has to be at least 2 letters
		
		
		for (List<String> letterSet : letterSets){
		
			if (letterSet.size() > 1){
				String index = "";
				//put the letters into a string to use as a parameter to look up valid matches by index
				for (String s : letterSet){
					index += s;
				}
				
					
				List<String> matches = wordService.getMatchingWordsFromIndex(index);
					
					
				for (String match : matches){
					if (maxAutoplayTimeElapsed(runningTime)) { break; }
					//find the position of the already played tile (the first instance of it)
					if (placedResults.size() >= Constants.MAX_WORD_MATCHES_WORDS_TO_START_GAME ) { break; }
				 Logger.d(TAG, "autoplay addWordsToStartGame match=" + match);
					
					//Logger.d(TAG, "autoplay word match=" + match + " playedLetter=" + playedTile.getLatestPlayedLetter().getLetter() + " playedTilePosition=" + playedTilePositionInWord);
				//	if (match.startsWith(playedTile.getLatestPlayedLetter().getLetter().toLowerCase())){
						//set the placed tiles and then called check rules
						//boolean ok = true;
						
						Logger.d(TAG, "autoplay word actual match=" + match);
						
						//let's split the word back into strings
						String[] wordSplit = match.trim().split("");  
							
						//set the initial boardPosition based on random staring tile
						//int boardPosition = playedTile.getBoardPosition(); 
						
					//	Logger.d(TAG, "autoplay word match playedTile.getBoardPosition()=" + playedTile.getBoardPosition());
						//int boardPosition = TileLayoutService.getTileIdBelow(playedTile.getBoardPosition());
		
						
						//reset the boardTiles each time through
						List<GameTile> gameBoardTiles = getBoardBaseTiles(boardTiles); 
			 
						//this is temp, for logging/debugging only
						//int numplacedtiles = 0;
						//for(GameTile tile : gameBoardTiles){
						//	if (tile.getPlacedLetter().length() > 0){
						//		numplacedtiles += 1;
						//	}
						//}
						
						//establish anchor position from already played tile
						int boardPosition = defaultLayout.getRandomStarterTilePosition();
						String axis = (Utils.coinFlip() == Constants.COIN_FLIP_HEADS ? Constants.AXIS_HORIZONTAL : Constants.AXIS_VERTICAL);
						
						//Logger.d(TAG, "autoplay word num placed tiles from boardtiles=" + numplacedtiles + " num placed in this turn=" + String.valueOf(wordSplit.length - 1));
						
						//handle if board position is 255
						//let's traverse down the letters, placing them on a board tile
						//for (String letter : wordSplit){
						//because java adds empty string as the first element, its a bit annoying
						//use 1 to get the first  element  
						for (int y = 1; y < wordSplit.length; y ++) {
							if (placedResults.size() >= Constants.MAX_WORD_MATCHES_ACROSS) { break; }
						 
											
							//	Logger.d(TAG, "autoplay word match loop boardPosition()=" + boardPosition + " difference=" + difference);
		
							/*	if (boardPosition == 255){
									//we've reached a border and cant go any further, abort this word
									ok = false;
									Logger.d(TAG, "autoplay, border hit trying to play board tiles");
									break;
								}
								*/
								//skip first letter
								String letter = wordSplit[y];
								//set letter on this version of the board, we will have to get the boardTile positions back out after choosing placedResults
								//perhaps add List<GameTile> to placedResult just for convenience
								gameBoardTiles.get(boardPosition).setPlacedLetter(letter.toUpperCase());
								
								Logger.d(TAG, "autoplay, add letter " + letter + " on position " + boardPosition);
								
								////advance the board position		
								 boardPosition = (axis == Constants.AXIS_HORIZONTAL ? TileLayoutService.getTileIdToTheRight(boardPosition) : TileLayoutService.getTileIdBelow(boardPosition));
					 
						}
						
						//now that we have the letters placed on the board, lets check the rules to validate and assign points
						try{
						 
								//add to the placedResults list if the word is good
								//later we will decide which placedResult to play
								 PlacedResult placedResult = GameService.checkPlayRules(context, defaultLayout, game, gameBoardTiles, false); 
								 
								 placedResults.add(placedResult);
								 Logger.d(TAG, "autoplay word match points=" + match + " " + placedResult.getTotalPoints());
							 
						}
						catch (DesignByContractException e){
		
					    	 //there was a problem, do not add to list
							
							 Logger.d(TAG, "autoplay DesignByContractException=" + e.getMessage());
					    
							
						}
					}
				 
			}
		}
		
	}
	
	private static boolean maxAutoplayTimeElapsed(long runningTime){
		if (runningTime > 0) {
			long startTime = runningTime;
			//advance running time
			runningTime = System.currentTimeMillis();
			boolean isTimeElapsed = (runningTime - startTime > Constants.MAX_AUTOPLAY_MILLISECONDS);
			
			Logger.d(TAG, "maxAutoplayTimeElapsed runningTime=" + runningTime + " startTime=" + startTime + " difference=" + (runningTime - startTime) + " isTimeElapsed=" + isTimeElapsed);
			
			//if (isTimeElapsed){ Logger.d(TAG, "maxAutoplayTimeElapsed = true"); }
			
			return isTimeElapsed;
		}
		else {//we are in "pre" autoplay, dont worry about timer
			return false;
		}
	}
	
	public static List<GameTile> getBoardBaseTilesAndRemovePlacedTiles(List<GameTile> boardTiles){
		List<GameTile> tiles = getBoardBaseTiles(boardTiles);
		
		for (GameTile tile : tiles){
			if (tile.isPlacement()){
				tile.removePlacement();
			}
		}
		
		
		return tiles;
	}
	
	private static List<GameTile> getBoardBaseTiles(List<GameTile> boardTiles){
		List<GameTile> tiles = new ArrayList<GameTile>();
		
		for (GameTile boardTile : boardTiles){
			GameTile tile = new GameTile();
			tile.setId(boardTile.getId());
			tile.setColumn(boardTile.getColumn());
			tile.setRow(boardTile.getRow());
			tile.setPlacedLetter(boardTile.getPlacedLetter());
			tiles.add(tile);
		}
		
		
		return tiles;
	}
	
	private static List<String> getSortedTrayLetters(List<String> trayLetters){
		List<String> tiles = new ArrayList<String>();
		
		for (String letter : trayLetters){
			 
			tiles.add(letter);
		}
		
		Collections.sort(tiles);
		
		return tiles;
	}
	
	
	//java holding on too tightly to references...hence the inner loop...ugh
	private static List<List<String>> getLetterSetBase(List<List<String>> letterSet){
		List<List<String>> set = new ArrayList<List<String>>();
		
		for (List<String> list : letterSet){
			List<String> strings = new ArrayList<String>();
			for (String s : list){
				strings.add(s);
			}
			set.add(strings);
		}
		
		return set;
	}
	
	
	//isOpponent = true means opponent is resigning
  	public static void resign(boolean isOpponent, Game game){
  
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

  	}
  	
  	
	public static void addGameToCompletedList(Game game){
		 List<GameListItem> games = GameData.getCompletedGameList();
		 
		 Logger.d(TAG, "addGameToCompletedList size=" + games.size() + " " + Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE);
		 
		 //put game at the beginning so that it saves in descending order
		 games.add(0, new  GameListItem(game.getId(), game.getCompletionDate()));
		 
		 //only store 10 games in this list.  clean out game storage and list for 11 and above
		 if (games.size() > Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE){
			 for (int i = games.size() - 1; i > Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE - 1; i--){
				 
				 removeGame(games.get(i).getGameId());
				 games.remove(i);
			 }
			 
		 }
		 Logger.d(TAG, "addGameToCompletedList size=" + games.size());
		 
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
		
		boolean logThisMethod = false;
		long runningTime = System.nanoTime();
		
		PlacedResult placedResult = new PlacedResult();
		placedResult.setBoardTiles(boardTiles); 
		
		List<GameTile> placedTiles = getGameTiles(boardTiles);
	 
	    if (logThisMethod){ Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getGameTiles", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
		
		//check to see if user is skipping, if so just return empty placedResult
		if (placedTiles.size() == 0){
			return placedResult;
		}
		
		//check to determine that overlays did not happen on same letter
		for (GameTile tile : placedTiles){
			if (logThisMethod){ 	Logger.d(TAG, "tile original=" + tile.getOriginalLetter() + " placed=" + tile.getPlacedLetter() + " position=" + tile.getId());}
			Check.Require(!tile.getOriginalLetter().equals(tile.getPlacedLetter()),  context.getString(R.string.game_play_invalid_overlay), Constants.ERROR_CODE_OVERLAY_PREVIOUS_LETTER);
		}
			if (logThisMethod){  Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "placedTiles", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
		
		List<PlayedTile> playedTiles = game.getPlayedTiles();
		
		//let's get these collections in the tileId order for certain
		Collections.sort(placedTiles, new GameTileComparator());
		Collections.sort(game.getPlayedTiles(), new PlayedTileComparator());
		
		if (logThisMethod){ Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "sorts", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
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
		if (logThisMethod){  Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "check 1 starting", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
 
		//the first turn (that plays letters) must have more than one letter played (every word must be at least two letters long
	 	Check.Require(game.getTurn() > 1 || isFirstPlayedWord && placedTiles.size() > 1, context.getString(R.string.game_play_too_few_letters), Constants.ERROR_CODE_TOO_FEW_LETTERS);
	 	//Check.Require(game.getTurn() > 1 || game.getTurn() == 1 && placedTiles.size() > 1, context.getString(R.string.game_play_too_few_letters));
 
	 	if (logThisMethod){  Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isMoveInValidStartPosition starting", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
	     
	 	Check.Require(isMoveInValidStartPosition(layout, game, placedTiles, isFirstPlayedWord), context.getString(R.string.game_play_invalid_start_position), Constants.ERROR_CODE_INVALID_START_POSITION);
	//	if (!this.isMoveInValidStartPosition(layout, game, placedTiles)){
	//		return R.string.game_play_invalid_start_position;
	//	}
	 	if (logThisMethod){  Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isMoveInValidStartPosition ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
	 	//see which axis the tiles were played on, x = horizontal, y = vertical
	 	String axis = getPlacedAxis(placedTiles);
	 	
	 	if (logThisMethod){ Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getPlacedAxis", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
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
	 	
        if (logThisMethod){  Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isMoveFreeOfGaps starting", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
	     
        Check.Require(isMoveFreeOfGaps(axis, playedSet, placedSet), context.getString(R.string.game_play_invalid_gaps), Constants.ERROR_CODE_INVALID_GAPS);
        

        if (logThisMethod){  Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isMoveFreeOfGaps ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
        
        Check.Require(isFirstPlayedWord || isWordConnectedToPlayedWords(placedTiles, playedTiles), context.getString(R.string.game_play_invalid_gaps_placed_words), Constants.ERROR_CODE_INVALID_PLACEMENT);
       // Check.Require(game.getTurn() == 1 || isWordConnectedToPlayedWords(placedTiles, playedTiles), context.getString(R.string.game_play_invalid_gaps_placed_words));
        
        if (logThisMethod){  Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "isWordConnectedToPlayedWords ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
        
        
        //determine the words that have been played
        List<PlacedWord> words = getWords(layout, axis, placedTiles, playedTiles);
        
        if (logThisMethod){ Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getWords ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
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
            		if (logThisMethod){ 	Logger.d(TAG, "checkPlayRules invalid word=" + word.getWord());}
            		invalidWords.add(word);
            	}
            }
        }
            
        wordService.finish();
        wordService = null;
        
        
        if (logThisMethod){     Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getTotalPoints ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
	     runningTime = System.nanoTime();
	 

        Check.Require(invalidWords.size() == 0, getInvalidWordsMessage(context, invalidWords), Constants.ERROR_CODE_INVALID_WORDS);

        if (logThisMethod){   Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", "getInvalidWordsMessage ended", Utils.convertNanosecondsToMilliseconds(System.nanoTime() - runningTime)));}
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
              
              //Logger.d(TAG, "getPlacedAxis row=" + rowCol.getRow() + " col=" + rowCol.getColumn());
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
