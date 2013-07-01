package com.riotapps.word.hooks;

import java.util.List;

import com.riotapps.word.data.StoreData;

public class StoreService {
	
	public static Purchase getPurchaseByStoreItemId(String storeItemId){
		return StoreData.getPurchaseByStoreItemId(storeItemId);
	}
	
	public static List<StoreItem> getStoreItems(){
		
		return StoreData.getStoreItems();
	}

	public static boolean isHideInterstitialAdPurchased(){
		//loop through purchased bundle from google play
		
		return false;
	}

	public static boolean isHideBannerAdsPurchased(){
		
		return false;
	}
	
	public static boolean isHopperPeekPurchased(){
		
		return false;
	}
	
	public static boolean isHideWordLookupAdPurchased(){
		//loop through purchased bundle from google play
		
		return isHideBannerAdsPurchased();
	}
}
