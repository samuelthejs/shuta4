package com.example.davkimfray.shuta4;


import android.animation.AnimatorInflater;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.davkimfray.shuta4.helper.CheckNetworkStatus;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherListingActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_M_NAME = "m_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ADMIN_ID = "admin_id";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private ArrayList<HashMap<String, String>> teacherList;
    private ListView teacherListView;
    private ProgressDialog pDialog;
    private SimpleAdapter teacherListAdapter;
    private String adminId;
    private Button btnStaffReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_listing);
        teacherListView = findViewById(R.id.teacherList);
        btnStaffReg = findViewById(R.id.btn_staff_reg);

        final Intent intent = getIntent();
        adminId = intent.getStringExtra(KEY_ADMIN_ID);

        if (adminId.equals("")){
            btnStaffReg.setVisibility(View.GONE);
        }

        btnStaffReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regClass = new Intent(getApplicationContext(), StaffRegistration.class);
                startActivity(regClass);
            }
        });

        new FetchTeacherAsyncTask().execute();



    }

    /**
     * Fetches the list of student from the server
     */
    private class FetchTeacherAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(TeacherListingActivity.this);
            pDialog.setMessage("Loading students. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "fetch_all_teachers.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray teachers;
                if (success == 1) {
                    teacherList = new ArrayList<>();
                    teachers = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate student list
                    for (int i = 0; i < teachers.length(); i++) {
                        JSONObject teacher = teachers.getJSONObject(i);
                        String fName = teacher.getString(KEY_F_NAME) +" ";

                        String mName;
                        if(teacher.getString(KEY_M_NAME) == "null"){
                             mName = "";
                        }else {
                             mName = teacher.getString(KEY_M_NAME) + " ";
                        }

                        String lName = teacher.getString(KEY_L_NAME);
                        String title = teacher.getString(KEY_TITLE);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_F_NAME, fName);
                        map.put(KEY_M_NAME, mName);
                        map.put(KEY_L_NAME, lName);
                        map.put(KEY_TITLE, title);
                        teacherList.add(map);
                    }
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
                    populateTeacherList();
                }
            });
        }

    }

    /**
     * Updating parsed JSON data into ListView
     * */
    private void populateTeacherList() {
         teacherListAdapter = new SimpleAdapter(TeacherListingActivity.this, teacherList,
                R.layout.teacher_list_item, new String[]{KEY_F_NAME, KEY_M_NAME, KEY_L_NAME, KEY_TITLE},
                 new int[]{R.id.txt_fName, R.id.txt_mName, R.id.txt_lName,  R.id.txt_title});

        // updating listview
        teacherListView.setAdapter(teacherListAdapter);





    }

}