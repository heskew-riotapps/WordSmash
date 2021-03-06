package com.riotapps.word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
//import com.revmob.RevMob;
//import com.revmob.RevMobAdsListener;
//import com.revmob.ads.fullscreen.RevMobFullscreen;
import com.riotapps.word.hooks.AlphabetService;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.Game.LastAction;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.PlayedTile;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.ui.CustomButtonDialog;

import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.GameAction.GameActionType;
import com.riotapps.word.ui.GameState;
import com.riotapps.word.ui.GameStateLocation;
import com.riotapps.word.ui.GameStateService;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.GameTile;
import com.riotapps.word.ui.HopperPeekDialog;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.ui.PlacedResult;
import com.riotapps.word.ui.WordLoaderThread;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.CustomProgressDialog;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.NetworkTaskResult;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Utils;
import com.riotapps.word.utils.WebClient;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.Enums.ResponseHandlerType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.ads.AdRequest.ErrorCode;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;

//Import the Chartboost SDK
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.riotapps.word.interfaces.ICloseDialog;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GameSurface extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ICloseDialog{
	private static final String TAG = GameSurface.class.getSimpleName();
	
	private PlacedResult placedResult = null;
	private CustomButtonDialog customDialog = null;
	private HopperPeekDialog hopperPeekdialog = null;
	private Chartboost cb;
	private GameSurface context = this;
	private GameSurfaceView gameSurfaceView;
	private ImageFetcher imageLoader;
	 private RelativeLayout scoreboard;
	 private SurfaceView surfaceView;
	 private PopupMenu popupMenu;
	 
	 private Button bRecall;
	 private Button bPlay;
	 private Button bSkip;
	 private Button bShuffle;
	 private boolean isButtonActive = false;
	 private boolean isNetworkTaskActive = false;
	 private boolean isRestartFromInterstitialAd = false;
	 private boolean isAdStarted = false; 
	 private boolean isGameReloaded = false;
	 private boolean hideInterstitialAd = false;
	 private boolean isChartBoostActive = false;
	 private boolean isRevMobActive = false;
	 private boolean isAdMobActive = false;
	 
	 private String lastPlayerActionBeforeAutoplay = "";
	 
	 public String getLastPlayerActionBeforeAutoplay() {
		return lastPlayerActionBeforeAutoplay;
	}

	public void setLastPlayerActionBeforeAutoplay(
			String lastPlayerActionBeforeAutoplay) {
		this.lastPlayerActionBeforeAutoplay = lastPlayerActionBeforeAutoplay;
	}

	private boolean isPreAutoplayTaskRunning = false;
	 
	 private AutoplayTask autoplayTask = null; 
	 private PreAutoplayTask preAutoplayTask = null; 
	 private List<PlacedResult> placedResults = new ArrayList<PlacedResult>();
	 private boolean hasPreAutoPlayRunThisTurn = false;
	 
//	 private RevMob revmob;
//	 private RevMobAdsListener revmobListener;
//	 private RevMobFullscreen revMobFullScreen;
	 private boolean hasPostAdRun = false;
	 private boolean isBoundToGCMService = false;
	 private boolean isCompletedThisSession = false;
	 
	 private String postTurnMessage = "";
	 private String postTurnTitle = "";
 
	 
	// private Timer timer = null;
	 private Timer runawayAdTimer = null;
	// CustomProgressDialog spinner = null;
	
	 private com.google.ads.InterstitialAd interstitial;
	 private GameActionType postTurnAction;
	 private CustomProgressDialog spinner;
	 
	 private boolean isGamePopulating = true;
	 //View bottom;
	private int currentPoints = 0;
	public static final int MSG_SCOREBOARD_VISIBILITY = 1;
	public static final int MSG_POINTS_SCORED = 2;
	//public static int SCOREBOARD_HEIGHT = 30;
	//public static final int BUTTON_CONTROL_HEIGHT = 48;
	private boolean buttonsLoaded = false;
	private int windowHeight;
	private int scoreboardHeight;
	private Game game;
	private GameState gameState;
//	private AlphabetService alphabetService;
//	private WordService wordService;
	
	public long runningTime = System.nanoTime();
	public long captureTime = System.nanoTime();


 
	private Player player;
	private TextView tvNumPoints;
	//public PlayerGame contextPlayerGame;
	private BroadcastReceiver gcmReceiver;
	private WordLoaderThread wordLoaderThread = null;
	private Tracker tracker;
 

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getScoreboardHeight() {
		return scoreboardHeight;
	}

	public void setScoreboardHeight(int scoreboardHeight) {
		this.scoreboardHeight = scoreboardHeight;
	}

	public Tracker getTracker() {
		if (this.tracker == null){
			this.tracker = EasyTracker.getTracker();
		}
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
    
    public void unfreezeButtons(){
    	this.isButtonActive = false;
    }

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		//ApplicationContext appContext = (ApplicationContext)this.getApplicationContext();
	    this.player = ((ApplicationContext)this.getApplicationContext()).getPlayer(); //PlayerService.getPlayerFromLocal(); 
		this.tvNumPoints = (TextView)findViewById(R.id.tvNumPoints);
 
		 Display display = getWindowManager().getDefaultDisplay(); 
	     this.windowHeight = display.getHeight();  // deprecated
	     
	     this.captureTime("onCreate starting");
	     
	    // Playtomic.Log().play();
	   //  Playtomic.Log().forceSend();
	     
	  	this.scoreboard = (RelativeLayout)findViewById(R.id.scoreboard);
	  	this.scoreboardHeight = this.scoreboard.getHeight();
	 //	ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayerScoreboard);
	// 	imageLoader.loadImage(gravatar, ivPlayer); //default image
	    
	 //	Bundle extras = getIntent().getExtras(); 
	 //	if(extras !=null)
	// 	{
	// 		String value = extras.getString("gameId");
	// 	}
	 	 
	 	//Logger.d(TAG, "Game about to be fetched from extra");
	 	Intent i = getIntent();
	 	//String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	boolean fromCompletedGameList = i.getBooleanExtra(Constants.EXTRA_FROM_COMPLETED_GAME_LIST, false);
	 	
	 	//do this so that back button does not get crazy if one navigates to game from completed game list continuously
	 	if (fromCompletedGameList){
	 		MenuUtils.hideMenu(this);
	 	}
	 	//this.game = (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
	 	
	 	this.captureTime("get game from local starting");
	 	
	 	String gameId = this.player.getActiveGameId();
	 	
	 	if (fromCompletedGameList){
	 		gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	}
	 	
	 	
	 	if (gameId == null || gameId.equals("")){
	 		//reroute player to main
	 		Intent intent = new Intent(this, com.riotapps.word.Main.class);
    		this.startActivity(intent); 
	 	}
	 	
	 	this.game = GameService.getGame(gameId); //(Game) i.getParcelableExtra(Constants.EXTRA_GAME);
		Logger.d(TAG, "onCreate game turn=" + game.getTurn());
	 	this.captureTime("get game from local ended");	 	
	// 	spinner = new CustomProgressDialog(this);
    //    spinner.setMessage(this.getString(R.string.progress_loading));
    //    spinner.show();

  		//Gson gson = new Gson();  
	    //Logger.d(TAG, "game json=" + gson.toJson(game));
	 	
	 	//temp
	 	//this.game = getTempGame();

		this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
		
	// 	this.captureTime("alphabet service starting");
	//	this.alphabetService = new AlphabetService(context);
		//this.wordService = new WordService(context);
	// 	this.captureTime("alphabet service started");
		
	
		//appContext.getWordService()
		
	 	this.captureTime("setup game starting");
		this.setupGame();
	 	this.captureTime("setup game ended");
		
	 	this.captureTime("gamesurfaceview starting");
		this.gameSurfaceView.construct(this);
		//this.gameSurfaceView.setParent(this);
	 	this.captureTime("gamesurfaceview started");		
	
		// Logger.d(TAG, "SetDerivedValues  this.getWindowHeight=" +  this.getWindowHeight() );
		// Logger.d(TAG, "SetDerivedValues  this.gameSurfaceView.height=" +  gameSurfaceView.getHeight() );
	 	
		//Logger.d(TAG, "scoreboard about to be loaded");
	 	//this.loadScoreboard();
	 	
	 //	this.captureTime("setup game starting");
//		this.setupGame();
//	 	this.captureTime("setup game ended");
		
	// 	this.captureTime("checkGameStatus starting");
	//	this.checkGameStatus();
	// 	this.captureTime("checkGameStatus ended");
	 	 
	 	//this.setupTimer();
	 	this.setupAdServer();
	 	this.setupMenu();
 
	 }
	
 

	private void setupAdServer(){
		Logger.d(TAG, "setupAdServer called");
		
	 	boolean isAdMob = Constants.INTERSTITIAL_ADMOB;
	 	boolean isChartBoost = Constants.INTERSTITIAL_CHARTBOOST;
		boolean isRevMob = false; //Constants.INTERSTITIAL_REVMOB;
		final int useRevMob = 0;
		
	 	if (!StoreService.isHideInterstitialAdPurchased())
	 	{
	 		//assign either chartboost or revmob randomly
	 		/*if (isChartBoost && isRevMob){
	 			
	 			int coinFlip = (int)(Math.random() * 2);
 
	 			if (coinFlip == useRevMob ){
		 			isChartBoost = false;
				}
				else{
		 			isRevMob = false;
				}
			}
*/
/*	 		if (isRevMob){
	 			Logger.d(TAG, "setupAdServer isRevMob=true");
	 			
	 			
		 		this.isRevMobActive = true;
		 		this.revmob = RevMob.start(this, this.getString(R.string.rev_mob_app_id));
		 
	 		}
	 		else*/ 
	 		if (isChartBoost){
	 			Logger.d(TAG, "setupAdServer isCharBoost=true");
	 			
		 		this.isChartBoostActive = true;
		 		this.setupChartBoost();
	 		}
	 		else if (isAdMob){
	 			this.isAdMobActive = true;
	 		}
	 	}
	 	else{
	 		this.hideInterstitialAd = true;
	 	}
	 		 		
	 	
	 	Logger.d(TAG, "revMob=" + this.isRevMobActive + " chartBoost=" + this.isChartBoostActive);
	}
	
	private void setupChartBoost(){
		this.cb = Chartboost.sharedChartboost();
		this.cb.onCreate(this, this.getString(R.string.chartboost_app_id), this.getString(R.string.chartboost_app_signature), this.chartBoostDelegate);
		this.cb.setImpressionsUseActivities(true);
		this.cb.startSession();
		this.cb.cacheInterstitial();

	}
	
	public void captureTime(String text){
	     this.captureTime = System.nanoTime();
	     Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", text, Utils.convertNanosecondsToMilliseconds(this.captureTime - this.runningTime)));
	     this.runningTime = this.captureTime;

	}
	/*
	private void setupTimer(){
		this.setupTimer(Constants.GAME_SURFACE_CHECK_START_IN_MILLISECONDS);
	}
	
    private void setupTimer(long delay){
    	Logger.d(TAG, "setupTimer called");
    	//expand this for chat updates at some point
    	if (this.game.getStatus() == 1 && !this.game.isContextPlayerTurn(this.player)){
    		if (this.timer == null){
    		 
				Logger.d(TAG, "setupTimer starting");
				this.captureTime("setupTimer starting");
		    	timer = new Timer();  
		    	updateGameTask updateGame = new updateGameTask();
		    	timer.scheduleAtFixedRate(updateGame, delay, Constants.GAME_SURFACE_CHECK_INTERVAL_IN_MILLISECONDS);
		    	this.captureTime("checkGameStatus ended");
    		}
    	}
    	else {
    		stopTimer();
    	}
    }
    
    private void stopTimer(){
    	Logger.d(TAG, "stopTimer called");
    	if (this.timer != null) {
    		this.timer.cancel();
    		this.timer = null;
    	}
    
    }
    
    private class updateGameTask extends TimerTask {
    	   public void run() {
    		   if (!game.isContextPlayerTurn(player)){
    			   ((Activity) context).runOnUiThread(new handleGameRefresh());
    		   }
    	   }
    }
	*/
	private void setupGame(){
		 Logger.d(TAG,"setupGame game turn=" + this.game.getTurn());
		GameService.loadScoreboard(this, this.game);
	 	
		//if (!this.game.isCompleted()){
			this.fillGameState();
		//}
	 	
	 	this.setupButtons();
	 	this.setupFonts();

	}
	 private void checkFirstTimeStatus(){
		 //first check to see if this score has already been alerted (from local storage) 
		 
		 if (!PlayerService.checkFirstTimeGameSurfaceAlertAlreadyShown(this)) {
			 DialogManager.SetupAlert(this, this.getString(R.string.game_surface_first_time_alert_title), this.getString(R.string.game_surface_first_time_alert_message));
		 }
		 
	 }
	 
	public void callback(){
		this.checkFirstTimeStatus();
		
		
		//just in case the game got stuck before the autoplay could complete
	 	if (this.isGamePopulating && this.game.isActive() && this.game.getPlayerGames().get(1).isTurn()){
	 		spinner = new CustomProgressDialog(this);
			    spinner.setMessage(String.format(this.getString(R.string.progress_opponent_thinking), this.game.getOpponent().getName()));
			    spinner.show();
			
	//		GameService.autoPlay(this, this.game, this.gameSurfaceView.getTiles());
			
			this.autoplayTask = new AutoplayTask();
			this.autoplayTask.execute();
	 	}
	 	
	 	//prime the pump for opponent's turn
		if (this.isGamePopulating && this.game.isActive() && !this.hasPreAutoPlayRunThisTurn && this.game.getPlayerGames().get(0).isTurn()){
			this.placedResults.clear();
			this.preAutoplayTask = new PreAutoplayTask();
			this.preAutoplayTask.execute();
		}
	 	
	 	this.isGamePopulating = false;
	}
	
	public void onInitialRenderComplete(){
	//	this.spinner.dismiss();
	}
 
	 public void switchToRecall(){
		//by default recall button will be hidden, it will be switched with shuffle button when a letter is dropped on the board
		 context.runOnUiThread(new handleButtonSwitchRunnable(2));
	 }

	 public void switchToShuffle(){
		 context.runOnUiThread(new handleButtonSwitchRunnable(1));
	 }
	 
	 public void switchToPlay(){
			//by default play button will be hidden, it will be switched with skip button when a letter is dropped on the board
			 context.runOnUiThread(new handleButtonSwitchRunnable(3));
		 }

	public void switchToSkip(){
			 context.runOnUiThread(new handleButtonSwitchRunnable(4));
		 }
		 
	 
	 public void openAlertDialog(String title, String message){
		 DialogManager.SetupAlert(this.context, title, message);
	 }
	 
	 
	 private class handleButtonSwitchRunnable implements Runnable {
		 private int activeButton; //1 = shuffle, 2 = recall 	
		 
		 public handleButtonSwitchRunnable(int activeButton){
		 		this.activeButton = activeButton;
		 	}
		 
		    public void run() {
		    	if (game.isCompleted()){
		    		bRecall.setVisibility(View.GONE);
				 	bShuffle.setVisibility(View.GONE);
		    		bPlay.setVisibility(View.GONE);
				 	bSkip.setVisibility(View.GONE);
					bRecall.setVisibility(View.GONE);
		    	}
		    	else{
			    	switch (this.activeButton){
				    	case 1:
				    		bRecall.setVisibility(View.GONE);
						 	bShuffle.setVisibility(View.VISIBLE);
				    		break;
				    	case 2:
				    		bRecall.setVisibility(View.VISIBLE);
						 	bShuffle.setVisibility(View.GONE);
				    		break;
				    	case 3:
				    		bPlay.setVisibility(View.VISIBLE);
						 	bSkip.setVisibility(View.GONE);
				    		break;
				    	case 4:
				    		bPlay.setVisibility(View.GONE);
				    		bSkip.setVisibility(View.VISIBLE);
				    		break;
			    	}
		    	}
		    }
	  }
	 
	 public void setPointsView(int points){
		 this.currentPoints = points;
		 context.runOnUiThread(new handlePointsViewRunnable(points, 1));
	 }
	 
	 public void setPointsAfterPlayView(){
		 if (this.game.getStatus() == 1) {
			 context.runOnUiThread(new handlePointsViewRunnable(this.currentPoints, 2));
		 }
		 else {
			 //hide points view since the game is over
			 context.runOnUiThread(new handlePointsViewRunnable(0, 2));
		 }
	 }
	 
	 private class handlePointsViewRunnable implements Runnable {
		 private int points;   	
		 private int type; //1 = normal, 2 = afterPlay  	
		 
		 public handlePointsViewRunnable(int points, int type){
		 	this.points = points;
		 	this.type = type;
		 	}
		 
		 
		    public void run() {
		    	if (this.type == 1){
			    	if (points == 0){
			 			tvNumPoints.setVisibility(View.INVISIBLE);
			 		}
			 		else {
			 			tvNumPoints.setText(String.format(context.getString(R.string.scoreboard_num_points),points));
			 			tvNumPoints.setVisibility(View.VISIBLE);
			 			tvNumPoints.setTextColor(Color.parseColor(context.getString(R.color.scoreboard_text_color)));
			 		}
		    	}
		    	else { //last played points returned after a play 
		    		if (points == 0){
			 			tvNumPoints.setVisibility(View.INVISIBLE);
			 		}
			 		else {
			 			tvNumPoints.setText(String.format(context.getString(R.string.scoreboard_num_points_scored),points));
			 			tvNumPoints.setVisibility(View.VISIBLE);
			 			tvNumPoints.setTextColor(Color.parseColor(context.getString(R.color.scoreboard_text_last_played_color)));
			 		}
		    		
		    	}
		    /*	switch (this.activeButton){
			    	case 1:
			    		bRecall.setVisibility(View.GONE);
					 	bShuffle.setVisibility(View.VISIBLE);
			    		break;
			    	case 2:
			    		bRecall.setVisibility(View.VISIBLE);
					 	bShuffle.setVisibility(View.GONE);
			    		break;
		    	}
		    	*/
		    }
	  }
	 
	 private void checkGameStatus(){
	/*	
		 //completed
		 //first check to see if this score has already been alerted (from local storage) 
		 if (this.game.getStatus() == 3 || this.game.getStatus() == 4){
			 if (!GameService.checkGameAlertAlreadyShown(this, this.game.getId())) {
				 String message = this.game.getWinnerAlertText(this, this.contextPlayerGame);
			 
				 DialogManager.SetupAlert(this, this.getString(R.string.game_alert_game_over_title), message);
			 }
		 }
		 */
	 }
	 
	 
	 
	 private void setupButtons(){
		this.isButtonActive = false;
		buttonsLoaded = true;
		//Logger.d(TAG,  "setupButtons called");

		this.bRecall = (Button) findViewById(R.id.bRecall);
		this.bPlay = (Button) findViewById(R.id.bPlay);
		this.bSkip = (Button) findViewById(R.id.bSkip);
		this.bShuffle = (Button) findViewById(R.id.bShuffle);
		 
		Button bHopperPeek = (Button) findViewById(R.id.bHopperPeek);
		Button bSwap = (Button) findViewById(R.id.bSwap);
		Button bPlayedWords = (Button) findViewById(R.id.bPlayedWords);
		Button bCancel = (Button) findViewById(R.id.bCancel);
		Button bResign = (Button) findViewById(R.id.bResign);
	 	this.bShuffle.setOnClickListener(this);
	 
	 	bHopperPeek.setOnClickListener(this);
	 	this.bSkip.setOnClickListener(this);
	 	bPlayedWords.setOnClickListener(this);
	 	this.bRecall.setOnClickListener(this);
	 	this.bPlay.setOnClickListener(this);
	 	
	 	String btnTextColor = this.game.isContextPlayerTurn() ? this.getString(R.color.button_text_color_on) : this.getString(R.color.button_text_color_off);
	 	
	 	this.bPlay.setTextColor(Color.parseColor(btnTextColor));
	 	this.bSkip.setTextColor(Color.parseColor(btnTextColor));
	 	bCancel.setTextColor(Color.parseColor(btnTextColor));
	 	bResign.setTextColor(Color.parseColor(btnTextColor));
	 	bSwap.setTextColor(Color.parseColor(btnTextColor));
	 	bPlayedWords.setTextColor(Color.parseColor(this.getString(R.color.button_text_color_on)));
	 	
	 	//bRecall.setVisibility(View.GONE);
	 	bShuffle.setVisibility(View.VISIBLE);
	 	
	 	bPlay.setVisibility(View.GONE);
	 	bSkip.setVisibility(View.VISIBLE);
	 	
	 	bPlay.setClickable(this.game.isContextPlayerTurn());
	 	
//	 	Logger.d(TAG, "getNumLettersLeft=" + this.game.getHopper().size());
	 	
	 	if (this.game.getPlayedWords().size() > 0){
	 		bPlayedWords.setOnClickListener(this);
	 		bPlayedWords.setClickable(true);	 		
	 	}
	 	else{
	 		bPlayedWords.setClickable(false);
	 		bPlayedWords.setTextColor(Color.parseColor(this.getString(R.color.button_text_color_off)));	 		
	 	}
	  	if (this.game.getHopper().size() > 0){
	 		bSwap.setOnClickListener(this);
	 		bSwap.setClickable(this.game.isContextPlayerTurn());
 		//	Logger.d(TAG, "bSwap clickable");
	  	}
 		else{
 			//Logger.d(TAG, "bSwap NOT clickable");
 			bSwap.setClickable(false);
 			bSwap.setTextColor(Color.parseColor(this.getString(R.color.button_text_color_off)));
 		}
	  	
	  	//based on store purchase
	  	bHopperPeek.setClickable(true);
	  	
	 	bSkip.setClickable(this.game.isContextPlayerTurn());
	 	bResign.setClickable(this.game.isContextPlayerTurn());

	 	
	 	//by default recall button will be hidden, it will be switched with shuffle button when a letter is dropped on the board
	 	this.bRecall.setVisibility(View.GONE); 
	 	
	 	//set cancel button area mode:
	 	//if it's the first play of the game by starting player, it should be "CANCEL" mode
	 	//if it's not the first play of the game, it should be in "RESIGN" mode
	 	
	 	Logger.d(TAG, "setupButtons game status=" + this.game.getStatus());
	 	
	 	if (this.game.getStatus() == 1) { //active

		 	//the starting player gets one chance (one turn) to cancel
		 	if (this.game.getTurn() == 1){
		 		bCancel.setOnClickListener(this);	
		 		bResign.setVisibility(View.GONE);
		 	}
		 	else{
	 			//else we are past each opponents first turn, therefore show the resign button 
	 			bCancel.setVisibility(View.GONE);	
		 		bResign.setOnClickListener(this);
		 		bResign.setVisibility(View.VISIBLE);
		 		bResign.setClickable(this.game.isContextPlayerTurn());
		 	}
		 	

	 	}
	 	else if(this.game.isCompleted()){  
	 		bHopperPeek.setVisibility(View.GONE);
	 		bRecall.setVisibility(View.GONE);
	 		bSwap.setVisibility(View.GONE);
	 		bSkip.setVisibility(View.GONE);
	 		bPlay.setVisibility(View.GONE);
	 		bResign.setVisibility(View.GONE);
	 		bCancel.setVisibility(View.GONE);
	 		bShuffle.setVisibility(View.GONE);
	 		if (this.game.getPlayedWords().size() == 0){ 
	 			bPlayedWords.setVisibility(View.GONE);
	 		}
	 		else {
	 			//LinearLayout llButtons = (LinearLayout)this.findViewById(R.id.llButtons);
	 			
	 			// LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams)llButtons.getLayoutParams();
	 			 LinearLayout.LayoutParams buttonParams = (LinearLayout.LayoutParams)bPlayedWords.getLayoutParams();
	 			
	 		    // buttonParams.width = R.integer.gameboard_lookup_words_width;
	 		   //  buttonParams.height = R.integer.gameboard_lookup_words_height;
	 		   //  buttonParams.weight = 0.5f;
	 		     buttonParams.setMargins(30, 0, 30, 0);
	 		    

	 		   
	 			 bPlayedWords.setLayoutParams(buttonParams);
	 			 //bPlayedWords.setLayoutParams(new LinearLayout.LayoutParams(R.integer.gameboard_lookup_words_width, R.integer.gameboard_lookup_words_height));
	 		}
	 	}
	 	
	 //	if (!StoreService.isHopperPeekPurchased() && PlayerService.getRemainingFreeUsesHopperPeek() == 0){
	 //	 	bHopperPeek.setVisibility(View.GONE);
	 //	}

	}
	    
	    
	    
	 private void fillGameState(){
		 this.gameState = GameStateService.getGameState(context, this.game.getId());  
		 
		 //there is some kind of issue that causes game state to occasionallget get out of whack
		 //let's check that here and if it in a whacked state, let's just clear it
		 boolean gameStateWhacked = false;
		  
		 if (game.getHopper().size() > 0){
			 for (GameStateLocation location : this.gameState.getLocations()){
				 Logger.d(TAG, "fillGameState location letter=" + location.getLetter() + " --getBoardLocation=" + location.getBoardLocation() + " getTrayLocation=" + location.getTrayLocation());

				 if (location.getTrayLocation() < 0 && location.getBoardLocation() < 0){
					 gameStateWhacked = true;
					 Logger.d(TAG, "fillGameState location traylocation=" + location.getTrayLocation());
				 }
				 if (location.getBoardLocation() > -1 && location.getLetter().equals("")){
					 gameStateWhacked = true;
					 Logger.d(TAG, "fillGameState location getBoardLocation=" + location.getBoardLocation());
				 }
				 if (location.getBoardLocation() == -1 && location.getLetter().equals("") && location.getTrayLocation() > -1){
					 gameStateWhacked = true;
					 Logger.d(TAG, "fillGameState location -getBoardLocation=" + location.getBoardLocation() + " getTrayLocation=" + location.getTrayLocation());
				 }
			 } 
			 
		 }
		 
		 if (gameStateWhacked) {  this.gameState = GameStateService.clearGameState(this.game.getId()); } 
		 //if this.turn != gameState.turn, clearGameState
		 
		 Logger.d(TAG, "filleGameState trayVersion=" + this.game.getPlayerGames().get(0).getTrayVersion());
		 
		 //if the game state is dated, clear it out
		 if (this.game.getPlayerGames().get(0).getTrayVersion() != this.gameState.getTrayVersion()){
			 this.gameState = GameStateService.clearGameState(this.game.getId());
		 }
		 
		for (PlayedTile tile : this.game.getPlayedTiles()){
			if (tile.getBoardPosition() == 196){
				 Logger.d(TAG, "filleGameState 196 is a PLAYED TILE");
					
			}	
		}
		
		// Logger.d(TAG, "filleGameState numTrayTiles=" + this.game.getPlayedTiles().size());
		// this.gameState.setTrayVersion(this.game.getContextPlayerTrayVersion(this.player));
		 
		 //load played tiles into game state
		 this.gameState.setPlayedTiles(this.game.getPlayedTiles());
		 
		 //locations are used to keep track of the tray tiles as they are placed on the board or tray
		 if (this.gameState.getLocations().size() == 0){

 //reset the game tiles based on the locally saved state (as long as the tray tiles have not been updated on the server)
			 this.gameState.setTrayVersion(this.game.getPlayerGames().get(0).getTrayVersion());
			 
			 int numTrayTiles = this.game.getPlayerGames().get(0).getTrayLetters().size();
			 
			 for(int x = 0; x < 7; x++){
				// TrayTile trayTile = new TrayTile();
				// trayTile.setIndex(x);
				// trayTile.setLetter(this.contextPlayerGame.getTrayLetters().size() > x ? this.contextPlayerGame.getTrayLetters().get(x) : "");
				 this.gameState.addTrayLocation(x, numTrayTiles > x ? this.game.getPlayerGames().get(0).getTrayLetters().get(x) : "");
				// this.gameState.getLocations().get(x).setTrayLocation(x);
				// this.gameState.getLocations().get(x).setLetter(this.contextPlayerGame.getTrayLetters().size() > x ? this.contextPlayerGame.getTrayLetters().get(x) : "");
			 }
			 //this.gameState.setTrayTiles(this.contextPlayerGame.getTrayLetters());
		 }
	 }
	 
 
//	public RelativeLayout getScoreboard() {
//		return scoreboard;
//	}

//	public void setScoreboard(RelativeLayout scoreboard) {
//		this.scoreboard = scoreboard;
//	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onDestroy called");
		
		this.gameSurfaceView.onDestroy();
	//this.appContext = null;
		
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		
		
		if (this.isChartBoostActive){
			
			this.cb.onDestroy(this);
			this.cb = null;
		}
		
		super.onDestroy();
		//doUnbindService();
	 
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onStop called");
		if (this.getGameState() != null){
			GameStateService.setGameState(this.getGameState());
		}
		
	///	this.stopTimer();
	//	this.stopRunawayAdTimer();
		this.gameSurfaceView.onStop();
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		 this.dismissHopperPeekDialog();
	//	try{
	//		this.spinner.dismiss();
	//	}
	//	catch(Exception e){}
		super.onStop();
		 EasyTracker.getInstance().activityStop(this);
		if (this.isChartBoostActive){
			
			if (this.cb.hasCachedInterstitial()){ this.cb.clearCache(); }
			this.cb.onStop(this);
		 
			//this.cb = n//ull;
		}
		
	/*	if (this.isRevMobActive){
		//	this.revmob.
			this.revMobFullScreen = null;
			this.revmobListener = null;
			this.revmob = null;		
		}
		*/
		if (this.gcmReceiver != null) {
			this.unregisterReceiver(this.gcmReceiver);
			this.gcmReceiver = null;
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onPause called");
		//Playtomic.Log().freeze();
		super.onPause();
 
		Logger.d(TAG, "onPause - stop timer about to be called");
		if (this.getGameState() != null){
			GameStateService.setGameState(this.getGameState());
		}
		
		if (this.preAutoplayTask != null){
			Logger.d(TAG, "onPause preAutoplayTask running");
			this.hasPreAutoPlayRunThisTurn = false;
    		//this.preAutoplayTask.cancel(true);
    		this.preAutoplayTask = null;
    	}
		
		Logger.d(TAG, "onPause preAutoplayTask stopped");

	///	this.stopTimer();
		//this.stopRunawayAdTimer();
		this.dismissHopperPeekDialog();

		
	//	try{
	//		this.spinner.dismiss();
	//	}
	//	catch(Exception e){}
		this.gameSurfaceView.onPause();
		
		if (this.spinner != null){
			this.spinner.dismiss();
			this.spinner = null;
		}
		
		
	//	if (this.wordLoaderThread != null){
	//		this.wordLoaderThread.interrupt();
	//		this.wordLoaderThread = null;
	//	}
		
	}

	@Override
	protected void onResume() {
		try{
			// TODO Auto-generated method stub
			Logger.d(TAG, "onResume called");
		//	this.captureTime("onResume starting");
		//	Playtomic.Log().unfreeze();
			super.onResume();
		//	this.captureTime("unfreezeButtons starting");
	
			GameActionType lastAction = this.postTurnAction;
			if (lastAction == null){
				lastAction = GameActionType.NO_TRANSLATION;
			}
			
			switch (lastAction){
				case CANCEL_GAME:
				case DECLINE_GAME:
				case RESIGN:
					//do nothing for these options
					break;
				default:
					this.unfreezeButtons();
	    		 	Logger.d(TAG, "unfreeze onResume");
					//	this.captureTime("gameSurfaceview resume starting");
					this.gameSurfaceView.onResume();
					//	this.captureTime("setup timer_ starting");
	  
			}
		}
		catch (Exception e){
			Logger.d(TAG, "onResume error=" + e.getMessage());
			
			//if anything happens here let's just restart the activity from scratch
			//occasionally a null error occurs, so let's just start over if that happens
			Intent intent = new Intent(this, com.riotapps.word.GameSurface.class);
    		this.startActivity(intent); 
    		this.finish();
		}

	}

 
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//override back button in case user just started game. this will make sure they don;t back through 
		//all of the pick opponent activities
 
		if (this.isChartBoostActive && this.cb.onBackPressed())
			// If a Chartboost view exists, close it and return
			return;
		else if (this.game.isCompleted() && !this.isCompletedThisSession){
			//if game is completed, just go back to whatever activity is in the stack
			this.gameSurfaceView.onStop();
			this.game = null;
			this.player = null;
			this.gameState = null;
			
			super.onBackPressed();
		}
		else {
			this.handleBack();
		}
	}
    private void setupMenu(){
      	
  	  this.popupMenu = new PopupMenu(this, findViewById(R.id.options));

  	  MenuUtils.fillMenu(this, this.popupMenu);
      this.popupMenu.setOnMenuItemClickListener(this);
      findViewById(R.id.options).setOnClickListener(this);
  }

	
    @Override
    public boolean onMenuItemClick(MenuItem item) {
    	Logger.d(TAG, "onMenuItemClick");
    	//probably need to stop thread here
    	return MenuUtils.handleMenuClick(this, item.getItemId());
    }

	
		private void handleBack(){
		 
				//this.spinner = new CustomProgressDialog(this);
				//this.spinner.setMessage(this.getString(R.string.progress_saving));
				//this.spinner.show();
				
				if (this.getGameState() != null && !this.game.isCompleted()){
					GameStateService.setGameState(this.getGameState());
				}
				
				boolean backToMain = false;
				if (this.game.isCompleted()) {backToMain = true;}
				
				if (this.preAutoplayTask != null){
		    		this.preAutoplayTask = null;
		    	}
				
				this.gameSurfaceView.onStop();
				this.game = null;
				this.player = null;
				this.gameState = null;
				
				if (backToMain){
					//in this case the game was completed in this session so lets just send player to main activity
					Intent intent = new Intent(this, com.riotapps.word.Main.class);
		    		this.startActivity(intent); 
				}	
				else{
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(startMain);
					this.finish(); 	
				}
	 	
		}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		
		Logger.d(TAG, "onActivityResult called requestCode=" + requestCode + "  resultCode=" + resultCode);
		 switch(requestCode) { 
		    case (1) :   
		      if (resultCode == Activity.RESULT_OK) { 
		    	  boolean hasGameBeenUpdated = intent.getBooleanExtra(Constants.EXTRA_IS_GAME_UPDATED, false);
		    	  if (hasGameBeenUpdated){
			    	  this.game = GameService.getGame(game.getId());
			    	  this.isGameReloaded = true;
			    	  this.setupGame();
					  this.checkGameStatus();
					  this.gameSurfaceView.resetGameAfterRefresh();
		    	  }
		      } 
		      break;

	 		}
		 

	}
	
	public void closeCustomDialog(int resultCode){
		 switch(resultCode) { 
		   case Constants.RETURN_CODE_HOPPER_PEEK_CLOSE:
			   this.unfreezeButtons();
			   this.dismissHopperPeekDialog();
			   break; 
		   
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CLICKED:
			 	this.dismissCustomDialog();
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_SKIP_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 			this.handleGameSkipOnClick(); 
			 break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CLOSE_CLICKED:
			    this.dismissCustomDialog();
	 	 		this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_SKIP_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			this.unfreezeButtons();
	 			break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CANCEL_CLICKED:
			    this.dismissCustomDialog();
				this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_SKIP_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
				
				this.unfreezeButtons();
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CLICKED:
			   this.dismissCustomDialog();
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_PLAY_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 			this.handleGamePlayOnClick(this.placedResult);
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CLOSE_CLICKED:
			    this.dismissCustomDialog();
	 	 		this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_PLAY_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			this.unfreezeButtons();
	 			break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CANCEL_CLICKED:
			    this.dismissCustomDialog();
				this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_PLAY_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
				
				this.unfreezeButtons();
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_OK_CLICKED:
			    this.dismissCustomDialog(); 
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_RESIGN_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 			this.handleGameResignOnClick();
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_CLOSE_CLICKED:
			    this.dismissCustomDialog(); 
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_RESIGN_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 			this.unfreezeButtons();
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_CANCEL_CLICKED:	
			    this.dismissCustomDialog(); 

			    this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_RESIGN_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);

			    this.unfreezeButtons();
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_OK_CLICKED:
			    this.dismissCustomDialog(); 
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_CANCEL_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 			this.handleGameCancelOnClick();
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CLOSE_CLICKED:
			    this.dismissCustomDialog(); 
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_CANCEL_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 			this.unfreezeButtons();
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CANCEL_CLICKED:	
			    this.dismissCustomDialog(); 

			    this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_CANCEL_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			    
			    this.unfreezeButtons();

	 		}
		 
	}

	private void dismissHopperPeekDialog(){
		if (this.hopperPeekdialog != null){
			if (this.hopperPeekdialog.isShowing()){
				this.hopperPeekdialog.dismiss();
			}
			this.hopperPeekdialog = null;
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.
		Logger.d(TAG, "onStart isChartBoostActive=" + isChartBoostActive + " isRevMobActive=" + isRevMobActive);
		//Toast.makeText(this, "onstart cb=" + isChartBoostActive + " rm="  + isRevMobActive, Toast.LENGTH_SHORT).show();
		
		if (this.isChartBoostActive) {
			if (this.cb == null) {
				this.setupChartBoost();	
			}
			this.cb.onStart(this);
		}
		/*
		if (this.isRevMobActive){
			this.revmobListener = new RevMobAdsListener() {
	            @Override
	            public void onRevMobAdReceived() {
	                Logger.d(TAG, "onRevMobAdReceived");
	              //  Toast.makeText(context, "onRevMobAdReceived", 1000);
	              //  handlePostTurnFinalAction(postTurnAction);
	            }
	 
	            @Override
	            public void onRevMobAdNotReceived(String message) {
	            	 Logger.d(TAG, "onRevMobAdNotReceived");
	            	// handlePostTurnFinalAction(postTurnAction);
	            }
	 
	            @Override
	            public void onRevMobAdDismiss() {
	            	 Logger.d(TAG, "onRevMobAdDismiss");
                     //Toast.makeText(GameSurface.this, "onRevMobAdDismiss.", Toast.LENGTH_SHORT).show();

	            	 handlePostAdServer();
	            	 //handlePostTurnFinalAction(postTurnAction);
	            }
	 
	            @Override
	            public void onRevMobAdClicked() {
	            	GameSurface.this.runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {
	                    	Logger.d(TAG, "onRevMobAdClicked");
	                    	handlePostAdServer();
	                    	//handlePostTurnFinalAction(postTurnAction);
	                       // Toast.makeText(GameSurface.this, "Click intercepted.", Toast.LENGTH_SHORT).show();
	                    }
	                });
	            }
	
				@Override
				public void onRevMobAdDisplayed() {
					 Logger.d(TAG, "onRevMobAdDisplayed");
					 handlePostAdServer();
					 //handlePostTurnFinalAction(postTurnAction);
					
				}
	        };
	        if (this.revmob == null){
	        	this.revmob = RevMob.start(this, this.getString(R.string.rev_mob_app_id));	
	        }
	        this.revMobFullScreen = revmob.createFullscreen(this, this.getString(R.string.rev_mob_main_between_play), this.revmobListener);
		}
		*/
		//this.doBindService();
		
	}
	
	@Override
	protected void onRestart() {
		//Log.w(TAG, "onRestart called");
		super.onRestart();
		this.gameSurfaceView.onRestart(); 
 
		Logger.d(TAG, "onRestart buttonsLoaded=" + buttonsLoaded);  
		
		if (this.isRestartFromInterstitialAd){
			Logger.d(TAG, "onRestart from InterstitialAd");  

			this.handleInterstitialAd();
			//this.handlePostTurnFinalAction(this.postTurnAction);
			this.isRestartFromInterstitialAd = false;
		}
		else {
			//Intent i = getIntent();
			
			//boolean hasGameBeenUpdated = i.getBooleanExtra(Constants.EXTRA_IS_GAME_UPDATED, false);
			/*
			int previousTurn = this.game.getTurn();
			Game previousGame = GameService.getGameFromLocal(game.getId());
			
			if (previousTurn < previousGame.getTurn()){
				//if its not the context user's turn and he goes to chat activity and leaves a chat text after the opponent
				//takes his turn, but before coming back to game surface, this logic handles that
				this.setupGame();
				this.checkGameStatus();
				this.gameSurfaceView.resetGameAfterRefresh();
			}
			else 
			*/
			if (buttonsLoaded){ 
				//reset buttons
				Logger.d(TAG, "onRestart game being fetched from local");
				if (!this.isGameReloaded) {this.game = GameService.getGame(game.getId());}
				Logger.d(TAG, "onRestart game turn=" + game.getTurn());
				this.fillGameState();
				this.setupButtons();
				this.gameSurfaceView.setInitialButtonStates();
				this.isButtonActive = false;
			}
			
			//reprime the pump
			try{
				if (this.preAutoplayTask == null && !hasPreAutoPlayRunThisTurn && this.game.isActive() && this.game.getPlayerGames().get(0).isTurn()){
					this.placedResults.clear();
					this.preAutoplayTask = new PreAutoplayTask();
					this.preAutoplayTask.execute();
				}
			}
			catch(Exception e){
				Logger.d(TAG, "onrestart preautoplay e=" + e.getMessage());
				
			}
			
			///this.setupTimer(Constants.GAME_SURFACE_CHECK_START_AFTER_RESTART_IN_MILLISECONDS);
		}
	}

	
	 @Override 
	    public void onClick(View v) {
		 Logger.d(TAG, "onclick this.isButtonActive=" + this.isButtonActive);
	    	Intent intent;
	    	if (this.isButtonActive == true){
	    		//skip processing to stop dialogs from doubling up
	    	}
	    	else {		
		    	switch(v.getId()){  
			    	case R.id.options:
				 		popupMenu.show();
				 		break;
			        case R.id.bShuffle: 
			        	this.isButtonActive = true;
			        	Logger.d(TAG, "bShuffle clicked");
			        	 this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
				        			Constants.TRACKER_LABEL_SHUFFLE, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			        	this.gameSurfaceView.shuffleTray();
			        	this.unfreezeButtons();
		    		 	Logger.d(TAG, "unfreeze shuffle");
			        	break;
			        case R.id.bHopperPeek:  
			        	this.isButtonActive = true;
			        	this.hopperPeekdialog = new HopperPeekDialog(this, this.game.getId());
			       	 
				    	this.hopperPeekdialog.show();
			        	//intent = new Intent(this, HopperPeek.class);
			        	//intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
						//startActivity(intent);
					 
						break;
			        case R.id.bPlayedWords:  
			        	this.isButtonActive = true;
			        	intent = new Intent(this, GameHistory.class);
			        	intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
						startActivity(intent);
					 
						break;
			   
			        case R.id.bRecall:
			        	this.isButtonActive = true;
			        	this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_RECALL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			        	this.gameSurfaceView.recallLetters();
			        	this.unfreezeButtons();
		    		 	Logger.d(TAG, "unfreeze recall");
						break;
			        case R.id.bPlay:
			        	this.isButtonActive = true;
			        	this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_PLAY_INITIAL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			        	//this.loadPlaySpinner();
			        	v.post(new Runnable() {
		                    public void run() {
		                        // Your job here
		                     gameSurfaceView.onPlayClick();
		                    }
		                });
			        	//this.gameSurfaceView.onPlayClick();
						break;
			       case R.id.bSkip:
			    	   this.isButtonActive = true;
			    	   this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_SKIP_INITIAL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			        	this.gameSurfaceView.onPlayClick();
						break;
			       case R.id.bSwap:
			    	   this.isButtonActive = true;
			    	   this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_SWAP_INITIAL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			        	this.onSwapClick();
						break;
			   
			        case R.id.bResign:  
			        	this.isButtonActive = true;
			        	 this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
				        			Constants.TRACKER_LABEL_RESIGN_INITIAL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			        	this.handleResign();
						break;
			        case R.id.bCancel:  
			        	this.isButtonActive = true;
			        	this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_CANCEL_INITIAL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			        	this.handleCancel();
						break;
		    	}
	    	}	
	 }
	
	 /*private void loadPlaySpinner(){
		// Toast.makeText(this, "checking spinner loading", Toast.LENGTH_LONG).show();
			this.spinner = new CustomProgressDialog(this);
			this.spinner.setMessage(this.getString(R.string.progress_checking));
			this.spinner.show();
	 }
	 
	 public void unloadPlaySpinner(){
		 if (spinner != null){
				spinner.dismiss();
			}
	 }
	 */
	 public void onFinishPlayNoErrors(final PlacedResult placedResult) {
		    runOnUiThread(new Runnable() {
		        public void run() {
		            // use data here
		        //	unloadPlaySpinner();
		            postPlayNoErrors(placedResult);
		        }
		    });
		}
	 
	 public void onFinishPlayErrors(final String message, final int code) {
		    runOnUiThread(new Runnable() {
		        public void run() {
		            // use data here
		        	//unloadPlaySpinner();
					
		        	//record error code from rule check
		        	trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			String.format(Constants.TRACKER_LABEL_PLAY_WITH_ERRORS, code), Constants.TRACKER_SINGLE_VALUE);
		        	
		        	openAlertDialog(context.getString(R.string.sorry), message);
					unfreezeButtons();
	    		 	Logger.d(TAG, "unfreeze onFinishPlayErrors");
		        }
		    });
		}


	    private void handleCancel(){
	    	this.customDialog = new CustomButtonDialog(this, 
	    			this.getString(R.string.game_surface_cancel_game_confirmation_title), 
	    			this.getString(R.string.game_surface_cancel_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no),
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_OK_CLICKED,
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CANCEL_CLICKED,
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CLOSE_CLICKED);
	    	
	    	
	    	this.customDialog.show();	
	    }
	    
	    private void handleGameCancelOnClick(){
	    	//stop thread first
	    	this.gameSurfaceView.onStop();
	     
	    	GameService.cancel(this.player, this.game);
	    	GameStateService.removeGameState(this.game.getId());
    		Intent intent = new Intent(this, com.riotapps.word.Main.class);
    		this.startActivity(intent); 
        
	    	//finish this activity so it is removed from history
    		this.finish();
	    	
	    }

	    private void handleResign(){
	    	this.customDialog = new CustomButtonDialog(this, 
	    			this.getString(R.string.game_surface_resign_game_confirmation_title), 
	    			this.getString(R.string.game_surface_resign_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no),
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_OK_CLICKED,
					Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_CANCEL_CLICKED,
					Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_CLOSE_CLICKED);
	    	
	    	
			 
	    	this.customDialog.show();	
	    }

	    
	    private void handleGameResignOnClick(){
	    	//stop thread first
	    	//this.gameSurfaceView.onStop();
	    	//try { 
				 
	    		GameService.resign(false, this.game);
	    		
	    		this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_CONCEDE, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), Math.abs(this.game.getPlayerGames().get(0).getScore() - this.game.getPlayerGames().get(1).getScore()));
	    		
	    		this.isCompletedThisSession = true;
	    		this.postTurnTitle = context.getString(R.string.game_over);
				this.postTurnMessage = 	game.getLastActionText(context); 
	    		//DialogManager.SetupAlert(context, context.getString(R.string.game_over), game.getLastActionText(context));
				setupButtons();
				setupGame();
				
				gameSurfaceView.resetGameAfterRefresh(); //resetGameAfterPlay();
				GameStateService.removeGameState(game.getId());
				this.setupMenu(); //in case this is the first completed game, it will add that option to menu
				//??this.gameSurfaceView.stopThreadLoop();
	    		//create alert for resigned game that when clicked, sends user back to main
			 
				handleInterstitialAd();
				
				
				
		//	} catch (DesignByContractException e) {
		//		 
		//		DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
		//	}
	    }
	    
	    private void dismissCustomDialog(){
			if (this.customDialog != null){
				customDialog.dismiss();
				customDialog = null;
			}
		}
	    public void postPlayNoErrors(final PlacedResult placedResult ){
			this.setPointsView(placedResult.getTotalPoints());
			
			//this.parent.handleGamePlayOnClick(placedResult);
			
			//if (placedResult.getPlacedTiles().size() == 800){
			if (placedResult.getPlacedTiles().size() == 0){
				
					//user is skipping this turn
					this.customDialog = new CustomButtonDialog(this, 
							this.getString(R.string.game_play_skip_title), 
							this.getString(R.string.game_play_skip_confirmation_text),
							this.getString(R.string.yes),
							this.getString(R.string.no), 
							Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CLICKED,
							Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CANCEL_CLICKED,
							Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CLOSE_CLICKED);
		 	    	
					this.customDialog.show();
				}
				else{
					//loop through placed words and show confirmation messages 
					this.placedResult = placedResult;
					this.customDialog = new CustomButtonDialog(this, 
							this.getString(R.string.game_play_title), 
							GameService.getPlacedWordsMessage(this, placedResult.getPlacedWords()),
							this.getString(R.string.yes),
							this.getString(R.string.no), 
							Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CLICKED,
							Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CANCEL_CLICKED,
							Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CLOSE_CLICKED,
							R.layout.played_word_dialog);
		 	    	
					this.customDialog.show();
	
				}
			//}
		}
	    
	    public void handleGamePlayOnClick(PlacedResult placedResult){
	    	//stop thread first
	    	
	    	//try{
	    	//DialogManager.SetupAlert(context, "played", "clicked");
 	    	//this.gameSurfaceView.stopThreadLoop();  //does thread loop need to still be stopped
		    	
	    		//just in case the primer is running, let's stop it
	    		if (this.preAutoplayTask != null){
		    		this.placedResults.clear();
		    		this.hasPreAutoPlayRunThisTurn = false;
		    		this.preAutoplayTask.cancel(true);
		    		this.preAutoplayTask = null;
		    	}
	     
	    		GameService.play(false, game, placedResult);

		       	this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_PLAY, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), this.game.getLastTurnPoints());

	    		gameState = GameStateService.clearGameState(game.getId());
			//} catch (DesignByContractException e) {
				if (game.isCompleted()) 
				{
					this.isCompletedThisSession = true;
					//display
					this.postTurnTitle = context.getString(R.string.game_over);
					this.postTurnMessage = 	game.getLastActionText(context); 
		
					//DialogManager.SetupAlert(context, context.getString(R.string.game_over), game.getLastActionText(context));
					setupButtons();
					setupGame();
		 			gameSurfaceView.resetGameAfterRefresh(); //resetGameAfterPlay();

					GameStateService.removeGameState(game.getId());
					this.setupMenu(); //in case this is the first completed game, it will add that option to menu
					//??this.gameSurfaceView.stopThreadLoop();
					handleInterstitialAd();
				}
				else{
					// show player his score, then kick off auto play
				 	//this.lastPlayerActionBeforeAutoplay = game.getLastActionText(context);
					
					Logger.d(TAG, "handleGamePlayOnClick about to call autoplay");
					
					spinner = new CustomProgressDialog(this);
	 			    spinner.setMessage(String.format(this.getString(R.string.progress_opponent_thinking), this.game.getOpponent().getName()));
	 			    spinner.show();
					
			//		GameService.autoPlay(this, this.game, this.gameSurfaceView.getTiles());
					
					this.autoplayTask = new AutoplayTask();
					this.autoplayTask.execute();
					
					/*
					 String opponentAction = game.getLastActionText(context);

					this.postTurnTitle = context.getString(R.string.post_turn_title_with_auto_play);
					this.postTurnMessage = 	String.format(this.getString(R.string.post_turn_message_with_auto_play), playerAction, opponentAction); 
		
					//DialogManager.SetupAlert(context, context.getString(R.string.post_turn_title_with_auto_play), String.format(this.getString(R.string.post_turn_message_with_auto_play), playerAction, opponentAction));
					setupButtons();

					setupGame();
		 			gameSurfaceView.resetGameAfterRefresh(); //resetGameAfterPlay();
		 			setPointsAfterPlayView();
		 			
					if (game.isCompleted()) {
						this.isCompletedThisSession = true;
						//perhaps replace play, skip
						GameStateService.removeGameState(game.getId());
						this.setupMenu(); //in case this is the first completed game, it will add that option to menu
						//??this.gameSurfaceView.stopThreadLoop();
					}
					*/
				}    	
				
				//handleInterstitialAd();
				//DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());
				//this.unfreezeButtons();
			//}
			 
	    }
	    
	    public void handleGameSkipOnClick(){
	    	//stop thread first
	    	
	    	//try{
	    	//DialogManager.SetupAlert(context, "played", "clicked");
 	    	//this.gameSurfaceView.stopThreadLoop();  //does thread loop need to still be stopped
	    	 
	    		GameService.skip(false, game);
	    		gameState = GameStateService.clearGameState(game.getId());
	            
	    		this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_SKIP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), (int)Constants.TRACKER_SINGLE_VALUE);
		    	 
			//} catch (DesignByContractException e) {
				if (game.isCompleted()) 
				{
					
	 			    
					this.isCompletedThisSession = true;
					//display
					this.postTurnTitle = context.getString(R.string.game_over);
					this.postTurnMessage = 	game.getLastActionText(context); 
					//DialogManager.SetupAlert(context, context.getString(R.string.game_over), game.getLastActionText(context));
					setupButtons();
					setupGame();
		 			gameSurfaceView.resetGameAfterRefresh(); //resetGameAfterPlay();

					GameStateService.removeGameState(game.getId());
					this.setupMenu(); //in case this is the first completed game, it will add that option to menu
					//???this.gameSurfaceView.stopThreadLoop();
					handleInterstitialAd();
				}
				else{
					// show player his score, then kick off auto play
					//this.lastPlayerActionBeforeAutoplay = game.getLastActionText(context);
					
					spinner = new CustomProgressDialog(this);
	 			    spinner.setMessage(String.format(this.getString(R.string.progress_opponent_thinking), this.game.getOpponent().getName()));
	 			    spinner.show();
	 			    
					//GameService.autoPlay(this, this.game, this.gameSurfaceView.getTiles());
					this.autoplayTask = new AutoplayTask();
					this.autoplayTask.execute();
/*
					String opponentAction = game.getLastActionText(context);
					
					this.postTurnTitle = context.getString(R.string.post_turn_title_with_auto_play);
					this.postTurnMessage = 	String.format(this.getString(R.string.post_turn_message_with_auto_play), playerAction, opponentAction); 
		
					//DialogManager.SetupAlert(context, context.getString(R.string.post_turn_title_with_auto_play), String.format(this.getString(R.string.post_turn_message_with_auto_play), playerAction, opponentAction));					
					setupButtons();
					setupGame();
		 			gameSurfaceView.resetGameAfterRefresh(); //resetGameAfterPlay();
		 			setPointsAfterPlayView();
					
					if (game.isCompleted()) {
						this.isCompletedThisSession = true;
						//perhaps replace play, skip
						GameStateService.removeGameState(game.getId());
						this.setupMenu(); //in case this is the first completed game, it will add that option to menu
						//??this.gameSurfaceView.stopThreadLoop();
					}
					*/
				}
			//	handleInterstitialAd();
			 
			//}
			 
	    }
	    
	    private void onSwapClick(){
	    	final SwapDialog dialog = new SwapDialog(context, this.game.getPlayerGames().get(0).getTrayLetters());
	    	dialog.show();
	    }
	    
	    public void handleGameSwapOnClick(List<String> swappedLetters){
	    	//stop thread first
	    	
	    	//DialogManager.SetupAlert(context, "played", "clicked");
 	    	//this.gameSurfaceView.stopThreadLoop();
	    //	try { 
	    		GameService.swap(false, game, swappedLetters);
	    		gameState = GameStateService.clearGameState(game.getId());
	    		
	    		//String playerAction = game.getLastActionText(context);

	    		if (this.game.getLastAction().equals(LastAction.ONE_LETTER_SWAPPED)){
		    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 1);
		    	 }
		    	 else if (this.game.getLastAction().equals(LastAction.TWO_LETTERS_SWAPPED)){
		    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 2);
		    	 }
		    	 else if (this.game.getLastAction().equals(LastAction.THREE_LETTERS_SWAPPED)){
		    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 3);
		    	 }
		    	 else if (this.game.getLastAction().equals(LastAction.FOUR_LETTERS_SWAPPED)){
		    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 4);
		    	 }
		    	 else if (this.game.getLastAction().equals(LastAction.FIVE_LETTERS_SWAPPED)){
		    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 5);
		    	 }
		    	 else if (this.game.getLastAction().equals(LastAction.SIX_LETTERS_SWAPPED)){
		    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 6);
		    	 }
		    	 else if (this.game.getLastAction().equals(LastAction.SEVEN_LETTERS_SWAPPED)){
		    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_PLAYER_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 7);
		    	 }
	    		
	    		spinner = new CustomProgressDialog(this);
 			    spinner.setMessage(String.format(this.getString(R.string.progress_opponent_thinking), this.game.getOpponent().getName()));
 			    spinner.show();
 			    
 			   this.autoplayTask = new AutoplayTask();
				this.autoplayTask.execute();
	    		//GameService.autoPlay(this, this.game, this.gameSurfaceView.getTiles());

	    		/*
				String opponentAction = game.getLastActionText(context);
				
				this.postTurnTitle = context.getString(R.string.post_turn_title_with_auto_play);
				this.postTurnMessage = 	String.format(this.getString(R.string.post_turn_message_with_auto_play), playerAction, opponentAction); 
				
			//	DialogManager.SetupAlert(context, context.getString(R.string.post_turn_title_with_auto_play), String.format(this.getString(R.string.post_turn_message_with_auto_play), playerAction, opponentAction));
				setupButtons();
				setupGame();
	 			gameSurfaceView.resetGameAfterRefresh(); //resetGameAfterPlay();
	 			setPointsAfterPlayView();
				

				if (game.isCompleted()) {
					//perhaps replace play, skip
					GameStateService.removeGameState(game.getId());
					this.gameSurfaceView.stopThreadLoop();
				}
				
				handleInterstitialAd();
*/
				//	} catch (DesignByContractException e) {
				 
		//		DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
		//	}
			 
	    }
	    public void handlePostAutoplay(){
	    	/*if (spinner != null) {
		 		spinner.dismiss();
		 		spinner = null;
		 	}*/
	    	spinner.updateMessage(this.getString(R.string.progress_almost_ready));
	    	 String opponentAction = game.getLastActionText(context);
	    	 
	    	 if (this.game.getLastAction().equals(LastAction.TURN_SKIPPED)){
	       		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_SKIP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), (int)Constants.TRACKER_SINGLE_VALUE);
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.WORDS_PLAYED)){
	       		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_PLAY, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), this.game.getLastTurnPoints());
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.RESIGNED)){
	       		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_CONCEDE, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), Math.abs(this.game.getPlayerGames().get(0).getScore() - this.game.getPlayerGames().get(1).getScore()));
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.ONE_LETTER_SWAPPED)){
	    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 1);
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.TWO_LETTERS_SWAPPED)){
	    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 2);
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.THREE_LETTERS_SWAPPED)){
	    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 3);
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.FOUR_LETTERS_SWAPPED)){
	    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 4);
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.FIVE_LETTERS_SWAPPED)){
	    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 5);
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.SIX_LETTERS_SWAPPED)){
	    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 6);
	    	 }
	    	 else if (this.game.getLastAction().equals(LastAction.SEVEN_LETTERS_SWAPPED)){
	    		 this.trackEvent(Constants.TRACKER_ACTION_GAME_AUTO_SWAP, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), 7);
	    	 }

				this.postTurnTitle = context.getString(R.string.post_turn_title_with_auto_play);
				this.postTurnMessage = 	String.format(this.getString(R.string.post_turn_message_with_auto_play), this.lastPlayerActionBeforeAutoplay, opponentAction); 
	
				//DialogManager.SetupAlert(context, context.getString(R.string.post_turn_title_with_auto_play), String.format(this.getString(R.string.post_turn_message_with_auto_play), playerAction, opponentAction));
				setupButtons();

				setupGame();
	 			gameSurfaceView.resetGameAfterRefresh(); //resetGameAfterPlay();
	 			setPointsAfterPlayView();
	 			
				if (game.isCompleted()) {
					this.isCompletedThisSession = true;
					//perhaps replace play, skip
					GameStateService.removeGameState(game.getId());
					this.setupMenu(); //in case this is the first completed game, it will add that option to menu
					//??this.gameSurfaceView.stopThreadLoop();
					
					//capture completed event
				}
				
				handleInterstitialAd();
	    }
	    
	    public void handlePostPreAutoplay(){
	    	this.isPreAutoplayTaskRunning = false;
	    	this.preAutoplayTask = null;
	    	this.hasPreAutoPlayRunThisTurn = true;
	    	 Logger.d(TAG, "handlePostPreAutoplay numPlacedResults derived=" + this.placedResults.size());
	    	
	    }
	    
	    public void handlePostAdServer(){
	    	if (!this.hasPostAdRun &&  this.postTurnMessage.length() > 0){  //chartboost dismiss and close both call this, lets make sure its not run twice
    		 	this.hasPostAdRun = true;
    		 	this.unfreezeButtons();
    		 	Logger.d(TAG, "unfreeze handlePostAdServer");
    		 	if (spinner != null) {
    		 		spinner.dismiss();
    		 		spinner = null;
    		 	}
    		 	DialogManager.SetupAlert(context, this.postTurnTitle , this.postTurnMessage);
    		 	
    		 	//lets prime the pump for the next play
    		 	if (this.game.isActive()){
    		 		this.preAutoplayTask = new PreAutoplayTask();
    		 		this.preAutoplayTask.execute();
    		 	}
    		 	else if (this.game.isCompleted()){
    		 		//save completed event
    		 		if (this.game.getPlayerGames().get(0).getScore() == this.game.getPlayerGames().get(1).getScore()){
    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_DRAW, this.game.getOpponentId()), (int) Constants.TRACKER_SINGLE_VALUE);

    		 		}
    		 		else if (this.game.getPlayerGames().get(0).getScore() > this.game.getPlayerGames().get(1).getScore()){
    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_LOSS, this.game.getOpponentId()), this.game.getPlayerGames().get(0).getScore() - this.game.getPlayerGames().get(1).getScore());

    		 		}
    		 		else {
    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_WON, this.game.getOpponentId()), this.game.getPlayerGames().get(1).getScore() - this.game.getPlayerGames().get(0).getScore());

    		 		}
    		 	}
	    	}
	    }
	    
	    private void handleInterstitialAd(){
	    	this.hasPostAdRun = false;
	    	if (this.hideInterstitialAd){
	    		this.handlePostAdServer();   		            	 					
	 			}
	 		else{
	 			if (this.isChartBoostActive) {
	 				if (spinner == null){
	 					spinner = new CustomProgressDialog(this);
	 		 			spinner.setMessage(this.getString(R.string.progress_wait));
	 		 			spinner.show();				
	 				}
	 				
		 			this.cb.setTimeout((int)Constants.GAME_SURFACE_INTERSTITIAL_AD_CHECK_IN_MILLISECONDS);
		 			this.cb.showInterstitial();
			    	Logger.d(TAG, "showInterstitial from Chartboost");
			    	//String toastStr = "Loading Interstitial";
			    	//if (cb.hasCachedInterstitial()) toastStr = "Loading Interstitial From Cache";
			    	//Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
	 			}
	 			/*
	 			else if (this.isRevMobActive) {
	 				 
	 				 this.revMobFullScreen.show();
			    	Logger.d(TAG, "showInterstitial RevMob");
			    	//String toastStr = "Loading Interstitial";
			    	//if (cb.hasCachedInterstitial()) toastStr = "Loading Interstitial From Cache";
			    	//Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
	 			}*/
 			}
	    }

	    
	    private class SwapDialog extends AlertDialog implements View.OnClickListener{ 
	    //	private Dialog dialog;
	    	private Button bOK;
	    	private Button bCancel;
	    	private List<String> swapped = new ArrayList<String>();
	    	private List<String> letters = new ArrayList<String>();
	    	private boolean letter_1 = false;
	    	private boolean letter_2 = false;
	    	private boolean letter_3 = false;
	    	private boolean letter_4 = false;
	    	private boolean letter_5 = false;
	    	private boolean letter_6 = false;
	    	private boolean letter_7 = false;
	    	private TextView tvLetter1;
	    	private TextView tvLetter2;
	    	private TextView tvLetter3;
	    	private TextView tvLetter4;
	    	private TextView tvLetter5;
	    	private TextView tvLetter6;
	    	private TextView tvLetter7;
	    	
	    	private View layout;
	    	Context context;
	    	int fullWidth;
	    	int tileSize;
	    	int textSize;
	    	//private Context context;
	    	//private Boolean onCancelFinishActivity;
	    	
	     
	    	@SuppressLint({ "NewApi", "NewApi" })
			public SwapDialog(Context context, List<String> letters) {
	    		super(context);
	    	    this.context = context;
	    	    Display display = getWindowManager().getDefaultDisplay();
	    	    Point size = new Point();
	    		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	    			 display.getSize(size);
	    			 this.fullWidth = size.x;
	    		 }
	    		 else {
	    			 this.fullWidth = display.getWidth();  // deprecated
	    		}
	    	    	
	    	    int tileWidth = Math.round(this.fullWidth / 7.1f);
	    	    if (tileWidth > 70){
	    	    	this.tileSize = 70;
	    	    }
	    	    else {
	    	    	this.tileSize = tileWidth;
	    	    }
	    	    	
	    	    textSize = Math.round(this.tileSize * .75f);
	    	    
	    	    this.letters = letters;
	    	//	this.dialog = new Dialog(ctx, R.style.DialogStyle);
	    	//	this.dialog.setContentView(R.layout.swapdialog);
	    	
	    		
	    	 
	    	}
	    	
	    	protected void onCreate(Bundle savedInstanceState) {
	    		// TODO Auto-generated method stub
	    		super.onCreate(savedInstanceState);
//	    		this.setContentView(BUTTON1);
	          //  this.setProgressStyle(R.style.CustomProgressStyle);
	    		
	    	}

	    	@Override
	    	public void onStart() {
	    		// TODO Auto-generated method stub
	    		super.onStart();
	 
	    		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
	            this.layout = inflater.inflate(R.layout.swapdialog, 
	                                            (ViewGroup) findViewById(R.id.progress_root));
	    		
	    		 //loop through letters, filling the views

	    		bOK = (Button) this.layout.findViewById(R.id.bOK);
	    		bOK.setOnClickListener(this);
	    		bCancel = (Button) this.layout.findViewById(R.id.bCancel);
	    
	    		tvLetter1 = (TextView) this.layout.findViewById(R.id.tvLetter1);
	    		//tvLetter1.setTextSize(this.textSize);
	    		tvLetter2 = (TextView) this.layout.findViewById(R.id.tvLetter2);
	    		//tvLetter2.setTextSize(this.textSize);
	    		tvLetter3 = (TextView) this.layout.findViewById(R.id.tvLetter3);
	    		//tvLetter3.setTextSize(this.textSize);
	    		tvLetter4 = (TextView) this.layout.findViewById(R.id.tvLetter4);
	    		//tvLetter4.setTextSize(this.textSize);
	    		tvLetter5 = (TextView) this.layout.findViewById(R.id.tvLetter5);
	    		//tvLetter5.setTextSize(this.textSize);
	    		tvLetter6 = (TextView) this.layout.findViewById(R.id.tvLetter6);
	    		//tvLetter6.setTextSize(this.textSize);
	    		tvLetter7 = (TextView) this.layout.findViewById(R.id.tvLetter7);
	    		//tvLetter7.setTextSize(this.textSize);
	    		TextView alert_title = (TextView) this.layout.findViewById(R.id.alert_title);
	    		
	    		TextView tvValue1 = (TextView) this.layout.findViewById(R.id.tvValue1);
	    		TextView tvValue2 = (TextView) this.layout.findViewById(R.id.tvValue2);
	    		TextView tvValue3 = (TextView) this.layout.findViewById(R.id.tvValue3);
	    		TextView tvValue4 = (TextView) this.layout.findViewById(R.id.tvValue4);
	    		TextView tvValue5 = (TextView) this.layout.findViewById(R.id.tvValue5);
	    		TextView tvValue6 = (TextView) this.layout.findViewById(R.id.tvValue6);
	    		TextView tvValue7 = (TextView) this.layout.findViewById(R.id.tvValue7);

	    		tvLetter1.setText(letters.get(0));
	    		tvLetter2.setText(letters.get(1));
	    		tvLetter3.setText(letters.get(2));
	    		tvLetter4.setText(letters.get(3));
	    		tvLetter5.setText(letters.get(4));
	    		tvLetter6.setText(letters.get(5));
	    		tvLetter7.setText(letters.get(6));
	    		
	    		bOK.setTypeface(ApplicationContext.getMainFontTypeface());
	    		bCancel.setTypeface(ApplicationContext.getMainFontTypeface());
	    		alert_title.setTypeface(ApplicationContext.getMainFontTypeface());
	    		tvLetter1.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	    		tvLetter2.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	    		tvLetter3.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	    		tvLetter4.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	    		tvLetter5.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	    		tvLetter6.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	    		tvLetter7.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	    		
	    		tvValue1.setText(Integer.toString(AlphabetService.getLetterValue(letters.get(0))));
	    		tvValue2.setText(Integer.toString(AlphabetService.getLetterValue(letters.get(1))));
	    		tvValue3.setText(Integer.toString(AlphabetService.getLetterValue(letters.get(2))));
	    		tvValue4.setText(Integer.toString(AlphabetService.getLetterValue(letters.get(3))));
	    		tvValue5.setText(Integer.toString(AlphabetService.getLetterValue(letters.get(4))));
	    		tvValue6.setText(Integer.toString(AlphabetService.getLetterValue(letters.get(5))));
	    		tvValue7.setText(Integer.toString(AlphabetService.getLetterValue(letters.get(6))));
	    		
	    		tvLetter1.setOnClickListener(this);
	    		tvLetter2.setOnClickListener(this);
	    		tvLetter3.setOnClickListener(this);
	    		tvLetter4.setOnClickListener(this);
	    		tvLetter5.setOnClickListener(this);
	    		tvLetter6.setOnClickListener(this);
	    		tvLetter7.setOnClickListener(this);

	    		
	    		this.setCanceledOnTouchOutside(false);
	    		bCancel.setOnClickListener(new View.OnClickListener() {
	    			@Override
	    			public void onClick(View v) {
	    				trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_SWAP_DISMISS_OUTSIDE, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	    				unfreezeButtons();
	        		 
	    				dismiss();
	    			}
	    		});

	    		ImageView close = (ImageView) this.layout.findViewById(R.id.img_close);
	    		//if button is clicked, close the custom dialog
	    		close.setOnClickListener(new View.OnClickListener() {
	    	 		@Override
	    			public void onClick(View v) {
	    	 			
	    	 			trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_SWAP_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	    	 			
	    	 			unfreezeButtons();
	    				dismiss();
	    			}
	    		});
	    		
	    		this.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_SWAP_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
						
						unfreezeButtons();
						
					}
				});

	    		this.setContentView(this.layout);
	    	}
	    		
	    	
	    	@Override
	    	public void dismiss(){
	    		unfreezeButtons();
	    		super.dismiss();	
	    	}

	    	public void handleOKClick(){
	    		//if no swapped letters were picked, inform user
	    		if (!letter_1 && !letter_2 && !letter_3 && !letter_4 && !letter_5 && !letter_6 && !letter_7){
	    			trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_SWAP_NONE_CLICKED, this.swapped.size());
	    			
	    			DialogManager.SetupAlert(context, context.getString(R.string.oops), context.getString(R.string.gameboard_swap_dialog_please_choose_text), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
	    		}
	    		//call handler in main class, passing the swapped letters
	    		else{
	    			if (letter_1) {this.swapped.add(this.letters.get(0));}
	    			if (letter_2) {this.swapped.add(this.letters.get(1));}
	    			if (letter_3) {this.swapped.add(this.letters.get(2));}
	    			if (letter_4) {this.swapped.add(this.letters.get(3));}
	    			if (letter_5) {this.swapped.add(this.letters.get(4));}
	    			if (letter_6) {this.swapped.add(this.letters.get(5));}
	    			if (letter_7) {this.swapped.add(this.letters.get(6));}
	    			
	    			trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_SWAP_OK, this.swapped.size());

	    			this.dismiss();
	    			handleGameSwapOnClick(this.swapped);
	    		}
	    	}

			@Override
			public void onClick(View v) {
				switch(v.getId()){  
				case R.id.bOK:
					this.handleOKClick();
					break;
				case R.id.tvLetter1:
					if (!letter_1){
						tvLetter1.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter1.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_1 = !letter_1;
					break;
				case R.id.tvLetter2:
					if (!letter_2){
						tvLetter2.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter2.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_2 = !letter_2;
					break;
				case R.id.tvLetter3:
					if (!letter_3){
						tvLetter3.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter3.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_3 = !letter_3;
					break;
				case R.id.tvLetter4:
					if (!letter_4){
						tvLetter4.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter4.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_4 = !letter_4;
					break;
				case R.id.tvLetter5:
					if (!letter_5){
						tvLetter5.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter5.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_5 = !letter_5;
					break;
				case R.id.tvLetter6:
					if (!letter_6){
						tvLetter6.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter6.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_6 = !letter_6;
					break;
				case R.id.tvLetter7:
					if (!letter_7){
						tvLetter7.setBackgroundResource(R.drawable.tray_tile_swap_bg);
					}
					else{
						tvLetter7.setBackgroundResource(R.drawable.tray_tile_bg);
					}
					letter_7 = !letter_7;
					break;
				}
			}
	    	
	    }

	 /* 
	    
	    private void setupRunawayAdTimer(){
	    	Logger.d(TAG, "setupRunawayAdTimer called");
	    	//interstitial sometimes gets in a funky state, and always does when ad blovkers are present
	    	//so let stop the load of the ad after 10 seconds, if it has not been displayed yet
	    	 
			this.captureTime("setupTimer starting");
	    	if (this.runawayAdTimer == null){
	    		this.runawayAdTimer = new Timer();  
	    	}
	    	checkRunawayAdTask checkRunawayAd = new checkRunawayAdTask();
	    	this.runawayAdTimer.schedule(checkRunawayAd, Constants.GAME_SURFACE_INTERSTITIAL_AD_CHECK_IN_MILLISECONDS);
	    	this.captureTime("setupRunawayAdTimer ended");
 
	    }
	    
	    private void stopRunawayAdTimer(){
	    	Logger.d(TAG, "stopRunawayAdTimer called");
	    	if (this.runawayAdTimer != null) {
	    		this.runawayAdTimer.cancel();
	    		this.runawayAdTimer = null;
	    	}
	    
	    }
	    
	    private class checkRunawayAdTask extends TimerTask {
    	   public void run() {
    		   if (!isAdStarted){
    				((Activity) context).runOnUiThread(new checkRunawayAdTaskRunnable());
    		   }
    	   }
	    }
	    
	    private class checkRunawayAdTaskRunnable implements Runnable {
		    public void run() {
		    	try { 
		    		interstitial.stopLoading();
  				    interstitial = null;
  				      
		    		if (spinner != null){
	    				spinner.dismiss();
	    			}
		    		handlePostAdServer();
	    			//handlePostTurnFinalAction(postTurnAction);
				} catch (Exception e) {
					 Logger.d(TAG, "checkRunawayAdTaskRunnable error=" + e.toString());
				}
		    }
	   }
	 
	    private void loadAdMobInterstitialAd(){
		    // Create the interstitial
	        interstitial = new  com.google.ads.InterstitialAd(this, this.getString(R.string.admob_pub_id_interstitial));
	
	        // Create ad request
	        AdRequest adRequest = new AdRequest();
	
	        // Begin loading your interstitial
	        interstitial.loadAd(adRequest);
	
	        // Set Ad Listener to use the callbacks below
	        interstitial.setAdListener(this);
	    }
	    
		@Override
		public void onDismissScreen(Ad ad) {
			// TODO Auto-generated method stub
			Logger.d(TAG, "interstitial onDismissScreen called");
			this.isAdStarted = true;
			this.spinner.dismiss();
			this.isRestartFromInterstitialAd = true;
			//this.handlePostTurnFinalAction(this.postTurnAction); //this will be handled by restart
		}

		@Override
		public void onFailedToReceiveAd(Ad ad, ErrorCode arg1) {
			Logger.d(TAG, "interstitial onFailedToReceiveAd called");
			this.isAdStarted = true;
			this.spinner.dismiss();
			handlePostAdServer();
			//this.handlePostTurnFinalAction(this.postTurnAction);
		}

		@Override
		public void onLeaveApplication(Ad ad) {
			this.isAdStarted = true;
			Logger.d(TAG, "interstitial onLeaveApplication called");
			 //not sure what this needs here
		//	this.handlePostTurnFinalAction(this.postTurnAction);
			
		}

		@Override
		public void onPresentScreen(Ad ad) {
			Logger.d(TAG, "interstitial onPresentScreen called");
		}

		@Override
		public void onReceiveAd(Ad ad) {
			Logger.d(TAG, "interstitial onReceiveAd called");
			this.isAdStarted = true;
			  if (ad == interstitial) {
			    interstitial.show();
			  }
		}
*/
		
		private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() { 

			/*
			 * Chartboost delegate methods
			 * 
			 * Implement the delegate methods below to finely control Chartboost's behavior in your app
			 * 
			 * Minimum recommended: shouldDisplayInterstitial()
			 */


			/* 
			 * shouldDisplayInterstitial(String location)
			 *
			 * This is used to control when an interstitial should or should not be displayed
			 * If you should not display an interstitial, return false
			 *
			 * For example: during gameplay, return false.
			 *
			 * Is fired on:
			 * - showInterstitial()
			 * - Interstitial is loaded & ready to display
			 */
			@Override
			public boolean shouldDisplayInterstitial(String location) {
				Logger.d(TAG, "SHOULD DISPLAY INTERSTITIAL '"+location+ "'?");
				return true;
			}

			/*
			 * shouldRequestInterstitial(String location)
			 * 
			 * This is used to control when an interstitial should or should not be requested
			 * If you should not request an interstitial from the server, return false
			 *
			 * For example: user should not see interstitials for some reason, return false.
			 *
			 * Is fired on:
			 * - cacheInterstitial()
			 * - showInterstitial() if no interstitial is cached
			 * 
			 * Notes: 
			 * - We do not recommend excluding purchasers with this delegate method
			 * - Instead, use an exclusion list on your campaign so you can control it on the fly
			 */
			@Override
			public boolean shouldRequestInterstitial(String location) {
				Logger.d(TAG, "SHOULD REQUEST INSTERSTITIAL '"+location+ "'?");
				return true;
			}

			/*
			 * didCacheInterstitial(String location)
			 * 
			 * Passes in the location name that has successfully been cached
			 * 
			 * Is fired on:
			 * - cacheInterstitial() success
			 * - All assets are loaded
			 * 
			 * Notes:
			 * - Similar to this is: cb.hasCachedInterstitial(String location) 
			 * Which will return true if a cached interstitial exists for that location
			 */
			@Override
			public void didCacheInterstitial(String location) {
				Logger.d(TAG, "INTERSTITIAL '"+location+"' CACHED");
			}

			/*
			 * didFailToLoadInterstitial(String location)
			 * 
			 * This is called when an interstitial has failed to load for any reason
			 * 
			 * Is fired on:
			 * - cacheInterstitial() failure
			 * - showInterstitial() failure if no interstitial was cached
			 * 
			 * Possible reasons:
			 * - No network connection
			 * - No publishing campaign matches for this user (go make a new one in the dashboard)
			 */
			@Override
			public void didFailToLoadInterstitial(String location) {
			    // Show a house ad or do something else when a chartboost interstitial fails to load

				Logger.d(TAG, "ChartBoost INTERSTITIAL '"+location+"' REQUEST FAILED");
				//Toast.makeText(context, "Interstitial '"+location+"' Load Failed",
				//		Toast.LENGTH_SHORT).show();
				handlePostAdServer();
				//handlePostTurnFinalAction(postTurnAction);
			}

			/*
			 * didDismissInterstitial(String location)
			 *
			 * This is called when an interstitial is dismissed
			 *
			 * Is fired on:
			 * - Interstitial click
			 * - Interstitial close
			 *
			 * #Pro Tip: Use the delegate method below to immediately re-cache interstitials
			 */
			@Override
			public void didDismissInterstitial(String location) {

				// Immediately re-caches an interstitial
				cb.cacheInterstitial(location);
				handlePostAdServer();
				//handlePostTurnFinalAction(postTurnAction);


				Logger.d(TAG, "ChartBoost INTERSTITIAL '"+location+"' DISMISSED");
				//Toast.makeText(context, "Dismissed Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
				
			}

			/*
			 * didCloseInterstitial(String location)
			 *
			 * This is called when an interstitial is closed
			 *
			 * Is fired on:
			 * - Interstitial close
			 */
			@Override
			public void didCloseInterstitial(String location) {
				Logger.i(TAG, "ChartBoost INSTERSTITIAL '"+location+"' CLOSED");
				//handlePostAdServer();
				//handlePostTurnFinalAction(postTurnAction);
				//Toast.makeText(context, "Closed Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didClickInterstitial(String location)
			 *
			 * This is called when an interstitial is clicked
			 *
			 * Is fired on:
			 * - Interstitial click
			 */
			@Override
			public void didClickInterstitial(String location) {
				Logger.i(TAG, "ChartBoost DID CLICK INTERSTITIAL '"+location+"'");
				handlePostAdServer();
				//handlePostTurnFinalAction(postTurnAction);
				//Toast.makeText(context, "Clicked Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didShowInterstitial(String location)
			 *
			 * This is called when an interstitial has been successfully shown
			 *
			 * Is fired on:
			 * - showInterstitial() success
			 */
			@Override
			public void didShowInterstitial(String location) {
				Logger.i(TAG, "ChartBoost INTERSTITIAL '" + location + "' SHOWN");
				//Toast.makeText(context, "Interstitial '"+location+"' shown",
				//		Toast.LENGTH_SHORT).show();
				
				//Dont do it here because boost does not use full page interstitial and 
				//tray can be seen through modal
				//handlePostTurnFinalAction(postTurnAction);
			}

			/*
			 * More Apps delegate methods
			 */

			/*
			 * shouldDisplayLoadingViewForMoreApps()
			 *
			 * Return false to prevent the pretty More-Apps loading screen
			 *
			 * Is fired on:
			 * - showMoreApps()
			 */
			@Override
			public boolean shouldDisplayLoadingViewForMoreApps() {
				return true;
			}

			/*
			 * shouldRequestMoreApps()
			 * 
			 * Return false to prevent a More-Apps page request
			 *
			 * Is fired on:
			 * - cacheMoreApps()
			 * - showMoreApps() if no More-Apps page is cached
			 */
			@Override
			public boolean shouldRequestMoreApps() {

				return true;
			}

			/*
			 * shouldDisplayMoreApps()
			 * 
			 * Return false to prevent the More-Apps page from displaying
			 *
			 * Is fired on:
			 * - showMoreApps() 
			 * - More-Apps page is loaded & ready to display
			 */
			@Override
			public boolean shouldDisplayMoreApps() {
				Logger.i(TAG, "ChartBoost SHOULD DISPLAY MORE APPS?");
				return true;
			}

			/*
			 * didFailToLoadMoreApps()
			 * 
			 * This is called when the More-Apps page has failed to load for any reason
			 * 
			 * Is fired on:
			 * - cacheMoreApps() failure
			 * - showMoreApps() failure if no More-Apps page was cached
			 * 
			 * Possible reasons:
			 * - No network connection
			 * - No publishing campaign matches for this user (go make a new one in the dashboard)
			 */
			@Override
			public void didFailToLoadMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS REQUEST FAILED");
				//Toast.makeText(context, "More Apps Load Failed",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didCacheMoreApps()
			 * 
			 * Is fired on:
			 * - cacheMoreApps() success
			 * - All assets are loaded
			 */
			@Override
			public void didCacheMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CACHED");
			}

			/*
			 * didDismissMoreApps()
			 *
			 * This is called when the More-Apps page is dismissed
			 *
			 * Is fired on:
			 * - More-Apps click
			 * - More-Apps close
			 */
			@Override
			public void didDismissMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS DISMISSED");
				//Toast.makeText(context, "Dismissed More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didCloseMoreApps()
			 *
			 * This is called when the More-Apps page is closed
			 *
			 * Is fired on:
			 * - More-Apps close
			 */
			@Override
			public void didCloseMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CLOSED");
				//Toast.makeText(context, "Closed More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didClickMoreApps()
			 *
			 * This is called when the More-Apps page is clicked
			 *
			 * Is fired on:
			 * - More-Apps click
			 */
			@Override
			public void didClickMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CLICKED");
				//Toast.makeText(context, "Clicked More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didShowMoreApps()
			 *
			 * This is called when the More-Apps page has been successfully shown
			 *
			 * Is fired on:
			 * - showMoreApps() success
			 */
			@Override
			public void didShowMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS SHOWED");
			}

			/*
			 * shouldRequestInterstitialsInFirstSession()
			 *
			 * Return false if the user should not request interstitials until the 2nd startSession()
			 * 
			 */
			@Override
			public boolean shouldRequestInterstitialsInFirstSession() {
				return true;
			}
		};

		
	    private void trackEvent(String action, String label, int value){
	    	this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, action, label, (long)value);
	    }
		   
		public void trackEvent(String category, String action, String label, long value){
			try{
				this.getTracker().sendEvent(category, action,label, value);
			}
			catch (Exception e){
	  			Logger.d(TAG, "trackEvent category=" + (category == null ? "null" : category) + " action=" + (action == null ? "null" : action) 
	  					 + " label=" + (label == null ? "null" : label)  + " value=" + value +" e=" + e.toString());
	  			
			}
		}

		@Override
		public void dialogClose(int resultCode) {
			
			
			Logger.d(TAG, "dialogClose resultCode=" + resultCode);
			 switch(resultCode) { 
			   case Constants.RETURN_CODE_HOPPER_PEEK_CLOSE:
				   this.unfreezeButtons();
				   this.dismissHopperPeekDialog();
				   break; 
			   
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CLICKED:
				 	this.dismissCustomDialog();
		 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_SKIP_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		 			
		 			this.handleGameSkipOnClick(); 
				 break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CLOSE_CLICKED:
				    this.dismissCustomDialog();
		 	 		this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_SKIP_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		 			this.unfreezeButtons();
		 			break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_SKIP_CANCEL_CLICKED:
				    this.dismissCustomDialog();
					this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_SKIP_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
					
					this.unfreezeButtons();
					break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CLICKED:
				   this.dismissCustomDialog();
		 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_PLAY_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		 			
		 			this.handleGamePlayOnClick(this.placedResult);
		 			break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CLOSE_CLICKED:
				    this.dismissCustomDialog();
		 	 		this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_PLAY_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		 			this.unfreezeButtons();
		 			break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_PLAY_CANCEL_CLICKED:
				    this.dismissCustomDialog();
					this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_PLAY_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
					
					this.unfreezeButtons();
					break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_OK_CLICKED:
				    this.dismissCustomDialog(); 
		 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_RESIGN_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		 			
		 			this.handleGameResignOnClick();
		 			break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_CLOSE_CLICKED:
				    this.dismissCustomDialog(); 
		 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
			        			Constants.TRACKER_LABEL_RESIGN_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		 			
		 			this.unfreezeButtons();
		 			
		 			break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_RESIGN_CANCEL_CLICKED:	
				    this.dismissCustomDialog(); 

				    this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_RESIGN_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);

				    this.unfreezeButtons();
				    break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_OK_CLICKED:
				    this.dismissCustomDialog(); 
		 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_CANCEL_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		 			
		 			this.handleGameCancelOnClick();
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CLOSE_CLICKED:
				    this.dismissCustomDialog(); 
		 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_CANCEL_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		 			
		 			this.unfreezeButtons();
		 			break;
			   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CANCEL_CLICKED:	
				    this.dismissCustomDialog(); 

				    this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
		        			Constants.TRACKER_LABEL_CANCEL_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
				    
				    this.unfreezeButtons();
				    break;
		 		}
			
		}
		
		private void setupFonts(){
			
			//buttons
			Button bHopperPeek = (Button) findViewById(R.id.bHopperPeek);
			Button bSwap = (Button) findViewById(R.id.bSwap);
			Button bPlayedWords = (Button) findViewById(R.id.bPlayedWords);
			Button bCancel = (Button) findViewById(R.id.bCancel);
			Button bResign = (Button) findViewById(R.id.bResign);
			
			this.bRecall.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			this.bPlay.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			this.bSkip.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			this.bShuffle.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bHopperPeek.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bSwap.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bPlayedWords.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bCancel.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bResign.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
				
		}
		
		 private class AutoplayTask extends AsyncTask<Void, Void, Void> {
		  
			 @Override
			 protected Void doInBackground(Void... params) {
				 GameSurface.this.lastPlayerActionBeforeAutoplay = GameSurface.this.game.getLastActionText(context);
		    	 GameService.autoPlay(GameSurface.this, GameSurface.this.game,  GameSurface.this.gameSurfaceView.getTiles(), true, GameSurface.this.placedResults);
				
		    	 
		    	 return null;
		    	 
		    	 //return game; 
		     }

		   //  protected void onProgressUpdate(Integer... progress) {
		   //      setProgressPercent(progress[0]);
		   //  }
		     protected void onPostExecute(Void param) {
		    	 GameSurface.this.handlePostAutoplay();
		     }

		 
		 }
		 
		 private class PreAutoplayTask extends AsyncTask<Void, Void, Void> {
			  
			 @Override
			 protected Void doInBackground(Void... params) {
				 GameSurface.this.captureTime("PreAutoplayTask STARTING");
				 GameSurface.this.hasPreAutoPlayRunThisTurn = false;
				 //while player is thinking, let's gather possible plays for opponent to save time
				 GameSurface.this.isPreAutoplayTaskRunning = true;
		     	 GameService.autoPlay(GameSurface.this, GameSurface.this.game,  GameService.getBoardBaseTilesAndRemovePlacedTiles(GameSurface.this.gameSurfaceView.getTiles()), false, GameSurface.this.placedResults);
				
		    	 
		    	 return null;
		    	 
		    	 //return game; 
		     }

		   //  protected void onProgressUpdate(Integer... progress) {
		   //      setProgressPercent(progress[0]);
		   //  }
		     protected void onPostExecute(Void param) {
				 GameSurface.this.captureTime("PreAutoplayTask ENDING");
		    	 GameSurface.this.handlePostPreAutoplay();
		     }

		 
		 }
		
}
