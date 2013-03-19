package com.example.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DbAdapterTestActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_adapter_test);
		
		TextView tv = (TextView)this.findViewById(R.id.textView1);
		tv.setText("");
		// DAO db = new DAO(this);
		//Cursor cur = db.fetchMovies();
//		String whereClause = DBInfo._ID + " >= (abs(random()) % (SELECT MAX(" + DBInfo._ID + ") FROM " + DBInfo.MOVIES + ")) LIMIT 1";
//		Cursor cur = db.query(DBInfo.MOVIES, 
//				new String[] {DBInfo._ID, DBInfo.TITLE, DBInfo.YEAR, DBInfo.DIRECTOR}, 
//				whereClause, null, null, null, null);
//		int i = 0;
//		cur.moveToFirst();
//		tv.append("TestString\n");
//		while(!cur.isAfterLast() && i < 10){
//			tv.append(cur.getString(0) + "\n" + cur.getString(1) + "\n" + cur.getString(2) + "\n" + cur.getString(3) + "\n");
//			cur.moveToNext();
//			i++;
//		}
//		cur.close();
		Question question = QuestionUtils.getRandomQuestion(this);
		tv.append("TestString\n");
		tv.append(question.getQuestion() + "\n");
		String[] choices = question.getAnswers();
		for(String choice : choices){
			tv.append(choice + "\n");
		}
	}
}
