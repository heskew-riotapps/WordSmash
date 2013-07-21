package com.riotapps.word;

import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.utils.ApplicationContext;

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
	        
	        TextView t2 = (TextView) findViewById(R.id.tvFullRulesBasics_2);
	        t2.setMovementMethod(LinkMovementMethod.getInstance());
	        
	        TextView tvFullRulesDictionary2 = (TextView) findViewById(R.id.tvFullRulesDictionary_2);
	        tvFullRulesDictionary2.setMovementMethod(LinkMovementMethod.getInstance());

			PlayerService.loadPlayerInHeader(this);  
	        MenuUtils.hideMenu(this);
	        MenuUtils.setHeaderTitle(this, this.getString(R.string.header_title_rules));
	        
	        this.setupFonts();
	        
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
	
	private void setupFonts(){
	
		TextView tvFullRulesBasics_0 = (TextView)findViewById(R.id.tvFullRulesBasics_0);	
		TextView tvFullRulesBasics_1 = (TextView)findViewById(R.id.tvFullRulesBasics_1);	
		TextView tvFullRulesBasics_2 = (TextView)findViewById(R.id.tvFullRulesBasics_2);
		TextView tvFullRulesBasics_3 = (TextView)findViewById(R.id.tvFullRulesBasics_3);
		TextView tvFullRulesBasics_4 = (TextView)findViewById(R.id.tvFullRulesBasics_4);
		TextView tvFullRulesLetters_1 = (TextView)findViewById(R.id.tvFullRulesLetters_1);
		TextView tvFullRulesLetters_2 = (TextView)findViewById(R.id.tvFullRulesLetters_2);
		TextView tvFullRulesLetters_3 = (TextView)findViewById(R.id.tvFullRulesLetters_3);
		TextView tvFullRulesLetters_1_num = (TextView)findViewById(R.id.tvFullRulesLetters_1_num);
		TextView tvFullRulesLetters_2_num = (TextView)findViewById(R.id.tvFullRulesLetters_2_num);
		TextView tvFullRulesLetters_3_num = (TextView)findViewById(R.id.tvFullRulesLetters_3_num);
		TextView tvFullRulesLetters_4_num = (TextView)findViewById(R.id.tvFullRulesLetters_4_num);
		TextView tvFullRulesLetters_5_num = (TextView)findViewById(R.id.tvFullRulesLetters_5_num);
		TextView tvFullRulesLetters_6_num = (TextView)findViewById(R.id.tvFullRulesLetters_6_num);
		TextView tvFullRulesLetters_7_num = (TextView)findViewById(R.id.tvFullRulesLetters_7_num);
		TextView tvFullRulesLetters_8_num = (TextView)findViewById(R.id.tvFullRulesLetters_8_num);
		TextView tvFullRulesLetters_9_num = (TextView)findViewById(R.id.tvFullRulesLetters_9_num);
		TextView tvFullRulesLetters_10_num = (TextView)findViewById(R.id.tvFullRulesLetters_10_num);
		TextView tvFullRulesLetters_11_num = (TextView)findViewById(R.id.tvFullRulesLetters_11_num);
		TextView tvFullRulesLetters_12_num = (TextView)findViewById(R.id.tvFullRulesLetters_12_num);
		TextView tvFullRulesLetters_13_num = (TextView)findViewById(R.id.tvFullRulesLetters_13_num);
		TextView tvFullRulesLetters_14_num = (TextView)findViewById(R.id.tvFullRulesLetters_14_num);
		TextView tvFullRulesLetters_15_num = (TextView)findViewById(R.id.tvFullRulesLetters_15_num);
		TextView tvFullRulesLetters_16_num = (TextView)findViewById(R.id.tvFullRulesLetters_16_num);
		TextView tvFullRulesLetters_17_num = (TextView)findViewById(R.id.tvFullRulesLetters_17_num);
		TextView tvFullRulesLetters_18_num = (TextView)findViewById(R.id.tvFullRulesLetters_18_num);
		TextView tvFullRulesLetters_19_num = (TextView)findViewById(R.id.tvFullRulesLetters_19_num);
		TextView tvFullRulesLetters_20_num = (TextView)findViewById(R.id.tvFullRulesLetters_20_num);
		TextView tvFullRulesLetters_21_num = (TextView)findViewById(R.id.tvFullRulesLetters_21_num);
		TextView tvFullRulesLetters_22_num = (TextView)findViewById(R.id.tvFullRulesLetters_22_num);
		TextView tvFullRulesLetters_23_num = (TextView)findViewById(R.id.tvFullRulesLetters_23_num);
		TextView tvFullRulesLetters_24_num = (TextView)findViewById(R.id.tvFullRulesLetters_24_num);
		TextView tvFullRulesLetters_25_num = (TextView)findViewById(R.id.tvFullRulesLetters_25_num);
		TextView tvFullRulesLetters_26_num = (TextView)findViewById(R.id.tvFullRulesLetters_26_num);
		TextView tvFullRulesScoring_1 = (TextView)findViewById(R.id.tvFullRulesScoring_1);
		TextView tvFullRulesScoring_2 = (TextView)findViewById(R.id.tvFullRulesScoring_2);
		TextView tvFullRulesScoring_3 = (TextView)findViewById(R.id.tvFullRulesScoring_3);
		TextView tvFullRulesScoring_4 = (TextView)findViewById(R.id.tvFullRulesScoring_4);
		TextView tvFullRulesScoring_5 = (TextView)findViewById(R.id.tvFullRulesScoring_5);
		TextView tvFullRulesScoring_6 = (TextView)findViewById(R.id.tvFullRulesScoring_6);
		TextView tvFullRulesScoring_7 = (TextView)findViewById(R.id.tvFullRulesScoring_7);
		TextView tvFullRulesLetters_1_value = (TextView)findViewById(R.id.tvFullRulesLetters_1_value);
		TextView tvFullRulesLetters_2_value = (TextView)findViewById(R.id.tvFullRulesLetters_2_value);
		TextView tvFullRulesLetters_3_value = (TextView)findViewById(R.id.tvFullRulesLetters_3_value);
		TextView tvFullRulesLetters_4_value = (TextView)findViewById(R.id.tvFullRulesLetters_4_value);
		TextView tvFullRulesLetters_5_value = (TextView)findViewById(R.id.tvFullRulesLetters_5_value);
		TextView tvFullRulesLetters_6_value = (TextView)findViewById(R.id.tvFullRulesLetters_6_value);
		TextView tvFullRulesLetters_7_value = (TextView)findViewById(R.id.tvFullRulesLetters_7_value);
		TextView tvFullRulesLetters_8_value = (TextView)findViewById(R.id.tvFullRulesLetters_8_value);
		TextView tvFullRulesLetters_9_value = (TextView)findViewById(R.id.tvFullRulesLetters_9_value);
		TextView tvFullRulesLetters_10_value = (TextView)findViewById(R.id.tvFullRulesLetters_10_value);
		TextView tvFullRulesLetters_11_value = (TextView)findViewById(R.id.tvFullRulesLetters_11_value);
		TextView tvFullRulesLetters_12_value = (TextView)findViewById(R.id.tvFullRulesLetters_12_value);
		TextView tvFullRulesLetters_13_value = (TextView)findViewById(R.id.tvFullRulesLetters_13_value);
		TextView tvFullRulesLetters_14_value = (TextView)findViewById(R.id.tvFullRulesLetters_14_value);
		TextView tvFullRulesLetters_15_value = (TextView)findViewById(R.id.tvFullRulesLetters_15_value);
		TextView tvFullRulesLetters_16_value = (TextView)findViewById(R.id.tvFullRulesLetters_16_value);
		TextView tvFullRulesLetters_17_value = (TextView)findViewById(R.id.tvFullRulesLetters_17_value);
		TextView tvFullRulesLetters_18_value = (TextView)findViewById(R.id.tvFullRulesLetters_18_value);
		TextView tvFullRulesLetters_19_value = (TextView)findViewById(R.id.tvFullRulesLetters_19_value);
		TextView tvFullRulesLetters_20_value = (TextView)findViewById(R.id.tvFullRulesLetters_20_value);
		TextView tvFullRulesLetters_21_value = (TextView)findViewById(R.id.tvFullRulesLetters_21_value);
		TextView tvFullRulesLetters_22_value = (TextView)findViewById(R.id.tvFullRulesLetters_22_value);
		TextView tvFullRulesLetters_23_value = (TextView)findViewById(R.id.tvFullRulesLetters_23_value);
		TextView tvFullRulesLetters_24_value = (TextView)findViewById(R.id.tvFullRulesLetters_24_value);
		TextView tvFullRulesLetters_25_value = (TextView)findViewById(R.id.tvFullRulesLetters_25_value);
		TextView tvFullRulesLetters_26_value = (TextView)findViewById(R.id.tvFullRulesLetters_26_value);
		TextView tvFullRulesScoringBonus_2l = (TextView)findViewById(R.id.tvFullRulesScoringBonus_2l);
		TextView tvFullRulesScoringBonus_3l = (TextView)findViewById(R.id.tvFullRulesScoringBonus_3l);
		TextView tvFullRulesScoringBonus_4l = (TextView)findViewById(R.id.tvFullRulesScoringBonus_4l);
		TextView tvFullRulesScoringBonus_2w = (TextView)findViewById(R.id.tvFullRulesScoringBonus_2w);
		TextView tvFullRulesScoringBonus_3w = (TextView)findViewById(R.id.tvFullRulesScoringBonus_3w);
		TextView tvFullRulesScoringSmasher_1 = (TextView)findViewById(R.id.tvFullRulesScoringSmasher_1);
		TextView tvFullRulesScoringSmasher_2 = (TextView)findViewById(R.id.tvFullRulesScoringSmasher_2);
		
		TextView tvFullRulesFinish_1 = (TextView)findViewById(R.id.tvFullRulesFinish_1);
		TextView tvFullRulesFinish_2 = (TextView)findViewById(R.id.tvFullRulesFinish_2);
		TextView tvFullRulesFinish_3 = (TextView)findViewById(R.id.tvFullRulesFinish_3);
		TextView tvFullRulesFinish_4 = (TextView)findViewById(R.id.tvFullRulesFinish_4);
		TextView tvFullRulesDictionary_1 = (TextView)findViewById(R.id.tvFullRulesDictionary_1);
		TextView tvFullRulesDictionary_2 = (TextView)findViewById(R.id.tvFullRulesDictionary_2);
	
		
		tvFullRulesBasics_0.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_3.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_4.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_3.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_1_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_2_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_3_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_4_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_5_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_6_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_7_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_8_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_9_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_10_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_11_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_12_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_13_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_14_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_15_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_16_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_17_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_18_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_19_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_20_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_21_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_22_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_23_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_24_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_25_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_26_num.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_3.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_4.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_5.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_6.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_7.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_1_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_2_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_3_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_4_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_5_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_6_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_7_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_8_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_9_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_10_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_11_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_12_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_13_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_14_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_15_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_16_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_17_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_18_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_19_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_20_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_21_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_22_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_23_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_24_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_25_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_26_value.setTypeface(ApplicationContext.getMainFontTypeface());
		
		tvFullRulesScoringBonus_2l.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoringBonus_3l.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoringBonus_4l.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoringBonus_2w.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoringBonus_3w.setTypeface(ApplicationContext.getMainFontTypeface());
		
		tvFullRulesScoringSmasher_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoringSmasher_2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesFinish_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesFinish_2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesFinish_3.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesFinish_4.setTypeface(ApplicationContext.getMainFontTypeface());
		
		tvFullRulesDictionary_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesDictionary_2.setTypeface(ApplicationContext.getMainFontTypeface());
	}
}
