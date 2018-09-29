package com.example.brucewayne.a3_2016030;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class A3_2016030_QuestionListFragment extends Fragment {

    private final String DB_SELECT_ALL =
            "SELECT * FROM " + A3_2016030_DbManagement.TABLE_NAME;
    private RecyclerView questionRecyclerView;
    private QuestionAdapter qAdapter;
    private ProgressBar progressBar;

    public A3_2016030_QuestionListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDB();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.a3_2016030_fragment_question_list, container, false);
        questionRecyclerView = view.findViewById(R.id.questionRecylerView);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.a3_2016030_menu_file, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_submit:
                A3_2016030_DbManagement dbHelper = new A3_2016030_DbManagement(getActivity());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String SQLretrieve = "SELECT * FROM " + A3_2016030_DbManagement.TABLE_NAME;
                Cursor cursor = db.rawQuery(SQLretrieve, null);

                String filename = "quiz_info.csv";
                ArrayList<String> fileContents = new ArrayList<>();
                fileContents.add("Question ID,Question,Correct Answer,Question Attempted,User Answer\n");

                FileOutputStream outputStream;

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String question = cursor.getString(1);
                    int correct_answer = cursor.getInt(2);
                    int has_attempted = cursor.getInt(3);
                    int user_answer = cursor.getInt(4);
                    String content = Integer.toString(id) + "," + question + ",";
                    if (correct_answer == 1) {
                        content += "True,";
                    } else {
                        content += "False,";
                    }

                    if (has_attempted == 1) {
                        content += "Yes,";
                        if (user_answer == 1) {
                            content += "True";
                        } else {
                            content += "False";
                        }
                    } else {
                        content += "No," + "-";
                    }
                    content += "\n";
                    fileContents.add(content);

                }

                try {
                    outputStream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                    for (int i = 0 ; i < fileContents.size() ; ++i) {
                        outputStream.write(fileContents.get(i).getBytes());
                    }
                    outputStream.close();
                    Toast.makeText(getActivity(), "Successfully wrote data to csv file", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                    new UploadFile().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Could not write to CSV file", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_mainmenu:
                Intent intent = new Intent(getActivity().getBaseContext(), A3_2016030_QuizHomePage.class);
                startActivity(intent);
                break;

        }

        return true;
    }

    private void initializeDB() {
        A3_2016030_DbManagement dbHelper = new A3_2016030_DbManagement(getActivity());
        SQLiteDatabase rdb = dbHelper.getReadableDatabase();
        Cursor cursor = rdb.rawQuery(DB_SELECT_ALL, null);
        A3_2016030_QuestionBank qBank = A3_2016030_QuestionBank.getInstance(getActivity());
        while (cursor.moveToNext()) {
            int qID = cursor.getInt(0);
            String question = cursor.getString(1);
            int answer = cursor.getInt(2);
            int attempted = cursor.getInt(3);
            int userAnswer = cursor.getInt(4);
            qBank.addQuestion(qID, question, answer, attempted, userAnswer);
        }
    }

    private void updateUI() {
        A3_2016030_QuestionBank qBank = A3_2016030_QuestionBank.getInstance(getActivity());
        ArrayList<A3_2016030_Question> questions = qBank.getQuestions();
        qAdapter = new QuestionAdapter(questions);
        questionRecyclerView.setAdapter(qAdapter);
    }

    private class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private A3_2016030_Question question;
        private TextView quesionText;

        public QuestionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.a3_2016030_list_item_question, parent, false));
            itemView.setOnClickListener(this);
            quesionText = itemView.findViewById(R.id.questionText);

        }

        public void bind(A3_2016030_Question question) {
            this.question = question;
            quesionText.setText(question.questionText);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity().getBaseContext(), A3_2016030_QuestionDetail.class);
            Bundle bundle = new Bundle();
            System.out.println(question.qID + " " + question.questionText + " " + question.attempted + " " + question.userAnswer);
            bundle.putInt("Question ID", question.qID);
            bundle.putString("Question Text", question.questionText);
            bundle.putInt("Attempted", question.attempted);
            bundle.putInt("User Answer", question.userAnswer);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {
        private ArrayList<A3_2016030_Question> questionList;

        public QuestionAdapter(ArrayList<A3_2016030_Question> questions) {
            questionList = questions;
        }

        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new QuestionHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            A3_2016030_Question question = questionList.get(position);
            holder.bind(question);
        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }
    }

    private class UploadFile extends AsyncTask<Void, Integer, String> {
        protected void onPreExecute() {
            ;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            DataOutputStream outputStream = null;
            FileInputStream fileInputStream = null;
            try {
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                String filePath = getActivity().getFilesDir().getParentFile().getPath() + "/files/quiz_info.csv";
                File file = new File(filePath);

                fileInputStream = new FileInputStream(file);

                URL url = new URL("http://192.168.65.164/upload.php");
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                httpURLConnection.setRequestProperty("uploaded_file", "quiz_info.csv");

                outputStream = new DataOutputStream(httpURLConnection.getOutputStream());

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+"quiz_info.csv"+"\"" + lineEnd);

                outputStream.writeBytes(lineEnd);

                int bytesAvailable = fileInputStream.available();
                int total_size = bytesAvailable;
                int bufferSize = Math.min(bytesAvailable, 1024*1024);
                byte[] buffer = new byte[bufferSize];

                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                int total_read = bytesRead;

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    publishProgress((int)(total_read/total_size));
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, 1024*1024);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);

                int serverResponse = httpURLConnection.getResponseCode();
                String serverResponseMsg = httpURLConnection.getResponseMessage();
                if (serverResponse == 200) {
                    System.out.println("Successfully uploaded file to server");
                    System.out.println(serverResponseMsg);
                } else {
                    System.out.println("Did not upload file to server");
                }
                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }


}
