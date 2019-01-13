package com.example.davkimfray.shuta4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    private List<Results> studentResults;

    int examIdCount = 0;
    public Animation uptodown,downtoup;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView exa_name, sub_name, sub_marks, grade;
        public LinearLayout exaHead, exaData;


        public MyViewHolder(View view) {
            super(view);
            exaHead = view.findViewById(R.id.exam_head);
            exaData = view.findViewById(R.id.lay_res1);
            exa_name = view.findViewById(R.id.txt_exa_name);
            sub_name = view.findViewById(R.id.txt_sub_name);
            sub_marks = view.findViewById(R.id.txt_marks);
            grade = view.findViewById(R.id.txt_grade);

        }
    }

    public ResultAdapter(List<Results> studentResults) {
        this.studentResults = studentResults;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.exam_result_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Results results = studentResults.get(position);

        //header visibility control
        if(results.getExaId() > examIdCount){
            examIdCount = results.getExaId();
            holder.exaHead.setVisibility(View.VISIBLE);
        }else {
            holder.exaHead.setVisibility(View.GONE);
        }
        holder.exa_name.setText(results.getExaName());
        holder.sub_name.setText(results.getSubName());
        holder.sub_marks.setText(results.getSubMarks() + "%");

        if(results.getSubMarks() > 80 && results.getSubMarks() <= 100){
            holder.grade.setText("A");
        }else if (results.getSubMarks() > 60 && results.getSubMarks() <= 80){
            holder.grade.setText("B");
        }else if (results.getSubMarks() > 40 && results.getSubMarks() <= 60){
            holder.grade.setText("C");
        }else if (results.getSubMarks() > 20 && results.getSubMarks() <= 40){
            holder.grade.setText("D");
        }else if (results.getSubMarks() >= 0 && results.getSubMarks() <= 20){
            holder.grade.setText("E");
        }

        holder.exaHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float deg = v.findViewById(R.id.img_btn_res1).getRotation() + 180F;
                v.findViewById(R.id.img_btn_res1).animate().rotation(deg).setInterpolator(
                        new AccelerateDecelerateInterpolator());

                if(holder.exaData.getTag() == "try"){
                    holder.exaData.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentResults.size();
    }

}
