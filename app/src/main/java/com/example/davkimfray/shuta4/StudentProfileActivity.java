package com.example.davkimfray.shuta4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private static final String KEY_STU_IMAGE = "stu_image";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private TextView txtView_stuName;
    private TextView txtView_stuRegNo;
    private TextView txtView_gender;
    private TextView txtView_claName;
    private TextView txtView_dob;
    private CircleImageView profileImage;
    private ImageView profileImageBg;
   // private TextView txtView_stuRegNo;
    private ProgressDialog pDialog;
    private String studentId;
    private String stuName;
    private String gender;
    private String dob;
    private String claName;
    private String regNo, stuImage;
    private StorageReference storageReference;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        txtView_stuName = findViewById(R.id.txt_name);
        txtView_stuRegNo = findViewById(R.id.txt_reg_no);
        txtView_gender = findViewById(R.id.txt_gender);
        txtView_claName = findViewById(R.id.txt_claName);
        txtView_dob = findViewById(R.id.txt_dob);
        profileImage = findViewById(R.id.profile_image);
        profileImageBg = findViewById(R.id.bg_profile_img);

        // txtView_stuRegNo = findViewById(R.id.txt_reg_no);

        storageReference = FirebaseStorage.getInstance().getReference();

        final Intent intent = getIntent();
         studentId = intent.getStringExtra(KEY_STU_ID);


        /**
         * button results section
         */
        Button btnRes = findViewById(R.id.btn_res);
        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRes = new Intent(getApplicationContext(),StudentResults.class);
                intentRes.putExtra(KEY_STU_ID, studentId);
                startActivity(intentRes);
            }
        });


        /**
         * button edit section
         */
        Button btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(getApplicationContext(),StudentRegistration.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intentEdit);
            }
        });


        /**
         * button delete section
         */
        Button btnDel = findViewById(R.id.btn_del);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intentDel = new Intent(getApplicationContext(),StudentResults.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intentDel);*/


                final AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfileActivity.this);
                builder.setMessage("Are you sure, you want to Delete?");
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Toast.makeText(StudentProfileActivity.this, "Weka function ya" +
                                " kufuta", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


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
                    stuImage = student.getString(KEY_STU_IMAGE);

                    //fetch image url from firebase
                    storageReference.child("/"+stuImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Glide.with(StudentProfileActivity.this).load(uri).into(profileImage);
                            Glide.with(StudentProfileActivity.this).load(uri).into(profileImageBg);
                        }
                    });
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
