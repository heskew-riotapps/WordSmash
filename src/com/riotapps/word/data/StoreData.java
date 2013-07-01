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
import com.riotapps.word.hooks.Purchase;
import com.riotapps.word.hooks.StoreItem;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.FileUtils;
import com.riotapps.word.utils.Storage;

public class StoreData {
	public static Purchase getPurchaseByStoreItemId(String storeItemId){
		SharedPreferences settings = Storage.getPurchaseSharedPreferences();
			
	    String _purchase =  settings.getString(String.format(Constants.PURCHASE_PREFS_ITEM, storeItemId), Constants.EMPTY_STRING);
		
	    if (_purchase.equals(Constants.EMPTY_STRING)){
	    	
	    	return new Purchase(storeItemId);
	    }
	    else {
			Gson gson = new Gson(); 			 
			Type type = new TypeToken<Purchase>() {}.getType();
			Purchase purchase = gson.fromJson(_purchase, type);
			gson = null;
			return purchase;
	    }
	}
	
	public static void savePurchase(Purchase purchase){
		Gson gson = new Gson(); 			 
		 
		SharedPreferences settings = Storage.getPurchaseSharedPreferences();
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(String.format(Constants.PURCHASE_PREFS_ITEM, purchase.getStoreItemId()), gson.toJson(purchase));
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
	
	public static List<StoreItem> getStoreItems(){
		
	 	Gson gson = new Gson(); 
		Type type = new TypeToken<List<StoreItem>>() {}.getType();


	    ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext(); 
	    
 
	    List<StoreItem> storeItems = gson.fromJson(FileUtils.ReadRawTextFile(appContext, R.raw.store), type);
	    
	 
		gson = null;    
		
		return storeItems;
	}
}
