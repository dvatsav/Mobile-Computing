package com.example.deepak.assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserDetails_A1_2016030 extends AppCompatActivity {

    private static final String TAG = "UserDetails";
    TextView studentName, rollNumber, branch, courseOne, courseTwo, courseThree, courseFour;
    boolean prevStop, prevPause, prevRestart, prevCreate, prevStart;

    private void unpackBundle(Bundle bundle)
    {
        studentName.setText(bundle.getString("Name"));
        rollNumber.setText(bundle.getString("Roll Number"));
        branch.setText(bundle.getString("Branch"));
        ArrayList<String> courses = bundle.getStringArrayList("Courses");
        courseOne.setText(courses.get(0));
        courseTwo.setText(courses.get(1));
        courseThree.setText(courses.get(2));
        courseFour.setText(courses.get(3));
    }

    private void initializeElements()
    {
        studentName = findViewById(R.id.details_name_value);
        rollNumber = findViewById(R.id.details_rollNumber_value);
        branch = findViewById(R.id.details_branch_value);
        courseOne = findViewById(R.id.details_courseOne_value);
        courseTwo = findViewById(R.id.details_courseTwo_value);
        courseThree = findViewById(R.id.details_courseThree_value);
        courseFour = findViewById(R.id.details_courseFour_value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Bundle bundle = getIntent().getExtras();
        initializeElements();
        if (bundle != null)
            unpackBundle(bundle);
        if (prevStop) {
            Log.i(TAG, "State of Activity - UserDetails changed: changed from Stopped to Create");
            Toast.makeText(this, "State of Activity - UserDetails changed: changed from Stopped to Create", Toast.LENGTH_SHORT).show();
            prevStop = false;
        } else if (prevPause){
            Log.i(TAG, "State of Activity - UserDetails changed: changed from Paused to Create");
            Toast.makeText(this, "State of Activity - UserDetails changed: changed from Paused to Create", Toast.LENGTH_SHORT).show();
            prevPause = false;

        } else {
            Log.i(TAG, "State of Activity - UserDetails changed: Activity Launched and started with onCreate");
            Toast.makeText(this, "State of Activity - UserDetails changed: Activity Launched and started with onCreate", Toast.LENGTH_SHORT).show();

        }
        prevCreate = true;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (prevCreate) {
            Log.i(TAG, "State of Activity - UserDetails changed: changed from Create to Start");
            Toast.makeText(this, "State of Activity - UserDetails changed: changed from Create to Start", Toast.LENGTH_SHORT).show();
            prevCreate = false;
        } else if (prevRestart){
            Log.i(TAG, "State of Activity - UserDetails changed: changed from Restart to Start");
            Toast.makeText(this, "State of Activity - UserDetails changed: changed from Restart to Start", Toast.LENGTH_SHORT).show();
            prevRestart = false;
        }
        prevStart = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (prevStart) {
            Log.i(TAG, "State of Activity - UserDetails changed: changed from Start to Resume");
            Toast.makeText(this, "State of Activity - UserDetails changed: changed from Start to Resume", Toast.LENGTH_SHORT).show();
            prevStart = false;
        } else if (prevPause){
            Log.i(TAG, "State of Activity - UserDetails changed: changed from Pause to Resume");
            Toast.makeText(this, "State of Activity - UserDetails changed: changed from Pause to Resume", Toast.LENGTH_SHORT).show();
            prevPause = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "State of Activity - UserDetails changed: changed from Activity Running to Pause");
        Toast.makeText(this, "State of Activity - UserDetails changed: changed from Activity Running to Pause", Toast.LENGTH_SHORT).show();
        prevPause = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(TAG, "State of Activity - UserDetails changed: changed from Pause to Stop");
        Toast.makeText(this, "State of Activity - UserDetails changed: changed from Pause to Stop", Toast.LENGTH_SHORT).show();
        prevStop = true;
    }

    @Override
    public void onRestart() {
        super.onRestart();

        Log.i(TAG, "State of Activity - UserDetails changed: changed from Stop to Restart");
        Toast.makeText(this, "State of Activity - UserDetails changed: changed from Stop to Restart", Toast.LENGTH_SHORT).show();
        prevRestart = true;
        prevStop = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "State of Activity - UserDetails changed: changed from Stop to Destroy");
        Toast.makeText(this, "State of Activity - UserDetails changed: changed from Stop to Destroy", Toast.LENGTH_SHORT).show();
        prevStop = false;
    }
}
