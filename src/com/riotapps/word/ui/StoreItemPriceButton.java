package com.riotapps.word.ui;

import com.riotapps.word.utils.ApplicationContext;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class StoreItemPriceButton extends Button{

	public StoreItemPriceButton(Context context, AttributeSet attrs){
		super(context, attrs);
		
		  //Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.SCOREBOARD_BUTTON_FONT);
		setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		
	}
}

