package com.riotapps.word;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
 
import com.google.ads.AdView;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Opponent;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

public class CompletedGames extends FragmentActivity {
	private static final String TAG = CompletedGames.class.getSimpleName();
	
	private final Context context = this;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.completedgames);
	        
	         
	        
	        PlayerService.loadPlayerInHeader(this);
	         
	        ApplicationContext.captureTime(TAG, "oncreate about to hide menu");
	        MenuUtils.hideMenu(this);
	        ApplicationContext.captureTime(TAG, "oncreate hide menu completed");
	        ApplicationContext.captureTime(TAG, "oncreate about to loadlist"); 
	        this.loadList();
	        ApplicationContext.captureTime(TAG, "loadlist copleted");
	    	if (StoreService.isHideBannerAdsPurchased()){
				AdView adView = (AdView)this.findViewById(R.id.adView);
				adView.setVisibility(View.GONE);
			}
	    	   ApplicationContext.captureTime(TAG, "oncreate completed");
	 }
	  private void loadList(){
		  
		  CompletedGameArrayAdapter adapter = new CompletedGameArrayAdapter(this, GameService.getCompletedGames());
	    
	    	ListView lvGames = (ListView) findViewById(R.id.lvGames);
	    	lvGames.setAdapter(adapter); 
	    	
	    	lvGames.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {
	    
	            	LinearLayout llItem = (LinearLayout) view.findViewById(R.id.llItem);
	            	
	            	String gameId = llItem.getTag().toString();
	  			
	            	Intent intent = new Intent(CompletedGames.this, com.riotapps.word.GameSurface.class);
	          		intent.putExtra(Constants.EXTRA_GAME_ID, gameId);
	          		intent.putExtra(Constants.EXTRA_FROM_COMPLETED_GAME_LIST, true);
	          		CompletedGames.this.startActivity(intent); 
	            }
	        });

	    }
	  private class CompletedGameArrayAdapter extends ArrayAdapter<Game> {
	   	  private final CompletedGames context;
 
	   	  private int itemCount = 0;;
	   	  private List<Game> values;
	   	  LayoutInflater inflater;
	   
	   	  public CompletedGameArrayAdapter(CompletedGames context, List<Game> games){  
	   	    super(context, R.layout.completed_game_item, games);
	    	    this.context = context;
	    	    this.values = games;

	    	    this.itemCount = games.size();
	    	    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	  }
	   	  
	    	  @Override
	    	  public View getView(int position, View rowView, ViewGroup parent) {

	    		  if ( rowView == null ) {
	    			  rowView = inflater.inflate(R.layout.completed_game_item, parent, false);
	    		  }

	    	   // View rowView = inflater.inflate(R.layout.choosefbfrienditem, parent, false);
	    		  //RelativeLayout rlItem = (RelativeLayout) rowView.findViewById(R.id.rlItem);
	    		  ImageView ivOpponent = (ImageView) rowView.findViewById(R.id.ivOpponent);
	    		  TextView tvOpponent = (TextView) rowView.findViewById(R.id.tvOpponent);
	    		  TextView tvSummary = (TextView) rowView.findViewById(R.id.tvSummary);
	    		  TextView tvSkillLevel = (TextView) rowView.findViewById(R.id.tvSkillLevel);
	    		  LinearLayout llBottomBorder = (LinearLayout)rowView.findViewById(R.id.llBottomBorder);
	    		   
		    	  Game game = values.get(position);// values[position];
		    	  Opponent opponent = game.getOpponent();
		    	  
		    	//  int opponentImageId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponent.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_MAIN), null, null);
 		  		 //  ivOpponent.setImageResource(opponentImageId);
 		  		  
 		  		//  Bitmap bm = GameSurfaceView.decodeSampledBitmapFromResource(getResources(), opponentImageId, 64, 64);
 		  		  //Bitmap bm = Bitmap.createScaledBitmap(bm, 64, 64, false);
 		  		  ivOpponent.setImageBitmap(opponent.getSmallBitmap());
		    	  tvOpponent.setText(opponent.getName());
		    	  tvSkillLevel.setText(String.format(context.getString(R.string.game_list_skill_level), opponent.getSkillLevelText(context)));
		    	  tvSummary.setText(game.getLastActionTextForList(context));
		    	  
		    	  
		    	  if (position == this.itemCount - 1){ //last item
		    		   //Logger.d(TAG, "position=wordCount");
			   		//	RelativeLayout rlLineItem = (RelativeLayout)rowView.findViewById(R.id.rlItem);
			   		//	int bgLineItem = context.getResources().getIdentifier("com.riotapps.word:drawable/text_selector_bottom", null, null);
			   		//	rlLineItem.setBackgroundResource(bgLineItem);
			   			//LinearLayout llBottomBorder = (LinearLayout)rowView.findViewById(R.id.llBottomBorder);
			   			llBottomBorder.setVisibility(View.INVISIBLE);
		    	   }
		    	   else{
		    		   llBottomBorder.setVisibility(View.VISIBLE);
		    	   }
		    	// bm = null;
		    	  rowView.setTag(game.getId());
		    	  return rowView;
	    	  }
 
	    }
	
}
