package com.example.test_game.data_base_records;

public class Record {

	private String name;
	private int score;
	private long date;

	public Record(String name, int score, long date) {
		this.name = name;
		this.score = score;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public long getDate() {
		return date;
	}

}
