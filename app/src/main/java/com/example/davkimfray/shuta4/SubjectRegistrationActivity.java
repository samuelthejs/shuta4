package com.example.davkimfray.shuta4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davkimfray.shuta4.helper.CheckNetworkStatus;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectRegistrationActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_SUB_NAME = "sub_name";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private ProgressDialog pDialog;
    EditText txtSubjectName;
    TextView txtErrorSubname;
    Button btnAddSubject;
    private  int success;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_registration);

        txtSubjectName = findViewById(R.id.txt_subName);
        txtErrorSubname = findViewById(R.id.txt_erroe_subname);
        btnAddSubject = findViewById(R.id.btn_add_subject);







        /**
         * Button Registration section
         */
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //checking for mandatory values
                if(txtSubjectName.getText().toString().trim().isEmpty()) {
                    txtErrorSubname.setText("*Enter Subject Name*");
                    txtSubjectName.setFocusable(true);
                }else{

                    txtErrorSubname.setText("");
                    if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                        //calling class addstudent
                       // uploadFile();

                        new AddClassAsyncTask().execute();
                    } else {
                        Toast.makeText(SubjectRegistrationActivity.this,
                                "Unable to connect to internet",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }


    /**
      * Adding student section using Async Task
     */
    private class AddClassAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(SubjectRegistrationActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

                 /* Toast.makeText(StudentRegistration.this, "Failed to add " +
                    "student", Toast.LENGTH_SHORT).show();*/
           HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_SUB_NAME, txtSubjectName.getText().toString());
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_subject.php", "POST", httpParams);

            try {
                success = jsonObject.getInt(KEY_SUCCESS);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    //Populate the Edit Texts once the network activity is finished executing
                    if(success == 0) {
                        Toast.makeText(SubjectRegistrationActivity.this, "Failed to add " +
                                        "Subject", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(SubjectRegistrationActivity.this, "Subject successfully" +
                                        " added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),
                                SubjectListingActivity.class);
                        startActivity(i);
                        finish();

                    }

                }
            });
        }
    }






}
