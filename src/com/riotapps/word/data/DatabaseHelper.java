package com.riotapps.word.data;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.GameThread;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	 private static final String TAG = DatabaseHelper.class.getSimpleName();
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.riotapps.word/databases/";
 
    private static String DB_NAME = "wordsmash.db";
 
    private SQLiteDatabase database = null; 
 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
        
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean create = false;
    	Logger.d(TAG, "createDataBase about the try and open database");
    	try{
    		
    		this.openDataBase();
    		
	    }catch(SQLiteException e){
	    	Logger.d(TAG, "createDataBase error opening database " + e.getMessage());
			//database does't exist yet.
		}
		
    	if(database != null){
    		//do nothing - database already exist
    		//make sure table does not exist and an empty database was not created mistakenly earlier
    		//also check version of database, stored in constants to know when to overwrite database
    		
    		Logger.d(TAG, "createDataBase about doesTableExist");
    		if (this.doesTableExist()){
    			//now check version
    			if (PlayerService.getWordDatabaseVersion() < Constants.WORD_DATABASE_VERSION){
    				Logger.d(TAG, "database version out of sync, copy again");
    				create = true;
    			}
    		}
    		else{
    			create = true;
    		}
    		database.close();
    	}
    	else{
    		create = true;
    		
    	}

    	if (create){
 
    		//By calling this method an empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
    		Logger.d(TAG, "createDataBase about getReadableDatabase");
        	this.getReadableDatabase();
        	
 
        	try {
        		Logger.d(TAG, "createDataBase about copyDataBase");
    			copyDataBase();
    			PlayerService.saveWordDatabaseVersion(Constants.WORD_DATABASE_VERSION);
    			
    			//load indexes
    			this.database = this.getReadableDatabase();
    			this.addIndexes();
    			
            	
 
    		} catch (IOException e) {
    			Logger.d(TAG, "createDataBase copy error = " + e.getMessage());
    			throw e;
        		//throw new Error("Error copying database");
 
        	}
        	finally{
        		this.database.close();
        	}
    	}
 
    }
 
    public boolean doesTableExist() {
	  	String queryf = "SELECT name FROM sqlite_master WHERE type='table' AND name='Word';";
	  
		  Cursor c = database.rawQuery(queryf, null);
		    if (c.getCount() == 0) {
		        c.close();
		        return false;
		    }else {
		        c.close();
		        return true;
		    }
	  }
    
    public void addIndexes(){
    	String wordIndexDropDDL = "DROP INDEX IF EXISTS 'idx_1'";
    	String lookupIndexDropDDL = "DROP INDEX IF EXISTS 'idx_2'";
    	
    	String wordIndexDDL = "CREATE INDEX 'idx_1' ON 'Word' ('word' ASC);";
    	String lookupIndexDDL = "CREATE INDEX 'idx_2' ON 'Word' ('idx' ASC);";

    	database.execSQL(wordIndexDropDDL);
    	database.execSQL(lookupIndexDropDDL);

    	database.execSQL(wordIndexDDL);
    	database.execSQL(lookupIndexDDL);
    }
   
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(database != null)
    		    database.close();
 
    	    super.close();
 
	}
    
    
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return database.query(....)" so it'd be easy
       // to you to create adapters for your views.
 
}