package com.example.test_game;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.test_game.data_base_records.Record;

public class RecordAdapter extends ArrayAdapter<Record> {

	private Context context;
	private LayoutInflater lInflater;
	private List<Record> listRecords;

	public RecordAdapter(Context context, int layoutResourceId,
			List<Record> listRecords) {
		super(context, layoutResourceId, listRecords);
		this.listRecords = listRecords;
		this.context = context;
		lInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.layout_item_list_records, parent,
					false);
		}

		Record record = getItem(position);
		((TextView) view.findViewById(R.id.textViewName)).setText(record
				.getName());		
		((TextView) view.findViewById(R.id.textViewScore)).setText(Integer
				.toString(record.getScore()));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(record.getDate());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
		((TextView) view.findViewById(R.id.textViewDate)).setText(dateFormat
				.format(calendar.getTime()));

		return view;

	}

	@Override
	public int getCount() {
		return listRecords.size();
	}

	@Override
	public Record getItem(int position) {
		return listRecords.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
