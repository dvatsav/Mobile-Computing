package com.example.brucewayne.a3_2016030;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class A3_2016030_QuestionDetailFragment extends Fragment {

    private final String QUESTION_ID = "Question ID";
    private final String QUESTION_TEXT = "Question Text";
    private final String QUESTION_ATTEMPTED = "Attempted";
    private final String QUESTION_ANSWER = "User Answer";

    RadioButton trueButton, falseButton;
    
    int q_id, q_attempted, q_usa;
    String q_text;

    public A3_2016030_QuestionDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        q_id = getArguments().getInt(QUESTION_ID);
        q_text = getArguments().getString(QUESTION_TEXT);
        q_attempted = getArguments().getInt(QUESTION_ATTEMPTED);
        q_usa = getArguments().getInt(QUESTION_ANSWER);
        
        View view = inflater.inflate(R.layout.a3_2016030_fragment_question_detail, container, false);

        TextView questionView = view.findViewById(R.id.questionDetailQuestionText);
        questionView.setText(q_text);

        trueButton = view.findViewById(R.id.questionDetailRadioTrue);
        falseButton = view.findViewById(R.id.questionDetailRadioFalse);
        
        if (q_attempted == 1) {
            if (q_usa == 1) {
                trueButton.setChecked(true);
            } else {
                falseButton.setChecked(true);
            }
        }
        
        Button saveButton = view.findViewById(R.id.questionDetailSubmitButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trueButton.isChecked() && !falseButton.isChecked()) {
                    Toast.makeText(getActivity(), "You must answer the question to save", Toast.LENGTH_SHORT).show();
                } else {
                    A3_2016030_DbManagement dbHelper = new A3_2016030_DbManagement(getActivity());
                    SQLiteDatabase wdb = dbHelper.getWritableDatabase();
                    String SQL_UPDATE =
                            "UPDATE " + A3_2016030_DbManagement.TABLE_NAME +
                                    " SET ATTEMPTED=1, USERANS=";
                    if (trueButton.isChecked()) {
                        SQL_UPDATE += Integer.toString(1);
                        q_attempted = 1;
                        q_usa = 1;
                    } else {
                        SQL_UPDATE += Integer.toString(0);
                        q_attempted = 1;
                        q_usa = 0;
                    }
                    SQL_UPDATE += " WHERE _ID=" + Integer.toString(q_id);
                    wdb.execSQL(SQL_UPDATE);
                    A3_2016030_QuestionBank questionBank = A3_2016030_QuestionBank.getInstance(getActivity());
                    questionBank.updateQuestion(q_id, q_attempted, q_usa);
                    Intent intent = new Intent(getActivity().getBaseContext(), A3_2016030_AnswerQuestion.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }


}
