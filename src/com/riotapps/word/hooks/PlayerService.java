package com.riotapps.word.hooks;

import java.lang.reflect.Type;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.riotapps.word.R;
 
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Check;
import com.riotapps.word.utils.Storage;
import com.riotapps.word.utils.Utils;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.GameStateService;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.NetworkConnectivity;
import com.riotapps.word.utils.Validations;
import com.facebook.model.GraphUser;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.data.PlayerData;


////make this class static
@SuppressLint({ "NewApi"})
public class PlayerService {
	private static final String TAG = PlayerService.class.getSimpleName();
 
	/*
	public static boolean checkAlertAlreadyShown(Context context, String alertId, String alertActivationDate){
		Gson gson = new Gson();
		
		SharedPreferences settings = Storage.getSharedPreferences();
		String json = settings.getString(Constants.USER_PREFS_ALERT_CHECK, "[]"); 
		
		Type type = new TypeToken<List<String>>() {}.getType();
		List<String> alerts = gson.fromJson(json, type);
		
		if (alerts.contains(alertId)){
			return true;
		}
		else {
			alerts.add(alertId);
			
			json = gson.toJson(alerts, type);
			
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(Constants.USER_PREFS_ALERT_CHECK, json);
			editor.putString(Constants.USER_PREFS_ALERT_CHECK_DATE, alertActivationDate);
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
	}
	
	public static boolean checkFirstTimeGameSurfaceAlertAlreadyShown(Context context){
		SharedPreferences settings = Storage.getSharedPreferences();
		if (settings.getBoolean(Constants.USER_PREFS_FIRST_TIME_GAME_SURFACE_ALERT_CHECK, false) == false) { 
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(Constants.USER_PREFS_FIRST_TIME_GAME_SURFACE_ALERT_CHECK,true);
			// Check if we're running on GingerBread or above
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			     // If so, call apply()
			     editor.apply();
			 // if not
			 } else {
			     // Call commit()
			     editor.commit();
			 }
			Logger.d(TAG, "checkFirstTimeAlertAlreadyShown=false");
			return false;
		}
		else{
			Logger.d(TAG, "checkFirstTimeAlertAlreadyShown=true");
			return true;
		}
	}
	*/
  
	
	public static void clearLocalStorage(){

		Storage.getSharedPreferences().edit().clear().commit();
		Storage.getSharedPreferences(Constants.GAME_STATE).edit().clear().commit();
 	}

	 
	 
	
	
	public static void loadPlayerInHeader(final FragmentActivity context, Boolean activateGravatarOnClick){
		loadPlayerInHeader(context, new Player());
	}
	
	public static void loadPlayerInHeader(final FragmentActivity context){
		loadPlayerInHeader(context, new Player());
	}
	
	public static void loadPlayerInHeader(final FragmentActivity context, Player player) { //, Boolean activateGravatarOnClick){
	//	 Player player = PlayerService.getPlayerFromLocal();

	/*
		Logger.d(TAG, "player.getBadgeDrawable()=" + player.getBadgeDrawable());
		ImageView ivContextPlayerBadge = (ImageView) context.findViewById(R.id.ivHeaderContextPlayerBadge);
		int contextPlayerBadgeId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + player.getBadgeDrawable(), null, null);
		ivContextPlayerBadge.setImageResource(contextPlayerBadgeId);

		TextView tvHeaderContextPlayerName = (TextView) context.findViewById(R.id.tvHeaderContextPlayerName);
		tvHeaderContextPlayerName.setText(player.getNameWithMaxLength(25));
		
		TextView tvHeaderContextPlayerWins = (TextView) context.findViewById(R.id.tvHeaderContextPlayerWins); 
		if (player.getNumWins() == 1){
			tvHeaderContextPlayerWins.setText(context.getString(R.string.header_1_win));
		}
		else { 			
			tvHeaderContextPlayerWins.setText(String.format(context.getString(R.string.header_num_wins), player.getNumWins())); 
		}
		*/
		player = null;
	}
	
	public static void addWinToPlayerRecord(){
		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
	    appContext.getPlayer().addWinToPlayerRecord();
	    
	    savePlayer(appContext.getPlayer());
	}

	public static void addLossToPlayerRecord(){
		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
	    appContext.getPlayer().addLossToPlayerRecord();
	    
	    savePlayer(appContext.getPlayer());
	}
	
	public static void addDrawToPlayerRecord(){
		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
	    appContext.getPlayer().addDrawToPlayerRecord();
	    
	    savePlayer(appContext.getPlayer());
	}
	
	public static void savePlayer(Player player){
		PlayerData.savePlayer(player);
		
		ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
	    appContext.setPlayer(player);
	 
	}
	
	public static boolean isFirstTime(){
		return PlayerData.getPlayer() == null;
	}
	
	public static Player getPlayer(){
		 Player player = PlayerData.getPlayer();
		 
		 //if player has not been created yet, let's create her now
		 if (player == null){
 			 
			 player = new Player();

			 player.setNumDraws(0);
			 player.setNumWins(0);
			 player.setNumLosses(0);
			 player.setId(Constants.DEFAULT_PLAYER_ID);
	 
			 PlayerData.savePlayer(player);
		 }
		 
		 return player;
	}
	
 
	public static String getBadgeDrawable(int numWins){
		if (numWins == -1) {
			return Constants.BADGE_INVITED;
		}
		if (numWins == 0) {
			return Constants.BADGE_0;
		}
		if (numWins >= 1 && numWins <= 4) {
			return Constants.BADGE_1_4;
		}
		if (numWins >= 5 && numWins <= 9) {
			return Constants.BADGE_5_9;
		}
		if (numWins >= 10 && numWins <= 14) {
			return Constants.BADGE_10_14;
		}
		if (numWins >= 15 && numWins <= 19) {
			return Constants.BADGE_15_19;
		}
		if (numWins >= 20 && numWins <= 24) {
			return Constants.BADGE_20_24;
		}
		if (numWins >= 25 && numWins <= 29) {
			return Constants.BADGE_25_29;
		}
		if (numWins >= 30 && numWins <= 39) {
			return Constants.BADGE_30_39;
		}
		if (numWins >= 40 && numWins <= 49) {
			return Constants.BADGE_40_49;
		}
		if (numWins >= 50 && numWins <= 59) {
			return Constants.BADGE_50_59;
		}
		if (numWins >= 60 && numWins <= 69) {
			return Constants.BADGE_60_69;
		}
		if (numWins >= 70 && numWins <= 79) {
			return Constants.BADGE_70_79;
		}
		if (numWins >= 80 && numWins <= 89) {
			return Constants.BADGE_80_89;
		}
		if (numWins >= 90 && numWins <= 99) {
			return Constants.BADGE_90_99;
		}
		if (numWins >= 100 && numWins <= 124) {
			return Constants.BADGE_100_124;
		}
		if (numWins >= 125 && numWins <= 149) {
			return Constants.BADGE_125_149;
		}
		if (numWins >= 150 && numWins <= 174) {
			return Constants.BADGE_150_174;
		}
		if (numWins >= 175 && numWins <= 199) {
			return Constants.BADGE_175_199;
		}
		if (numWins >= 200 && numWins <= 224) {
			return Constants.BADGE_200_224;
		}
		if (numWins >= 225 && numWins <= 249) {
			return Constants.BADGE_225_249;
		}
		if (numWins >= 250 && numWins <= 274) {
			return Constants.BADGE_250_274;
		}
		if (numWins >= 275 && numWins <= 299) {
			return Constants.BADGE_275_299;
		}
		if (numWins >= 300 && numWins <= 349) { 
			return Constants.BADGE_300_349;
		}
		if (numWins >= 350 && numWins <= 399) {
			return Constants.BADGE_350_399;
		}
		if (numWins >= 400 && numWins <= 449) {
			return Constants.BADGE_400_449;
		}
		if (numWins >= 450 && numWins <= 499) {
			return Constants.BADGE_450_499;
		}
		if (numWins >= 500) { // stop here for now
			return Constants.BADGE_500_599;
		}

		if (numWins >= 500 && numWins <= 599) {
			return Constants.BADGE_500_599;
		}
		if (numWins >= 600 && numWins <= 699) {
			return Constants.BADGE_600_699;
		}
		if (numWins >= 700 && numWins <= 799) {
			return Constants.BADGE_700_799;
		}
		if (numWins >= 800 && numWins <= 899) {
			return Constants.BADGE_800_899;
		}
		if (numWins >= 900 && numWins <= 999) {
			return Constants.BADGE_900_999;
		}
		if (numWins >= 1000) { // && numWins <= 1249) {
			return Constants.BADGE_1000_1249;
		}
		if (numWins >= 1250 && numWins <= 1499) {
			return Constants.BADGE_1250_1499;
		}
		if (numWins >= 1500 && numWins <= 1749) {
			return Constants.BADGE_1500_1749;
		}
		if (numWins >= 1750 && numWins <= 1999) {
			return Constants.BADGE_1750_1999;
		}
		if (numWins >= 2000 && numWins <= 2499) {
			return Constants.BADGE_2000_2499;
		}
		if (numWins >= 2500 && numWins <= 2999) {
			return Constants.BADGE_2500_2999;
		}
		if (numWins >= 3000 && numWins <= 3999) {
			return Constants.BADGE_3000_3999;
		}
		if (numWins >= 4000 && numWins <= 4999) {
			return Constants.BADGE_4000_4999;
		}
		if (numWins >= 5000) {
			return Constants.BADGE_5000;
		}
		//fallthrough
		return Constants.BADGE_0;
	}

}
