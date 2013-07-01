package com.riotapps.word;

import java.util.List;

import com.riotapps.word.hooks.OpponentGroup;
import com.riotapps.word.hooks.OpponentGroupService;
import com.riotapps.word.hooks.OpponentService;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreItem;
import com.riotapps.word.hooks.StoreService;
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
	        this.loadStoreItems();
	        MenuUtils.hideMenu(this);
	 }
	
	
	 private void loadStoreItems(){

	    	LinearLayout llItems = (LinearLayout)findViewById(R.id.llItems);
	   
	    	llItems.removeAllViews();

	    	ApplicationContext.captureTime(TAG, "loadList opponents fetch starting");
	    	
	    	List<StoreItem> storeItems = StoreService.getStoreItems();

	    		 
	        for (StoreItem item : storeItems){
	 	        	llItems.addView(getStoreItemView(item));
			}

	    }
	    
		
	    public View getStoreItemView(StoreItem item) {

	  		View view = this.inflater.inflate(R.layout.character_item, null);
	 
 
		/* 	TextView tvCharacter = (TextView)view.findViewById(R.id.tvCharacter);
		 
 
		 	tvCharacter.setText(opponentGroup.getName());
		  
		 	 
		 	view.setTag(opponentGroup.getId());
		 	view.setOnClickListener(this);
	  	  */
	  	    return view;
	  	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		//call google payment integration here
	//	OpponentGroup og = OpponentGroupService.getInactiveOpponentGroup(Integer.parseInt(v.getTag().toString()));
	//	Logger.d(TAG, "tag=" + v.getTag().toString());
	//	og.setActivated(true);
		
	//	OpponentGroupService.saveOpponentGroup(og);
		
	//	this.appContext.setOpponents(OpponentService.getActivatedOpponents());
	}
	
	
	

}
