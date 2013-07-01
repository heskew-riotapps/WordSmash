package com.riotapps.word.utils;

 
import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
	
	public static SharedPreferences getSharedPreferences(){
		return getSharedPreferences(Constants.USER_PREFS);
	}

	public static SharedPreferences getGameSharedPreferences(){
		return getSharedPreferences(Constants.GAME_PREFS);
	}
	
	public static SharedPreferences getOpponentSharedPreferences(){
		return getSharedPreferences(Constants.OPPONENT_PREFS);
	}
	
	public static SharedPreferences getPurchaseSharedPreferences(){
		return getSharedPreferences(Constants.PURCHASE_PREFS);
	}
	
	public static SharedPreferences getSharedPreferences(String name){
		Context ctx = ApplicationContext.getAppContext();
		return ctx.getSharedPreferences(name, Context.MODE_PRIVATE);	
	}
	
	
}
