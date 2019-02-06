package com.example.davkimfray.shuta4;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davkimfray.shuta4.helper.CheckNetworkStatus;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassRegistrationActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_CLA_NAME = "cla_name";
    private static final String KEY_TEA_ID = "tea_id";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private ProgressDialog pDialog;
    EditText txtClassName;
    TextView txtErrorCname, txtErrorTea;
    Button btnAddClass;
    AutoCompleteTextView autoTeacher;
    private  int success;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_registration);

        txtClassName = findViewById(R.id.txt_className);
        txtErrorCname = findViewById(R.id.txt_erroe_cname);
        autoTeacher = findViewById(R.id.auto_txt_teacher);
        txtErrorTea = findViewById(R.id.txt_erro_tea);
        btnAddClass = findViewById(R.id.btn_add_class);


        List<String> teachers = new ArrayList<String>();
        teachers.add("Form I");
        teachers.add("Form II");
        teachers.add("Form III");
        teachers.add("Form IV");

        // Creating adapter for spinner
        ArrayAdapter<String> teacherAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, teachers);

        autoTeacher.setAdapter(teacherAdapter);





        /**
         *  Class Spinner section
         */



      /*  // Spinner click/select listener
        spinnerClaId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classSelected = parent.getItemAtPosition(position).toString();
                switch (classSelected){
                    case "Form I":
                        classId = "1";
                        break;
                    case "Form II":
                        classId = "2";
                        break;
                    case "Form III":
                        classId = "3";
                        break;
                    case "Form IV":
                        classId = "4";
                        break;
                }
            }*/





        /**
         * Button Registration section
         */
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //checking for mandatory values
                if(txtClassName.getText().toString().trim().isEmpty()) {
                    txtErrorCname.setText("*Enter Lastname*");
                    txtClassName.setFocusable(true);
                }else{

                    txtErrorCname.setText("");
                    if(autoTeacher.getText().toString().trim().isEmpty()) {
                        txtErrorTea.setText("*Enter Firststname*");
                        autoTeacher.setFocusable(true);
                    }else{

                        txtErrorTea.setText("");

                                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                                    //calling class addstudent
                                   // uploadFile();

                                    new AddClassAsyncTask().execute();
                                } else {
                                    Toast.makeText(ClassRegistrationActivity.this,
                                            "Unable to connect to internet",
                                            Toast.LENGTH_LONG).show();
                                }

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
            pDialog = new ProgressDialog(ClassRegistrationActivity.this);
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
            httpParams.put(KEY_CLA_NAME, txtClassName.getText().toString());
            httpParams.put(KEY_TEA_ID, autoTeacher.getText().toString());
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_class.php", "POST", httpParams);

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
                        Toast.makeText(ClassRegistrationActivity.this, "Failed to add " +
                                        "student", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ClassRegistrationActivity.this, "Class successfully" +
                                        " added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),
                                StudentListingActivity.class);
                        startActivity(i);
                        finish();

                    }

                }
            });
        }
    }






}
