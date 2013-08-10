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

	  public List<String> getMatchingWordsFromIndex(String index) {
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

	  public List<String> getMatchingWordsFromIndexArray(String[] index) {
			List<String> matches = new ArrayList<String>();
 			
		  	String queryf = "select word from Word where idx  IN (" + this.makePlaceholders(index.length) + ")";
		  
			  Cursor c = database.rawQuery(queryf, index);
			  try{
				  if (c.getCount() > 0) {
					  c.moveToFirst();
					  while (c.isAfterLast() == false) 
					  {
					      String match  = c.getString(0);
					      matches.add(match);
					      c.moveToNext();
					  }
				  }
			  }
			  finally{
				  //just in case, we don't want cursor left open
				  c.close();				  
			  }

			  
			  return matches;
		  }

	  
	  public List<String> getWordsFromWordArray(String[] words) {
			List<String> matches = new ArrayList<String>();
			
		  	String queryf = "select word from Word where word  IN (" + this.makePlaceholders(words.length) + ")";
		  
			  Cursor c = database.rawQuery(queryf, words);
			  try{
				  if (c.getCount() > 0) {
					  c.moveToFirst();
					  while (c.isAfterLast() == false) 
					  {
					      String match  = c.getString(0);
					      matches.add(match);
					      c.moveToNext();
					  }
				  }
			  }
			  finally{
				  //just in case, we don't want cursor left open
				  c.close();				  
			  }

			  
			  return matches;
		  }

	  private String makePlaceholders(int len) {
		    if (len < 1) {
		        // It will lead to an invalid query anyway ..
		        throw new RuntimeException("No placeholders");
		    } else {
		        StringBuilder sb = new StringBuilder(len * 2 - 1);
		        sb.append("?");
		        for (int i = 1; i < len; i++) {
		            sb.append(",?");
		        }
		        return sb.toString();
		    }
		}
	  //add "in" query and add index creation to Database Helper
}
