package com.example.davkimfray.shuta4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TeacherHomeActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String KEY_ADMIN_ID = "admin_id";

    String adminId;
    Button btnProfile,btnStudent,btnStaff,btnResults,btnClass,btnEvent,btnSubject;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);

        btnProfile = findViewById(R.id.btn_profile);
        btnClass = findViewById(R.id.btn_class);
        btnSubject = findViewById(R.id.btn_subjects);
        btnStudent = findViewById(R.id.btn_student);
        btnStaff = findViewById(R.id.btn_staff);
        btnResults = findViewById(R.id.btn_results);

        btnProfile.setOnClickListener(this);
        btnClass.setOnClickListener(this);
        btnSubject.setOnClickListener(this);
        btnStudent.setOnClickListener(this);
        btnStaff.setOnClickListener(this);
        btnResults.setOnClickListener(this);


        final Intent intent = getIntent();
        adminId = intent.getStringExtra(KEY_ADMIN_ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_profile:
                Intent intent1 = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                // intent1.putExtra(KEY_STU_ID, studentId);
                ///jhfgj
                startActivity(intent1);
                break;
            case R.id.btn_student:
                Intent intent2 = new Intent(getApplicationContext(),StudentListingActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent2);
                break;
            case R.id.btn_staff:
                Intent intent3 = new Intent(getApplicationContext(),TeacherListingActivity.class);
                intent3.putExtra(KEY_ADMIN_ID, adminId);
                startActivity(intent3);
                break;
            case R.id.btn_results:
                Intent intent4 = new Intent(getApplicationContext(),StudentResults.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent4);
                break;
            case R.id.btn_subjects:
                Intent intent5 = new Intent(getApplicationContext(),SubjectListingActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent5);
                break;
            case R.id.btn_class:
                Intent intent6 = new Intent(getApplicationContext(),ClassListingActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent6);
                break;

        }
    }
    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(TeacherHomeActivity.this);
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
