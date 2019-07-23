package com.example.sqlitetest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sqlitetest.ShowStudent;
import com.example.sqlitetest.R;
import com.example.sqlitetest.Table.Student;

import java.util.ArrayList;
import java.util.List;

public class find_Adapter extends RecyclerView.Adapter<find_Adapter.ViewHolder>{
    private List<Student> findStudent = new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_list_item,viewGroup,false);
        return new ViewHolder(view);
    }
    public find_Adapter(List<Student> findStudent,Context context){
        this.findStudent =findStudent;
        this.context = context;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Student student = findStudent.get(i);
        Glide.with(context).load(student.getImagePath()).into(viewHolder.student_picture);
        viewHolder.stusent_major.setText(student.getMajor());
        viewHolder.student_name.setText(student.getName());
        viewHolder.student_id.setText(student.getStudent_number());
        viewHolder.student_grade.setText(student.getGrade());
        viewHolder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"你点击了第"+i+"个按钮",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShowStudent.class);
                intent.putExtra("position1",i);
                intent.putExtra("path",student.getImagePath());
                context.startActivity(intent);
            }
            });
    }

    @Override
    public int getItemCount() {
        return findStudent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView student_name;
        TextView student_id;
        TextView student_grade;
        TextView stusent_major;
        ImageView student_picture;
        LinearLayout detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            student_grade = itemView.findViewById(R.id.student_grade);
            student_id = itemView.findViewById(R.id.student_id);
            student_name = itemView.findViewById(R.id.student_name);
            stusent_major = itemView.findViewById(R.id.student_major);
            student_picture = itemView.findViewById(R.id.student_image);
            detail = itemView.findViewById(R.id.detail);
        }
    }
}
