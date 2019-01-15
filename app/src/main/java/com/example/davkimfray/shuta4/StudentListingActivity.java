package com.example.davkimfray.shuta4;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davkimfray.shuta4.helper.CheckNetworkStatus;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentListingActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_STU_ID = "stu_id";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_M_NAME = "m_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String KEY_REG_NO = "reg_no";
    private static final String KEY_CLA_NAME = "cla_name";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private ArrayList<HashMap<String, String>> studentList;
    private ListView studentListView;
    private ProgressDialog pDialog;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private SimpleAdapter studentListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_listing);
        studentListView = findViewById(R.id.studentList);

        FloatingActionButton studentReg = findViewById(R.id.float_student_reg);
        studentReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regStudent = new Intent(getApplicationContext(), StudentRegistration.class);
                startActivity(regStudent);
            }
        });
        new FetchMoviesAsyncTask().execute();



    }

    /**
     * Fetches the list of student from the server
     */
    private class FetchMoviesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(StudentListingActivity.this);
            pDialog.setMessage("Loading students. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "fetch_all_data.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray movies;
                if (success == 1) {
                    studentList = new ArrayList<>();
                    movies = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate student list
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject student = movies.getJSONObject(i);
                        Integer studentId = student.getInt(KEY_STU_ID);
                        String fName = student.getString(KEY_F_NAME) +" ";

                        String mName;
                        if(student.getString(KEY_M_NAME) == "null"){
                             mName = "";
                        }else {
                             mName = student.getString(KEY_M_NAME) + " ";
                        }

                        String lName = student.getString(KEY_L_NAME);
                        String regNo = student.getString(KEY_REG_NO);
                        String claName = student.getString(KEY_CLA_NAME);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_STU_ID, studentId.toString());
                        map.put(KEY_F_NAME, fName);
                        map.put(KEY_M_NAME, mName);
                        map.put(KEY_L_NAME, lName);
                        map.put(KEY_REG_NO, regNo);
                        map.put(KEY_CLA_NAME, claName);
                        studentList.add(map);
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
     * */
    private void populateStudentList() {
         studentListAdapter = new SimpleAdapter(StudentListingActivity.this, studentList,
                R.layout.list_item, new String[]{KEY_STU_ID, KEY_F_NAME, KEY_M_NAME,
                        KEY_L_NAME, KEY_REG_NO, KEY_CLA_NAME}, new int[]{R.id.txt_stuId, R.id.txt_fName,
                        R.id.txt_mName, R.id.txt_lName,  R.id.txt_regNo, R.id.txt_class});
        // updating listview
        studentListView.setAdapter(studentListAdapter);
      //  Call MovieUpdateDeleteActivity when a movie is clicked
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    String stuId = ((TextView) view.findViewById(R.id.txt_stuId))
                            .getText().toString();
                    Intent intent = new Intent(getApplicationContext(),
                            StudentProfileActivity.class);
                    intent.putExtra(KEY_STU_ID, stuId);
                    startActivityForResult(intent, 20);

                } else {
                    Toast.makeText(StudentListingActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public boolean onCreateOptionMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);

        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}