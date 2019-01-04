package com.example.davkimfray.shuta4;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;


import com.example.davkimfray.shuta4.helper.HttpJsonParser;
import com.transitionseverywhere.TransitionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTabbedActivity extends Activity {
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
    private static final String BASE_URL = "http://10.0.0.26/android/";
    private TabHost tabhost;
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

    int inc=0;
    LinearLayout l1,l2;
    Animation uptodown,downtoup;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_tabbed);

        tabhost = findViewById(R.id.tabhost);
        tabhost.setup();

        TabHost.TabSpec spec = tabhost.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("My Profile");
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Results");
        tabhost.addTab(spec);


        txtView_stuName = findViewById(R.id.txt_name);
        txtView_stuRegNo = findViewById(R.id.txt_reg_no);
        txtView_gender = findViewById(R.id.txt_gender);
        txtView_claName = findViewById(R.id.txt_claName);
        txtView_dob = findViewById(R.id.txt_dob);
        // txtView_stuRegNo = findViewById(R.id.txt_reg_no);


        Intent intent = getIntent();
        studentId = intent.getStringExtra(KEY_STU_ID);
     //   studentId="4";


//


        final Button button = findViewById(R.id.button);
        l1 = (LinearLayout) findViewById(R.id.l1);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        //l1.setAnimation(uptodown);

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                inc++;
                if(inc ==1) {
                    l1.setAnimation(uptodown);
                }else if(inc == 2) {
                    l1.setAnimation(downtoup);
                }
               // button.setText("jgjjjfhfhfhgfgh");
            }

        });

        // Spinner element

        // Spinner click listener===spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

    new FetchStudentDetailsAsyncTask().execute();

    }


    private class FetchStudentDetailsAsyncTask extends AsyncTask<String, String, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Display progress bar
        pDialog = new ProgressDialog(StudentTabbedActivity.this);
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
                if(student.getString(KEY_M_NAME).equals("null")){
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

    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(StudentTabbedActivity.this);
        builder.setMessage("Are you sure, you want to Exit?");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();





    }

}
