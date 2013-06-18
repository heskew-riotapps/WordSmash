package com.riotapps.word.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.R;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.Opponent;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.FileUtils;
import com.riotapps.word.utils.Storage;

public class OpponentData {

	public static List<Opponent> getLocalOpponents(){
		
		//this might change to sqlLite
		Gson gson = new Gson(); 
		 
		Type type = new TypeToken<List<Opponent>>() {}.getType();
	    SharedPreferences settings = Storage.getSharedPreferences();
	
	    List<Opponent> opponents;
	    
	    String _opponents =  settings.getString(Constants.USER_PREFS_OPPONENTS, Constants.EMPTY_STRING);

    	ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();

	    
	    if (_opponents == Constants.EMPTY_STRING){
	    	//opponents are not stored in active storage, which means its the user's first time through.
	    	//prime opponents from .dat file
	    	_opponents = FileUtils.ReadRawTextFile(appContext, R.raw.opponents);
	    	
		    //load objects into the list
		    opponents = gson.fromJson(_opponents, type);
		    
		    saveOpponents(opponents);

	    }
	    else{
		    //sync the lists if the count is different 
		    List<Opponent> opponentsFromDat;
	    	
		    //load objects into the list
		    opponentsFromDat = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.opponents), type);
		    opponents = gson.fromJson(_opponents, type);

		    if (opponentsFromDat.size() > opponents.size()){
		    	for (Opponent fromDat : opponentsFromDat){
		    		boolean add = true;
		    		for (Opponent fromLocal : opponents){
		    			if (fromDat == null){
		    				add = false;
		    			}
		    			else if (fromDat.getId() == fromLocal.getId()){ 
			    			add = false;
			    			
			    			//update opponent's name from dat
			    			fromLocal.setImagePrefix(fromDat.getImagePrefix());
			    			fromLocal.setSkillLevel(fromDat.getSkillLevel());
			    			fromLocal.setName(fromDat.getName());
			    			
			    			break;
			    		}
			    	}	
		    		if (add){
		    			opponents.add(fromDat);
		    		}
		    	}
		    	saveOpponents(opponents);
		    }
	    }

	    
	    
		gson = null;    
		return opponents;
	}

	public static void saveOpponents(List<Opponent> opponents){
		Gson gson = new Gson(); 
		SharedPreferences settings = Storage.getSharedPreferences();
	    SharedPreferences.Editor editor = settings.edit();
		
		editor.putString(Constants.USER_PREFS_OPPONENTS, gson.toJson(opponents));
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


