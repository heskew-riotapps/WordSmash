package com.riotapps.word.utils;

import com.riotapps.word.R;
import com.riotapps.word.hooks.PlayerService;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomProgressDialog extends AlertDialog{
	private static final String TAG = CustomProgressDialog.class.getSimpleName();
	  private String dialogText = "";
	  private TextView dialog_text;
	  
	 // public void SetText(String text){
	//	  this.dialogText = text;
	 // }
	
	  public CustomProgressDialog(Context context) {  
	        super(context);
	       // this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
 
	     //   this.setContentView(R.layout.progress);
	    
	    }

	@Override
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
	        View layout = inflater.inflate(R.layout.progress, 
	                                        (ViewGroup) findViewById(R.id.progress_root));
	     
	        this.setCancelable(false);
	        dialog_text = (TextView) layout.findViewById(R.id.dialog_text);
	     	if (this.dialogText.length() > 0) {
	     		dialog_text.setText(this.dialogText);
	     	}
	     	else {
	     		dialog_text.setVisibility(View.GONE);
	     	}

	     	
	 		this.setContentView(layout);
	}

	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
	//	super.setMessage(message);
		  this.dialogText = (String)message;
	}  
	@Override
	public void show() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "SHOW");
		super.show();
	}  
	    
	  
	public void updateMessage(String message){
		 this.dialogText = message;
		 //this.runOnUiThread(changeMessage);
		 dialog_text.setText(CustomProgressDialog.this.dialogText);
	}

	private Runnable changeMessage = new Runnable() {
	    @Override
	    public void run() {
	        //Log.v(TAG, strCharacters);
	    	dialog_text.setText(CustomProgressDialog.this.dialogText);
	    }
	};
	
}
