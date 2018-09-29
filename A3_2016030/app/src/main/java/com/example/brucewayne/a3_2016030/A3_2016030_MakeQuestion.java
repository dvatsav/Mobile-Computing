package com.example.brucewayne.a3_2016030;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class A3_2016030_MakeQuestion extends AppCompatActivity {

    @InjectView(R.id.questionContent)
    EditText questionContent;
    @InjectView(R.id.radioGroup)
    RadioGroup radioGroup;
    @InjectView(R.id.trueRadio)
    RadioButton trueRadio;
    @InjectView(R.id.falseRadio)
    RadioButton falseRadio;
    @InjectView(R.id.submitNewQuestion)
    Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a3_2016030_activity_make_question);
        ButterKnife.inject(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionContent.getText().toString();
                if (question.equals("")) {
                    Toast.makeText(A3_2016030_MakeQuestion.this, "You must enter some question", Toast.LENGTH_LONG).show();
                } else {
                    A3_2016030_DbManagement dbHelper = new A3_2016030_DbManagement(A3_2016030_MakeQuestion.this);
                    SQLiteDatabase wdb = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("QUESTION", question);
                    if (falseRadio.isChecked()) {
                        values.put("ANSWER", 0);
                    } else {
                        values.put("ANSWER", 1);
                    }
                    long newRow = wdb.insert(A3_2016030_DbManagement.TABLE_NAME, null, values);
                    Toast.makeText(A3_2016030_MakeQuestion.this, "Successfully added question", Toast.LENGTH_SHORT).show();
                    questionContent.setText("");
                    Intent intent = new Intent(A3_2016030_MakeQuestion.this, A3_2016030_QuizHomePage.class);
                    startActivity(intent);
                }
            }
        });
    }
}
