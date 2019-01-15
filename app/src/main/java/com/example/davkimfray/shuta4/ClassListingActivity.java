
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

public class ClassListingActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_CLA_NAME = "cla_name";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_M_NAME = "m_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private ArrayList<HashMap<String, String>> classList;
    private ListView classListView;
    private ProgressDialog pDialog;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private SimpleAdapter classListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_listing);
        classListView = findViewById(R.id.classList);

     /*   FloatingActionButton studentReg = findViewById(R.id.float_student_reg);
        studentReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regStudent = new Intent(getApplicationContext(), StudentRegistration.class);
                startActivity(regStudent);
            }
        });*/
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
            pDialog = new ProgressDialog(ClassListingActivity.this);
            pDialog.setMessage("Loading students. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "get_classes.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray classes;
                if (success == 1) {
                    classList = new ArrayList<>();
                    classes = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate student list
                    for (int i = 0; i < classes.length(); i++) {
                        JSONObject darasa = classes.getJSONObject(i);
                        String claName = darasa.getString(KEY_CLA_NAME);
                        String fName = darasa.getString(KEY_F_NAME) +" ";
                        String mName;
                        if(darasa.getString(KEY_M_NAME) == "null"){
                             mName = "";
                        }else {
                             mName = darasa.getString(KEY_M_NAME) + " ";
                        }

                        String lName = darasa.getString(KEY_L_NAME);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_CLA_NAME, claName);
                        map.put(KEY_F_NAME, fName);
                        map.put(KEY_M_NAME, mName);
                        map.put(KEY_L_NAME, lName);
                        classList.add(map);
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
        classListAdapter = new SimpleAdapter(ClassListingActivity.this, classList,
                R.layout.class_list_item, new String[]{KEY_CLA_NAME, KEY_F_NAME, KEY_M_NAME, KEY_L_NAME },
                 new int[]{R.id.txt_class_name, R.id.txt_tea_fname, R.id.txt_tea_mname, R.id.txt_tea_lname });
        // updating listview
        classListView.setAdapter(classListAdapter);
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
                classListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}