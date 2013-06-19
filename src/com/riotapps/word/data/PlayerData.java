package com.riotapps.word.data;

import java.lang.reflect.Type;

import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Storage;
import com.riotapps.word.hooks.Player;

public class PlayerData {

	public static Player getPlayer(){
		//this might change to sqlLite
		 Gson gson = new Gson(); 
		 Type type = new TypeToken<Player>() {}.getType();
	     SharedPreferences settings = Storage.getSharedPreferences();
	     
	    // Logger.w(TAG, "getPlayerFromLocal player=" + settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_JSON));
	     String _player = settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_STRING);
	     
	     if (_player == Constants.EMPTY_STRING){
	    	 return null;
	     
	     }
	     Player player = gson.fromJson(_player, type);
	     gson = null;
	     return player;

	}
	
	public static void savePlayer(Player player){
		Gson gson = new Gson(); 
		SharedPreferences settings = Storage.getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
		editor.apply();
        
		gson = null;
	}
	
}
