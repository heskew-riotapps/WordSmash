package com.riotapps.word;

import java.util.List;

import com.riotapps.word.hooks.OpponentGroup;
import com.riotapps.word.hooks.OpponentGroupService;
import com.riotapps.word.hooks.OpponentService;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Logger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Store  extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = Store.class.getSimpleName();
	 
	private	ApplicationContext appContext;
	private	LayoutInflater inflater;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.store);
	        
	        PlayerService.loadPlayerInHeader(this);
	        this.appContext = (ApplicationContext)this.getApplicationContext(); 
	        this.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        this.loadCharacterOptions();
	        MenuUtils.hideMenu(this);
	 }
	
	
	 private void loadCharacterOptions(){
		 
			ApplicationContext.captureTime(TAG, "loadList starting");
	 
	    	LinearLayout llItems = (LinearLayout)findViewById(R.id.llItems);
	   
	    	llItems.removeAllViews();

	    	
	  //	Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() size=" + this.player.getActiveGamesYourTurn().size() );

	    	ApplicationContext.captureTime(TAG, "loadList opponents fetch starting");
	    	
	    	List<OpponentGroup> opponentGroups = OpponentGroupService.getInactiveOpponentGroups();
	    	int i = 1;
	    	int j = 1;
	    	
	 
	    	
 
		  //	int w  = displaymetrics.widthPixels;
	    	//if opponents lists is empty, something is screwy
	    	if (opponentGroups.size() > 0){    	
	     		
	    		
	    		LinearLayout llHorizontal = null;
	    		
	    		 
		        for (OpponentGroup o : opponentGroups){
		        	//if (i == 3){break;}
		        	//Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() game=" +g.getId() );
		        	 
		        	llItems.addView(getOpponentGroupView(o));
		        	 
				}
		       
	    	}
	    	else{
		        	llItems.setVisibility(View.GONE); //?? maybe show all but indicate of already bought
		        }

	    }
	    
		
	    public View getOpponentGroupView(OpponentGroup opponentGroup) {

	  		View view = this.inflater.inflate(R.layout.character_item, null);
	 
 
		 	TextView tvCharacter = (TextView)view.findViewById(R.id.tvCharacter);
		 
 
		 	tvCharacter.setText(opponentGroup.getName());
		  
		 	 
		 	view.setTag(opponentGroup.getId());
		 	view.setOnClickListener(this);
	  	    return view;
	  	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		//call google payment integration here
		OpponentGroup og = OpponentGroupService.getInactiveOpponentGroup(Integer.parseInt(v.getTag().toString()));
		Logger.d(TAG, "tag=" + v.getTag().toString());
		og.setActivated(true);
		
		OpponentGroupService.saveOpponentGroup(og);
		
		this.appContext.setOpponents(OpponentService.getActivatedOpponents());
	}
	
	
	

}
