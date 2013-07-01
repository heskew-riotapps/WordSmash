package com.riotapps.word;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.riotapps.word.hooks.AlphabetService;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Letter;
import com.riotapps.word.ui.MenuUtils;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;

public class HopperPeek extends FragmentActivity{
	private static final String TAG = HopperPeek.class.getSimpleName();
	private Game game;
	ApplicationContext appContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hopperpeek);
		 
		
	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	this.game = GameService.getGame(gameId); 
	 	
		this.appContext = (ApplicationContext)this.getApplicationContext(); 
		
 	 	GameService.loadScoreboard(this, this.game);
 	 	MenuUtils.hideMenu(this);
 	 	
 	 	this.loadLetters();
	}

	private void loadLetters(){
		List<Letter> letters = AlphabetService.getLetters();
		
		TextView tvLetter1 = (TextView)this.findViewById(R.id.tvLetter1);
		TextView tvLetter2 = (TextView)this.findViewById(R.id.tvLetter2);
		TextView tvLetter3 = (TextView)this.findViewById(R.id.tvLetter3);
		TextView tvLetter4 = (TextView)this.findViewById(R.id.tvLetter4);
		TextView tvLetter5 = (TextView)this.findViewById(R.id.tvLetter5);
		TextView tvLetter6 = (TextView)this.findViewById(R.id.tvLetter6);
		TextView tvLetter7 = (TextView)this.findViewById(R.id.tvLetter7);
		TextView tvLetter8 = (TextView)this.findViewById(R.id.tvLetter8);
		TextView tvLetter9 = (TextView)this.findViewById(R.id.tvLetter9);
		TextView tvLetter10 = (TextView)this.findViewById(R.id.tvLetter10);
		TextView tvLetter11 = (TextView)this.findViewById(R.id.tvLetter11);
		TextView tvLetter12 = (TextView)this.findViewById(R.id.tvLetter12);
		TextView tvLetter13 = (TextView)this.findViewById(R.id.tvLetter13);
		TextView tvLetter14 = (TextView)this.findViewById(R.id.tvLetter14);
		TextView tvLetter15 = (TextView)this.findViewById(R.id.tvLetter15);
		TextView tvLetter16 = (TextView)this.findViewById(R.id.tvLetter16);
		TextView tvLetter17 = (TextView)this.findViewById(R.id.tvLetter17);
		TextView tvLetter18 = (TextView)this.findViewById(R.id.tvLetter18);
		TextView tvLetter19 = (TextView)this.findViewById(R.id.tvLetter19);
		TextView tvLetter20 = (TextView)this.findViewById(R.id.tvLetter20);
		TextView tvLetter21 = (TextView)this.findViewById(R.id.tvLetter21);
		TextView tvLetter22 = (TextView)this.findViewById(R.id.tvLetter22);
		TextView tvLetter23 = (TextView)this.findViewById(R.id.tvLetter23);
		TextView tvLetter24 = (TextView)this.findViewById(R.id.tvLetter24);
		TextView tvLetter25 = (TextView)this.findViewById(R.id.tvLetter25);
		TextView tvLetter26 = (TextView)this.findViewById(R.id.tvLetter26);
		
		String baseText = this.getString(R.string.hopper_peek_letter);
		
		tvLetter1.setText(String.format(baseText, letters.get(0).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(0).getCharacter())));
		tvLetter2.setText(String.format(baseText, letters.get(1).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(1).getCharacter())));
		tvLetter3.setText(String.format(baseText, letters.get(2).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(2).getCharacter())));
		tvLetter4.setText(String.format(baseText, letters.get(3).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(3).getCharacter())));
		tvLetter5.setText(String.format(baseText, letters.get(4).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(4).getCharacter())));
		tvLetter6.setText(String.format(baseText, letters.get(5).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(5).getCharacter())));
		tvLetter7.setText(String.format(baseText, letters.get(6).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(6).getCharacter())));
		tvLetter8.setText(String.format(baseText, letters.get(7).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(7).getCharacter())));
		tvLetter9.setText(String.format(baseText, letters.get(8).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(8).getCharacter())));
		tvLetter10.setText(String.format(baseText, letters.get(9).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(9).getCharacter())));
		tvLetter11.setText(String.format(baseText, letters.get(10).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(10).getCharacter())));
		tvLetter12.setText(String.format(baseText, letters.get(11).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(11).getCharacter())));
		tvLetter13.setText(String.format(baseText, letters.get(12).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(12).getCharacter())));
		tvLetter14.setText(String.format(baseText, letters.get(13).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(13).getCharacter())));
		tvLetter15.setText(String.format(baseText, letters.get(14).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(14).getCharacter())));
		tvLetter16.setText(String.format(baseText, letters.get(15).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(15).getCharacter())));
		tvLetter17.setText(String.format(baseText, letters.get(16).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(16).getCharacter())));
		tvLetter18.setText(String.format(baseText, letters.get(17).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(17).getCharacter())));
		tvLetter19.setText(String.format(baseText, letters.get(18).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(18).getCharacter())));
		tvLetter20.setText(String.format(baseText, letters.get(19).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(19).getCharacter())));
		tvLetter21.setText(String.format(baseText, letters.get(20).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(20).getCharacter())));
		tvLetter22.setText(String.format(baseText, letters.get(21).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(21).getCharacter())));
		tvLetter23.setText(String.format(baseText, letters.get(22).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(22).getCharacter())));
		tvLetter24.setText(String.format(baseText, letters.get(23).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(23).getCharacter())));
		tvLetter25.setText(String.format(baseText, letters.get(24).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(24).getCharacter())));
		tvLetter26.setText(String.format(baseText, letters.get(25).getCharacter(), this.game.getNumLettersLeftInHopperAndOpponentTray(letters.get(25).getCharacter())));
		 		
	}


}
