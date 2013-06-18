package com.riotapps.word.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.R;
import com.riotapps.word.hooks.Opponent;
import com.riotapps.word.hooks.OpponentGroup;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.FileUtils;
import com.riotapps.word.utils.Storage;

public class OpponentGroupData {
	
 

	public static List<OpponentGroup> getActiveOpponentGroups(){
		Gson gson = new Gson(); 
		 
		Type type = new TypeToken<List<OpponentGroup>>() {}.getType();
	    SharedPreferences settings = Storage.getSharedPreferences();

 	
	    List<OpponentGroup> opponentGroups;
	    List<OpponentGroup> playerOpponentGroups;
	     	    
  
    	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
    	 
    	//load all opponent groups shipped in this app version
    	opponentGroups = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.opponent_groups), type);
    		  
    	
    	//load all opponent groups already activated for this player 
    	playerOpponentGroups = gson.fromJson(settings.getString(Constants.USER_PREFS_OPPONENT_GROUPS, Constants.EMPTY_JSON_ARRAY), type);

    	
    	//if player already has locally activated character sets, return them
    	if (playerOpponentGroups.size() > 0){
    		return playerOpponentGroups;
    	}
    	else{
    		
    		//reinitialize
    	    playerOpponentGroups = new ArrayList<OpponentGroup>();
    		
    	
        	//load all opponent groups shipped in this app version
        	opponentGroups = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.opponent_groups), type);
        	for (OpponentGroup og : opponentGroups){
        		//add auto activated opponent groups to player's list
        		if (og.isActivated()){
        			OpponentGroup o = new OpponentGroup();
        			o.setId(og.getId());
        			o.setName(og.getName());
        			o.setActivated(og.isActivated());
        			o.setConstant(og.getConstant());        		
        		 
        			playerOpponentGroups.add(o);		
        		}
        	}
        	
        	//store opponents locally, also check for sync 
        	saveOpponentGroups(playerOpponentGroups);
    	}
    	
 
		gson = null;
		return playerOpponentGroups;	
	}
	
	public static List<OpponentGroup> getInactiveOpponentGroups(){
		Gson gson = new Gson(); 
		 
		Type type = new TypeToken<List<OpponentGroup>>() {}.getType();
	    SharedPreferences settings = Storage.getSharedPreferences();

 	
	    List<OpponentGroup> opponentGroups;
	    List<OpponentGroup> playerOpponentGroups;
	    List<OpponentGroup> inactiveOpponentGroups = new ArrayList<OpponentGroup>();
	    	    
  
    	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
    	 
    	//load all opponent groups shipped in this app version
    	opponentGroups = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.opponent_groups), type);
    		  
    	
    	//load all opponent groups already activated for this player 
    	playerOpponentGroups = gson.fromJson(settings.getString(Constants.USER_PREFS_OPPONENT_GROUPS, Constants.EMPTY_JSON_ARRAY), type);

		for (OpponentGroup og : opponentGroups){
		
			//if the group is not auto activated, see if the player has already bought it
			if (!og.isActivated()){
				boolean inactive = true;
				if (playerOpponentGroups.size() > 0){
					for (OpponentGroup og_ : playerOpponentGroups){
						if (og.getId() == og_.getId()){
							//player has already bought this character set, skip it
							inactive = false;
							break;	
						}
					}
				}
				if (inactive){
					OpponentGroup o = new OpponentGroup();
        			o.setId(og.getId());
        			o.setName(og.getName());
        			o.setActivated(og.isActivated());
        			o.setConstant(og.getConstant());        		
        	
					inactiveOpponentGroups.add(og);
				}
			}
			
		}

		gson = null;
		return inactiveOpponentGroups;
	}
	
	public static void saveOpponentGroups(List<OpponentGroup> opponentGroups){
		Gson gson = new Gson(); 
		SharedPreferences settings = Storage.getSharedPreferences();
	    SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(Constants.USER_PREFS_OPPONENT_GROUPS, gson.toJson(opponentGroups));
		// Check if we're running on GingerBread or above
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		     // If so, call apply()
		     editor.apply();
		 // if not
		} else {
		     // Call commit()
		     editor.commit();
		} 
	    
		gson = null;
	}
	
}
