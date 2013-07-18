package com.riotapps.word.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WordData {
	 private SQLiteDatabase database;
	  private DatabaseHelper dbHelper;
	 // private String[] allColumns = { DatabaseHelper.COLUMN_ID,
	//		  DatabaseHelper.COLUMN_COMMENT };

	  public WordData(Context context) {
	    dbHelper = new DatabaseHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getReadableDatabase();
	  }
	  
	  public void close() {
		    dbHelper.close();
	  }
	  
	  public boolean doesIndexExist(String word) {
		  	String queryf = "select * from Word where idx ='" + word + "'";
		  
			  Cursor c = database.rawQuery(queryf, null);
			    if (c.getCount() == 0) {
			        c.close();
			        return false;
			    }else {
			        c.close();
			        return true;
			    }
		  }
	  
	  public boolean doesWordExist(String word) {
		  	String queryf = "select * from Word where word ='" + word + "'";
		  
			  Cursor c = database.rawQuery(queryf, null);
			    if (c.getCount() == 0) {
			        c.close();
			        return false;
			    }else {
			        c.close();
			        return true;
			    }
		  }

}
