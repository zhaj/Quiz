package com.example.quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter extends SQLiteOpenHelper implements DBInfo{

	private static final String DATABASE_NAME = "quizdb";
	private static final int DATABASE_VERSION = 9;
	
	private static final String MOVIES_FILE = "movies.csv";
	private static final String STARS_FILE = "stars.csv";
	private static final String STARS_IN_MOVIES_FILE = "stars_in_movies.csv";
	
	private static SQLiteDatabase mDb;
	private static Context mContext;
	
	public static SQLiteDatabase getSQLiteDatabase(Context ctx){
		if(mDb == null){
			new DbAdapter(ctx);
		}
		return mDb;
	}
	
	private DbAdapter(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = ctx;
		mDb = getWritableDatabase();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//create 3 tables
		db.execSQL(DBInfo.CREATE_MOVIE_TABLE);	
		db.execSQL(DBInfo.CREATE_STAR_TABLE);	
		db.execSQL(DBInfo.CREATE_STAR_IN_MOVIE_TABLE);
		db.execSQL(DBInfo.CREATE_STATISTICS_TABLE);
		// populate database
		populateTables(db);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + STARS_IN_MOVIES);
		db.execSQL("DROP TABLE IF EXISTS " + MOVIES);
		db.execSQL("DROP TABLE IF EXISTS " + STARS);
		db.execSQL("DROP TABLE IF EXISTS " + STATISTICS);
		onCreate(db);
	}
	
	// populate database
	private void populateTables(SQLiteDatabase db){	
		polulateMovies(db);
		polulateStars(db);
		polulateStarsInMovies(db);
	}
	
	private void polulateMovies(SQLiteDatabase db){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					mContext.getAssets().open(MOVIES_FILE)));
			String line;
			
			while((line=in.readLine())!=null) {
				ContentValues values = new ContentValues();
				String[] data = line.trim().split(",");
				values.put(_ID, data[0]);
				values.put(TITLE, data[1]);
				values.put(YEAR, data[2]);
				values.put(DIRECTOR, data[3]);
				db.insert(MOVIES, null, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void polulateStars(SQLiteDatabase db){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					mContext.getAssets().open(STARS_FILE)));
			String line;
			
			while((line=in.readLine())!=null) {
				ContentValues values = new ContentValues();
				String[] data = line.trim().split(",");
				values.put(_ID, data[0]);
				values.put(FIRST_NAME, data[1]);
				values.put(LAST_NAME, data[2]);
				db.insert(STARS, null, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void polulateStarsInMovies(SQLiteDatabase db){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					mContext.getAssets().open(STARS_IN_MOVIES_FILE)));
			String line;
			
			while((line=in.readLine())!=null) {
				ContentValues values = new ContentValues();
				String[] data = line.trim().split(",");
				values.put(STAR_ID, data[0]);
				values.put(MOVIE_ID, data[1]);
				db.insert(STARS_IN_MOVIES, null, values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
