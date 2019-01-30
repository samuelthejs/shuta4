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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davkimfray.shuta4.helper.CheckNetworkStatus;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
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

public class StudentRegistration extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_M_NAME = "m_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOB = "dob";
    private static final String KEY_CLA_ID = "cla_id";
    private static final String KEY_REG_NO = "reg_no";
    private static final String KEY_STU_IMAGE = "stu_image";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private ProgressDialog pDialog;
    DatePickerDialog dobPicker;
    EditText txtDob, txtFname, txtMname, txtLname, txtRegNo;
    TextView txtErrorLname, txtErrorFname, txtErrorRegno, txtErrorDob, txtErrorGender;
    RadioGroup radGender;
    RadioButton radiosex;
    Spinner spinnerClaId;
    Button btnStuReg;
    FloatingActionButton buttonChoose;
    CircleImageView profilePic;
    String classSelected, classId, gender, imgName;
    private  int success;


    // constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    StorageReference sRef;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_reg);

        txtFname = findViewById(R.id.txt_fname);
        txtMname = findViewById(R.id.txt_mname);
        txtLname = findViewById(R.id.txt_lname);
        txtRegNo = findViewById(R.id.txt_reg_no);
        radGender = findViewById(R.id.rad_gender);
        txtErrorLname = findViewById(R.id.txt_erroe_lname);
        txtErrorFname = findViewById(R.id.txt_erroe_fname);
        txtErrorRegno = findViewById(R.id.txt_erroe_regno);
        txtErrorDob = findViewById(R.id.txt_erroe_dob);
        txtErrorGender = findViewById(R.id.txt_erroe_gender);
        txtDob = findViewById(R.id.txt_dob);
        txtDob.setInputType(InputType.TYPE_NULL);
        spinnerClaId = findViewById(R.id.spinner_cla_id);
        profilePic = findViewById(R.id.img_profile_pic);
        btnStuReg = findViewById(R.id.btn_stu_reg);
        buttonChoose = findViewById(R.id.btn_upload_img);



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
              //  gender = radiosex.getText().toString();


                //checking for mandatory values
                if(txtLname.getText().toString().trim().isEmpty()) {
                    txtErrorLname.setText("*Enter Lastname*");
                    txtLname.setFocusable(true);
                }else{

                    txtErrorLname.setText("");
                    if(txtFname.getText().toString().trim().isEmpty()) {
                        txtErrorFname.setText("*Enter Firststname*");
                        txtFname.setFocusable(true);
                    }else{

                        txtErrorFname.setText("");
                        if (txtRegNo.getText().toString().trim().isEmpty()) {
                            txtErrorRegno.setText("*Enter Reg NO*");
                            txtRegNo.setFocusable(true);
                        }else{

                            txtErrorRegno.setText("");
                            if (txtDob.getText().toString().trim().isEmpty()) {
                                txtErrorDob.setText("*Enter Date of Birth*");
                                txtDob.setFocusable(true);
                            }else{

                                txtErrorDob.setText("");
                                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                                    //calling class addstudent
                                   // uploadFile();

                                    new AddStudentAsyncTask().execute();
                                } else {
                                    Toast.makeText(StudentRegistration.this,
                                            "Unable to connect to internet",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }
                }

            }
        });



        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

    }


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            imgName = System.currentTimeMillis() + "." + getFileExtension(filePath);
            Toast.makeText(StudentRegistration.this, imgName,Toast.LENGTH_LONG).show();

            //checking if file is available
           /* if (filePath != null) {
                //displaying progress dialog while image is uploading

                //getting the storage reference
                sRef = storageReference.child(imgName);

                //adding the file to reference
                sRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //displaying success toast
                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                //display an error if no file is selected
            }*/


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
            httpParams.put(KEY_STU_IMAGE, imgName);
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






    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading

            Toast.makeText(this, imgName,Toast.LENGTH_LONG).show();
            //getting the storage reference
            sRef = storageReference.child(imgName);

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }
}
