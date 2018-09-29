package com.example.brucewayne.a3_2016030;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class A3_2016030_QuizHomePage extends AppCompatActivity {

    @InjectView(R.id.makeQuestion)
    Button makeQuestion;
    @InjectView(R.id.answerQuestion)
    Button answerQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a3_2016030_activity_quiz_home_page);
        ButterKnife.inject(this);

        makeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A3_2016030_QuizHomePage.this, A3_2016030_MakeQuestion.class);
                startActivity(intent);
            }
        });

        answerQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A3_2016030_QuizHomePage.this, A3_2016030_AnswerQuestion.class);
                startActivity(intent);
            }
        });
    }
}
