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
import com.riotapps.word.hooks.OpponentGroup;
import com.riotapps.word.hooks.OpponentGroupService;
import com.riotapps.word.hooks.OpponentRecord;
import com.riotapps.word.hooks.OpponentService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.FileUtils;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Storage;

public class OpponentData {
	
	private static final String TAG = OpponentData.class.getSimpleName();
	
	public static List<Opponent> getActivatedOpponents(){
		Logger.d(TAG, "getActivatedOpponents called");
		List<OpponentGroup> opponentGroups = OpponentGroupData.getActiveOpponentGroups();
		
		Logger.d(TAG, "num opponentGroups=" + opponentGroups.size());
		
		//this might change to sqlLite
		Gson gson = new Gson(); 
		Type type = new TypeToken<List<Opponent>>() {}.getType();


	    ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext(); 
	    
	    String _opponents = FileUtils.ReadRawTextFile(appContext, R.raw.opponents);
    	
	    //load objects into the list
	    List<Opponent> opponents = gson.fromJson(_opponents, type);
	    
		Logger.d(TAG, "num opponents from gson=" + opponents.size());
		
	    List<Opponent> activeOpponents = new ArrayList<Opponent>();
	    
	    //filter out inactive groups
	    for (Opponent o : opponents){
	    	Logger.d(TAG, "opponent loop name=" + o.getName());
	    	
	    	boolean include = false;
	    	for (OpponentGroup og : opponentGroups){
	    		if (o.getOpponentGroupId() == og.getId()){
	    			include = true;
	    			break;
	    		}
	    	}
	    	if (include){
	    		Opponent opponent = new Opponent();
	    		opponent.setId(o.getId());
	    		opponent.setImagePrefix(o.getImagePrefix());
	    		opponent.setName(o.getName());
	    		opponent.setOpponentGroupId(o.getOpponentGroupId());
	    		opponent.setSkillLevel(o.getSkillLevel());
	    		activeOpponents.add(opponent);
	    	}
	    }
	    
	    opponents = null;
	    opponentGroups = null;
		gson = null;    
		Logger.d(TAG, "num activeOpponents=" + activeOpponents.size());
		return activeOpponents;
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
	
 
	public static OpponentRecord getOpponentRecord(int opponentId){
	    SharedPreferences settings = Storage.getOpponentSharedPreferences();
	
	    String _opponentRecord =  settings.getString(String.format(Constants.OPPONENT_PREFS_RECORD, String.valueOf(opponentId)), Constants.EMPTY_STRING);
		
	    if (_opponentRecord.equals(Constants.EMPTY_STRING)){
	    	
	    	return new OpponentRecord();
	    }
	    else {
			Gson gson = new Gson(); 			 
			Type type = new TypeToken<OpponentRecord>() {}.getType();
			OpponentRecord record = gson.fromJson(_opponentRecord, type);
			gson = null;
			return record;
	    }
	}
	
	public static void saveOpponentRecord(int opponentId, OpponentRecord record){
		Gson gson = new Gson(); 			 
		 
		SharedPreferences settings = Storage.getOpponentSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(String.format(Constants.OPPONENT_PREFS_RECORD, String.valueOf(opponentId)), gson.toJson(record));
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


