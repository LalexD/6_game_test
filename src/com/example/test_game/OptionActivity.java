package com.example.test_game;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.test_game.data.db.GameDB;

public class OptionActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.layout_option);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	        String key) {
		GameDB gameDB = new GameDB(this);
		gameDB.deleteGame();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
		        .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
		        .unregisterOnSharedPreferenceChangeListener(this);
	}
}
