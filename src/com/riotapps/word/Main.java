package com.riotapps.word;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.conn.ConnectTimeoutException;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gcm.GCMRegistrar;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Opponent;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.NetworkTaskResult;
import com.riotapps.word.utils.Storage;
import com.riotapps.word.utils.Utils;
import com.riotapps.word.utils.Enums.RequestType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = Main.class.getSimpleName();
	TextView tvStartByNickname;
	Button bStart;
	Button bOptions;
	Button bBadges;
	ImageView ivContextPlayer;
	ImageView ivContextPlayerBadge;
	Context context = this;
	Player player;
  
	//Timer timer = null;
	boolean callingIntent = false;
 
	ApplicationContext appContext;
	LayoutInflater inflater;
	private boolean isListLoading = false;
 
	private View inflatedView;
    private Tracker tracker;
	
	public Tracker getTracker() {
		if (this.tracker == null){
			this.tracker = EasyTracker.getTracker();
		}
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        //PlayerService playerSvc = new PlayerService();
        //Player player = PlayerService.getPlayerFromLocal();
        
       // Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	   // t.show();
        
        ApplicationContext.captureTime(TAG, "onCreate started");
        
        Logger.d(TAG, "onCreate called");
           
	    bStart = (Button) findViewById(R.id.bStart);
	    bOptions = (Button) findViewById(R.id.bOptions);
	    bBadges = (Button) findViewById(R.id.bBadges);
	    
	    bStart.setOnClickListener(this);
		bOptions.setOnClickListener(this);
		bBadges.setOnClickListener(this);
		this.appContext = (ApplicationContext)this.getApplicationContext();
		
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.inflatedView = this.inflater.inflate(R.layout.gameyourturnlistitem, null);
		
		this.player = appContext.getPlayer(); //PlayerService.getPlayerFromLocal();
		PlayerService.loadPlayerInHeader(this);
		
		//SharedPreferences settings = Storage.getSharedPreferences();
	    //String completedDate = settings.getString(Constants.USER_PREFS_LATEST_COMPLETED_GAME_DATE, Constants.DEFAULT_COMPLETED_GAMES_DATE);

	    
	 	//Bundle extras = getIntent().getExtras(); 
	 	//Boolean isGameListPrefetched = false;
	 	//if(extras !=null)
	 	//{
	 	//	isGameListPrefetched = extras.getBoolean(Constants.EXTRA_GAME_LIST_PREFETCHED, false);
	 	//}
		
	  	this.loadOpponents();
	 	ApplicationContext.captureTime(TAG, "loadLists ended");
	 	/*
	 	long lastPlayerCheckTime = GameService.getLastGameListCheckTime(this);
		if (!isGameListPrefetched && Utils.convertNanosecondsToMilliseconds(System.nanoTime()) - lastPlayerCheckTime > Constants.LOCAL_GAME_LIST_STORAGE_DURATION_IN_MILLISECONDS){
			//fetch games
 
			try { 
				String json = PlayerService.setupAuthTokenCheck(this, this.player.getAuthToken());
				//this will bring back the players games too
				new NetworkTask(this, RequestType.POST, "", json, false).execute(Constants.REST_GET_PLAYER_BY_TOKEN);
			} catch (DesignByContractException e) {
				//this should never happen unless there is some tampering
				 DialogManager.SetupAlert(context, getString(R.string.oops), e.getLocalizedMessage(), true, 0);
			}

		}	
		else if (isGameListPrefetched){
			GameService.updateLastGameListCheckTime(this);
		}
		*/
		//no games yet, send player to StartGame to get started
	//	else if (this.player.getTotalNumLocalGames() == 0){
     //   	Intent intent = new Intent(getApplicationContext(), StartGame.class);
	//		startActivity(intent);	
	//	}
		//else {
		//	this.loadLists();
		//}
		//this.setupTimer();
		
//	 	this.checkAlert();
	 	ApplicationContext.captureTime(TAG, "checkAlert ended");
		

		
		ApplicationContext.captureTime(TAG, "onCreate ended");
		
		
	//	this.loadListTask = new LoadListTask();
	//	this.loadListTask.execute("");
		
    }
  /* 
    private void checkAlert(){
    	if (this.player != null){
	    	if (this.player.getLatestAlerts().size() > 0){
		    	 if (!PlayerService.checkAlertAlreadyShown(this, this.player.getLatestAlerts().get(0).getId(), this.player.getLatestAlerts().get(0).getActivationDateString())) {	
					 DialogManager.SetupAlert(this, !this.player.getLatestAlerts().get(0).getTitle().equals("") ? this.player.getLatestAlerts().get(0).getTitle() : this.getString(R.string.alert_default_title), this.player.getLatestAlerts().get(0).getText());
				 }
	    	}
    	}
    }
    */
    private void trackEvent(String action, String label, int value){
    	this.trackEvent(action, label, (long)value);
    }
    
    private void trackEvent(String action, String label, long value){
  		try{
  			Logger.d(TAG, "trackEvent this.tracker is null=" + (this.tracker == null));
  			this.tracker.sendEvent(Constants.TRACKER_CATEGORY_MAIN_LANDING, action,label, value);
  		}
  		catch (Exception e){
  			Logger.d(TAG, "trackEvent e=" + e.toString());
  		}
  	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		loadListTaskFromReceiver = null;
		loadListTask = null;		
	}

	

    @Override
	protected void onRestart() {
		//Log.w(TAG, "onRestart called"); 
		super.onRestart();
		
		if (this.player == null){
			this.player = appContext.getPlayer(); //PlayerService.getPlayerFromLocal();
		}
	 
	}
   
    
    @Override
	protected void onResume() {
		super.onResume();
	 
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
		//this.stopTimer();
		 EasyTracker.getInstance().activityStop(this);
		this.player = null;
	 

	}

	@Override
	public void onBackPressed() {
		// do nothing if back is pressed
		
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		this.finish(); 
	}

	private void loadOpponents(){
 
		ApplicationContext.captureTime(TAG, "loadList starting");
 
    	LinearLayout llOpponents = (LinearLayout)findViewById(R.id.llOpponents);
    	LinearLayout llOpponentsWrapper = (LinearLayout)findViewById(R.id.llOpponentsWrapper);
    	
    	ApplicationContext.captureTime(TAG, "loadList view clears starting");
    	//clear out view
    	llOpponents.removeAllViews();

    	
  //	Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() size=" + this.player.getActiveGamesYourTurn().size() );

    	ApplicationContext.captureTime(TAG, "loadList getActiveGamesYourTurn check starting");
    	
    	List<Opponent> opponents = appContext.getOpponents();
    	int i = 1;
    	
    	//if opponents lists is empty, something is screwy
    	if (opponents.size() > 0){    		
     		
	        for (Opponent o : opponents){
	        	//Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() game=" +g.getId() );

	        	
	        	llOpponents.addView(getOpponentView(o, i == 1, opponents.size() == i));
	        	 i += 1;
			}
    	}
   

    	ApplicationContext.captureTime(TAG, "loadList completed");
    }
    
    public View getOpponentView(Opponent opponent, boolean firstItem, boolean lastItem ) {
    	
    //	Logger.d(TAG, "getGameView started");
    //	ApplicationContext.captureTime(TAG, "getGameView inflate starting");
  		//View view = LayoutInflater.from(this).inflate(R.layout.gameyourturnlistitem, null);
  		View view = this.inflater.inflate(R.layout.gameyourturnlistitem, null);
  		//View view = new View(context);
  		//view = this.inflatedView;
    	
    //	ApplicationContext.captureTime(TAG, "getGameView inflate ended");
  		//just in case something fluky happened to the game and only one player was saved
  	 	int numPlayers = game.getPlayerGames().size();
	 	//Logger.w(TAG, "getGameView numPlayers=" + numPlayers);
	 	if (numPlayers == 1){
	 		view.setVisibility(View.GONE);  
	 		return view;
	 	}
	 	
	//	Logger.d(TAG, "getGameView 1");
  		// ImageFetcher imageLoader = new ImageFetcher(this, Constants.LARGE_AVATAR_SIZE, Constants.LARGE_AVATAR_SIZE, 0);
         //imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
 	//	Logger.d(TAG, "getGameView 2");
  	   // TextView tvPlayerName = (TextView)view.findViewById(R.id.tvOpponent1);
	 //	tvPlayerName.setText(game.getId()); //temp
	 	
	 //	ApplicationContext.captureTime(TAG, "getGameView view finds starting");
	 	
        ImageView ivOpponentBadge_1 = (ImageView)view.findViewById(R.id.ivOpponentBadge_1);
	 	ImageView ivOpponentBadge_2 = (ImageView)view.findViewById(R.id.ivOpponentBadge_2);
	 	ImageView ivOpponentBadge_3 = (ImageView)view.findViewById(R.id.ivOpponentBadge_3);
	 	RelativeLayout rlPlayer_1 = (RelativeLayout)view.findViewById(R.id.rlPlayer_1);
	 	RelativeLayout rlPlayer_2 = (RelativeLayout)view.findViewById(R.id.rlPlayer_2);
	 	LinearLayout rlAvatars = (LinearLayout)view.findViewById(R.id.rlAvatars);
	 	
		RelativeLayout.LayoutParams layoutLastAction = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        ImageView ivOpponent1 = (ImageView)view.findViewById(R.id.ivOpponent1);
	 	ImageView ivOpponent2 = (ImageView)view.findViewById(R.id.ivOpponent2);
	 	ImageView ivOpponent3 = (ImageView)view.findViewById(R.id.ivOpponent3);
	 	
	 	TextView tvOpponent_1 = (TextView)view.findViewById(R.id.tvOpponent_1);
	 	TextView tvOpponent_2 = (TextView)view.findViewById(R.id.tvOpponent_2);
	 	TextView tvOpponent_3 = (TextView)view.findViewById(R.id.tvOpponent_3);
	 	
	 	TextView tvLastAction = (TextView)view.findViewById(R.id.tvLastAction);
	 	ImageView ivChatAlert = (ImageView)view.findViewById(R.id.ivChatAlert);
	 	RelativeLayout llLastAction = (RelativeLayout)view.findViewById(R.id.llLastAction);
	 	
	 //	ApplicationContext.captureTime(TAG, "getGameView chat check starting");
	 	if (!GameService.checkGameChatAlert(context, game, false)){
	 		ivChatAlert.setVisibility(View.GONE);
	 	}
	 	
	 	tvLastAction.setText(game.getLastActionTextForList(context, player.getId()));
	 
	 	//first opponent
	 	List<PlayerGame> opponentGames = game.getOpponentPlayerGames(this.player);

	 	if (opponentGames.size() == 1){
	 		tvOpponent_1.setText(opponentGames.get(0).getPlayer().getNameWithMaxLength(25));
	 	}
	 	else if (opponentGames.size() == 2){
	 		tvOpponent_1.setText(opponentGames.get(0).getPlayer().getNameWithMaxLength(19));
	 	}
	 	else{
	 		tvOpponent_1.setText(opponentGames.get(0).getPlayer().getNameWithMaxLength(13));
	 	}
	 	//Logger.d(TAG, "getGameView 4.1");
	 	//RelativeLayout rlPlayer_1 = (RelativeLayout)view.findViewById(R.id.rlPlayer_1);
		int opponentBadgeId_1 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(0).getPlayer().getBadgeDrawable(), null, null);
		ivOpponentBadge_1.setImageResource(opponentBadgeId_1);

		imageLoader.loadImage(opponentGames.get(0).getPlayer().getImageUrl(), ivOpponent1);  
		//Logger.d(TAG, "getGameView 5");
		//optional 2nd opponent
		
	//	ApplicationContext.captureTime(TAG, "getGameView last item check starting");
		
		if (lastItem){
			RelativeLayout rlLineItem = (RelativeLayout)view.findViewById(R.id.rlLineItem);
			int bgLineItem = context.getResources().getIdentifier("com.riotapps.word:drawable/text_selector_bottom", null, null);
			rlLineItem.setBackgroundResource(bgLineItem);
			LinearLayout llBottomBorder = (LinearLayout)view.findViewById(R.id.llBottomBorder);
			llBottomBorder.setVisibility(View.INVISIBLE);

		}
	//	if (firstItem){
	//		RelativeLayout rlLineItem = (RelativeLayout)view.findViewById(R.id.rlLineItem);
	//		int bgLineItem = context.getResources().getIdentifier("com.riotapps.word:drawable/text_selector_top", null, null);
	//		rlLineItem.setBackgroundResource(bgLineItem);
	//	}
		//drawable/text_selector_bottom
		
	//	ApplicationContext.captureTime(TAG, "getGameView size check (1) starting");
		if (opponentGames.size() >= 2){
			if (opponentGames.size() == 2){
				tvOpponent_2.setText(opponentGames.get(1).getPlayer().getNameWithMaxLength(19));
		 	}
		 	else{
		 		tvOpponent_2.setText(opponentGames.get(1).getPlayer().getNameWithMaxLength(13));
		 	}
		  
			int opponentBadgeId_2 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(1).getPlayer().getBadgeDrawable(), null, null);
			ivOpponentBadge_2.setImageResource(opponentBadgeId_2);
			imageLoader.loadImage( opponentGames.get(1).getPlayer().getImageUrl(), ivOpponent2);
	 	}
	 	else {
	 		
	 		rlPlayer_2.setVisibility(View.GONE);
	 		//TableRow trOpponent2 = (TableRow)view.findViewById(R.id.trOpponent2);
	 	//	trOpponent2.setVisibility(View.GONE);
	 		ivOpponent2.setVisibility(View.GONE);
	 	}
		//Logger.d(TAG, "getGameView 6");
	 	//optional 3rd opponent
	 	if (opponentGames.size() >= 3){
		 	
		 	tvOpponent_3.setText(opponentGames.get(2).getPlayer().getNameWithMaxLength(13));
			int opponentBadgeId_3 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(2).getPlayer().getBadgeDrawable(), null, null);
			ivOpponentBadge_3.setImageResource(opponentBadgeId_3);
			imageLoader.loadImage( opponentGames.get(2).getPlayer().getImageUrl(), ivOpponent3);
	 	}
	 	else {
	 	//	TableRow trOpponent3 = (TableRow)view.findViewById(R.id.trOpponent3);
	 	//	trOpponent3.setVisibility(View.GONE);
	 		RelativeLayout rlPlayer_3 = (RelativeLayout)view.findViewById(R.id.rlPlayer_3);
	 		rlPlayer_3.setVisibility(View.GONE);
	 		ivOpponent3.setVisibility(View.GONE);
	 	}
		///Logger.d(TAG, "getGameView 7");
	 	
	 	int badgeSize = Utils.convertDensityPixelsToPixels(context, 11);
	 	int badgeRightMargin = Utils.convertDensityPixelsToPixels(context, 2);
	 	int textSize;
	 	
	 	int badgeTopMargin;
	 	
	// 	ApplicationContext.captureTime(TAG, "getGameView size check (2) starting");
		if (opponentGames.size() == 1){
			badgeTopMargin = Utils.convertDensityPixelsToPixels(context, 5);
			//textSize = Utils.convertDensityPixelsToPixels(context, 14);
			textSize = this.getResources().getInteger(R.integer.main_landing_1_opponent_text_size);
			tvOpponent_1.setTextSize(textSize);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(badgeSize, badgeSize);
		    rlp.setMargins(0, badgeTopMargin, badgeRightMargin, 0); // llp.setMargins(left, top, right, bottom);
		    ivOpponentBadge_1.setLayoutParams(rlp);
		    
			layoutLastAction.addRule(RelativeLayout.RIGHT_OF, rlAvatars.getId());	
			layoutLastAction.addRule(RelativeLayout.BELOW, rlPlayer_1.getId());
			layoutLastAction.setMargins(3, 0, 0, 0);
			//layoutLastAction.setMargins(left, top, right, bottom); 
			llLastAction.setLayoutParams(layoutLastAction);
			
			//RelativeLayout.LayoutParams layoutAvatars = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//layoutAvatars.setMargins(0, 0, 0, 30);
			//rlAvatars.setLayoutParams(layoutAvatars);

		}
		else if (opponentGames.size() == 2){
		
			
			badgeTopMargin = Utils.convertDensityPixelsToPixels(context, 5);
			//textSize = Utils.convertDensityPixelsToPixels(context, 12);
			textSize = this.getResources().getInteger(R.integer.main_landing_2_opponents_text_size);
			tvOpponent_1.setTextSize(textSize);
			tvOpponent_2.setTextSize(textSize);	
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(badgeSize, badgeSize);
		    rlp.setMargins(0, badgeTopMargin, badgeRightMargin, 0); // llp.setMargins(left, top, right, bottom);
		    ivOpponentBadge_2.setLayoutParams(rlp);
		    ivOpponentBadge_1.setLayoutParams(rlp);
		    
			layoutLastAction.addRule(RelativeLayout.BELOW, rlAvatars.getId());	
			llLastAction.setLayoutParams(layoutLastAction);
			//tvLastAction.setLayoutParams(layoutLastAction);
			
			RelativeLayout.LayoutParams layoutOpponent2  = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutOpponent2.setMargins(5, 4, 0, 0);
			layoutOpponent2.addRule(RelativeLayout.RIGHT_OF, rlAvatars.getId());	
			layoutOpponent2.addRule(RelativeLayout.BELOW, rlPlayer_1.getId());
			rlPlayer_2.setLayoutParams(layoutOpponent2);

		}
		else{
			
			badgeTopMargin = Utils.convertDensityPixelsToPixels(context, 2);
		//	textSize = Utils.convertDensityPixelsToPixels(context, 11);
			textSize = this.getResources().getInteger(R.integer.main_landing_3_opponents_text_size);
			tvOpponent_1.setTextSize(textSize);
			tvOpponent_2.setTextSize(textSize);	
			tvOpponent_3.setTextSize(textSize);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(badgeSize, badgeSize);
		    rlp.setMargins(0, badgeTopMargin, badgeRightMargin, 0); // llp.setMargins(left, top, right, bottom);
		    ivOpponentBadge_3.setLayoutParams(rlp);
		    ivOpponentBadge_2.setLayoutParams(rlp);
		    ivOpponentBadge_1.setLayoutParams(rlp);
		    
			//RelativeLayout.LayoutParams layoutOpponent2  = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//layoutOpponent2.setMargins(0, 4, 0, 0);
			//layoutOpponent2.addRule(RelativeLayout.RIGHT_OF, rlAvatars.getId());	
			//layoutOpponent2.addRule(RelativeLayout.BELOW, rlPlayer_1.getId());
			//rlPlayer_2.setLayoutParams(layoutOpponent2);
			
			layoutLastAction.addRule(RelativeLayout.BELOW, rlAvatars.getId());
			llLastAction.setLayoutParams(layoutLastAction);
			//tvLastAction.setLayoutParams(layoutLastAction);

		}
	 	
		//ApplicationContext.captureTime(TAG, "getGameView almost over starting");
	 	view.setTag(game.getId());
	 	view.setOnClickListener(this);
  	    return view;
  	}
    
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	
		 this.callingIntent = true;
			    
    	//stop running task if one is active
    	if (this.runningTask != null){
	  		this.runningTask.cancel(true);
	  		this.runningTask = null;
	  	} 
    	
    	switch(v.getId()){  
        case R.id.bStart:  
        
        	
        	if (this.player.getNumActiveGames() >= Constants.MAX_ACTIVE_GAMES){
        		this.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED, Constants.TRACKER_LABEL_START_GAME_MAX_REACHED, this.player.getNumActiveGames());
        		
        		DialogManager.SetupAlert(this, this.getString(R.string.oops), this.getString(R.string.validation_max_games_reached));
        	}
        	else {
        		this.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED, Constants.TRACKER_LABEL_START_GAME, Constants.TRACKER_DEFAULT_OPTION_VALUE);
        		
            	intent = new Intent(getApplicationContext(), StartGame.class);
    			startActivity(intent);
        	}
			break;
        case R.id.bBadges:  
        	this.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED, Constants.TRACKER_LABEL_BADGES, Constants.TRACKER_DEFAULT_OPTION_VALUE);
        	
        	intent = new Intent(getApplicationContext(), Badges.class);
			startActivity(intent);
			break; 
        case R.id.bOptions:  
        	this.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED, Constants.TRACKER_LABEL_OPTIONS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
        	
        	intent = new Intent(getApplicationContext(), Options.class);
			startActivity(intent);
			break;
        default:
        	String gameId = (String)v.getTag();
        	//DialogManager.SetupAlert(context, "tapped", gameId);
        	this.handleGameClick(gameId);
    	}
    	
    }  
 

   

    

    
    
}
        
