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
import com.riotapps.word.hooks.OpponentService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.CustomButtonDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
	private static final String TAG = Main.class.getSimpleName();
 
	Context context = this;
	Player player;
	 private PopupMenu popupMenu;
     private final static int ONE = 1;
     private final static int TWO = 2;
     private final static int THREE = 3;
	//Timer timer = null;
	boolean callingIntent = false;
 
	ApplicationContext appContext;
	LayoutInflater inflater;
 
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
           
	  //  bStart = (Button) findViewById(R.id.bStart);
	 //   bOptions = (Button) findViewById(R.id.bOptions);
	 //   bBadges = (Button) findViewById(R.id.bBadges);
	    
	 //   bStart.setOnClickListener(this);
//		bOptions.setOnClickListener(this);
//		bBadges.setOnClickListener(this);
		this.appContext = (ApplicationContext)this.getApplicationContext(); 
		
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//this.inflatedView = this.inflater.inflate(R.layout.gameyourturnlistitem, null);

        Logger.d(TAG, "onCreate appContent.getplayer about to be called");
		this.player = appContext.getPlayer(); //PlayerService.getPlayerFromLocal();
		
        Logger.d(TAG, "player is null =" + (this.player == null)); 
		PlayerService.loadPlayerInHeader(this, this.player);
		
		//SharedPreferences settings = Storage.getSharedPreferences();
	    //String completedDate = settings.getString(Constants.USER_PREFS_LATEST_COMPLETED_GAME_DATE, Constants.DEFAULT_COMPLETED_GAMES_DATE);

	    
	 	//Bundle extras = getIntent().getExtras(); 
	 	//Boolean isGameListPrefetched = false;
	 	//if(extras !=null)
	 	//{
	 	//	isGameListPrefetched = extras.getBoolean(Constants.EXTRA_GAME_LIST_PREFETCHED, false);
	 	//}
		
	  	this.loadOpponents();
	  	this.setupMenu();
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
		
 	
		ApplicationContext.captureTime(TAG, "onCreate ended");
		
		
	//	this.loadListTask = new LoadListTask();
	//	this.loadListTask.execute("");
		
    }
 
    private void setupMenu(){
    	
    	  popupMenu = new PopupMenu(this, findViewById(R.id.options));
          popupMenu.getMenu().add(Menu.NONE, ONE, Menu.NONE, "Item 1");
          popupMenu.getMenu().add(Menu.NONE, TWO, Menu.NONE, "Item 2");
          popupMenu.getMenu().add(Menu.NONE, THREE, Menu.NONE, "Item 3");
          popupMenu.setOnMenuItemClickListener(this);
          findViewById(R.id.options).setOnClickListener(this);
    }
    
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
    //	LinearLayout llOpponentsWrapper = (LinearLayout)findViewById(R.id.llOpponentsWrapper);
    	
   // 	ApplicationContext.captureTime(TAG, "loadList view clears starting");
    	//clear out view
    	llOpponents.removeAllViews();

    	
  //	Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() size=" + this.player.getActiveGamesYourTurn().size() );

    	ApplicationContext.captureTime(TAG, "loadList opponents fetch starting");
    	
    	List<Opponent> opponents = appContext.getOpponents();
    	int i = 1;
    	int j = 1;
    	
    	int horizontalNum = 2;
    	
    	DisplayMetrics displaymetrics = new DisplayMetrics();
	 	this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    int screenWidth = displaymetrics.widthPixels;
	    
	    int itemWidth = Math.round(screenWidth / 2) - 1;
	   LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	  //	int w  = displaymetrics.widthPixels;
    	//if opponents lists is empty, something is screwy
    	if (opponents.size() > 0){    	
     		
    		
    		LinearLayout llHorizontal = null;
    		
    		 
	        for (Opponent o : opponents){
	        	//if (i == 3){break;}
	        	//Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() game=" +g.getId() );
	        	if (j == 1){
	        		llHorizontal = new LinearLayout(context);
	        		llHorizontal.setLayoutParams(params);
	        		llHorizontal.setOrientation(LinearLayout.HORIZONTAL);
	        	}
	        	
	        	llHorizontal.addView(getOpponentView(o, itemWidth, itemWidth));
	         
	        	if (j == horizontalNum || i == opponents.size()){
	        		llOpponents.addView(llHorizontal);
	        		j = 0;
	        	}
	        	//llOpponents.addView(getOpponentView(o));
	        	 j += 1;
	        	 i += 1;
			}
    	}
   

    	ApplicationContext.captureTime(TAG, "loadList completed");
    }
    
	
    public View getOpponentView(Opponent opponent, int width, int height) {
    	
 
  		View view = this.inflater.inflate(R.layout.opponent_item, null);
 
	    LayoutParams params = new LayoutParams(width, height);

  		//hide record if player has not played opponent
//        ImageView ivOpponentBadge = (ImageView)view.findViewById(R.id.ivOpponentBadge);
	    LinearLayout llLineItem = (LinearLayout)view.findViewById(R.id.llLineItem);
        ImageView ivOpponent = (ImageView)view.findViewById(R.id.ivOpponent);
	 	TextView tvOpponent = (TextView)view.findViewById(R.id.tvOpponent);
	 	//TextView tvYourRecordLabel = (TextView)view.findViewById(R.id.tvYourRecordLabel);
	 	TextView tvYourWins = (TextView)view.findViewById(R.id.tvYourWins);
	 	TextView tvYourLosses = (TextView)view.findViewById(R.id.tvYourLosses);
	 	TextView tvYourDraws = (TextView)view.findViewById(R.id.tvYourDraws);
	 	TextView tvSkillLevel = (TextView)view.findViewById(R.id.tvSkillLevel);
	 	
	 	
	 	llLineItem.setLayoutParams(params);
	 Logger.d(TAG, "opponent=" + opponent.getName());
	 	tvOpponent.setText(opponent.getName());
	 	//tvYourRecordLabel.setText(String.format(this.getString(R.string.main_your_record_label), opponent.getName()));
	 	tvYourWins.setText(String.format(this.getString(R.string.main_wins), opponent.getNumLosses()));
	 	tvYourLosses.setText(String.format(this.getString(R.string.main_losses), opponent.getNumWins()));
	 	tvYourDraws.setText(String.format(this.getString(R.string.main_draws), opponent.getNumDraws()));
	 	tvYourDraws.setText(String.format(this.getString(R.string.main_draws), opponent.getNumDraws()));
	 	tvSkillLevel.setText(opponent.getSkillLevelText(this));
	 	
	 	//Logger.d(TAG, "getGameView 4.1");
	 	//RelativeLayout rlPlayer_1 = (RelativeLayout)view.findViewById(R.id.rlPlayer_1);
	//	int opponentBadgeId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponent.getBadgeDrawable(), null, null);
	//	ivOpponentBadge.setImageResource(opponentBadgeId);

		int opponentImageId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponent.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_MAIN), null, null);
		ivOpponent.setImageResource(opponentImageId);
		
		//ApplicationContext.captureTime(TAG, "getGameView almost over starting");
	 	view.setTag(opponent.getId());
	 	view.setOnClickListener(this);
  	    return view;
  	}
    
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	
  
		 //open dialog mgr to confirm that player wants to play this particular opponent
		 
		 
		 switch (v.getId()){
		 	case R.id.options:
		 		popupMenu.show();
		 		break;
			default:
				 int opponentId = Integer.parseInt(v.getTag().toString());
			   	this.handleGameStartPrompt(opponentId);
			    		 
		 }
 
    }  
 

    @Override
    public boolean onMenuItemClick(MenuItem item) {
       /*    TextView tv = (TextView) findViewById(R.id.selection);
           switch (item.getItemId()) {
           case ONE:
                  tv.setText("ONE");
                  break;
           case TWO:
                  tv.setText("TWO");
                  break;
           case THREE:
                  tv.setText("THREE");
                  break;
           } */
           return false;
    }

    private void handleGameStartPrompt(int opponentId){
		 Opponent o = OpponentService.getOpponent(opponentId);

    	
    	final CustomButtonDialog dialog = new CustomButtonDialog(this, 
    			this.getString(R.string.main_game_start_prompt_title), 
    			String.format(this.getString(R.string.main_game_start_prompt), o.getName()),
    			this.getString(R.string.yes),
    			this.getString(R.string.no));
    	
    	dialog.setOnOKClickListener(new View.OnClickListener() {
	 		@Override
			public void onClick(View v) {
	 			dialog.dismiss(); 
	 		//	trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        //			Constants.TRACKER_LABEL_CANCEL_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 		
	 		}
		});

    	dialog.setOnDismissListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
		 			dialog.dismiss(); 
		 	//		trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		     //   			Constants.TRACKER_LABEL_CANCEL_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			 		}
			});
    	
     	dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
	//			trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	  //      			Constants.TRACKER_LABEL_CANCEL_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
				
			}
		});  
    	dialog.show();	
    }

    private void handleGameStartOnClick(){
    	//start game and go to game surface
    }
    
 	

    
    
}
        
