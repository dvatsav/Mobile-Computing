package com.example.brucewayne.a3_2016030;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class A3_2016030_QuestionDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a3_2016030_activity_question_detail);
        Bundle bundle = getIntent().getExtras();
        System.out.println(bundle.get("Question ID"));

        Fragment fragment = new A3_2016030_QuestionDetailFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.questionDetailFragmentContainer, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }


}
