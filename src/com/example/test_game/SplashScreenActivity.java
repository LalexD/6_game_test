package com.example.test_game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity {
	private static Handler handler;
	private static Runnable runnable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_splash_screen);
		if (handler == null) {
			handler = new Handler();
			runnable = new Runnable() {
				@Override
				public void run() {
					if (!SplashScreenActivity.this.isFinishing()) {
						Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
						SplashScreenActivity.this.startActivity(intent);
						SplashScreenActivity.this.finish();
					}
				}
			};
			handler.postDelayed(runnable, 1000);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			handler.removeCallbacks(runnable);
			Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
			SplashScreenActivity.this.startActivity(intent);
			finish();
		}
		return true;
	}
}
