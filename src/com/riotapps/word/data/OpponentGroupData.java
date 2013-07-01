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
 	
	    List<OpponentGroup> opponentGroups;
	    List<OpponentGroup> activeOpponentGroups = new ArrayList<OpponentGroup>();
	     	    
  
    	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
    	 
    	//load all opponent groups shipped in this app version
    	opponentGroups = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.opponent_groups), type);
    		  
    	for (OpponentGroup og : opponentGroups){
    		//add auto activated opponent groups to player's list
    		if (og.isPurchased() || og.isAutoActivated()){
    			OpponentGroup o = new OpponentGroup();
    			o.setId(og.getId());
    			o.setName(og.getName());
    			o.setConstant(og.getConstant());        		
    		 
    			activeOpponentGroups.add(o);		
    		}
    	}
     
    	opponentGroups = null;
		gson = null;
		return activeOpponentGroups;	
	}
	
	//this will likely go away
	public static List<OpponentGroup> getInactiveOpponentGroups(){
		Gson gson = new Gson(); 
		 
		Type type = new TypeToken<List<OpponentGroup>>() {}.getType();
 	
	    List<OpponentGroup> opponentGroups;
	    List<OpponentGroup> inactiveOpponentGroups = new ArrayList<OpponentGroup>();
	     	    
  
    	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
    	 
    	//load all opponent groups shipped in this app version
    	opponentGroups = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.opponent_groups), type);
    		  
    	for (OpponentGroup og : opponentGroups){
    		if (!og.isPurchased() && !og.isAutoActivated()){
    			OpponentGroup o = new OpponentGroup();
    			o.setId(og.getId());
    			o.setName(og.getName());
    			o.setConstant(og.getConstant());        		
    		 
    			inactiveOpponentGroups.add(o);		
    		}
    	}
     
    	opponentGroups = null;
		gson = null;
		return inactiveOpponentGroups;
	}
	
	//this will go away
	public static void saveOpponentGroups___(List<OpponentGroup> opponentGroups){
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
