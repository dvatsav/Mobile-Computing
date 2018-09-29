package com.example.deepak.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Registration_A1_2016030 extends AppCompatActivity {

    EditText studentName, rollNumber, branch, courseOne, courseTwo, courseThree, courseFour;
    Button clear, submit;
    private static final String TAG = "Registration";
    boolean prevStop, prevPause, prevRestart, prevCreate, prevStart;

    private Bundle createBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("Name", studentName.getText().toString());
        bundle.putString("Roll Number", rollNumber.getText().toString());
        bundle.putString("Branch", branch.getText().toString());
        ArrayList<String> courses = new ArrayList<>();
        courses.add(courseOne.getText().toString());
        courses.add(courseTwo.getText().toString());
        courses.add(courseThree.getText().toString());
        courses.add(courseFour.getText().toString());
        bundle.putStringArrayList("Courses", courses);
        return bundle;
    }

    private void clearAll() {
        studentName.getText().clear();
        rollNumber.getText().clear();
        branch.getText().clear();
        courseOne.getText().clear();
        courseTwo.getText().clear();
        courseThree.getText().clear();
        courseFour.getText().clear();
    }

    private void initializeElements() {
        studentName = findViewById(R.id.name);
        rollNumber = findViewById(R.id.rollNumber);
        branch = findViewById(R.id.branch);
        courseOne = findViewById(R.id.courseOne);
        courseTwo = findViewById(R.id.courseTwo);
        courseThree = findViewById(R.id.courseThree);
        courseFour = findViewById(R.id.courseFour);
        clear = findViewById(R.id.clearButton);
        submit = findViewById(R.id.submitButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeElements();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = createBundle();
                Intent intent = new Intent(Registration_A1_2016030.this, UserDetails_A1_2016030.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });
        if (prevStop) {
            Log.i(TAG, "State of Activity - Registration changed: changed from Stopped to Create");
            Toast.makeText(this, "State of Activity - Registration changed: changed from Stopped to Create", Toast.LENGTH_SHORT).show();
            prevStop = false;
        } else if (prevPause){
            Log.i(TAG, "State of Activity - Registration changed: changed from Paused to Create");
            Toast.makeText(this, "State of Activity - Registration changed: changed from Paused to Create", Toast.LENGTH_SHORT).show();
            prevPause = false;
        } else {
            Log.i(TAG, "State of Activity - Registration changed: Activity Launched and started with onCreate");
            Toast.makeText(this, "State of Activity - Registration changed: Activity Launched and started with onCreate", Toast.LENGTH_SHORT).show();
        }
        prevCreate = true;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (prevCreate) {
            Log.i(TAG, "State of Activity - Registration changed: changed from Create to Start");
            Toast.makeText(this, "State of Activity - Registration changed: changed from Create to Start", Toast.LENGTH_SHORT).show();
            prevCreate = false;
        } else if (prevRestart){
            Log.i(TAG, "State of Activity - Registration changed: changed from Restart to Start");
            Toast.makeText(this, "State of Activity - Registration changed: changed from Restart to Start", Toast.LENGTH_SHORT).show();
            prevRestart = false;
        }
        prevStart = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (prevStart) {
            Log.i(TAG, "State of Activity - Registration changed: changed from Start to Resume");
            Toast.makeText(this, "State of Activity - Registration changed: changed from Start to Resume", Toast.LENGTH_SHORT).show();
            prevStart = false;
        } else if (prevPause){
            Log.i(TAG, "State of Activity - Registration changed: changed from Pause to Resume");
            Toast.makeText(this, "State of Activity - Registration changed: changed from Pause to Resume", Toast.LENGTH_SHORT).show();
            prevPause = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "State of Activity - Registration changed: changed from Activity Running to Pause");
        Toast.makeText(this, "State of Activity - Registration changed: changed from Activity Running to Pause", Toast.LENGTH_SHORT).show();
        prevPause = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(TAG, "State of Activity - Registration changed: changed from Pause to Stop");
        Toast.makeText(this, "State of Activity - Registration changed: changed from Pause to Stop", Toast.LENGTH_SHORT).show();
        prevStop = true;
    }

    @Override
    public void onRestart() {
        super.onRestart();

        Log.i(TAG, "State of Activity - Registration changed: changed from Stop to Restart");
        Toast.makeText(this, "State of Activity - Registration changed: changed from Stop to Restart", Toast.LENGTH_SHORT).show();
        prevStop = false;
        prevRestart = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "State of Activity - Registration changed: changed from Stop to Destroy");
        Toast.makeText(this, "State of Activity - Registration changed: changed from Stop to Destroy", Toast.LENGTH_SHORT).show();
        prevStop = false;
    }
}
