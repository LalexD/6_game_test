package com.example.test_game.game;

import android.content.Context;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import com.example.test_game.R;

public class Game {
	private int numAnswers;
	private int score;
	private int corrAnswer;
	private Integer[] answers;
	private String[] name_flags;

	public Game(Context context) {
		name_flags = context.getResources().getStringArray(R.array.name_country);
		answers = new Integer[4];
	}

	public void setData(int numAnswers, int score, int corAnswer,
	        int answerButton1, int answerButton2,
	        int answerButton3, int answerButton4)
	{
		this.corrAnswer = corAnswer;
		this.numAnswers = numAnswers;
		this.score = score;
		answers[0] = answerButton1;
		answers[1] = answerButton2;
		answers[2] = answerButton3;
		answers[3] = answerButton4;
	}

	public void takeRandomAnswers() {
		Integer lastFlag = -1;
		Set<Integer> setAnswers = new LinkedHashSet<Integer>();
		if (corrAnswer != 0)
			lastFlag = answers[corrAnswer - 1];
		Random random = new Random();
		while (setAnswers.size() <= 4) {
			setAnswers.add(random.nextInt(name_flags.length));
		}
		answers = setAnswers.toArray(new Integer[setAnswers.size()]);
		do {
			corrAnswer = random.nextInt(5 - 1) + 1;
		} while (lastFlag == getIdFlag());
	}

	public void incScore() {
		score++;
	}

	public void incNumAnswers() {
		numAnswers++;
	}

	public int getIdFlag() {
		return answers[corrAnswer - 1];
	}

	public int getNumAnswers() {
		return numAnswers;
	}

	public int getScore() {
		return score;
	}

	public int getCorrAnswer() {
		return corrAnswer;
	}

	public int getAnswerButton1() {
		return answers[0];
	}

	public int getAnswerButton2() {
		return answers[1];
	}

	public int getAnswerButton3() {
		return answers[2];
	}

	public int getAnswerButton4() {
		return answers[3];
	}

}
