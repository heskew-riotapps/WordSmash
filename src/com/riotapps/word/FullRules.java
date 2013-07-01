package com.riotapps.word;

import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.ui.MenuUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class FullRules extends FragmentActivity{
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fullrules);
	        
	        TextView t2 = (TextView) findViewById(R.id.fullRules2);
	        t2.setMovementMethod(LinkMovementMethod.getInstance());
	        
	        TextView fullRulesDictionary2 = (TextView) findViewById(R.id.fullRulesDictionary2);
	        fullRulesDictionary2.setMovementMethod(LinkMovementMethod.getInstance());

			PlayerService.loadPlayerInHeader(this);  
	        MenuUtils.hideMenu(this);
	        
	        if (StoreService.isHideBannerAdsPurchased()){
				AdView adView = (AdView)this.findViewById(R.id.adView);
				adView.setVisibility(View.GONE);
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
}
