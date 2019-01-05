package com.example.davkimfray.shuta4;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;


public class StudentResults extends AppCompatActivity implements View.OnClickListener{

    Button btnRes1,btnRes2,btnRes3,btnRes4;
    LinearLayout layRes1,layRes2,layRes3,layRes4;
    Animation uptodown,downtoup;
    Drawable upArrow,downArrow;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_results);

        btnRes1 = findViewById(R.id.btn_res1);
        btnRes2 = findViewById(R.id.btn_res2);
        btnRes3 = findViewById(R.id.btn_res3);
        btnRes4 = findViewById(R.id.btn_res4);
        layRes1 = findViewById(R.id.lay_res1);
        layRes2 = findViewById(R.id.lay_res2);
        layRes3 = findViewById(R.id.lay_res3);
        layRes4 = findViewById(R.id.lay_res4);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        upArrow = getResources().getDrawable(android.R.drawable.arrow_up_float);
        downArrow = getResources().getDrawable(android.R.drawable.arrow_down_float);

        btnRes1.setOnClickListener(this);
        btnRes2.setOnClickListener(this);
        btnRes3.setOnClickListener(this);
        btnRes4.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_res1:
                if (layRes1.getVisibility() == View.VISIBLE){
                    layRes1.setAnimation(downtoup);
                    layRes1.setVisibility(View.GONE);
                    float deg = btnRes1.getRotation() + 180F;
                    btnRes1.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                }else{
                    layRes1.setAnimation(uptodown);
                    layRes1.setVisibility(View.VISIBLE);
                    float deg = (btnRes1.getRotation() == 180F) ? 0F : 180F;
                    btnRes1.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

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
        }
    }
}
