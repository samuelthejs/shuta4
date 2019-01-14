package com.example.davkimfray.shuta4;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.davkimfray.shuta4.helper.CheckNetworkStatus;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentRegistration extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_M_NAME = "m_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOB = "dob";
    private static final String KEY_CLA_ID = "cla_id";
    private static final String KEY_REG_NO = "reg_no";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private ProgressDialog pDialog;
    DatePickerDialog dobPicker;
    EditText txtDob, txtFname, txtMname, txtLname, txtRegNo;
    RadioGroup radGender;
    RadioButton radiosex;
    Spinner spinnerClaId;
    Button btnStuReg;
    String classSelected, classId, gender;
    private  int success;



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_reg);

        txtFname = findViewById(R.id.txt_fname);
        txtMname = findViewById(R.id.txt_mname);
        txtLname = findViewById(R.id.txt_lname);
        txtRegNo = findViewById(R.id.txt_reg_no);
        radGender = findViewById(R.id.rad_gender);
        txtDob = findViewById(R.id.txt_dob);
        txtDob.setInputType(InputType.TYPE_NULL);
        spinnerClaId = findViewById(R.id.spinner_cla_id);
        btnStuReg = findViewById(R.id.btn_stu_reg);


        /**
         * Date of birth section
         */
        txtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                //date picker dialod
                dobPicker = new DatePickerDialog(StudentRegistration.this, new
                        DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtDob.setText(year + "-" + (month +1)+ "-"+ dayOfMonth);
                    }
                },year,month,day);
                dobPicker.show();
            }
        });


        /**
         *  Class Spinner section
         */
        List<String> classes = new ArrayList<String>();
        classes.add("Form I");
        classes.add("Form II");
        classes.add("Form III");
        classes.add("Form IV");

        // Creating adapter for spinner
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(
               this, android.R.layout.simple_spinner_item, classes);

        // Drop down layout style - list view with radio button
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching class adapter to spinner
        spinnerClaId.setAdapter(classAdapter);

        // Spinner click/select listener
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        /**
         * Button Registration section
         */
        btnStuReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Gender section
                 */
                int selectedRadio = radGender.getCheckedRadioButtonId();
                radiosex = findViewById(selectedRadio);
                gender = radiosex.getText().toString();

                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    //calling class addstudent
                    new AddStudentAsyncTask().execute();
                } else {
                    Toast.makeText(StudentRegistration.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    /**
     * Adding student section using Async Task
     */
    private class AddStudentAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(StudentRegistration.this);
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
            httpParams.put(KEY_F_NAME, txtFname.getText().toString());
            httpParams.put(KEY_M_NAME, txtMname.getText().toString());
            httpParams.put(KEY_L_NAME, txtLname.getText().toString());
            httpParams.put(KEY_GENDER, gender);
            httpParams.put(KEY_DOB, txtDob.getText().toString());
            httpParams.put(KEY_REG_NO, txtRegNo.getText().toString());
            httpParams.put(KEY_CLA_ID, classId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_student.php", "POST", httpParams);

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
                        Toast.makeText(StudentRegistration.this, "Failed to add " +
                                        "student", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(StudentRegistration.this, "Student successfully" +
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
