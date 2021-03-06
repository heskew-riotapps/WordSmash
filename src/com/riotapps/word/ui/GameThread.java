package com.riotapps.word.ui;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

@SuppressLint("WrongCall")
public class GameThread extends Thread {
	 private SurfaceHolder surfaceHolder;
	 volatile boolean running = false;
	 private static final String TAG = GameThread.class.getSimpleName();
	  
	 private long milliseconds = System.currentTimeMillis();
	 
	 GameSurfaceView parent;
	 long sleepTime;
	 long tickCount = 0;
	 private Object pauseLock = new Object();  
	 private boolean paused;
	  
//	 public GameThread(GameSurfaceView sv, long st){
//	  super();
//	  parent = sv;
//	  sleepTime = st;
//	 }
	 
	  public GameThread(SurfaceHolder surfaceHolder, GameSurfaceView surfaceView) {
	    this.surfaceHolder = surfaceHolder;
	    parent = surfaceView;
	 }
	  
	  public SurfaceHolder getSurfaceHolder() {
		    return this.surfaceHolder;
	  }
	  
//	  public void getStarted(){
//		  Log.w(getClass().getSimpleName() + "getStarted", "getStarted called");
//		  this.start();
//	  }
	
	  
	 public void setRunning(boolean r){
	  this.running = r;
	 }
	 
	 public void onPause() {
		    synchronized (pauseLock) {
		        paused = true;
		    }
		}
	 
	 public void onResume() {
		    synchronized (pauseLock) {
		        paused = false;
		        pauseLock.notifyAll();
		    }
		}
	  
	 @SuppressLint("WrongCall")
	@Override
	 public void run() {
		 Canvas c;
		 
		 long millisecondsLoop = 0;
		// this.parent.parent.captureTime("gamethread run started");
		    while (this.running) {
		    	
		    	
		    	
		    	//get game board drawn as quickly as possible, so don't thread sleep at first
		     	if (this.tickCount > 1){
			    	try {
			    		Thread.sleep(3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    	 
		    	
		    //	milliseconds = System.currentTimeMillis();

		    	//doing it this way just to not have a thread.sleep
		    //	if (millisecondsLoop == 0 || (milliseconds - millisecondsLoop >= 3)){ //make this variable or a constant
		   // 		millisecondsLoop = milliseconds;
		    		
			    	if (parent.isReadyToDraw()) {
				        c = null;
				        try {
				            c = this.surfaceHolder.lockCanvas(null);
				            synchronized (this.surfaceHolder) {
				            //	this.parent.parent.captureTime("gamethread draw started");
				            	 parent.drawFromThread(c); 
				            	
				            }  
				        } 
				        finally {
				            // do this in a finally so that if an exception is thrown
				            // during the above, we don't leave the Surface in an
				            // inconsistent state
				            if (c != null) {
				                this.surfaceHolder.unlockCanvasAndPost(c);
				            }
				        }
				      
				        this.tickCount += 1;
				    //   Log.d(TAG, "Game loop executed " + tickCount + " times");
	
			    	}
		    //	}
		    //	synchronized (pauseLock) {
          	//	    while (paused) {
          	//	        try {
          	//	            pauseLock.wait();
          	//	        } catch (InterruptedException e) {
          	//	        }
          	//	    }
          	//	} 
		    }
	 }
	 
	}
