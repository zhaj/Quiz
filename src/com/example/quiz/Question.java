package com.example.quiz;

public class Question {

	private String question;
	private String[] answers;
	private int correctAnswer;
	private int userAnswer;
	
	public Question(String question, int correctAnswer, String... answers){
		this.question = question;
		this.answers = new String[answers.length];
		for(int i = 0; i < this.answers.length; i++){
			this.answers[i] = answers[i];
		}
		this.correctAnswer = correctAnswer;
		System.out.println(question);
	}
	
	public String getQuestion() {
		return question;
	}
	
	public String[] getAnswers() {
		return answers;
	}
	
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	
	public int getUserAnswer() {
		return userAnswer;
	}
	
	public void setUserAnswer(int userAnswer) {
		this.userAnswer = userAnswer;
	}
	
	
}
