package com.example.davkimfray.shuta4;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.example.davkimfray.shuta4.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StudentResultsOld extends AppCompatActivity implements View.OnClickListener{

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
    private ListView marksListView, examListView;
    private ProgressDialog pDialog;

    Button btnRes1,btnRes2,btnRes3,btnRes4;
    ImageView imgBtnRes1;
    LinearLayout layRes1,layRes2,layRes3,layRes4;
    Animation uptodown,downtoup;
    Drawable upArrow,downArrow;

    String studentId;
    int examIdCount = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_results);

        imgBtnRes1 = findViewById(R.id.img_btn_res1);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        upArrow = getResources().getDrawable(android.R.drawable.arrow_up_float);
        downArrow = getResources().getDrawable(android.R.drawable.arrow_down_float);

       /* Intent intent = getIntent();
        studentId = intent.getStringExtra(KEY_STU_ID);*/
       studentId = "1";
        new FetchResultsAsyncTask().execute();


    }


    /**
     * Fetches the list of student from the server
     */
    private class FetchResultsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(StudentResultsOld.this);
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
                    resultList = new ArrayList<>();
                    results = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate student list
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        Integer exaId = result.getInt(KEY_EXA_ID);
                        String exaName = result.getString(KEY_EXA_NAME);
                        String subName = result.getString(KEY_SUB_NAME);
                        String marks = result.getString(KEY_MARKS);
                        HashMap<String, String> map = new HashMap<String, String>();
                        //  map.put(KEY_EXA_ID, exaId.toString());
                        if(exaId > examIdCount){
                            examIdCount = exaId;
                            map.put(KEY_EXA_NAME, exaName);

                        }
                        map.put(KEY_SUB_NAME, subName);
                        map.put(KEY_MARKS, marks);
                        map.put(KEY_GRADE, "B");
                        resultList.add(map);
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
                    populateResultsList();
                }
            });
        }

    }

    /**
     * Updating parsed JSON data into ListView
     * */
    private void populateResultsList() {
        ListAdapter examAdapter = new SimpleAdapter(StudentResultsOld.this, resultList,
                R.layout.exam_result_list_item, new String[]{KEY_EXA_NAME}, new int[]{R.id.txt_exa_name});

        ListAdapter marksAdapter = new SimpleAdapter(StudentResultsOld.this, resultList,
                R.layout.marks_result_list_item, new String[]{KEY_SUB_NAME, KEY_GRADE,
                KEY_MARKS}, new int[]{R.id.txt_sub_name, R.id.txt_grade, R.id.txt_marks});


        // updating listview
        examListView.setAdapter(examAdapter);
        marksListView.setAdapter(marksAdapter);

        //  display marks when an exam is clicked
        examListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                float deg = view.findViewById(R.id.img_btn_res1).getRotation() + 180F;
                view.findViewById(R.id.img_btn_res1).animate().rotation(deg).setInterpolator(
                        new AccelerateDecelerateInterpolator());

            }
         /*   @Override
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
            }*/
        });
    }


    @Override
    public void onClick(View v) {
     /*   switch (v.getId()){
            case R.id.btn_res1:
                if (layRes1.getVisibility() == View.VISIBLE){
                    layRes1.setAnimation(downtoup);
                    layRes1.setVisibility(View.GONE);
                    float deg = imgBtnRes1.getRotation() + 180F;
                    imgBtnRes1.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    //grade color A #00F878, B #B7FF3E, C#FBF400 ,D #FF9600 , F #FF0000
                }else{
                    layRes1.setAnimation(uptodown);
                    layRes1.setVisibility(View.VISIBLE);
                    float deg = (imgBtnRes1.getRotation() == 180F) ? 0F : 180F;
                    imgBtnRes1.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                }
                break;

            case R.id.btn_res2:
                if (layRes2.getVisibility() == View.VISIBLE){
                    layRes2.setAnimation(downtoup);
                    layRes2.setVisibility(View.GONE);
                    float deg = btnRes2.getRotation() + 180F;
                    btnRes2.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                    //   btnRes2.setCompoundDrawables(null,null,downArrow,null);
                }else{
                    layRes2.setAnimation(uptodown);
                    layRes2.setVisibility(View.VISIBLE);
                    float deg = (btnRes2.getRotation() == 180F) ? 0F : 180F;
                    btnRes2.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                    //  btnRes2.setCompoundDrawables(null,null,upArrow,null);
                }
                break;

            case R.id.btn_res3:
                if (layRes3.getVisibility() == View.VISIBLE){
                    layRes3.setAnimation(downtoup);
                    layRes3.setVisibility(View.GONE);
                    float deg = btnRes3.getRotation() + 180F;
                    btnRes3.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                    //  btnRes3.setCompoundDrawables(null,null,downArrow,null);
                }else{
                    layRes3.setAnimation(uptodown);
                    layRes3.setVisibility(View.VISIBLE);
                    float deg = (btnRes3.getRotation() == 180F) ? 0F : 180F;
                    btnRes3.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                    //  btnRes3.setCompoundDrawables(null,null,upArrow,null);
                }
                break;

            case R.id.btn_res4:
                if (layRes4.getVisibility() == View.VISIBLE){
                    layRes4.setAnimation(downtoup);
                    layRes4.setVisibility(View.GONE);
                    float deg = btnRes4.getRotation() + 180F;
                    btnRes4.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    //  btnRes4.setCompoundDrawables(null,null,downArrow,null);
                }else{
                    layRes4.setAnimation(uptodown);
                    layRes4.setVisibility(View.VISIBLE);
                    float deg = (btnRes4.getRotation() == 180F) ? 0F : 180F;
                    btnRes4.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                    // btnRes4.setCompoundDrawables(null,null,upArrow,null);
                }
                break;
        }*/
    }
}
