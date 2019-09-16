package com.example.sqlitetest.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sqlitetest.Bean.Saved_Student;
import com.example.sqlitetest.R;
import com.example.sqlitetest.Bean.Student;

import org.litepal.crud.DataSupport;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sqlitetest.Activity.FindStudent.findStudentList;
import static com.example.sqlitetest.Activity.FindStudent.find_adapter;
import static com.example.sqlitetest.Activity.MainActivity.list_adapter;
import static com.example.sqlitetest.Activity.MainActivity.students;

public class ShowStudent extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "123";
    private TextView get_name;
    private TextView get_id;
    private TextView get_grade;
    private CircleImageView get_image;
    private TextView get_major;
    private Button fix_mess;
    private Button delete_student;
    private ImageView back;
    private TextView title;
    int position;
    int position1;
    Student student;
    Context context = ShowStudent.this;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);
        get_grade = findViewById(R.id.get_grade);
        get_id = findViewById(R.id.get_id);
        get_image = findViewById(R.id.get_image);
        get_major = findViewById(R.id.get_major);
        get_name = findViewById(R.id.get_name);
        fix_mess = findViewById(R.id.fix_mess);
        delete_student = findViewById(R.id.delete_student);
        back = findViewById(R.id.open_nav);
        title = findViewById(R.id.title);
        fix_mess.setOnClickListener(this);
        back.setOnClickListener(this);
        delete_student.setOnClickListener(this);
        initView();
        init();
    }

    private void initView() {
        back.setImageResource(R.drawable.back);
        title.setText("学生详情");
    }

    private void init() {
        //获取从find_Adapter 和 list_Adapter发来的数据
        position = getIntent().getIntExtra("position", -1);
        path = getIntent().getStringExtra("path");
        if (position == -1) {
            Log.e(TAG, "init: ");
        } else {
            student = students.get(position);
            get_grade.setText(student.getGrade());
            get_id.setText(student.getStudent_number());
            get_name.setText(student.getName());
            get_major.setText(student.getMajor());
            Glide.with(context).load(path).into(get_image);
        }
        position1 = getIntent().getIntExtra("position1", -1);
        if (position1 == -1) {
            Log.e(TAG, "init: ");
        } else {
            student = findStudentList.get(position1);
            get_grade.setText(student.getGrade());
            get_id.setText(student.getStudent_number());
            get_name.setText(student.getName());
            get_major.setText(student.getMajor());
            Glide.with(context).load(path).into(get_image);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(context,MainActivity.class);
        startActivity(intent1);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_nav:
                Intent intent1 = new Intent(context,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.fix_mess:
                if (position1 >= 0) {
                    Intent intent = new Intent(ShowStudent.this, Fix_detail.class);
                    intent.putExtra("position2", position1);
                    intent.putExtra("path",path);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ShowStudent.this, Fix_detail.class);
                    intent.putExtra("position3", position);
                    intent.putExtra("path",path);
                    startActivity(intent);
                }
                //Log.e(TAG, "------------" + position1);
                break;
            case R.id.delete_student:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setMessage("确定要删除该学生吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        student.delete();
                        DataSupport.deleteAll(Saved_Student.class,"account=?",student.getStudent_number());
                        if (position == -1){
                            find_adapter.notifyDataSetChanged();
                            Intent intent = new Intent(context,MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            list_adapter.notifyDataSetChanged();
                            Intent intent = new Intent(context,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
    }
}
//1320行