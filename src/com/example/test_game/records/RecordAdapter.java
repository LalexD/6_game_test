package com.example.test_game.records;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.example.test_game.R;

public class RecordAdapter extends ArrayAdapter<Record> {

	private Context context;
	private LayoutInflater lInflater;
	private List<Record> listRecords;

	static class ViewHolder {
		private TextView name;
		private TextView date;
		private TextView score;
	}

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
		ViewHolder holder;
		if (convertView == null) {
			convertView = lInflater.inflate(R.layout.i_list_record, parent,
			        false);
			holder = new ViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.textViewDate);
			holder.name = (TextView) convertView.findViewById(R.id.textViewName);
			holder.score = (TextView) convertView.findViewById(R.id.textViewScore);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		Record record = getItem(position);
		holder.name.setText(record.getName());
		holder.score.setText(Integer.toString(record.getScore()));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(record.getDate());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
		holder.date.setText(dateFormat.format(calendar.getTime()));

		return convertView;

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
