package com.riotapps.word.data;

import java.lang.reflect.Type;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.R;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.FileUtils;
import com.riotapps.word.utils.Storage;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.StoreItem;

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
		 
	     settings = null;
	     gson = null;
	     return player;

	}
	
	public static void savePlayer(Player player){
		Gson gson = new Gson(); 
		SharedPreferences settings = Storage.getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
		editor.apply();
        
	    settings = null;
		gson = null;
	}
	
	public static int getRemainingFreeUsesHopperPeek(){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_HOPPER_PEEK, Constants.FREE_USES_HOPPER_PEEK);
	    
	    settings = null;
	    
	    return uses;
	}
	
	public static int getRemainingFreeUsesWordDefinition(){
		
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, Constants.FREE_USES_WORD_DEFINITION);
	    
	    settings = null;
	    
	    return uses;
	}
	
	public static int removeAFreeUseFromHopperPeek(){
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_HOPPER_PEEK, Constants.FREE_USES_HOPPER_PEEK);
	    
	    if (uses > 0){
	    	uses -= 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_REMAINING_USES_HOPPER_PEEK, uses);
	  		editor.apply();
	    } 
	    
	    settings = null;
	    return uses;
	}
	
	public static int removeAFreeUseFromWordDefinition(){
	    SharedPreferences settings = Storage.getSharedPreferences();
	    int uses = settings.getInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, Constants.FREE_USES_WORD_DEFINITION);
	    
	    if (uses > 0){
	    	uses -= 1;
	    	SharedPreferences.Editor editor = settings.edit();
		
	  		editor.putInt(Constants.USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION, uses);
	  		editor.apply();
	    } 
	    
	    settings = null;
	    return uses;
	}	
	
}
