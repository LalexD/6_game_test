package com.example.test_game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import com.example.test_game.R;
import com.example.test_game.data.db.GameDB;
import com.example.test_game.game.Game;
import com.example.test_game.records.Record;


public class GameActivity extends Activity implements OnClickListener {
	public Button buttonAnswer_1, buttonAnswer_2, buttonAnswer_3,
	        buttonAnswer_4;
	public ImageView pictureFlag;
	private SharedPreferences settingPref;
	private ProgressBar progressAnswers;
	private String[] img_id, name_flags;
	private Game game;
	private GameDB gameDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_game);
		pictureFlag = (ImageView) findViewById(R.id.imageViewFlag);
		buttonAnswer_1 = (Button) findViewById(R.id.buttonAnswer1);
		buttonAnswer_2 = (Button) findViewById(R.id.buttonAnswer2);
		buttonAnswer_3 = (Button) findViewById(R.id.buttonAnswer3);
		buttonAnswer_4 = (Button) findViewById(R.id.buttonAnswer4);
		buttonAnswer_1.setOnClickListener(this);
		buttonAnswer_2.setOnClickListener(this);
		buttonAnswer_3.setOnClickListener(this);
		buttonAnswer_4.setOnClickListener(this);
		progressAnswers = (ProgressBar) findViewById(R.id.progressAnswers);
		settingPref = PreferenceManager.getDefaultSharedPreferences(this);
		progressAnswers.setMax(Integer.parseInt(settingPref.getString(
		        "number_question", "10")));
		name_flags = getResources().getStringArray(R.array.name_country);
		img_id = getResources().getStringArray(R.array.id_flag);
		gameDB = new GameDB(this);
		new refreshViewTask().execute();
	}

	private void toast(String text) {
		final Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				toast.cancel();
			}
		}, 500);
	}

	@Override
	public void onClick(View v) {
		game.incNumAnswers();
		progressAnswers.incrementProgressBy(1);
		if (v.getId() == getResources().getIdentifier(
		        "buttonAnswer" + game.getCorrAnswer(), "id", getPackageName())) {
			game.incScore();
			toast("Ok");
		} else
			toast("No");

		if (game.getNumAnswers() >= progressAnswers.getMax()) {
			if (gameDB.getMinScore() < game.getScore()
			        || !gameDB.checkNumRecords(10)) {
				dialogWin();
			} else
				dialogLose();

		} else
		{
			new refreshViewTask().execute();
		}

	}

	protected void dialogWin() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Congratulation!");
		builder.setMessage("You have new best score: " + game.getScore()
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
				deleteGame();
				game.incNumAnswers();
				finish();

			}
		});
		builder.setNegativeButton("Cancel",
		        new DialogInterface.OnClickListener() {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
				        dialog.cancel();
				        game.incNumAnswers();
				        deleteGame();
				        finish();
			        }
		        });

		builder.show();
	}

	protected void comeTopResults() {
		Intent intent = new Intent(this, TopResultActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	protected void dialogLose() {
		deleteGame();
		game.incNumAnswers();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Game end");
		builder.setMessage("You score: " + game.getScore()
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

	protected void deleteGame() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				gameDB.deleteGame();
			}
		}).start();
	}

	protected void saveScore(final String name) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				gameDB.addRecord(new Record(name, game.getScore(), System
				        .currentTimeMillis()));

			}
		}).start();
	}

	protected void saveGame() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				gameDB.saveGame(game);
			}
		}).start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (game.getNumAnswers() <= progressAnswers.getMax())
			saveGame();
		else
			finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gameDB.close();
	}

	private class refreshViewTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... context) {
			if (game == null) {
				game = gameDB.getGame();
				if (game == null) {
					game = new Game(GameActivity.this);
					game.takeRandomAnswers();
				}
			}
			else
				game.takeRandomAnswers();

			AssetManager assetManager = GameActivity.this.getAssets();
			InputStream imgStream;
			Bitmap bitmap = null;
			try {
				imgStream = assetManager.open(img_id[game.getIdFlag()]);
				bitmap = BitmapFactory.decodeStream(imgStream);
			} catch (IOException e)
			{
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			buttonAnswer_1.setText(name_flags[game.getAnswerButton1()]);
			buttonAnswer_2.setText(name_flags[game.getAnswerButton2()]);
			buttonAnswer_3.setText(name_flags[game.getAnswerButton3()]);
			buttonAnswer_4.setText(name_flags[game.getAnswerButton4()]);
			pictureFlag.setImageBitmap(bitmap);
			progressAnswers.setProgress(game.getNumAnswers());
			if (game.getNumAnswers() == progressAnswers.getMax())
				dialogWin();

		}

	}

}
