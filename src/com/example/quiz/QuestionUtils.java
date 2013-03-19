package com.example.quiz;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QuestionUtils {

	private static final int QUESTION_NO = 10;
	private static final int OPTION_NO = 4;
	private static SQLiteDatabase db;

	private static class Movie{
		public String id;
		public String title;
		public String year;
		public String director;
		
		public Movie(String id, String title, String year, String director){
			this.id = id;
			this.title = title;
			this.year = year;
			this.director = director;
		}
	}
	
	private static class Star{
		public String id;
		public String first_name;
		public String last_name;
		
		public Star(String id, String first_name, String last_name){
			this.id = id;
			this.first_name = first_name.replace('"', '\0');
			this.last_name =last_name.replace('"', '\0');
		}
		public String getFullName(){
			if(first_name.length() == 0)
				return last_name;
			else
				return "\""+first_name + " " + last_name+"\"";
		}
	}
	
	public static Question getRandomQuestion(Context context){
		if(db == null){
			db = DbAdapter.getSQLiteDatabase(context);
		}
		
		Random r = new Random();
		int qNo = r.nextInt(QUESTION_NO);
		Question question = null;
		switch(qNo){
			case 0: question = getQuestionZero();break;
			case 1: question = getQuestionOne(); break;
			case 2: question = getQuestionTwo();break;
			case 3: question = getQuestionThree();break;
			case 4: question = getQuestionFour();break;
			case 5: question = getQuestionFive();break; 
			case 6: question = getQuestionSix(); break;
			case 7: question = getQuestionSeven();break; 
			case 8: question = getQuestionEight();break;
			case 9: question = getQuestionNine(); break;
			default: question = getQuestionZero(); break;
		}
		return question;
	}
	
	// Who directed the movie X?
	private static Question getQuestionZero(){
		Movie movie = getRandomMovie();
		Set<String> directorSet = getDirectors(movie.director);
		
		// Build question and options
		String[] directors = new String[OPTION_NO];
		int correctAnswer = 0, i = 0;
		for(String director : directorSet){
			if(director.equals(movie.director)){
				correctAnswer = i;
			}
			directors[i++] = director;
		}
		
		return new Question(
				"Who directed the movie " + movie.title + "?", 
				correctAnswer, directors);
	}
	
	//When was the movie X released?
	private static Question getQuestionOne(){
		Movie movie = getRandomMovie();
		Set<String> timeSet = getRandomYears(movie.year);
		
		// Build question and options
		String[] years = new String[OPTION_NO];
		int correctAnswer = 0, i = 0;
		for(String year : timeSet){
			if(year.equals(movie.year)){
				correctAnswer = i;
			}
			years[i++] = year;
		}
		
		return new Question(
				"When was the movie " + movie.title + " released?", 
				correctAnswer, years);
	}
	
	//Which star was in the movie X?
	private static Question getQuestionTwo(){
		Movie movie = getRandomMovie();
		Set<Star> correctStarSet = getStarsByMovieID(movie.id,true,1);
		Set<Star> starSet = getStarsByMovieID(movie.id,false, 3);
		
		// Build question and options
		Star star =  (Star) correctStarSet.toArray()[0];
		starSet.add(star);
		String[] options = new String[OPTION_NO];
		int correctAnswer = 0, i = 0;
		for(Star s : starSet){
			if(s.id.equals(star.id)){
				correctAnswer = i;
			}
			options[i++] = s.getFullName();
		}
		
		return new Question(
				"Which star was in the movie " + movie.title + "?", 
				correctAnswer, options);
	}
	
	//Who directed the star X?
	private static Question getQuestionThree(){
		Star star = getRandomStar();
		Set<String> correctDirSet = getDirectorsByStarID(star.id,true,1);
		Set<String> dirSet = getDirectorsByStarID(star.id,false,3);
		
		// Build question and options
		String dir = (String) correctDirSet.toArray()[0];
		dirSet.add(dir);
		String[] options = new String[OPTION_NO];
		int correctAnswer = 0, i = 0;
		for(String d : dirSet){
			if(d.equals(dir)){
				correctAnswer = i;
			}
			options[i++] = d;
		}
		
		return new Question(
				"Who directed the star " + star.getFullName() + "?", 
				correctAnswer, options);
	}
	
	//Who directed the star X in year Y?
	private static Question getQuestionFour(){
		Star star = getRandomStar();
		Set<Movie> movieSet = getMoviesByStarID(star.id,true,OPTION_NO,"director,year");
		
		// Build question and options
		String[] options = new String[OPTION_NO];
		int i = 0;
		String year = "";
		int correctAnswer = (int) (Math.random() % OPTION_NO);
		for(Movie m : movieSet){
			if(i == correctAnswer){
				year = m.year;
			}
			options[i++] = m.director;
		}
		
		if(movieSet.size() < OPTION_NO){
			Set<String> dirSet = null;
			dirSet = getDirectorsByStarID(star.id,false,OPTION_NO - movieSet.size());
			for(String s : dirSet){
				options[i++] = s;
			}
		}
		
		return new Question(
				"Who directed the star " + star.getFullName() + " in "+ year +"?", 
				correctAnswer, options);
	}
	
	//Which star appears in both movies X and Y?
	private static Question getQuestionFive(){
		Star star = null;
		Set<Movie> movieSet = null;
		do{
			star = getRandomStar();
			movieSet = getMoviesByStarID(star.id, true, 2, null);
		}while(movieSet.size() != 2);
		Movie[] movies = new Movie[2];
		movies[0] =  (Movie) movieSet.toArray()[0];
		movies[1] =  (Movie) movieSet.toArray()[1];
		
		Set<Star> starSet = getStarsByMovieID(movies[0].id, false, 3);
		starSet.add(star);
		 
		// Build question and options
		String[] options = new String[OPTION_NO];
		int i = 0;
		int correctAnswer =  0;
		for(Star s :starSet){
			if(s.id.equals(star.id)){
				correctAnswer = i;
			}
			options[i++] = s.getFullName();
		}
			
		return new Question(
					"Which star appears in both movies "+ movies[0].title + " and "+ movies[1].title+"?",
					correctAnswer, options);
	}
	
	//Which star did not appear in the same movie with the star X?
	private static Question getQuestionSix(){
		Star star = null;
		Set<Star> starSet = null;
		do{
			star = getRandomStar();
			starSet = getStarsInMovie(star.id,true,3);
		}while(starSet.size() != 3);
		Set<Star> correctStarSet = getStarsInMovie(star.id, false, 1);
		Star answerStar = (Star) correctStarSet.toArray()[0];
		starSet.add(answerStar);
		
		// Build question and options
		String[] options = new String[OPTION_NO];
		int i = 0;
		int correctAnswer =  0;
		for(Star s:starSet){
			if(s.id.equals(answerStar.id)){
				correctAnswer = i;
			}
			options[i++] = s.getFullName();
		}
		
		return new Question(
				"Which star did not appear in the same movie with "+star.getFullName() +"?",
				correctAnswer, options);
	}	
	
	//In which movie the stars X and Y appear together?
	private static Question getQuestionSeven(){
		Movie movie = null;
		Set<Star> starsSet = null;
		do{
			movie = getRandomMovie();
			starsSet = getStarsByMovieID(movie.id, true, 2);
		}while(starsSet.size() != 2);
		
		Star[] stars = new Star[2];
		stars[0] = (Star) starsSet.toArray()[0];
		stars[1] = (Star) starsSet.toArray()[0];
		Set<Movie> movieSet = getMoviesByStarID(stars[0].id,false,3, null);
		if(movieSet.size() != 3){
			movieSet = getMoviesByStarID(stars[1].id,false,3, null);
		}
		movieSet.add(movie);

		// Build question and options
		String[] options = new String[OPTION_NO];
		int i = 0;
		int correctAnswer =  0;
		for(Movie m :movieSet){
			if(m.id.equals(movie.id)){
				correctAnswer = i;
			}
			options[i++] = m.title;
		}
		
		return new Question(
				"In which movie, "+stars[0].getFullName()+" and "+stars[1].getFullName() +" appear together?",
				correctAnswer, options);
	}

	//Which star was not in the movie X?
	private static Question getQuestionEight(){
		Movie movie = null;
		Set<Star> correctStarSet = null;
		Set<Star> starSet = null;
		do{
			movie = getRandomMovie();
			starSet = getStarsByMovieID(movie.id,true,3);
			
		}while(starSet.size() != 3);
		
		correctStarSet = getStarsByMovieID(movie.id,false, 3);
		
		Star star = null;
		for(Star s : correctStarSet){
			if(starSet.size() != OPTION_NO){
				star = s;
				starSet.add(s);
			}
			else 
				break;
		}
		
		// Build question and options
		String[] options = new String[OPTION_NO];
		int correctAnswer = 0, i = 0;
		for(Star s : starSet){
			if(s.id.equals(star.id)){
				correctAnswer = i;
			}
			options[i++] = s.getFullName();
		}
		
		return new Question(
				"Which star was not in the movie " + movie.title + "?", 
				correctAnswer, options);
	}

	//who did not direct the star X?
	private static Question getQuestionNine(){
		Star star = null;
		Set<String> dirSet = null;
		do{
			star = getRandomStar();
			dirSet = getDirectorsByStarID(star.id,true,3);
		}while(dirSet.size() != 3);
		Set<String> correctDirSet = getDirectorsByStarID(star.id,false,1);
		
		// Build question and options
		String dir = (String) correctDirSet.toArray()[0];
		dirSet.add(dir);
		String[] options = new String[OPTION_NO];
		int correctAnswer = 0, i = 0;
		for(String d : dirSet){
			if(d.equals(dir)){
				correctAnswer = i;
			}
			options[i++] = d;
		}
		
		return new Question(
				"Who did not direct the star " + star.getFullName() + "?", 
				correctAnswer, options);
	}
	
	// groupby argument is like this: a,b
	// no group by, set groupby = null
	private static Set<Movie> getMoviesByStarID(String starId, boolean hasStar, int limit, String groupbyClause) {
		StringBuffer qry = new StringBuffer();
		if(hasStar == true){
			qry.append("select * from " + DBInfo.MOVIES + 
					" where " + DBInfo._ID + " in (select " + DBInfo.MOVIE_ID + " from " + DBInfo.STARS_IN_MOVIES +
													" where " + DBInfo.STAR_ID + " = " + starId + ") ");
		}
		else{
			qry.append("select * from " + DBInfo.MOVIES + 
					" where " + DBInfo._ID + " not in (select " + DBInfo.MOVIE_ID + " from " + DBInfo.STARS_IN_MOVIES +
													" where " + DBInfo.STAR_ID + " = " + starId + ") ");
		}

		if(groupbyClause != null){
			qry.append(" group by "+ groupbyClause);
		}
		
		if(limit != -1){
			qry.append(" limit " + limit + ";");
		}
		else{
			qry.append(";");
		}
		
		Cursor cur = db.rawQuery(qry.toString(),null);
		cur.moveToFirst();
		Set<Movie> movieSet = new HashSet<Movie>();
		while(!cur.isAfterLast()){
			movieSet.add(new Movie(cur.getString(0).trim(), cur.getString(1).trim(), 
					cur.getString(2).trim(), cur.getString(3).trim()));
			cur.moveToNext();
		}
		cur.close();
		return movieSet;
	}
	
	private static Set<String> getDirectorsByStarID(String starId, boolean hasStar, int limit) {
		StringBuffer qry = new StringBuffer();
		if(hasStar == true){
			qry.append("select distinct("+ DBInfo.DIRECTOR + ") from " + DBInfo.MOVIES + 
					" where " + DBInfo._ID + " in (select " + DBInfo.MOVIE_ID + " from " + DBInfo.STARS_IN_MOVIES +
													" where " + DBInfo.STAR_ID + " = " + starId + ") ");
		}
		else{
			qry.append("select distinct("+ DBInfo.DIRECTOR + ") from " + DBInfo.MOVIES + 
					" where " + DBInfo._ID + " not in (select " + DBInfo.MOVIE_ID + " from " + DBInfo.STARS_IN_MOVIES +
													" where " + DBInfo.STAR_ID + " = " + starId + ") ");
		}

		if(limit != -1){
			qry.append("limit " + limit + ";");
		}
		else{
			qry.append(";");
		}
		
		Cursor cur = db.rawQuery(qry.toString(),null);
		cur.moveToFirst();
		Set<String> dirSet = new HashSet<String>();
		while(!cur.isAfterLast()){
			dirSet.add(cur.getString(0).trim());
			cur.moveToNext();
		}
		cur.close();
		return dirSet;
	}
	
	private static Set<Star> getStarsInMovie(String starid, boolean withStarId, int limit) {
		//select * from stars where id in (select distinct(t1.star_id) from stars_in_movies t1, stars_in_movies t2 where t2.star_id = 48002 and t1.star_id != 48002 and t1.movie_id = t2.movie_id);

		StringBuffer qry = new StringBuffer();
		if(withStarId == true){
			qry.append("select * from " + DBInfo.STARS + 
					" where " + DBInfo._ID + " in (select distinct(t1.star_id) from stars_in_movies t1, stars_in_movies t2 " +
														"where t1.movie_id = t2.movie_id and " +
														"t2.star_id = " + starid + " and t1.star_id != "+ starid + ") ");
		}
		else{
			qry.append("select * from " + DBInfo.STARS + 
					" where " + DBInfo._ID + " not in (select distinct(t1.star_id) from stars_in_movies t1, stars_in_movies t2 " +
														"where t1.movie_id = t2.movie_id and " +
														"t2.star_id = " + starid + " and t1.star_id != "+ starid + ") ");
		}

		if(limit != -1){
			qry.append("limit " + limit + ";");
		}
		else{
			qry.append(";");
		}
		
		Cursor cur = db.rawQuery(qry.toString(),null);
		cur.moveToFirst();
		Set<Star> starSet = new HashSet<Star>();
		while(!cur.isAfterLast()){
			starSet.add(new Star(cur.getString(0).trim(), cur.getString(1).trim(), cur.getString(2).trim()));
			cur.moveToNext();
		}
		cur.close();
		return starSet;
	}
	
	// limit = -1 : no limits
	private static Set<Star> getStarsByMovieID(String movieId, boolean inMovie, int limit) {
		StringBuffer qry = new StringBuffer();
		if(inMovie == true){
			qry.append("select * from " + DBInfo.STARS + 
					" where " + DBInfo._ID + " in (select " + DBInfo.STAR_ID + " from " + DBInfo.STARS_IN_MOVIES +
													" where " + DBInfo.MOVIE_ID + " = " + movieId + ") ");
		}
		else{
			qry.append("select * from " + DBInfo.STARS + 
					" where " + DBInfo._ID + " not in (select "+ DBInfo.STAR_ID +" from "+ DBInfo.STARS_IN_MOVIES +
													" where "+ DBInfo.MOVIE_ID +" = "+ movieId + ") ");
		}

		if(limit != -1){
			qry.append(" limit " + limit + ";");
		}
		else{
			qry.append(";");
		}
		
		Cursor cur = db.rawQuery(qry.toString(),null);
		cur.moveToFirst();
		Set<Star> starSet = new HashSet<Star>();
		while(!cur.isAfterLast()){
			starSet.add(new Star(cur.getString(0).trim(), cur.getString(1).trim(), cur.getString(2).trim()));
			cur.moveToNext();
		}
		cur.close();
		return starSet;
	}
	
	private static Movie getRandomMovie(){
		String whereClause = DBInfo._ID + " >= (abs(random()) % (SELECT MAX(" + DBInfo._ID + ") FROM " + DBInfo.MOVIES + ")) LIMIT 1";
		Cursor cur = db.query(DBInfo.MOVIES, 
				new String[] {DBInfo._ID, DBInfo.TITLE, DBInfo.YEAR, DBInfo.DIRECTOR}, 
				whereClause, null, null, null, null);
		cur.moveToFirst();
		Movie movie = new Movie(cur.getString(0).trim(), cur.getString(1).trim(), 
				cur.getString(2).trim(), cur.getString(3).trim());
		cur.close();
		return movie;
	}
	
	private static Star getRandomStar(){
		String whereClause = DBInfo._ID + " >= (abs(random()) % (SELECT MAX(" + DBInfo._ID + ") FROM " + DBInfo.STARS + ")) LIMIT 1";
		Cursor cur = db.query(DBInfo.STARS, 
							new String[] {DBInfo._ID, DBInfo.FIRST_NAME, DBInfo.LAST_NAME}, 
							whereClause, null, null, null, null);
		cur.moveToFirst();
		Star star = new Star(cur.getString(0).trim(), cur.getString(1).trim(), cur.getString(2).trim());
		cur.close();
		return star;
	}
	
	private static Set<String> getDirectors(String correctDirector){
		Set<String> directorSet = new HashSet<String>();
		directorSet.add(correctDirector);
		//get three wrong answers
		String whereClause = DBInfo.DIRECTOR + " !=  " + correctDirector;
		Cursor cur = db.query(true, //distinct results
							DBInfo.MOVIES, 
							new String[] {DBInfo.DIRECTOR}, 
							whereClause, null, null, null,null, (OPTION_NO - 1)+"");
        cur.moveToFirst();
        while(!cur.isAfterLast()){
        	directorSet.add(cur.getString(0).trim());
        	cur.moveToNext();
        }
        cur.close();
		return directorSet;
	}
	
	private static Set<String> getRandomYears(String correctYear){
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		Set<String> yearSet = new HashSet<String>();
		yearSet.add(correctYear);
		int min = Integer.parseInt(correctYear) - 10;
		while(yearSet.size() != OPTION_NO){
			int year = min + (int)(Math.random() * ((currentYear - min) + 1));
			yearSet.add(year+"");
		}
		return yearSet;
	}
}
