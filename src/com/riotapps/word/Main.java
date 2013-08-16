package com.riotapps.word;

import java.util.List;

import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Opponent;
import com.riotapps.word.hooks.OpponentService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.interfaces.ICloseDialog;
import com.riotapps.word.ui.CustomButtonDialog;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.BitmapFactory;

public class Main extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ICloseDialog{
	private static final String TAG = Main.class.getSimpleName();
 
	private Context context = this;
	private Player player;
	 private PopupMenu popupMenu;
	 private CustomButtonDialog customDialog;
	 private LinearLayout llOpponents = null;
        
     private int chosenOpponentId = 0;
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

        //Logger.d(TAG, "onCreate appContent.getplayer about to be called");
		this.player = appContext.getPlayer(); //PlayerService.getPlayerFromLocal();
		
       // Logger.d(TAG, "player is null =" + (this.player == null)); 
		PlayerService.loadPlayerInHeader(this, this.player);
		MenuUtils.hideHeaderTitle(this);
		this.setupFonts();
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
	 
	 	this.checkFirstTimeStatus();
		ApplicationContext.captureTime(TAG, "onCreate ended");
		
		
	//	this.loadListTask = new LoadListTask();
	//	this.loadListTask.execute("");
		
		if (StoreService.isHideBannerAdsPurchased()){
			AdView adView = (AdView)this.findViewById(R.id.adView);
			adView.setVisibility(View.GONE);
		}
    }
 
	 private void checkFirstTimeStatus(){
		 //first check to see if this score has already been alerted (from local storage) 
		 
		 if (!PlayerService.checkFirstTimeMainAlertAlreadyShown(this)) {
			 DialogManager.SetupAlert(this, this.getString(R.string.main_first_time_alert_title), this.getString(R.string.main_first_time_alert_message));
		 }
		 
	 }
    private void setupMenu(){
    	
    	  popupMenu = new PopupMenu(this, findViewById(R.id.options));

    	  MenuUtils.fillMenu(this, popupMenu);
   
    	  popupMenu.setOnMenuItemClickListener(this);
          findViewById(R.id.options).setOnClickListener(this);
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
	 
		//check to see if there is an active game first, occasionally the app might 
		//get into this odd state where the main activity comes into focus without being redirected first to game surface via splash
		if (this.player.getActiveGameId().length() > 0){
			Intent intent = new Intent(Main.this, com.riotapps.word.GameSurface.class);
    		Main.this.startActivity(intent); 
		}
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
 
    	this.llOpponents = (LinearLayout)findViewById(R.id.llOpponents);
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
	    
	    Logger.d(TAG, "SCREEN WIDTH=" + screenWidth);
	    Logger.d(TAG, "opponents.size()=" + opponents.size());
	    
	    
	    int itemWidth = Math.round(screenWidth / 2) - 1;
	    
	    //if (itemWidth > 380){itemWidth = 380;}
	    
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
 
	    RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(width, height);

  		//hide record if player has not played opponent
//        ImageView ivOpponentBadge = (ImageView)view.findViewById(R.id.ivOpponentBadge);
	    RelativeLayout rlLineItem = (RelativeLayout)view.findViewById(R.id.rlLineItem);
	    //LinearLayout llLineItem = (LinearLayout)view.findViewById(R.id.llLineItem);
        ImageView ivOpponent = (ImageView)view.findViewById(R.id.ivOpponent);
	 	TextView tvOpponent = (TextView)view.findViewById(R.id.tvOpponent);
	 	//TextView tvYourRecordLabel = (TextView)view.findViewById(R.id.tvYourRecordLabel);
	  	TextView tvYourWins = (TextView)view.findViewById(R.id.tvYourWins);
	  	TextView tvYourLosses = (TextView)view.findViewById(R.id.tvYourLosses);
	  	TextView tvYourDraws = (TextView)view.findViewById(R.id.tvYourDraws);
	 	TextView tvSkillLevel = (TextView)view.findViewById(R.id.tvSkillLevel);
	 	
	 	tvOpponent.setTypeface(ApplicationContext.getMainFontTypeface());
	 	tvYourWins.setTypeface(ApplicationContext.getMainFontTypeface());
	 	tvYourLosses.setTypeface(ApplicationContext.getMainFontTypeface());
	 	tvYourDraws.setTypeface(ApplicationContext.getMainFontTypeface());
	 	tvSkillLevel.setTypeface(ApplicationContext.getMainFontTypeface());
	 	
	 	
	 	rlLineItem.setLayoutParams(params);
	// Logger.d(TAG, "opponent=" + opponent.getName());
	 	tvOpponent.setText(opponent.getName());
	 	//tvYourRecordLabel.setText(String.format(this.getString(R.string.main_your_record_label), opponent.getName()));
	  //	tvYourWins.setText(String.format(this.getString(R.string.main_wins), opponent.getNumLosses()));
	  //	tvYourLosses.setText(String.format(this.getString(R.string.main_losses), opponent.getNumWins()));
	  //	tvYourDraws.setText(String.format(this.getString(R.string.main_draws), opponent.getNumDraws()));
	 	tvYourWins.setText(String.valueOf(opponent.getNumWins()));
	  	tvYourLosses.setText(String.valueOf(opponent.getNumLosses()));
	  	tvYourDraws.setText(String.valueOf(opponent.getNumDraws()));
	 	//tvYourDraws.setText(String.format(this.getString(R.string.main_draws), opponent.getNumDraws()));
	 	tvSkillLevel.setText(opponent.getSkillLevelText(this));
	 	
	 	//Logger.d(TAG, "getGameView 4.1");
	 	//RelativeLayout rlPlayer_1 = (RelativeLayout)view.findViewById(R.id.rlPlayer_1);
	//	int opponentBadgeId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponent.getBadgeDrawable(), null, null);
	//	ivOpponentBadge.setImageResource(opponentBadgeId);

	 	RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(width, height);
		ivOpponent.setLayoutParams(paramsImage);
		
		int opponentImageId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponent.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_MAIN), null, null);
		ivOpponent.setImageResource(opponentImageId);
		
		
		 
		
		// if (Main.bgOpponentGroup1_Opponent1 == null) {
		//	Bitmap bm = GameSurfaceView.decodeSampledBitmapFromResource(getResources(), opponentImageId, width, height);
		//	bm = Bitmap.createScaledBitmap(bm, width, height, false);
		// }
			
		// 	ivOpponent.setImageBitmap(bm);
			
			//ivOpponent.setBackgroundResource(opponentImageId);
		
		//ApplicationContext.captureTime(TAG, "getGameView almost over starting");
	 	view.setTag(opponent.getId());
	 	view.setOnClickListener(this);
  	    return view;
  	}
    
    
    @Override 
    public void onClick(View v) {
    
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
    	return MenuUtils.handleMenuClick(this, item.getItemId());
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		
		Logger.d(TAG, "onActivityResult called requestCode=" + requestCode + "  resultCode=" + resultCode);
		 switch(requestCode) { 
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_OK_CLICKED:
			   this.dismissCustomDialog();
	 
			   this.handleGameStartOnClick();
			   break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_CLOSE_CLICKED:
			   this.dismissCustomDialog();
			   this.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_START_GAME_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			   break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_CANCEL_CLICKED:
			   this.dismissCustomDialog();
			   this.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_START_GAME_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			   break;
			   
		 }
    }
    
    private void dismissCustomDialog(){
		if (this.customDialog != null){
			customDialog.dismiss();
			customDialog = null;
		}
	}
    
    private void handleGameStartPrompt(int opponentId){
		Opponent o = OpponentService.getOpponent(opponentId);
    	this.chosenOpponentId = opponentId;
    	
    	this.customDialog = new CustomButtonDialog(this, 
    			this.getString(R.string.main_game_start_prompt_title), 
    			String.format(this.getString(R.string.main_game_start_prompt), o.getName()),
    			this.getString(R.string.yes),
    			this.getString(R.string.no),
    			Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_OK_CLICKED,
    			Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_CANCEL_CLICKED,
    			Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_CLOSE_CLICKED);	
    	
     
    	this.customDialog.show();	
    }
    
    

    private void handleGameStartOnClick(){
    	//start game and go to game surface
    	try {
		
    		
			Game game = GameService.createGame(context, player, chosenOpponentId);

			this.trackEvent(Constants.TRACKER_ACTION_GAME_STARTED, Constants.TRACKER_LABEL_OPPONENT, chosenOpponentId);
			
			this.llOpponents.removeAllViews();
			this.llOpponents = null;
			
			Intent intent = new Intent(this, com.riotapps.word.GameSurface.class);
    		//intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
    		this.startActivity(intent); 
    		this.finish();
			
		} catch (DesignByContractException e) {
			// TODO Auto-generated catch block
			
			DialogManager.SetupAlert(this, this.getString(R.string.sorry), e.getMessage());
		}

    }

	@Override
	public void dialogClose(int resultCode) {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onActivityResult called resultCode=" + resultCode);
		 switch(resultCode) { 
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_OK_CLICKED:
			   this.dismissCustomDialog();
	 
			   this.handleGameStartOnClick();
			   break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_CLOSE_CLICKED:
			   this.dismissCustomDialog();
			   this.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_START_GAME_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			   break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_CANCEL_CLICKED:
			   this.dismissCustomDialog();
			   this.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_START_GAME_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			   break;
			   
		 }
		
	}
    
	private void setupFonts(){
		
		TextView txtMainLabel = (TextView)findViewById(R.id.txtMainLabel);

		txtMainLabel.setTypeface(ApplicationContext.getMainFontTypeface());
	}
 	

    
    
}
        
