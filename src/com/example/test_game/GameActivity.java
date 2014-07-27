package com.example.test_game;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.test_game.data_base_records.DataBaseRecords;
import com.example.test_game.data_base_records.Record;

public class GameActivity extends Activity {
	private Button buttonAnswer_1, buttonAnswer_2, buttonAnswer_3,
			buttonAnswer_4;
	private ImageView pictureFlag;
	private SharedPreferences settingPref, lastGamePref;
	private ProgressBar progressAnswers;
	private String[] name_flags, img_id;
	private int correctAnswer, numCorrectAnswers;
	private Integer[] answers;
	private DataBaseRecords dbRecords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_game);
		pictureFlag = (ImageView) findViewById(R.id.imageViewFlag);
		buttonAnswer_1 = (Button) findViewById(R.id.buttonAnswer1);
		buttonAnswer_2 = (Button) findViewById(R.id.buttonAnswer2);
		buttonAnswer_3 = (Button) findViewById(R.id.buttonAnswer3);
		buttonAnswer_4 = (Button) findViewById(R.id.buttonAnswer4);
		progressAnswers = (ProgressBar) findViewById(R.id.progressAnswers);
		settingPref = PreferenceManager.getDefaultSharedPreferences(this);
		lastGamePref = getSharedPreferences("LastGame_Preferences",
				MODE_PRIVATE);
		progressAnswers.setMax(Integer.parseInt(settingPref.getString(
				"number_question", "10")));
		name_flags = getResources().getStringArray(R.array.name_country);
		img_id = getResources().getStringArray(R.array.id_flag);
		dbRecords=new DataBaseRecords(this);
		loadGame();

	}

	private void viewAnswers() {
		buttonAnswer_1.setText(name_flags[answers[0]]);
		buttonAnswer_2.setText(name_flags[answers[1]]);
		buttonAnswer_3.setText(name_flags[answers[2]]);
		buttonAnswer_4.setText(name_flags[answers[3]]);
		pictureFlag.setImageBitmap(getBitmapFromAsset(this,
				img_id[answers[correctAnswer - 1]]));
	}

	private void takeRandomAnswers() {
		Set<Integer> setAnswers = new LinkedHashSet<Integer>();
		Random random = new Random();
		while (setAnswers.size() <= 4) {
			setAnswers.add(random.nextInt(name_flags.length));
		}
		answers = setAnswers.toArray(new Integer[setAnswers.size()]);
		correctAnswer = random.nextInt(5 - 1) + 1;
		viewAnswers();
	}

	public static Bitmap getBitmapFromAsset(Context context, String filePath) {
		AssetManager assetManager = context.getAssets();
		InputStream imgStream;
		Bitmap bitmap = null;
		try {
			imgStream = assetManager.open(filePath);
			bitmap = BitmapFactory.decodeStream(imgStream);
		} catch (IOException e) {
		}

		return bitmap;
	}

	public void onClick(View v) {

		progressAnswers.incrementProgressBy(1);
		if (v.getId() == getResources().getIdentifier(
				"buttonAnswer" + correctAnswer, "id", getPackageName())) {
			numCorrectAnswers++;
			Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();

		if (progressAnswers.getProgress() >= progressAnswers.getMax()) {

			if (dbRecords.getMinScore() < numCorrectAnswers || !dbRecords.checkNumRecords(10)) {
				dialogWin();

			} else
				dialogLose();

		} else
			takeRandomAnswers();

	}

	protected void dialogWin() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Congratulation!");
		builder.setMessage("You have new best score: " + numCorrectAnswers
				+ "!\n Please enter name for save");

		final EditText input = new EditText(this);

		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveScore(input.getText().toString());
				comeTopResults();
				finish();

			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						finish();
					}
				});

		builder.show();
	}

	protected void comeTopResults(){
		Intent intent = new Intent(this, TopResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	protected void dialogLose() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Game end");
		builder.setMessage("You score: " + numCorrectAnswers
				+ " !\n Please click \"Ok\" for exit. ");
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.show();
	}

	protected void saveScore(String name) {
		DataBaseRecords dbRecords = new DataBaseRecords(this);
		dbRecords.addRecord(new Record(name, numCorrectAnswers, System
				.currentTimeMillis()));
	}

	@Override
	protected void onStop() {
		super.onStop();
		saveGame();
	}

	protected void saveGame() {
		Editor editor = lastGamePref.edit();
		editor.putInt("answerOk", numCorrectAnswers);
		if (progressAnswers.getProgress() >= progressAnswers.getMax())
			editor.putBoolean("checkLastGame", false);
		else {
			editor.putInt("answerProgress", progressAnswers.getProgress());
			editor.putBoolean("checkLastGame", true);
			editor.putInt("correctAnswer", correctAnswer);
			editor.putInt("numCorrectAnswer", numCorrectAnswers);
			editor.putInt("buttonAnswers1", answers[0]);
			editor.putInt("buttonAnswers2", answers[1]);
			editor.putInt("buttonAnswers3", answers[2]);
			editor.putInt("buttonAnswers4", answers[3]);
		}
		editor.apply();
	}

	protected void loadGame() {
		if (lastGamePref.getBoolean("checkLastGame", false)) {
			progressAnswers.setProgress(lastGamePref
					.getInt("answerProgress", 0));
			correctAnswer = lastGamePref.getInt("correctAnswer", 0);
			numCorrectAnswers = lastGamePref.getInt("numCorrectAnswer", 0);
			answers = new Integer[4];
			answers[0] = lastGamePref.getInt("buttonAnswers1", 0);
			answers[1] = lastGamePref.getInt("buttonAnswers2", 0);
			answers[2] = lastGamePref.getInt("buttonAnswers3", 0);
			answers[3] = lastGamePref.getInt("buttonAnswers4", 0);
			viewAnswers();
		} else
			takeRandomAnswers();

	}
}
