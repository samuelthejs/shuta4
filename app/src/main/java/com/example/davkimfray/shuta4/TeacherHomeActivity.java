package com.example.davkimfray.shuta4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TeacherHomeActivity extends AppCompatActivity implements View.OnClickListener{

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
        btnEvent = findViewById(R.id.btn_event);

        btnProfile.setOnClickListener(this);
        btnClass.setOnClickListener(this);
        btnSubject.setOnClickListener(this);
        btnStudent.setOnClickListener(this);
        btnStaff.setOnClickListener(this);
        btnResults.setOnClickListener(this);
        btnEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_profile:
                Intent intent1 = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent1);
                break;
            case R.id.btn_student:
                Intent intent2 = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent2);
                break;
            case R.id.btn_staff:
                Intent intent3 = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent3);
                break;
            case R.id.btn_results:
                Intent intent4 = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent4);
                break;
            case R.id.btn_subjects:
                Intent intent5 = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent5);
                break;
            case R.id.btn_class:
                Intent intent6 = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent6);
                break;
            case R.id.btn_event:
                Intent intent7 = new Intent(getApplicationContext(),TeacherProfileActivity.class);
                // i.putExtra(KEY_STU_ID, studentId);
                startActivity(intent7);
                break;

        }
    }
}