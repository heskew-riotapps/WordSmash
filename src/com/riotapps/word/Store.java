package com.riotapps.word;

import java.util.Arrays;
import java.util.List;

import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.example.android.trivialdrivesample.util.SkuDetails;
import com.riotapps.word.hooks.OpponentGroup;
import com.riotapps.word.hooks.OpponentGroupService;
import com.riotapps.word.hooks.OpponentService;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreItem;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Store  extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = Store.class.getSimpleName();
	 
	private ApplicationContext appContext;
	private	LayoutInflater inflater;
	private IabHelper mHelper;
	private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.store);
	        
	        PlayerService.loadPlayerInHeader(this);
	        this.appContext = (ApplicationContext)this.getApplicationContext(); 
	        this.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	       
	        MenuUtils.hideMenu(this);
	        MenuUtils.setHeaderTitle(this, this.getString(R.string.header_title_store));
	   
	        // compute your public key and store it in base64EncodedPublicKey
	        mHelper = new IabHelper(this, StoreService.getIABPublicKey());
	        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
	        	   public void onIabSetupFinished(IabResult result) {
	        	                
	        	         // Hooray, IAB is fully set up!
	        	      Logger.d(TAG, "In-app Billing is ready to go...almost ");
	        	      onSetupFinished(result);
	        	   }
	        	});
	        
	        
	        
	 }
	 
	 public void onSetupFinished(IabResult result){
		 if (!result.isSuccess()) {
	         // Oh noes, there was a problem.
	         Logger.d(TAG, "Problem setting up In-app Billing: " + result);
	      }  
		 else{
			 IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
			 			= new IabHelper.OnIabPurchaseFinishedListener() {
			     public void onIabPurchaseFinished(IabResult result, com.example.android.trivialdrivesample.util.Purchase purchase) 
			     {
			     	onPurchaseFinished(result, purchase);
			     }
			 };
			 
			 IabHelper.QueryInventoryFinishedListener mGotInventoryListener 
							= new IabHelper.QueryInventoryFinishedListener() {
					public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
						onPurchaseCheck(result, inventory);
				
					}
				};
				
	 
				mHelper.queryInventoryAsync(true, StoreService.getAllSkus(), mGotInventoryListener);
				//mHelper.queryInventoryAsync(mGotInventoryListener);
		 }
		 
	 }
	 
	 public void onPurchaseCheck(IabResult result, Inventory inventory){
		  if (result.isFailure()) {
		         // handle error here
		       }
	       else {
	         // update the local purchases first just in case something is out of sync
	         if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_HOPPER_PEEK)){
	        	com.example.android.trivialdrivesample.util.Purchase skuPeek = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_HOPPER_PEEK);
	        	StoreService.savePurchase(skuPeek.getSku(), skuPeek.getToken()); 
	         }
	         if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE)){
		        	com.example.android.trivialdrivesample.util.Purchase skuPremium = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE);
		        	StoreService.savePurchase(skuPremium.getSku(), skuPremium.getToken()); 
		     }  
	         if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_WORD_DEFINITIONS)){
		        	com.example.android.trivialdrivesample.util.Purchase skuDefs = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_WORD_DEFINITIONS);
		        	StoreService.savePurchase(skuDefs.getSku(), skuDefs.getToken()); 
		     }  
	         if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL)){
		        	com.example.android.trivialdrivesample.util.Purchase skuInterstitial = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL);
		        	StoreService.savePurchase(skuInterstitial.getSku(), skuInterstitial.getToken()); 
		     }  
	         if (inventory.hasPurchase(Constants.SKU_GOOGLE_PLAY_WORD_HINTS)){
		        	com.example.android.trivialdrivesample.util.Purchase skuHints = inventory.getPurchase(Constants.SKU_GOOGLE_PLAY_WORD_HINTS);
		        	StoreService.savePurchase(skuHints.getSku(), skuHints.getToken()); 
		     }  
	         
	    	 this.loadStore(inventory);

	       }
	 }
	
	 @Override
	 public void onDestroy() {
	    super.onDestroy();
	    if (mHelper != null) mHelper.dispose();
	    mHelper = null;
	 }
	 
	 private void onPurchaseFinished(IabResult result, com.example.android.trivialdrivesample.util.Purchase purchase){
		 if (result.isFailure()) {
             Logger.d(TAG, "Error purchasing: " + result);
             return;
          }  
		 else {
			 StoreService.savePurchase(purchase.getSku(), purchase.getToken());
			 
			 String thankYouMessage = this.getString(R.string.thank_you);
			 if (purchase.getSku().equals(Constants.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL)){
				 thankYouMessage = this.getString(R.string.purchase_thanks_hide_interstitial);
			 }
			 else if (purchase.getSku().equals(Constants.SKU_GOOGLE_PLAY_HOPPER_PEEK)){
				 thankYouMessage = this.getString(R.string.purchase_thanks_hopper_peek);
			 }
			 else if (purchase.getSku().equals(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE)){
				 thankYouMessage = this.getString(R.string.purchase_thanks_premium_upgrade);
			 }
			 else if (purchase.getSku().equals(Constants.SKU_GOOGLE_PLAY_WORD_DEFINITIONS)){
				 thankYouMessage = this.getString(R.string.purchase_thanks_word_definitions);
			 }
			 else if (purchase.getSku().equals(Constants.SKU_GOOGLE_PLAY_WORD_HINTS)){
				 thankYouMessage = this.getString(R.string.purchase_thanks_word_hints);
			 }
				 
			 //popup a thank you dialog
			 DialogManager.SetupAlert(this, this.getString(R.string.thank_you), thankYouMessage);
			 
		 }
       /*   else if (purchase.getSku().equals(SKU_GAS)) {
             // consume the ggas and update the UI
          }
          else if (purchase.getSku().equals(SKU_PREMIUM)) {
             // give user access to premium content and update the UI
          }
          */
	 }
	 
	 private void purchaseItem(String sku){
		 mHelper.launchPurchaseFlow(this, sku, 10001,   
				   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	 }
	 
	 
	 private void loadStore(Inventory inventory){
		 TextView tvPremiumUpgradeTitle = (TextView)this.findViewById(R.id.tvPremiumUpgradeTitle);
		 TextView tvPremiumUpgradeDescription = (TextView)this.findViewById(R.id.tvPremiumUpgradeDescription);
		 TextView tvPremiumUpgradePrice = (TextView)this.findViewById(R.id.tvPremiumUpgradePrice);
		 
		 SkuDetails skuPremiumUpgrade = inventory.getSkuDetails(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE);
		 tvPremiumUpgradeTitle.setText(skuPremiumUpgrade.getTitle());
		 tvPremiumUpgradeDescription.setText(skuPremiumUpgrade.getDescription());
		 tvPremiumUpgradePrice.setText(skuPremiumUpgrade.getPrice());
		 tvPremiumUpgradePrice.setTag(Constants.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE);
		 
		 tvPremiumUpgradePrice.setOnClickListener(this);
	 }
	 
		
	    public View getStoreItemView(StoreItem item) {

	  		View view = this.inflater.inflate(R.layout.character_item, null);
	 
 
		/* 	TextView tvCharacter = (TextView)view.findViewById(R.id.tvCharacter);
		 
 
		 	tvCharacter.setText(opponentGroup.getName());
		  
		 	 
		 	view.setTag(opponentGroup.getId());
		 	view.setOnClickListener(this);
	  	  */
	  	    return view;
	  	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		this.purchaseItem(v.getTag().toString());
		
		
		//call google payment integration here
	//	OpponentGroup og = OpponentGroupService.getInactiveOpponentGroup(Integer.parseInt(v.getTag().toString()));
	//	Logger.d(TAG, "tag=" + v.getTag().toString());
	//	og.setActivated(true);
		
	//	OpponentGroupService.saveOpponentGroup(og);
		
	//	this.appContext.setOpponents(OpponentService.getActivatedOpponents());
	}
	
	
	

}
