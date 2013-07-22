package com.riotapps.word;

import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.CustomProgressDialog;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.jeremybrooks.knicker.AccountApi;
import net.jeremybrooks.knicker.KnickerException;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.Knicker.PartOfSpeech;
import net.jeremybrooks.knicker.Knicker.SourceDictionary;
import net.jeremybrooks.knicker.dto.Definition;
import net.jeremybrooks.knicker.dto.TokenStatus;


public class GameLookup extends FragmentActivity  implements View.OnClickListener {
	private static final String TAG = GameLookup.class.getSimpleName();
	private Game game;
	private Player player;
	private Context context = this;
	private WordnikLookup task;
	private LinearLayout llDefs;
	private TextView tvNotFound;
	private TextView tvPreviewMessage;
	private CustomProgressDialog spinner;
	private String word;
	ApplicationContext appContext;
	private Tracker tracker;
	
	public Tracker getTracker() {
		if (this.tracker == null){
			this.tracker = EasyTracker.getTracker();
		}
		return tracker;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamelookup);
		 
		
	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	this.word = i.getStringExtra(Constants.EXTRA_WORD_LOOKUP);
	 	
	 	this.game = GameService.getGame(gameId); 
	 	this.appContext = (ApplicationContext)this.getApplicationContext(); 
	    this.player = this.appContext.getPlayer(); 
	 	GameService.loadScoreboard(this, this.game);
	     
	     TextView tvWord = (TextView)this.findViewById(R.id.tvWord);
	     tvWord.setText(word);
	     
	     ImageView ivWordnik = (ImageView)this.findViewById(R.id.ivWordnik);
	     ivWordnik.setOnClickListener(this);
	 
	     this.llDefs = (LinearLayout)this.findViewById(R.id.llDefs);
	     this.tvNotFound = (TextView)this.findViewById(R.id.tvNotFound);
	     this.tvPreviewMessage = (TextView) this.findViewById(R.id.tvPreviewMessage);
	     //tvNotFound.setText(context.getString(R.string.lookup_definition_not_found));
	     TextView tvOK = (TextView)this.findViewById(R.id.tvOK);
	     Button bNoThanks = (Button)this.findViewById(R.id.bNoThanks);
	     Button bStore = (Button)this.findViewById(R.id.bStore);
	     
	     MenuUtils.hideMenu(this);
	     this.setupFonts();
	     
	     if (StoreService.isHideBannerAdsPurchased()){
				AdView adView = (AdView)this.findViewById(R.id.adView);
				adView.setVisibility(View.GONE);
		 
			}
	     
	     if (!StoreService.isWordDefinitionLookupPurchased() && PlayerService.getRemainingFreeUsesWordDefinition() == 0){
	    	 this.tvPreviewMessage.setText(this.getString(R.string.word_definition_purchase_offer));
	   		 this.llDefs.setVisibility(View.GONE);
	   		 this.tvNotFound.setVisibility(View.GONE);
	   		tvOK.setVisibility(View.GONE);
	   		ivWordnik.setVisibility(View.GONE);
	     }
	     else {
	    	 if (!StoreService.isWordDefinitionLookupPurchased()){
					
					int remainingFreeUses  = PlayerService.removeAFreeUseFromWordDefinition();
					if (remainingFreeUses > 1){
						this.tvPreviewMessage.setText(String.format(this.getString(R.string.word_definition_preview), String.valueOf(remainingFreeUses)));
					}
					else if (remainingFreeUses == 1){
						this.tvPreviewMessage.setText(this.getString(R.string.word_definition_1_preview_left));					
					}
					else{
						this.tvPreviewMessage.setText(this.getString(R.string.word_definition_no_previews_left));					
					}
					
					tvOK.setVisibility(View.GONE);
				}
	    	 else {
		    		bNoThanks.setVisibility(View.GONE);
	    		    bStore.setVisibility(View.GONE); 
	    		    //change margin of definition area?
	    		//    tvOK.setOnClickListener(this);
	    		    tvPreviewMessage.setVisibility(View.GONE);
	    		    this.tvPreviewMessage.setVisibility(View.GONE);
	    		    
	    		    //remove bottom margin and
	    		    ScrollView scoller = (ScrollView)findViewById(R.id.scroller);
	    	  
	    	        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scoller.getLayoutParams();

	    	            layoutParams.bottomMargin = 10;
	    	            //layoutParams.setMargins(0, 0, 0, 100);
	    	            scoller.setLayoutParams(layoutParams);
	    		    
	    	            LinearLayout llOK = (LinearLayout)findViewById(R.id.llOK);
	    	            llOK.setVisibility(View.GONE);
	    		    
	    	 }
	    	 
	    	
		     System.setProperty("WORDNIK_API_KEY", this.getString(R.string.wordnik_apiKey));
		 
		     this.spinner = new CustomProgressDialog(this);
			 this.spinner.setMessage(this.getString(R.string.progress_looking_up));
			 this.spinner.show();
			 
		     task = new WordnikLookup(word.toLowerCase());
		     task.execute("");
	     }
	     
	     if (bNoThanks.getVisibility() == View.VISIBLE){
    		 bStore.setOnClickListener(this);
		     bNoThanks.setOnClickListener(this);
    	 }
	     this.trackEvent(Constants.TRACKER_ACTION_WORD_LOOKED_UP, this.word, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	     
	}
	
	public void trackEvent(String action, String label, long value){
		try{
			this.getTracker().sendEvent(Constants.TRACKER_CATEGORY_GAME_LOOKUP, action,label, value);
		}
		catch (Exception e){
  			Logger.d(TAG, "trackEvent action=" + (action == null ? "null" : action) 
  					 + " label=" + (label == null ? "null" : label)  + " value=" + value +" e=" + e.toString());
  			
		}
	}
	
	@Override
	protected void onStart() {
		 
		super.onStart();
		 EasyTracker.getInstance().activityStart(this);
	}


	@Override
	protected void onStop() {
	 
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if (this.spinner != null){
			this.spinner.dismiss();
		}
	}


	private class WordnikLookup extends AsyncTask<String, Void, List<Definition>> {
		 private String word;
		
		  public WordnikLookup(String word){
			this.word = word;
		  }
		
	      @Override
	      protected List<Definition> doInBackground(String... params) {

	    	  List<Definition> def = new ArrayList<Definition>();
			try {
				//Logger.d(TAG, "WordnikLookup definitions about to be called");
				def = WordApi.definitions(word, 60, null, false, null, true, false );
				/*	(String word, int limit,
							   Set<PartOfSpeech> partOfSpeech, boolean includeRelated,
							   Set<SourceDictionary> sourceDictionaries,
							   boolean useCanonical, boolean includeTags)
				*/	 
			} catch (KnickerException e) {
				// TODO Auto-generated catch block
				Logger.d(TAG, "Wordnik lookup error=" + e.toString());
			}
	    	  
	    	  return def;
	      }      

	      @Override
	      protected void onPostExecute(List<Definition> results) {  
	    	  int i = 1;
	    	//  Logger.d(TAG, "wordnik onPostExecute result.size=" + result.size());
	    	  fillDefinitionsView(results);
	    	 
	      }

	      @Override
	      protected void onPreExecute() {
	      }

	      @Override
	      protected void onProgressUpdate(Void... values) {
	      }
	 }

	 public void fillDefinitionsView(List<Definition> results){
		 int i = 1;
		 
		 if (results.size() > 0) {
   		  tvNotFound.setVisibility(View.GONE);
	   	  //if no results....write "no definition found" message
		    	  for (Definition d : results) {
		    		 // Toast toast = Toast.makeText(context, (i++) + ") " + d.getPartOfSpeech() + ": " + d.getText(), 1000);
		    		 // toast.show();
		    		  llDefs.addView(getDefinitionView(i, d));
			          i += 1;
		    	  }   
		    	  llDefs.setVisibility(View.VISIBLE);
	   	  }
	   	  else {
	   		  tvNotFound.setVisibility(View.VISIBLE);
	   		  llDefs.setVisibility(View.GONE);	
	   		  tvNotFound.setText(context.getString(R.string.lookup_definition_not_found));
	   	  }
		 
		 if (spinner != null){
				spinner.dismiss();
		 }
	 }
	
	  public View getDefinitionView(int num, Definition definition ) {

	  		View view = LayoutInflater.from(this).inflate(R.layout.gamelookupitem,null);
	  
		 	TextView tvNum = (TextView)view.findViewById(R.id.tvNum);
		 //TextView tvPartOfSpeech = (TextView)view.findViewById(R.id.tvPartOfSpeech);
		 	TextView tvDefinition = (TextView)view.findViewById(R.id.tvDefinition);
		 	TextView tvAttribution = (TextView)view.findViewById(R.id.tvAttribution);

		 	tvNum.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 	tvDefinition.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 	tvAttribution.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 	
		 	tvNum.setText(String.format(this.getString(R.string.lookup_definition_num), num));
		 	tvDefinition.setText(String.format(this.getString(R.string.lookup_definition), definition.getPartOfSpeech(), definition.getText()));
		 	//tvDefinition.setText(definition.getText());
		 	tvAttribution.setText(definition.getAttributionText());
		 
	  	    return view;
	  	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){  
        case R.id.ivWordnik:  
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Constants.WORDNIK_WORD_URL, this.word.toLowerCase())));
	   		startActivity(browserIntent);
        	break;
        case R.id.llOK:
        	this.finish();
			break;
		case R.id.bStore: 
			Intent intent = new Intent(this, com.riotapps.word.Store.class);
			this.startActivity(intent); 
			break;
		case R.id.bNoThanks:
			this.finish();
			break;
		default:
			this.finish();
			break;
		}
		
	}
	
	private void setupFonts(){
		TextView tvWord = (TextView)findViewById(R.id.tvWord);
		TextView header = (TextView)findViewById(R.id.header);
		TextView tvPreviewMessage = (TextView)findViewById(R.id.tvPreviewMessage);
		TextView tvNotFound = (TextView)findViewById(R.id.tvNotFound);
			 
		Button bStore = (Button)findViewById(R.id.bStore);
		Button bNoThanks = (Button)findViewById(R.id.bNoThanks);
		
		bNoThanks.setTypeface(ApplicationContext.getMainFontTypeface());
		bStore.setTypeface(ApplicationContext.getMainFontTypeface());
		
		tvWord.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		
		header.setTypeface(ApplicationContext.getMainFontTypeface());
		tvPreviewMessage.setTypeface(ApplicationContext.getMainFontTypeface());
		tvNotFound.setTypeface(ApplicationContext.getMainFontTypeface());		
		
	}
}