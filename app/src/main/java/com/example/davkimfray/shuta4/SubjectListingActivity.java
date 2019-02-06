
package com.example.davkimfray.shuta4;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.davkimfray.shuta4.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SubjectListingActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_SUB_NAME = "sub_name";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private ArrayList<HashMap<String, String>> subjectList;
    private ListView subjectListView;
    private ProgressDialog pDialog;
    private SimpleAdapter subjectListAdapter;
    private Button btnSubReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_listing);
        subjectListView = findViewById(R.id.subject_list);
        btnSubReg = findViewById(R.id.btn_class_reg);

        btnSubReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regClass = new Intent(getApplicationContext(), SubjectRegistrationActivity.class);
                startActivity(regClass);
            }
        });

    
      new FetchClassesAsyncTask().execute();


      }

    /**
     * Fetches the list of student from the server
     */
    private class FetchClassesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(SubjectListingActivity.this);
            pDialog.setMessage("Loading students. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "get_subjects.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray classes;
                if (success == 1) {
                    subjectList = new ArrayList<>();
                    classes = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate student list
                    for (int i = 0; i < classes.length(); i++) {
                        JSONObject darasa = classes.getJSONObject(i);
                        String subName = darasa.getString(KEY_SUB_NAME);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_SUB_NAME, subName);
                        subjectList.add(map);
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
                    populateStudentList();
                }
            });
        }

    }

    /**
     * Updating parsed JSON data into ListView
     */
    private void populateStudentList() {
        subjectListAdapter = new SimpleAdapter(SubjectListingActivity.this, subjectList,
                R.layout.subject_list_item, new String[]{KEY_SUB_NAME}, new int[]{R.id.txt_sub_name});
        // updating listview
        subjectListView.setAdapter(subjectListAdapter);
    }


}