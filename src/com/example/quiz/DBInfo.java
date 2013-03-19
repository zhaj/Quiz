package com.example.quiz;

public interface DBInfo {
	//movies table
	 static final String MOVIES = "movies";
	 static final String _ID = "_id";
	 static final String TITLE = "_title";
	 static final String YEAR = "year";
	 static final String DIRECTOR = "director";
	 
	 static final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MOVIES + 
													"(" + _ID + " integer primary key autoincrement, " 
														+ TITLE + " text not null, "
														+ YEAR + " integer, "
														+ DIRECTOR + " text not null);";
	//stars table
	 static final String STARS = "stars";
	 static final String FIRST_NAME = "first_name";
	 static final String LAST_NAME = "last_name";
	
	 static final String CREATE_STAR_TABLE = "CREATE TABLE " + STARS + 
			 											"(" + _ID + " integer primary key autoincrement, " 
			 												+ FIRST_NAME + " text not null, "
			 												+ LAST_NAME + " text not null);";
	 
	 //stars in movies
	 static final String STARS_IN_MOVIES = "stars_in_movies";
	 static final String STAR_ID = "star_id";
	 static final String MOVIE_ID = "movie_id";
	 static final String CREATE_STAR_IN_MOVIE_TABLE = "CREATE TABLE " + STARS_IN_MOVIES + 
															"(" + STAR_ID + " integer not null, "
																+ MOVIE_ID + " integer not null, " 
																+ "FOREIGN KEY (" + MOVIE_ID + ") REFERENCES "+ MOVIES +" (" + _ID + ") "
																+ "FOREIGN KEY (" + STAR_ID + ") REFERENCES "+ STARS +" (" + _ID + "));";

	 //statistics table
	 static final String STATISTICS = "statistics";
	 static final String RIGHT = "right";
	 static final String WRONG = "wrong";
	 static final String CREATE_STATISTICS_TABLE = "CREATE TABLE " + STATISTICS + 
														"(" + _ID + " integer primary key autoincrement, " 
														+ RIGHT + " int not null default 0, "
														+ WRONG + " int not null default 0);";
	 
}
