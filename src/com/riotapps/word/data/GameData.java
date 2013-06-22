package com.riotapps.word.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameListItem;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Storage;

public class GameData {

	public static List<Game> getLocalActiveGames(){
		
		//this might change to sqlLite
		Gson gson = new Gson(); 
		Type type = new TypeToken<List<String>>() {}.getType();
	    SharedPreferences settings = Storage.getSharedPreferences();
	
	    List<String> gameIds = gson.fromJson(settings.getString(Constants.USER_PREFS_ACTIVE_GAMES, Constants.EMPTY_JSON_ARRAY), type);

		List<Game> games = new ArrayList<Game>();
	    
		//loop through gameIds fetching individual games from storage
 	    for(String gameId : gameIds){
 	    	Game game = getLocalGame(gameId);
	    	if (game != null){
	    	    games.add(game);		     
	    	}
	    }
	    
		gson = null;    
		return games;
	}
	
	public static List<String> getLocalCompletedGames(){
		
		//this might change to sqlLite
		 Gson gson = new Gson(); 
		 Type type = new TypeToken<List<String>>() {}.getType();
	     SharedPreferences settings = Storage.getSharedPreferences();
	
	     List<String> games = gson.fromJson(settings.getString(Constants.USER_PREFS_COMPLETED_GAMES, Constants.EMPTY_JSON_ARRAY), type);
	     gson = null;
	     return games;
	}
	
	public static Game getGame(String id){
		Gson gson = new Gson(); 
		
		Game game = null;
		
		Type type = new TypeToken<Game>() {}.getType();
	    SharedPreferences settings = Storage.getGameSharedPreferences();

		String _game = gson.fromJson(settings.getString(id, Constants.EMPTY_STRING), type);
    	if (_game != Constants.EMPTY_STRING){
    	    game = gson.fromJson(_game, type);
    	}
    	gson = null;
    	return game;
 	}
	
	
	public static void saveGame(Game game){
		Gson gson = new Gson(); 
		SharedPreferences settings = Storage.getGameSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(String.format(Constants.USER_PREFS_GAME_JSON, game.getId()), gson.toJson(game));
		editor.apply();
        
		gson = null;
	}
	
	public static List<GameListItem> getCompletedGameList(){
		Gson gson = new Gson(); 
		
		Type type = new TypeToken<List<GameListItem>>() {}.getType();
	    SharedPreferences settings = Storage.getGameSharedPreferences();

		List<GameListItem> _games = gson.fromJson(settings.getString(Constants.USER_PREFS_GAME_LIST_JSON, Constants.EMPTY_JSON_ARRAY), type);
 
    	gson = null;
    	return _games;
 	} 
	
	public static void saveCompletedGameList(List<GameListItem> games){
		Gson gson = new Gson(); 
		SharedPreferences settings = Storage.getGameSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(Constants.USER_PREFS_GAME_LIST_JSON, gson.toJson(games));
		editor.apply();
        
		gson = null;
	}
}
