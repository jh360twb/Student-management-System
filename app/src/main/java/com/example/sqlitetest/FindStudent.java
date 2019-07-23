package com.example.sqlitetest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sqlitetest.Adapter.find_Adapter;
import com.example.sqlitetest.Table.Student;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class FindStudent extends BaseActivity implements View.OnClickListener {
    public static List<Student> findStudentList = new ArrayList<>();
    private EditText fill_id;
    private ImageView find_id;
    private ImageView back_;
    private RecyclerView recyclerView;
    public static find_Adapter find_adapter;
    Context context = FindStudent.this;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_student);
        fill_id = findViewById(R.id.fill_id);
        find_id = findViewById(R.id.find_id);
        recyclerView = findViewById(R.id.find_list);
        back_ = findViewById(R.id.back_);
        iniRecyclerView();
        find_id.setOnClickListener(this);
        back_.setOnClickListener(this);
    }
    private void init() {
        ////litepal的查询操作

        String mess = fill_id.getText().toString();
        List<Student> findStudentList1 = DataSupport.where("student_number like ?","%"+mess+"%").find(Student.class);
        if (findStudentList1.size() == 0){
            Toast.makeText(context, "没有找到类似学号的学生", Toast.LENGTH_SHORT).show();
        }else {
            for (Student student : findStudentList1) {
                findStudentList.add(student);
            }
        }
        find_adapter.notifyDataSetChanged();
        //Toast.makeText(context, ""+findStudentList.size(), Toast.LENGTH_SHORT).show();
    }

    private void iniRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        find_adapter = new find_Adapter(findStudentList, context);
        recyclerView.setAdapter(find_adapter);
        findStudentList.clear();
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(context,MainActivity.class);
        startActivity(intent1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_id:
                //点击查询

                init();
                break;
            case R.id.back_:
                Intent intent1 = new Intent(context,MainActivity.class);
                startActivity(intent1);
            default:
                break;
        }
    }
}
