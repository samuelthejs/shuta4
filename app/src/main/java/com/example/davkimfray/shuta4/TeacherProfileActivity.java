package com.example.davkimfray.shuta4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_LONG;

public class TeacherProfileActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA= "data";
    private static final String KEY_TEA_ID = "stu_id";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_M_NAME = "m_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_REG_NO = "reg_no";
    private static final String KEY_TEA_IMAGE = "tea_image";
    private static final String BASE_URL = "https://davkimfray.000webhostapp.com/android/";
    private TextView txtView_teaName;
    private TextView txtView_RegNo;
    private TextView txtView_gender;
    private TextView txtView_claName;
    private TextView txtView_dob;
    // private TextView txtView_stuRegNo;
    private ProgressDialog pDialog;
    private String teacherId;
    private String teaName;
    private String gender;
    private String regNo;
    private String teaImage;
    private StorageReference storageReference;
    private CircleImageView profileImage;
    private ImageView profileImageBg;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        txtView_teaName = findViewById(R.id.txt_name);
        txtView_RegNo = findViewById(R.id.txt_reg_no);
        txtView_gender = findViewById(R.id.txt_gender);
        profileImage = findViewById(R.id.tea_profile_image);
        profileImageBg = findViewById(R.id.tea_bg_profile_img);

        final Intent intent = getIntent();
        teacherId = intent.getStringExtra(KEY_TEA_ID);

        storageReference = FirebaseStorage.getInstance().getReference();

        new FetchTeacherDetailsAsyncTask().execute();

    }

    class FetchTeacherDetailsAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(TeacherProfileActivity.this);
            pDialog.setMessage("Loading Details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_TEA_ID, teacherId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "get_teacher_details.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject teacher;
                if (success == 1) {
                    //Parse the JSON response
                    teacher = jsonObject.getJSONObject(KEY_DATA);
                    if(teacher.getString(KEY_M_NAME) == "null"){
                        teaName = teacher.getString(KEY_F_NAME) + " " +
                                teacher.getString(KEY_L_NAME);
                    }else{
                        teaName = teacher.getString(KEY_F_NAME) + " " +
                                teacher.getString(KEY_M_NAME) + " " +
                                teacher.getString(KEY_L_NAME);
                    }
                    gender = teacher.getString(KEY_GENDER);
                    regNo = teacher.getString(KEY_REG_NO);
                    teaImage = teacher.getString(KEY_TEA_IMAGE);

                    //fetch image url from firebase
                    storageReference.child("/"+teaImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Glide.with(TeacherProfileActivity.this).load(uri).into(profileImage);
                            Glide.with(TeacherProfileActivity.this).load(uri).into(profileImageBg);
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
                    txtView_teaName.setText(teaName);
                    txtView_RegNo.setText(regNo);
                    txtView_gender.setText(gender);


                }
            });
        }
    }

}
