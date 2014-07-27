package com.example.test_game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	private Button buttonStartGame;
	private Button buttonTopResult;
	private Button buttonOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		buttonStartGame = (Button) findViewById(R.id.buttonStart);
		buttonTopResult = (Button) findViewById(R.id.buttonTopResult);
		buttonOptions = (Button) findViewById(R.id.buttonOptions);
		buttonStartGame.setOnClickListener(this);
		buttonTopResult.setOnClickListener(this);
		buttonOptions.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonStart: {
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.buttonTopResult: {
			Intent intent = new Intent(this, TopResultActivity.class);
			startActivity(intent);
			break;
		}

		case R.id.buttonOptions: {
			Intent intent = new Intent(this, OptionActivity.class);
			startActivity(intent);
			break;
		}
		}
	}

}