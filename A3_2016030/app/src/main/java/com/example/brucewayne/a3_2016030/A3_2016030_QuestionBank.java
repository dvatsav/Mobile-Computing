package com.example.brucewayne.a3_2016030;

import android.content.Context;

import java.util.ArrayList;

class A3_2016030_QuestionBank {
    private static A3_2016030_QuestionBank qBank;
    private ArrayList<A3_2016030_Question> qList = new ArrayList<>();
    public static A3_2016030_QuestionBank getInstance(Context context) {
        if (qBank == null) {
            qBank = new A3_2016030_QuestionBank(context);
        }
        return qBank;
    }

    private A3_2016030_QuestionBank(Context context) {
    }

    public ArrayList<A3_2016030_Question> getQuestions() {
        return qList;
    }

    public void addQuestion(int qID, String question, int answer, int attempted, int userAnswer) {
        for (int i = 0 ; i < qList.size() ; ++i) {
            if (qList.get(i).questionText.compareTo(question) == 0) {
                return;
            }
        }
        A3_2016030_Question q = new A3_2016030_Question(qID, question, answer, attempted, userAnswer);
        qList.add(q);
        return;
    }

    public void updateQuestion(int qID, int attempted, int userAnswer) {
        qList.get(qID - 1).attempted = attempted;
        qList.get(qID - 1).userAnswer = userAnswer;
    }

    public int questionCount() {
        return qList.size();
    }
}
