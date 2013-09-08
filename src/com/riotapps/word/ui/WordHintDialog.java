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


public class WordHintDialog  extends AlertDialog implements View.OnClickListener{
	private static final String TAG = WordHintDialog.class.getSimpleName();
	
	//private Game game;
	private Context parent;
	private View layout;
	private TextView description;
	private Tracker tracker;
	private ImageView imgHinter;
	private List<WordHint> hints;
	private TextView tvInstructions;
	
	private LinearLayout llOK;
	private TextView tvOK;
	private Button bNoThanks;
	private Button bStore;
	
	private LinearLayout llHints;
 

	
	private int disallowCode = Constants.WORD_HINTS_ALLOWED;
	
	public Tracker getTracker() {
		if (this.tracker == null){
			this.tracker = EasyTracker.getTracker();
		}
		return tracker;
	}
	 
	public WordHintDialog(Context context, int disallowCode){ //Game game){
		//hints not allowed (, or used up for this game
		super(context);
		this.parent = context;
		this.disallowCode = disallowCode;
		//disallow code 1 = no more previews, 2 = no more for this game
	}
	
	public WordHintDialog(Context context, List<WordHint> hints){ //Game game){
		
		super(context);
		this.hints = hints;
		this.parent = context;
 
	//   getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	 
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

        this.setCanceledOnTouchOutside(false);
        
		this.imgHinter = (ImageView) this.layout.findViewById(R.id.imgHinter);
		this.llOK = (LinearLayout) this.layout.findViewById(R.id.llOK);
		this.tvOK = (TextView) this.layout.findViewById(R.id.tvOK);
		TextView tvAlertTitle = (TextView) this.layout.findViewById(R.id.tvAlertTitle);
		this.tvInstructions = (TextView)this.layout.findViewById(R.id.tvInstructions);
		
	//	this.imgHinter.setVisibility(View.GONE);
		
		this.bNoThanks = (Button)this.layout.findViewById(R.id.bNoThanks);
		this.bStore = (Button)this.layout.findViewById(R.id.bStore);
		this.llHints = (LinearLayout)this.layout.findViewById(R.id.llHints);
		
		tvOK.setTypeface(ApplicationContext.getMainFontTypeface());
		tvInstructions.setTypeface(ApplicationContext.getMainFontTypeface());
		
		bNoThanks.setTypeface(ApplicationContext.getMainFontTypeface());
		bStore.setTypeface(ApplicationContext.getMainFontTypeface());
		tvAlertTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		
		//Logger.d(TAG, "free hopper peeks=" + PlayerService.getRemainingFreeUsesHopperPeek());
		boolean isPurchased = StoreService.isWordHintsPurchased();
		int remainingFreeUses = PlayerService.getRemainingFreeUsesWordHints();
		
	 	this.description = (TextView)this.layout.findViewById(R.id.description);
	 	this.description.setTypeface(ApplicationContext.getMainFontTypeface());
//		this.loadHints();
 
		if (this.disallowCode == Constants.WORD_HINTS_NO_MORE_PREVIEWS){

			this.description.setText(this.parent.getString(R.string.word_hint_previews_over));			
 
			this.llHints.setVisibility(View.GONE);
			this.tvOK.setVisibility(View.GONE);
			this.imgHinter.setVisibility(View.GONE);
		 	//tvOK.setVisibility(View.GONE);
		}
		else if (this.disallowCode == Constants.WORD_HINTS_MAX_USED_FOR_GAME){
			this.description.setText(String.format(this.parent.getString(R.string.word_hint_used_this_game), Constants.MAX_NUM_HINTS_PER_GAME));					

			this.setPurchaseOfferViews(isPurchased, remainingFreeUses);

		 	this.llHints.setVisibility(View.GONE);
		 	this.imgHinter.setVisibility(View.GONE);
		 	//tvOK.setVisibility(View.GONE);	
		}
		else if (this.hints.size() == 0) {
			this.description.setText(this.parent.getString(R.string.word_hint_description_none_found));

			this.setPurchaseOfferViews(isPurchased, remainingFreeUses);
			
		}
		else{
			
		    this.description.setText(this.parent.getString(R.string.word_hint_description));
			
			this.loadHints();	
			
			this.setPurchaseOfferViews(isPurchased, remainingFreeUses);
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
		 
		//this.trackEvent(Constants.TRACKER_ACTION_WORD_HINT, String.valueOf(this.game.getTurn()), this.game.getHopper().size());
	     
	 }

	private void setPurchaseOfferDescription(int remainingFreeUses){
		/*if (remainingFreeUses > 1){
			this.description.setText(String.format(this.parent.getString(R.string.word_hint_preview), this.description.getText(), String.valueOf(remainingFreeUses)));
		}
		else if (remainingFreeUses == 1){
			this.description.setText(String.format(this.parent.getString(R.string.word_hint_1_preview_left), this.description.getText()));					
		}
		else{
			this.description.setText(String.format(this.parent.getString(R.string.word_hint_no_previews_left), this.description.getText()));					
		}
		*/
		if (remainingFreeUses > 1){
			this.tvInstructions.setText(String.format(this.parent.getString(R.string.word_hint_preview), String.valueOf(remainingFreeUses)));
		}
		else if (remainingFreeUses == 1){
			this.tvInstructions.setText(this.parent.getString(R.string.word_hint_1_preview_left));					
		}
		else{
			this.tvInstructions.setText(this.parent.getString(R.string.word_hint_no_previews_left));					
		}
	}
	
	private void setPurchaseOfferViews(boolean isPurchased, int remainingFreeUses){
		if (!isPurchased){
			this.tvOK.setVisibility(View.GONE);
			this.setPurchaseOfferDescription(remainingFreeUses);
		}
		else{
			
			this.llOK.setOnClickListener(this);
			this.bStore.setVisibility(View.GONE);
			this.bNoThanks.setVisibility(View.GONE);
		}
	}
	
	private void trackEvent(String action, String label, long value){
		try{
			this.getTracker().sendEvent(Constants.TRACKER_CATEGORY_WORD_HINTS, action,label, value);
		}
		catch (Exception e){
  			Logger.d(TAG, "trackEvent action=" + (action == null ? "null" : action) 
  					 + " label=" + (label == null ? "null" : label)  + " value=" + value +" e=" + e.toString());
  			
		}
	}
		
	
	@Override
	public void dismiss(){
		
	//	this.game = null;
 		this.layout = null;
		this.description = null;
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
				((ICloseDialog)this.parent).dialogClose(Constants.RETURN_CODE_WORD_HINT_CLOSE);
				break;
			case R.id.bStore:
				this.dismiss(); 
				Intent intent = new Intent(this.parent, com.riotapps.word.Store.class);
				this.trackEvent(Constants.TRACKER_ACTION_WORD_HINT, Constants.TRACKER_LABEL_WORD_HINTS_GO_TO_STORE, Constants.TRACKER_DEFAULT_OPTION_VALUE);

				this.parent.startActivity(intent); 
				break;
			case R.id.bNoThanks:
				this.dismiss();
				this.trackEvent(Constants.TRACKER_ACTION_WORD_HINT, Constants.TRACKER_LABEL_WORD_HINTS_DECLINE_STORE, Constants.TRACKER_DEFAULT_OPTION_VALUE);

				((ICloseDialog)this.parent).dialogClose(Constants.RETURN_CODE_WORD_HINT_CLOSE);
				break;
			default:
				 String placedResultId = v.getTag().toString();
				 ((ICloseDialog)this.parent).dialogClose(Constants.RETURN_CODE_WORD_HINT_DIALOG_CHOICE_MADE, placedResultId);
	
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
	  	 
	  	tvPoints.setText(String.format(this.parent.getString(R.string.word_hint_num_points), String.valueOf(hint.getPoints())));
	  	tvWord.setText(hint.getWord());
	  	
	  	view.setTag(hint.getId());
	 	view.setOnClickListener(this);
  	    return view;
  	}
	
	
	
}
