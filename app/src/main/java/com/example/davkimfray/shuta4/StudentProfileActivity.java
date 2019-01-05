package com.example.davkimfray.shuta4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.davkimfray.shuta4.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentProfileActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA= "data";
    private static final String KEY_STU_ID = "stu_id";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_M_NAME = "m_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOB = "dob";
    private static final String KEY_CLA_NAME = "cla_name";
    private static final String KEY_REG_NO = "reg_no";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private TextView txtView_stuName;
    private TextView txtView_stuRegNo;
    private TextView txtView_gender;
    private TextView txtView_claName;
    private TextView txtView_dob;
   // private TextView txtView_stuRegNo;
    private ProgressDialog pDialog;
    private String studentId;
    private String stuName;
    private String gender;
    private String dob;
    private String claName;
    private String regNo;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        txtView_stuName = findViewById(R.id.txt_name);
        txtView_stuRegNo = findViewById(R.id.txt_reg_no);
        txtView_gender = findViewById(R.id.txt_gender);
        txtView_claName = findViewById(R.id.txt_claName);
        txtView_dob = findViewById(R.id.txt_dob);
       // txtView_stuRegNo = findViewById(R.id.txt_reg_no);

        Intent intent = getIntent();
         studentId = intent.getStringExtra(KEY_STU_ID);
        new FetchStudentDetailsAsyncTask().execute();
    }

    class FetchStudentDetailsAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(StudentProfileActivity.this);
            pDialog.setMessage("Loading Details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_STU_ID, studentId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "get_student_details.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject student;
                if (success == 1) {
                    //Parse the JSON response
                    student = jsonObject.getJSONObject(KEY_DATA);
                    if(student.getString(KEY_M_NAME) == "null"){
                        stuName = student.getString(KEY_F_NAME) + " " +
                                student.getString(KEY_L_NAME);
                    }else{
                        stuName = student.getString(KEY_F_NAME) + " " +
                                student.getString(KEY_M_NAME) + " " +
                                student.getString(KEY_L_NAME);
                    }
                    gender = student.getString(KEY_GENDER);
                    claName = student.getString(KEY_CLA_NAME);
                    dob = student.getString(KEY_DOB);
                    regNo = student.getString(KEY_REG_NO);
                }
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
                    txtView_stuName.setText(stuName);
                    txtView_stuRegNo.setText(regNo);
                    txtView_gender.setText(gender);
                    txtView_claName.setText(claName);
                    txtView_dob.setText(dob);

                }
            });
        }
    }
}
