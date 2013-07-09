package com.riotapps.word.ui;

import java.util.List;

import com.riotapps.word.GameSurface;
import com.riotapps.word.Main;
import com.riotapps.word.R;
import com.riotapps.word.hooks.AlphabetService;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.Letter;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.StoreService;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;


public class HopperPeekDialog  extends Dialog implements View.OnClickListener{
	private static final String TAG = HopperPeekDialog.class.getSimpleName();
	
	private Game game;
	private GameSurface parent;
	private View layout;
	private TextView peek_description;
	
	public HopperPeekDialog(GameSurface context, Game game){
		super(context);
		this.game = game;
		this.parent = context;
	//	WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	//	params.horizontalMargin = 10f;
		
	//	getWindow().setAttributes(params);
	   getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//getWindow().setBackgroundDrawable(null);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		this.setContentView(BUTTON1);
      //  this.setProgressStyle(R.style.CustomProgressStyle);
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.layout = inflater.inflate(R.layout.hopperpeekdialog, null); 
//                                        (ViewGroup) findViewById(R.id.progress_root));
		
		 //loop through letters, filling the views
        
     //   this.setContentView(this.layout);

		LinearLayout llOK = (LinearLayout) this.layout.findViewById(R.id.llOK);
		TextView tvOK = (TextView) this.layout.findViewById(R.id.tvOK);
		Button bNoThanks = (Button)this.layout.findViewById(R.id.bNoThanks);
		Button bStore = (Button)this.layout.findViewById(R.id.bStore);

		
		Logger.d(TAG, "free hopper peeks=" + PlayerService.getRemainingFreeUsesHopperPeek());
		
		if (!StoreService.isHopperPeekPurchased() && PlayerService.getRemainingFreeUsesHopperPeek() == 0){
			this.peek_description = (TextView)this.layout.findViewById(R.id.peek_description);
			this.peek_description.setText(this.parent.getString(R.string.hopper_peek_purchase_offer));
			TableLayout tblLetters =  (TableLayout)this.layout.findViewById(R.id.tblLetters);
			tblLetters.setVisibility(View.GONE);
			tvOK.setVisibility(View.GONE);
		}
		else{
			this.loadLetters();	
			
			if (!StoreService.isHopperPeekPurchased()){
				tvOK.setVisibility(View.GONE);
				bStore.setOnClickListener(this);
				bNoThanks.setOnClickListener(this);
				int remainingFreeUses  = PlayerService.removeAFreeUseFromHopperPeek();
				if (remainingFreeUses > 1){
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_preview), this.peek_description.getText(), String.valueOf(remainingFreeUses)));
				}
				else if (remainingFreeUses == 1){
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_1_preview_left), this.peek_description.getText()));					
				}
				else{
					this.peek_description.setText(String.format(this.parent.getString(R.string.hopper_peek_no_previews_left), this.peek_description.getText()));					
				}
			}
			else{
				
				llOK.setOnClickListener(this);
				bStore.setVisibility(View.GONE);
				bNoThanks.setVisibility(View.GONE);
			}
		}
		 

		ImageView close = (ImageView) this.layout.findViewById(R.id.img_close);
		//if button is clicked, close the custom dialog
		close.setOnClickListener(new View.OnClickListener() {
	 		@Override
			public void onClick(View v) {
	 			
	 			//trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        	//		Constants.TRACKER_LABEL_SWAP_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			//
	 			
				dismiss();
			}
		});
		
		this.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        	//		Constants.TRACKER_LABEL_SWAP_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
				
				dismiss();
				
			}
		});
		
		//LayoutParams params = new LayoutParams();
		//params.horizontalMargin = 10f;
		
		this.setContentView(this.layout);
	}
		
	
	@Override
	public void dismiss(){
		parent.unfreezeButtons();
		super.dismiss();	
	}

	 

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.llOK:
				this.dismiss();
				break;
			case R.id.bStore:
				this.dismiss(); 
				Intent intent = new Intent(this.parent, com.riotapps.word.Store.class);
				this.parent.startActivity(intent); 
				break;
			case R.id.bNoThanks:
				this.dismiss();
				break;
		}
	}
	
	private void loadLetters(){
		List<Letter> letters = AlphabetService.getLetters();
		
		TextView tvLetter1 = (TextView)this.layout.findViewById(R.id.tvLetter1);
		TextView tvLetter2 = (TextView)this.layout.findViewById(R.id.tvLetter2);
		TextView tvLetter3 = (TextView)this.layout.findViewById(R.id.tvLetter3);
		TextView tvLetter4 = (TextView)this.layout.findViewById(R.id.tvLetter4);
		TextView tvLetter5 = (TextView)this.layout.findViewById(R.id.tvLetter5);
		TextView tvLetter6 = (TextView)this.layout.findViewById(R.id.tvLetter6);
		TextView tvLetter7 = (TextView)this.layout.findViewById(R.id.tvLetter7);
		TextView tvLetter8 = (TextView)this.layout.findViewById(R.id.tvLetter8);
		TextView tvLetter9 = (TextView)this.layout.findViewById(R.id.tvLetter9);
		TextView tvLetter10 = (TextView)this.layout.findViewById(R.id.tvLetter10);
		TextView tvLetter11 = (TextView)this.layout.findViewById(R.id.tvLetter11);
		TextView tvLetter12 = (TextView)this.layout.findViewById(R.id.tvLetter12);
		TextView tvLetter13 = (TextView)this.layout.findViewById(R.id.tvLetter13);
		TextView tvLetter14 = (TextView)this.layout.findViewById(R.id.tvLetter14);
		TextView tvLetter15 = (TextView)this.layout.findViewById(R.id.tvLetter15);
		TextView tvLetter16 = (TextView)this.layout.findViewById(R.id.tvLetter16);
		TextView tvLetter17 = (TextView)this.layout.findViewById(R.id.tvLetter17);
		TextView tvLetter18 = (TextView)this.layout.findViewById(R.id.tvLetter18);
		TextView tvLetter19 = (TextView)this.layout.findViewById(R.id.tvLetter19);
		TextView tvLetter20 = (TextView)this.layout.findViewById(R.id.tvLetter20);
		TextView tvLetter21 = (TextView)this.layout.findViewById(R.id.tvLetter21);
		TextView tvLetter22 = (TextView)this.layout.findViewById(R.id.tvLetter22);
		TextView tvLetter23 = (TextView)this.layout.findViewById(R.id.tvLetter23);
		TextView tvLetter24 = (TextView)this.layout.findViewById(R.id.tvLetter24);
		TextView tvLetter25 = (TextView)this.layout.findViewById(R.id.tvLetter25);
		TextView tvLetter26 = (TextView)this.layout.findViewById(R.id.tvLetter26);
		this.peek_description = (TextView)this.layout.findViewById(R.id.peek_description);
		
		String baseText = parent.getString(R.string.hopper_peek_letter);
		peek_description.setText(String.format(parent.getString(R.string.gameboard_hopper_peek_dialog_decription), this.game.getTotalNumLetterCountLeftInHopperAndOpponentTray(), this.game.getOpponent().getName()));
		
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
