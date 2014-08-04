package com.example.test_game;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import com.example.test_game.R;
import com.example.test_game.data.db.GameDB;
import com.example.test_game.records.Record;
import com.example.test_game.records.RecordAdapter;

public class TopResultActivity extends Activity {
	private RecordAdapter recordAdapter;
	private ListView listViewScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_top_result);
		listViewScore = (ListView) findViewById(R.id.listViewScore);
		new LoadRecordsDBTask().execute();

	}

	public class LoadRecordsDBTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			GameDB dbRecords = new GameDB(TopResultActivity.this);
			List<Record> listRecords = dbRecords.getTopRecords();
			recordAdapter = new RecordAdapter(TopResultActivity.this,
			        R.layout.i_list_record, listRecords);
			dbRecords.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			listViewScore.setAdapter(recordAdapter);

	}
	}


}
