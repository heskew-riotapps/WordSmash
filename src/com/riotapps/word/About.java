package com.riotapps.word;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
 

public class About extends FragmentActivity implements View.OnClickListener{
	
	private final Context context = this;
	
	//attribution to smiley in here
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.about);
	        
	        PlayerService.loadPlayerInHeader(this);
	        
	        TextView tvSupportText = (TextView)this.findViewById(R.id.tvSupportText);
	        TextView tvSupportLink = (TextView)this.findViewById(R.id.tvSupportLink);
	        TextView tvVersion = (TextView)this.findViewById(R.id.tvVersion);
	        TextView tvVersionName = (TextView)this.findViewById(R.id.tvVersionName);
	        TextView tvBuildNumber = (TextView)this.findViewById(R.id.tvBuildNumber);
	        TextView tvSmileyAttribution = (TextView)this.findViewById(R.id.tvSmileyAttribution);
	        TextView tvSmileyAttributionLink = (TextView)this.findViewById(R.id.tvSmileyAttributionLink);
	        TextView tvDeviceSpecs = (TextView)this.findViewById(R.id.tvDeviceSpecs); 

	        
	        String versionCode = this.getString(R.string.version);
	        String versionName = this.getString(R.string.versionName);
	      
	        /*
	        try {
				PackageInfo pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
				versionCode = pinfo.versionCode;
				versionName = pinfo.versionName;
				
			} catch (NameNotFoundException e) {
				versionCode = 2;
				versionName = "x";
			}
			*/
	        
	        tvBuildNumber.setText(String.format(this.getString(R.string.about_build_number), this.getString(R.string.build)));
	        tvVersion.setText(String.format(this.getString(R.string.about_version), versionCode));	
	        tvVersionName.setText(String.format(this.getString(R.string.version_name_display),versionName));
	        tvDeviceSpecs.setText(String.format(this.getString(R.string.about_device_specs), this.getString(R.string.derived_device_screen_size), this.getString(R.string.derived_device_resolution)));
	
	        tvSupportLink.setOnClickListener(this);
	        tvSupportText.setOnClickListener(this);
	        tvSupportText.setOnClickListener(this);
	        tvSmileyAttribution.setOnClickListener(this);
	        tvSmileyAttributionLink.setOnClickListener(this);
	        
	        MenuUtils.hideMenu(this);
	        MenuUtils.setHeaderTitle(this, this.getString(R.string.header_title_about));
	        this.setupFonts();
	        
	        AdView adView = (AdView)this.findViewById(R.id.adView);
	    	if (StoreService.isHideBannerAdsPurchased()){	
				adView.setVisibility(View.GONE);
			}
	    	else {
	    		adView.loadAd(new AdRequest());
	    	}
	 }


	@Override
	public void onClick(View v) {
		Intent browserIntent;
		
		switch(v.getId()){  
        case R.id.tvSupportLink:  
        case R.id.tvSupportText:  
        	browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SUPPORT_SITE_URL));
	   		startActivity(browserIntent);
			break;
        case R.id.tvSmileyAttributionLink:
        case R.id.tvSmileyAttribution:
        	browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.CODICODE_URL));
	   		startActivity(browserIntent);
			break;
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
	
	private void setupFonts(){
		TextView about_main = (TextView)findViewById(R.id.about_main);	
		TextView tvVersion = (TextView)findViewById(R.id.tvVersion);	
		TextView tvVersionName = (TextView)findViewById(R.id.tvVersionName);
		TextView tvBuildNumber = (TextView)findViewById(R.id.tvBuildNumber);
		TextView tvSupportText = (TextView)findViewById(R.id.tvSupportText);
		TextView tvSupportLink = (TextView)findViewById(R.id.tvSupportLink);
		TextView tvSmileyAttribution = (TextView)findViewById(R.id.tvSmileyAttribution);
		TextView tvSmileyAttributionLink = (TextView)findViewById(R.id.tvSmileyAttributionLink);

		about_main.setTypeface(ApplicationContext.getMainFontTypeface());
		tvVersion.setTypeface(ApplicationContext.getMainFontTypeface());
		tvVersionName.setTypeface(ApplicationContext.getMainFontTypeface());
		tvBuildNumber.setTypeface(ApplicationContext.getMainFontTypeface());
		tvSupportText.setTypeface(ApplicationContext.getMainFontTypeface());
		tvSupportLink.setTypeface(ApplicationContext.getMainFontTypeface());
		tvSmileyAttribution.setTypeface(ApplicationContext.getMainFontTypeface());
		tvSmileyAttributionLink.setTypeface(ApplicationContext.getMainFontTypeface());
		
	}

}
