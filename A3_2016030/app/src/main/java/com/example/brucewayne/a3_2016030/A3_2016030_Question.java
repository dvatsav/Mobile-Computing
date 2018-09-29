package com.example.brucewayne.a3_2016030;

public class A3_2016030_Question {
    int qID;
    String questionText;
    int answer;
    int attempted = 0;
    int userAnswer = 0;

    public A3_2016030_Question(int qID, String text, int answer, int attempted, int userAnswer) {
        this.qID = qID;
        this.questionText = text;
        this.answer = answer;
        this.attempted = attempted;
        this.userAnswer = userAnswer;
    }
}
