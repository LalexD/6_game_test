package com.example.test_game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class SplashScreen extends Activity {

	private Thread mSplashThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash_screen);
		final SplashScreen sPlashScreen = this;

		mSplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {

						wait(1000);
					}
				} catch (InterruptedException ex) {
				}				
				finish();
				try {
					join();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		};

		mSplashThread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mSplashThread) {
				mSplashThread.notifyAll();
			}
		}
		return true;
	}
	
	@Override 
	public void  finish(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		super.finish();
		
		
		
	}
}