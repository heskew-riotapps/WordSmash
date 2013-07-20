package com.riotapps.word.data;

import java.util.ArrayList;
import java.util.List;

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
	  
	  public void tempAddIndexes____(){
		  String wordIndexDDL = "CREATE INDEX 'idx_1' ON 'Word' ('word' ASC);";
	    	String lookupIndexDDL = "CREATE INDEX 'idx_2' ON 'Word' ('idx' ASC);";

	    	database.execSQL(wordIndexDDL);
	    	database.execSQL(lookupIndexDDL);
	  }
	  
	  public void close() {
		  database.close();
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
	  
	  
	  public List<String> getMatchingWords(String index) {
		List<String> matches = new ArrayList<String>();
	  	String queryf = "select word from Word where idx ='" + index + "'";
	  
		  Cursor c = database.rawQuery(queryf, null);
		  
		  if (c.getCount() > 0) {
			  c.moveToFirst();
			  while (c.isAfterLast() == false) 
			  {
			      String match  = c.getString(0);
			      matches.add(match);
			      c.moveToNext();
			  }
			  
		  }
		  
		  c.close();
		  
		  return matches;
	  }

	  public List<String> getMatchingIndexes(String index) {
			List<String> matches = new ArrayList<String>();
		  	String queryf = "select idx from Word where idx ='" + index + "'";
		  
			  Cursor c = database.rawQuery(queryf, null);
			  
			  if (c.getCount() > 0) {
				  c.moveToFirst();
				  while (c.isAfterLast() == false) 
				  {
				      String match  = c.getString(0);
				      matches.add(match);
				      c.moveToNext();
				  }
			  }
			  
			  c.close();
			  
			  return matches;
		  }


	  //add "in" query and add index creation to Database Helper
}
