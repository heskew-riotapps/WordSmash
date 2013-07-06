package com.riotapps.word;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.services.WordLoaderService;
import com.riotapps.word.utils.*;

public class Splash  extends FragmentActivity {
   
	private static final String TAG = Splash.class.getSimpleName();

    final Context context = this;
    Splash me = this;
    Handler handler;
    public long startTime = System.nanoTime();
    private IabHelper mHelper;
    private IabHelper.QueryInventoryFinishedListener onPurchaseCheck;
    ApplicationContext appContext;
     
   // public void test(){}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
      
        this.captureTime("onCreate starting");    
        this.startBackgroundService();
        //sendMessage(this, "123", "message from Wordsmash");
        
      //  Playtomic.Log(//).play();
 	 	
        this.captureTime("handleRouting starting");
        
        Player player = PlayerService.getPlayer();
        
        this.appContext = (ApplicationContext)this.getApplicationContext();
		this.appContext.setPlayer(player);

		
		 // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, StoreService.getIABPublicKey());
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
        	   public void onIabSetupFinished(IabResult result) {
        		   onSetupFinished(result);
        	   }
        	});
		//pub logic in to wait for purchase check, perhaps kick it off from inventory listener
		
		
        
      //  this.handleRouting();
		 /*
        new Handler().postDelayed(new Runnable() {
            public void run() {

            	Intent intent;
            	if (appContext.getPlayer().getActiveGameId().length() > 0){
            		intent = new Intent(Splash.this, com.riotapps.word.GameSurface.class);
            		intent.putExtra(Constants.EXTRA_GAME_ID, appContext.getPlayer().getActiveGameId());
            	}
            	else{
            		intent = new Intent(Splash.this, com.riotapps.word.Main.class);
            	}
            	//intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            	Splash.this.startActivity(intent); 
            	//Splash.this.finish();

                // transition from splash to main  
            //    overridePendingTransition(R.animator.activityfadein,
             //          R.animator.splashfadeout);
                
            
        		
              	

            }
        }, Constants.SPLASH_DELAY_DURATION);
        
        */
         
        this.captureTime("onCreate ended");
        
     }
    
    public void onSetupFinished(IabResult result){
    	  if (!result.isSuccess()) {
 	         // Oh noes, there was a problem.
 	         Logger.d(TAG, "Problem setting up In-app Billing: " + result);
 	      }    
    	  else{
 	         // Hooray, IAB is fully set up!
 	      Logger.d(TAG, "In-app Billing is ready to go...almost ");
 	      
			IabHelper.QueryInventoryFinishedListener mGotInventoryListener 
						= new IabHelper.QueryInventoryFinishedListener() {
				public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
					onPurchaseCheck(result, inventory);
				
				}
			};
			mHelper.queryInventoryAsync(true, StoreService.getAllSkus(), mGotInventoryListener);

    	  }
    	
    }
    
	 public void onPurchaseCheck(IabResult result, Inventory inventory){
		 Logger.d(TAG, "onPurchaseCheck");
		  if (result.isFailure()) {
		         // handle error here
		       } 
	       else {
	         // does the user have the premium upgrade?
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
	       }
		  
		  Intent intent;
	      	if (appContext.getPlayer().getActiveGameId().length() > 0){
	      		intent = new Intent(Splash.this, com.riotapps.word.GameSurface.class);
	      		intent.putExtra(Constants.EXTRA_GAME_ID, appContext.getPlayer().getActiveGameId());
	      	}
	      	else{
	      		intent = new Intent(Splash.this, com.riotapps.word.Main.class);
	      	}
	      	//intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	      	Splash.this.startActivity(intent); 
	 }
	    
    private void startBackgroundService(){
  //  	this.startService(new Intent(this, WordLoaderService.class));
    }
   
    
	 @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 EasyTracker.getInstance().activityStart(this);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		 EasyTracker.getInstance().activityStop(this);
	}

	    
    public void captureTime(String text){
    	ApplicationContext.captureTime(TAG, text);
	  
	}
    private void handleRouting(){
    	captureTime("handleProcessing starting");
 
    	//perhaps put a thread sleep in here for a small delay
    	try {
			Thread.sleep(Constants.SPLASH_DELAY_DURATION);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Intent intent;
		
      	intent = new Intent(this, com.riotapps.word.Main.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	this.startActivity(intent); 
     }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
 		finish();
	}

 	
 	
	
}
	
 
