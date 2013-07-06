package com.riotapps.word.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.riotapps.word.About;
import com.riotapps.word.CompletedGames;
import com.riotapps.word.FullRules;
import com.riotapps.word.R;
import com.riotapps.word.Store;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

public class MenuUtils {
	private static final String TAG = MenuUtils.class.getSimpleName();
	
	public static void fillMenu(FragmentActivity context, PopupMenu popupMenu){

			 
	  	  //if player has any completed games show completed games menu option
			if (GameService.getCompletedGames().size() > 0 ){
					popupMenu.getMenu().add(Menu.NONE, Constants.MENU_COMPLETED_GAMES, Menu.NONE, context.getString(R.string.main_menu_completed_games));
			}
			popupMenu.getMenu().add(Menu.NONE,  Constants.MENU_STORE, Menu.NONE, context.getString(R.string.main_menu_store));
	        popupMenu.getMenu().add(Menu.NONE,  Constants.MENU_RULES, Menu.NONE, context.getString(R.string.main_menu_rules));
	        popupMenu.getMenu().add(Menu.NONE,  Constants.MENU_SHARE, Menu.NONE, context.getString(R.string.main_menu_share));
	        popupMenu.getMenu().add(Menu.NONE,  Constants.MENU_ABOUT, Menu.NONE, context.getString(R.string.main_menu_about));
	     
		}
	
		public static void hideMenu(FragmentActivity context){
			ImageButton popupMenu = (ImageButton)context.findViewById(R.id.options);	
	    	  popupMenu.setVisibility(View.GONE);
		}
		
		public static void hideHeaderTitle(FragmentActivity context){
			TextView tvActivityTitle = (TextView)context.findViewById(R.id.tvActivityTitle);	
			tvActivityTitle.setVisibility(View.GONE);
		}
		public static void setHeaderTitle(FragmentActivity context, String title){
			TextView tvActivityTitle = (TextView)context.findViewById(R.id.tvActivityTitle);	
			tvActivityTitle.setText(title);
		}

		public static boolean handleMenuClick(FragmentActivity context, int option){
			Intent intent;
			boolean ok = false;
			Logger.d(TAG, "handleMenuClick clicked");
			switch (option) {
		        case Constants.MENU_ABOUT:
		     	   intent = new Intent(context, About.class);
		     	   context.startActivity(intent);
		     	   ok = true;
		           break;
		        case Constants.MENU_COMPLETED_GAMES:
		     	   intent = new Intent(context, CompletedGames.class);
		     	   context.startActivity(intent);	
		     	   ok = true;
		           break;
		        case Constants.MENU_RULES:
		     	   intent = new Intent(context, FullRules.class);
		     	   context.startActivity(intent);
		     	   ok = true;
		           break;
		        case Constants.MENU_STORE:
		     	   intent = new Intent(context, Store.class);
		     	   context.startActivity(intent);
		     	   ok = true;
		           break;
		        case Constants.MENU_SHARE:
		     	   Intent sendIntent = new Intent();
		     	   sendIntent.setAction(Intent.ACTION_SEND);
		     	   sendIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message));
		     	   sendIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_subject));
		     	   sendIntent.setType("text/plain");
		     	   context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_using)));
		     	   ok = true;
	        }  
	        return ok;

		}

}
