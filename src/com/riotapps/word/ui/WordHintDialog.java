package com.riotapps.word.ui;

import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.word.GameSurface;
import com.riotapps.word.Main;
import com.riotapps.word.R;
import com.riotapps.word.hooks.AlphabetService;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Letter;
import com.riotapps.word.hooks.Opponent;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.interfaces.ICloseDialog;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;


public class WordHintDialog  extends Dialog implements View.OnClickListener{
	private static final String TAG = HopperPeekDialog.class.getSimpleName();
	
	private Game game;
	private Context parent;
	private View layout;
	private TextView peek_description;
	private Tracker tracker;
	private List<WordHint> hints;
	private LinearLayout llHints;

	
	public Tracker getTracker() {
		if (this.tracker == null){
			this.tracker = EasyTracker.getTracker();
		}
		return tracker;
	}
	
	public WordHintDialog(Context context, List<WordHint> hints){ //Game game){
		
		super(context);
		this.hints = hints;
		this.parent = context;
	
	//	WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	//	params.horizontalMargin = 10f;
		
	//	getWindow().setAttributes(params);
	   getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//getWindow().setBackgroundDrawable(null);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		this.setContentView(BUTTON1);
      //  this.setProgressStyle(R.style.CustomProgressStyle);
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.layout = inflater.inflate(R.layout.wordhintdialog, null); 
//                                        (ViewGroup) findViewById(R.id.progress_root));
		
		 //loop through letters, filling the views
        
     //   this.setContentView(this.layout);

		LinearLayout llOK = (LinearLayout) this.layout.findViewById(R.id.llOK);
		TextView tvOK = (TextView) this.layout.findViewById(R.id.tvOK);
		TextView tvAlertTitle = (TextView) this.layout.findViewById(R.id.tvAlertTitle);

		Button bNoThanks = (Button)this.layout.findViewById(R.id.bNoThanks);
		Button bStore = (Button)this.layout.findViewById(R.id.bStore);
		this.llHints = (LinearLayout)this.layout.findViewById(R.id.llHints);
		
		tvOK.setTypeface(ApplicationContext.getMainFontTypeface());
		bNoThanks.setTypeface(ApplicationContext.getMainFontTypeface());
		bStore.setTypeface(ApplicationContext.getMainFontTypeface());
		tvAlertTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		
		//Logger.d(TAG, "free hopper peeks=" + PlayerService.getRemainingFreeUsesHopperPeek());
		
		this.peek_description = (TextView)this.layout.findViewById(R.id.peek_description);
		this.peek_description.setTypeface(ApplicationContext.getMainFontTypeface());
		
		if (!StoreService.isWordHintsPurchased() && PlayerService.getRemainingFreeUsesWordHints() == 0){
		
			this.peek_description.setText(this.parent.getString(R.string.hopper_peek_purchase_offer));
		 
			this.llHints.setVisibility(View.GONE);
			tvOK.setVisibility(View.GONE);
		}
		else{
			this.peek_description.setText(String.format(this.parent.getString(R.string.gameboard_hopper_peek_dialog_description), String.valueOf(this.game.getTotalNumLetterCountLeftInHopperAndOpponentTray()), this.game.getOpponent().getName()));
			
			this.loadHints();	
			
			if (!StoreService.isHopperPeekPurchased()){
				tvOK.setVisibility(View.GONE);
			
			
				int remainingFreeUses  = PlayerService.removeAFreeUseFromWordHints();
				/*
				if (remainingFreeUses > 1){
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_preview), this.peek_description.getText(), String.valueOf(remainingFreeUses)));
				}
				else if (remainingFreeUses == 1){
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_1_preview_left), this.peek_description.getText()));					
				}
				else{
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_no_previews_left), this.peek_description.getText()));					
				}
				*/
			}
			else{
				
				llOK.setOnClickListener(this);
				bStore.setVisibility(View.GONE);
				bNoThanks.setVisibility(View.GONE);
			}
		}
		 

		ImageView close = (ImageView) this.layout.findViewById(R.id.img_close);
		//if button is clicked, close the custom dialog
		close.setOnClickListener(new View.OnClickListener() {
	 		@Override
			public void onClick(View v) {
	 			
				WordHintDialog.this.close();
			}
		});
		
		this.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
 
				WordHintDialog.this.close();
				
			}
		});
		
		//LayoutParams params = new LayoutParams();
		//params.horizontalMargin = 10f;
		 if (bNoThanks.getVisibility() == View.VISIBLE){
    		 bStore.setOnClickListener(this);
		     bNoThanks.setOnClickListener(this);
    	 }
		 
		this.setContentView(this.layout);
		 
		this.trackEvent(Constants.TRACKER_ACTION_HOPPER_PEEK, String.valueOf(this.game.getTurn()), this.game.getHopper().size());
	     
	 }
			
	private void trackEvent(String action, String label, long value){
		try{
			this.getTracker().sendEvent(Constants.TRACKER_CATEGORY_GAME_HINTS, action,label, value);
		}
		catch (Exception e){
  			Logger.d(TAG, "trackEvent action=" + (action == null ? "null" : action) 
  					 + " label=" + (label == null ? "null" : label)  + " value=" + value +" e=" + e.toString());
  			
		}
	}
		
	
	@Override
	public void dismiss(){
		
		this.game = null;
 		this.layout = null;
		this.peek_description = null;
		this.tracker = null;
			
		super.dismiss();	
	}

	private void close(){
		this.dismiss();
		this.trackEvent(Constants.TRACKER_ACTION_HOPPER_PEEK, Constants.TRACKER_LABEL_HOPPER_PEEK_CLOSE, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		((ICloseDialog)this.parent).dialogClose(Constants.RETURN_CODE_HOPPER_PEEK_CLOSE);
	}
	
	 
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.llOK:
				this.close();
				break;
			case R.id.bStore:
				this.dismiss(); 
				Intent intent = new Intent(this.parent, com.riotapps.word.Store.class);
				this.trackEvent(Constants.TRACKER_ACTION_HOPPER_PEEK, Constants.TRACKER_LABEL_HOPPER_PEEK_GO_TO_STORE, Constants.TRACKER_DEFAULT_OPTION_VALUE);

				this.parent.startActivity(intent); 
				break;
			case R.id.bNoThanks:
				this.dismiss();
				this.trackEvent(Constants.TRACKER_ACTION_HOPPER_PEEK, Constants.TRACKER_LABEL_HOPPER_PEEK_DECLINE_STORE, Constants.TRACKER_DEFAULT_OPTION_VALUE);

				((ICloseDialog)this.parent).dialogClose(Constants.RETURN_CODE_HOPPER_PEEK_CLOSE);
				break;
			default:
				this.close();	
				
				break;
		}
	}
	
	private void loadHints(){
 		
		 for (WordHint hint : this.hints){
 
			 this.llHints.addView(getHintView(hint));
  
		 }
		
	}
	
	
	 private View getHintView(WordHint hint) {
  		View view = getLayoutInflater().inflate(R.layout.wordhintitem, null);
	    RelativeLayout rlLineItem = (RelativeLayout)view.findViewById(R.id.rlLineItem);
 
	 	TextView tvWord = (TextView)view.findViewById(R.id.tvWord);
	  	TextView tvPoints = (TextView)view.findViewById(R.id.tvPoints);
 	 	
	  	tvWord.setTypeface(ApplicationContext.getMainFontTypeface());
	  	tvPoints.setTypeface(ApplicationContext.getMainFontTypeface());
	  	 
	  	tvPoints.setText(String.valueOf(hint.getPoints()));
	  	tvWord.setText(hint.getWord());
	  	
	  	view.setTag(hint.getId());
	 	view.setOnClickListener(this);
  	    return view;
  	}
	
	
	
}
