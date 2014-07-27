package com.example.test_game;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.test_game.data_base_records.DataBaseRecords;
import com.example.test_game.data_base_records.Record;

public class TopResultActivity extends Activity {
	private RecordAdapter recordAdapter;
	private List<Record> listRecords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_top_result);
		DataBaseRecords dbRecords = new DataBaseRecords(this);
		listRecords = dbRecords.getTopRecords();
		recordAdapter = new RecordAdapter(this,
				R.layout.layout_item_list_records, listRecords);
		ListView listViewScore = (ListView) findViewById(R.id.listViewScore);
		listViewScore.setAdapter(recordAdapter);

	}
}
