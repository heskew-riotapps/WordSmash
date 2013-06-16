package com.riotapps.word;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.services.WordLoaderService;
import com.riotapps.word.utils.*;

public class Splash  extends FragmentActivity {
   
	private static final String TAG = Splash.class.getSimpleName();

    final Context context = this;
    Splash me = this;
    Handler handler;
    public long startTime = System.nanoTime();
 
     
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
        
        Player player = PlayerService.getPlayerFromLocal();
        
        ApplicationContext appContext = (ApplicationContext)this.getApplicationContext();
		appContext.setPlayer(player);

        
      //  this.handleRouting();
        new Handler().postDelayed(new Runnable() {
            public void run() {

            	
                  
            	Intent intent = new Intent(Splash.this, com.riotapps.word.Main.class);
            	intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            	Splash.this.startActivity(intent); 
            	Splash.this.finish();

                Splash.this.startActivity(intent);
                Splash.this.finish();

                // transition from splash to main  
            //    overridePendingTransition(R.animate.activityfadein,
             //           R.animate.splashfadeout);
                
            
        		
              	

            }
        }, Constants.SPLASH_DELAY_DURATION);
         
        this.captureTime("onCreate ended");
        
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
	
 
