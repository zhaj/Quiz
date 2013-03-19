package com.example.quiz;

import com.example.quiz.MainActivity;
import com.example.quiz.Quiz;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button StartButton;
	private Button StatisticsButton;
	private Button adAdapterTestButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Retrieve the button, start the quiz
        StartButton = (Button)this.findViewById(R.id.startbutton);
        StartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent intent = new Intent(MainActivity.this, Quiz.class);
				startActivity(intent);
	        }
        });
        
        //statistics button
        StatisticsButton = (Button)this.findViewById(R.id.statistics_button);
        StatisticsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, Statistics.class);
				startActivity(intent);
			}
		});
        
        adAdapterTestButton = (Button)this.findViewById(R.id.dbAdapterTest);
        adAdapterTestButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, DbAdapterTestActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
