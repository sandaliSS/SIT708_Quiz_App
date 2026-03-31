package com.example.quizapp;


public class Question {
    private String title;
    private String detail;
    private String[] answers;
    private int correctIndex;

    public Question(String title, String detail, String[] answers, int correctIndex) {
        this.title = title;
        this.detail = detail;
        this.answers = answers;
        this.correctIndex = correctIndex;
    }

    public String getTitle() { return title; }
    public String getDetail() { return detail; }
    public String[] getAnswers() { return answers; }
    public int getCorrectIndex() { return correctIndex; }
}