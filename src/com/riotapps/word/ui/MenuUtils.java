package com.riotapps.word.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.riotapps.word.About;
import com.riotapps.word.Badges;
import com.riotapps.word.FullRules;
import com.riotapps.word.R;
import com.riotapps.word.Store;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;

public class MenuUtils {
	public static void fillMenu(FragmentActivity context, android.widget.PopupMenu popupMenu){

	  	  //if player has any completed games show completed games menu option
	  	  popupMenu.getMenu().add(Menu.NONE, Constants.MENU_COMPLETED_GAMES, Menu.NONE, context.getString(R.string.main_menu_completed_games));
	        popupMenu.getMenu().add(Menu.NONE,  Constants.MENU_RULES, Menu.NONE, context.getString(R.string.main_menu_rules));
	        popupMenu.getMenu().add(Menu.NONE,  Constants.MENU_STORE, Menu.NONE, context.getString(R.string.main_menu_store));
	        popupMenu.getMenu().add(Menu.NONE,  Constants.MENU_SHARE, Menu.NONE, context.getString(R.string.main_menu_share));
	        popupMenu.getMenu().add(Menu.NONE,  Constants.MENU_ABOUT, Menu.NONE, context.getString(R.string.main_menu_about));
	     
		}

		public static boolean handleMenuClick(int option){
			Intent intent;
			ApplicationContext appContext = (ApplicationContext)ApplicationContext.getAppContext().getApplicationContext();
			switch (option) {
	        case Constants.MENU_ABOUT:
	     	   intent = new Intent(appContext, About.class);
	     	  appContext.startActivity(intent);	
	            break;
	        case Constants.MENU_COMPLETED_GAMES:
	     	   intent = new Intent(appContext, About.class);
	     	  appContext.startActivity(intent);	
	            break;
	        case Constants.MENU_BADGES:
	     	   intent = new Intent(appContext, Badges.class);
	     	  appContext.startActivity(intent);	
	            break;
	        case Constants.MENU_RULES:
	     	   intent = new Intent(appContext, FullRules.class);
	     	  appContext.startActivity(intent);	
	            break;
	        case Constants.MENU_STORE:
	     	   intent = new Intent(appContext, Store.class);
	     	  appContext.startActivity(intent);	
	            break;
	        case Constants.MENU_SHARE:
	     	   Intent sendIntent = new Intent();
	     	   sendIntent.setAction(Intent.ACTION_SEND);
	     	   sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
	     	   sendIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my text to send.");
	     	   sendIntent.setType("text/plain");
	     	   appContext.startActivity(Intent.createChooser(sendIntent, appContext.getString(R.string.share_using)));
	        }  
	        return false;

		}

}
