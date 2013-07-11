package com.riotapps.word.ui;

import java.util.ArrayList;
import java.util.List;

import net.jeremybrooks.knicker.KnickerException;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.Definition;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.riotapps.word.GameHistory;
import com.riotapps.word.GameSurface;
import com.riotapps.word.R;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.CustomProgressDialog;
import com.riotapps.word.utils.Logger;

public class WordLookupDialog extends Dialog implements View.OnClickListener{
	private static final String TAG = WordLookupDialog.class.getSimpleName();
	
	//private Game game;

	private GameHistory parent;
	private View layout;
	private WordnikLookup task;
	private LinearLayout llDefs;
	private TextView tvNotFound;
	private TextView tvPreviewMessage;
	private CustomProgressDialog spinner;
	private String word;
	ApplicationContext appContext;
	
	public WordLookupDialog(GameHistory context, String word){
		super(context);
		//this.game = game;
		this.word = word;
		this.parent = context;
	//	WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	//	params.horizontalMargin = 10f;
		
	//	getWindow().setAttributes(params);
	   getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//getWindow().setBackgroundDrawable(null);
	}

	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.layout = inflater.inflate(R.layout.wordlookupdialog, null); 
//                                        (ViewGroup) findViewById(R.id.progress_root));
		
		 //loop through letters, filling the views
        
     //   this.setContentView(this.layout);
        
	     TextView tvWord = (TextView)this.layout.findViewById(R.id.tvWord);
	     tvWord.setText(word);
 
	     
	     ImageView ivWordnik = (ImageView)this.layout.findViewById(R.id.ivWordnik);
	     ivWordnik.setOnClickListener(this);
	 
	     this.llDefs = (LinearLayout)this.layout.findViewById(R.id.llDefs);
	     this.tvNotFound = (TextView)this.layout.findViewById(R.id.tvNotFound);
	     this.tvPreviewMessage = (TextView) this.layout.findViewById(R.id.tvPreviewMessage);
	     //tvNotFound.setText(context.getString(R.string.lookup_definition_not_found));
	     
		LinearLayout llOK = (LinearLayout) this.layout.findViewById(R.id.llOK);
 		
	 
		Button bNoThanks = (Button)this.layout.findViewById(R.id.bNoThanks);
		Button bStore = (Button)this.layout.findViewById(R.id.bStore);

		
		Logger.d(TAG, "free lookups=" + PlayerService.getRemainingFreeUsesWordDefinition());
		
	     if (!StoreService.isWordDefinitionLookupPurchased() && PlayerService.getRemainingFreeUsesWordDefinition() == 0){
	    	 Logger.d(TAG, "free previews over");
	    	 this.tvPreviewMessage.setText(this.parent.getString(R.string.word_definition_purchase_offer));
	   		 this.llDefs.setVisibility(View.GONE);
	   		 this.tvNotFound.setVisibility(View.GONE);
	     }
	     else {
	    	 if (!StoreService.isWordDefinitionLookupPurchased()){
					//bStore.setOnClickListener(this);
					//bNoThanks.setOnClickListener(this);
					int remainingFreeUses  = PlayerService.removeAFreeUseFromWordDefinition();
					if (remainingFreeUses > 1){
						this.tvPreviewMessage.setText(String.format(this.parent.getString(R.string.word_definition_preview), String.valueOf(remainingFreeUses)));
					}
					else if (remainingFreeUses == 1){
						this.tvPreviewMessage.setText(this.parent.getString(R.string.word_definition_1_preview_left));					
					}
					else{
						this.tvPreviewMessage.setText(this.parent.getString(R.string.word_definition_no_previews_left));					
					}
				}

		     System.setProperty("WORDNIK_API_KEY", this.parent.getString(R.string.wordnik_apiKey));
		 
		     this.spinner = new CustomProgressDialog(this.parent);
			 this.spinner.setMessage(this.parent.getString(R.string.progress_looking_up));
			 this.spinner.show();
			 
		     task = new WordnikLookup(word.toLowerCase());
		     task.execute("");
		     
		
	     }
	     
	  	this.setContentView(this.layout);
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
	   		  tvNotFound.setText(this.parent.getString(R.string.lookup_definition_not_found));
	   	  }
		 
		 if (spinner != null){
				spinner.dismiss();
		 }
	 }
	
	  public View getDefinitionView(int num, Definition definition ) {

	  		View view = LayoutInflater.from(this.parent).inflate(R.layout.gamelookupitem,null);
	  
		 	TextView tvNum = (TextView)view.findViewById(R.id.tvNum);
		 //TextView tvPartOfSpeech = (TextView)view.findViewById(R.id.tvPartOfSpeech);
		 	TextView tvDefinition = (TextView)view.findViewById(R.id.tvDefinition);
		 	TextView tvAttribution = (TextView)view.findViewById(R.id.tvAttribution);
		 	
		 	tvNum.setText(String.format(this.parent.getString(R.string.lookup_definition_num), num));
		 	tvDefinition.setText(String.format(this.parent.getString(R.string.lookup_definition), definition.getPartOfSpeech(), definition.getText()));
		 	//tvDefinition.setText(definition.getText());
		 	tvAttribution.setText(definition.getAttributionText());
		 
	  	    return view;
	  	}

	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dismiss(){
	 
		super.dismiss();	
	}

}