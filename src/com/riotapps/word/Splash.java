package com.riotapps.word;

import org.apache.http.conn.ConnectTimeoutException;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gcm.GCMRegistrar;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.services.BackgroundService;
import com.riotapps.word.services.ProcessBridge;
import com.riotapps.word.services.WordLoaderService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.*;
import com.riotapps.word.utils.Enums.RequestType;

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
        this.handleRouting();
       
         
        this.captureTime("onCreate ended");
     }
    
    private void startBackgroundService(){
    	this.startService(new Intent(this, WordLoaderService.class));
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
		
      	intent = new Intent(this, com.riotapps.word.Welcome.class);
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
	
 
