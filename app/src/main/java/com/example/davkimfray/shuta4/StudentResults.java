package com.example.davkimfray.shuta4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;


public class StudentResults extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_STU_ID = "stu_id";
    private static final String KEY_EXA_ID = "exa_id";
    private static final String KEY_EXA_NAME = "exa_name";
    private static final String KEY_SUB_NAME = "sub_name";
    private static final String KEY_MARKS = "marks";
    private static final String KEY_GRADE = "grade";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";

    private ArrayList<HashMap<String, String>> resultList, marksList;
    private ProgressDialog pDialog;

    private List<Results> resultsList = new ArrayList<>();
    TextView txtNoResults;
    private RecyclerView recyclerView;
    private ResultAdapter rAdapter;

    ImageView imgBtnRes1;

    String studentId;
    int examIdCount = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_results);

        txtNoResults = findViewById(R.id.txt_no_results);
        recyclerView = findViewById(R.id.recycler_results);
        imgBtnRes1 = findViewById(R.id.img_btn_res1);
        rAdapter = new ResultAdapter(resultsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rAdapter);


        Intent intent = getIntent();
        studentId = intent.getStringExtra(KEY_STU_ID);
      // studentId = "1";
        new FetchResultsAsyncTask().execute();

        recyclerView.setAdapter(rAdapter);


    }


    /**
     * Fetches the list of results from the server
     */
    private class FetchResultsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(StudentResults.this);
            pDialog.setMessage("Loading results. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_STU_ID,studentId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "get_student_results.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray results;
                if (success == 1) {
                    results = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate student list
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject result = results.getJSONObject(i);
                            Results res = new Results(result.getInt("exa_id"), result.getString("exa_name"),
                                    result.getString("sub_name"), Double.parseDouble(result.getString("marks")));
                            resultsList.add(res);
                        }

                }else {
                    txtNoResults.setVisibility(View.VISIBLE);

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
                   // populateResultsList();
                    rAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    /**
     * Updating parsed JSON data into ListView
     * */
    private void populateResultsList() {
      /*  ListAdapter examAdapter = new SimpleAdapter(StudentResults.this, resultList,
                R.layout.exam_result_list_item, new String[]{KEY_EXA_NAME}, new int[]{R.id.txt_exa_name});

        ListAdapter marksAdapter = new SimpleAdapter(StudentResults.this, resultList,
                R.layout.marks_result_list_item, new String[]{KEY_SUB_NAME, KEY_GRADE,
                KEY_MARKS}, new int[]{R.id.txt_sub_name, R.id.txt_grade, R.id.txt_marks});


        // updating listview


        //  display marks when an exam is clicked
        examListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                float deg = view.findViewById(R.id.img_btn_res1).getRotation() + 180F;
                view.findViewById(R.id.img_btn_res1).animate().rotation(deg).setInterpolator(
                        new AccelerateDecelerateInterpolator());

            }
         *//*   @Override
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
            }*//*
        });*/
    }

}
