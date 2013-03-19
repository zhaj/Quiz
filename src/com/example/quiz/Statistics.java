package com.example.quiz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Statistics extends Activity{

	// Maintain all scores the user has obtained.
	// This data fields should be initialized by reading
	// from the database and updated whenever the user
	// takes the quiz.
	// The number of quizzes taken is simply the size of the list.
	// Average time spend on each question can also be calculated, 
	// assuming each quiz takes 3 mins.
	private static List<Integer> correctAnswers = new ArrayList<Integer>();
	private static List<Integer> wrongAnswers = new ArrayList<Integer>();
	private static int currentCorrectAnswers = 0;
	private static int currentWrongAnswers = 0;
	// This field is used to prevent multiple clicks for a question
	private static boolean currentIncremented = false;
	
	private static SQLiteDatabase db;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);
		if(db == null){
			db = DbAdapter.getSQLiteDatabase(this);
		}
		readFromDatabase();
		TextView tv= (TextView) this.findViewById(R.id.score_textView);
		tv.setText("");
		tv.append("number of quizzes taken: " + getNumberOfQuizzes() + "\n");
		tv.append("correct answers: " + getCorrectAnswers() + "\n");
		tv.append("incorrect answers: " + getWrongAnswers() + "\n");
		tv.append("average time spent per question: " + getAverageTime() + "\n");
	}
	
	public static void readFromDatabase(){
		correctAnswers.clear();
		wrongAnswers.clear();
		Cursor cur = db.query(DBInfo.STATISTICS, 
				new String[]{DBInfo.RIGHT,DBInfo.WRONG}, 
					null, null, null, null, null, null);
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			correctAnswers.add(cur.getInt(0));
			wrongAnswers.add(cur.getInt(1));
			cur.moveToNext();
		}
		cur.close();
	}
	
	public static void writeToDatabase(Context context){
		if(db == null){
			db = DbAdapter.getSQLiteDatabase(context);
		}
		ContentValues values = new ContentValues();
		
		values.put(DBInfo.RIGHT, currentCorrectAnswers);
		values.put(DBInfo.WRONG, currentWrongAnswers);
		long result = db.insert(DBInfo.STATISTICS, null, values);
		System.out.println("insert to database:"+ result+ " "+currentCorrectAnswers+ " "+ currentWrongAnswers);
	}
	
	public static void resetIncrement(){
		currentIncremented = false;
	}
	
	public static void incrementCurrentCorrectAnswers(){
		if(!currentIncremented){
			currentCorrectAnswers++;
			currentIncremented = true;
		}
	}
	
	public static void incrementCurrentWrongAnswers(){
		if(!currentIncremented){
			currentWrongAnswers++;
			currentIncremented = true;
		}
	}
	
	public static int getCurrentCorrectAnswers() {
		return currentCorrectAnswers;
	}

	public static int getCurrentWrongAnswers() {
		return currentWrongAnswers;
	}
	
	public static void setCurrentCorrectAnswers(int correctNum) {
		currentCorrectAnswers = correctNum;
	}

	public static void setCurrentWrongAnswers(int wrongNum) {
		currentWrongAnswers = wrongNum;
	}

	public static int getNumberOfQuizzes(){
		return correctAnswers.size();
	}
	
	public static int getCorrectAnswers(){
		int no = 0;
		for(int i : correctAnswers){
			no += i;
		}
		return no;
	}
	
	public static int getWrongAnswers(){
		int no = 0;
		for(int i : wrongAnswers){
			no += i;
		}
		return no;
	}
	
	public static double getAverageTime(){
		int qNo = 0;
		for(int i = 0; i < correctAnswers.size(); i++){
			qNo += correctAnswers.get(i);
			qNo += wrongAnswers.get(i);
		}
		return qNo / (60.0 * 3 * correctAnswers.size());
	}
}
