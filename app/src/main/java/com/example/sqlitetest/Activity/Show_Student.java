package com.example.sqlitetest.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sqlitetest.R;
import com.example.sqlitetest.Bean.Student;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sqlitetest.Activity.Login.flag1_log;
import static com.example.sqlitetest.Activity.Login.flag2_log;
import static com.example.sqlitetest.Activity.Login.flag_right;

public class Show_Student extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "123";
    private TextView get_name;
    private TextView get_id;
    private TextView get_grade;
    private CircleImageView get_image;
    private TextView get_major;
    private Button fix_mess;
    private Button fix_password;
    private Button quit_log;
    private ImageView back;
    private TextView title;
    String student_num = "";
    Student student;
    Context context = Show_Student.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__student);
        get_grade = findViewById(R.id.get_grade);
        get_id = findViewById(R.id.get_id);
        get_image = findViewById(R.id.get_image);
        get_major = findViewById(R.id.get_major);
        get_name = findViewById(R.id.get_name);
        quit_log = findViewById(R.id.quit_log);
        fix_mess = findViewById(R.id.fix_mess);
        fix_password = findViewById(R.id.fix_password);
        back = findViewById(R.id.open_nav);
        title = findViewById(R.id.title);
        fix_mess.setOnClickListener(this);
        quit_log.setOnClickListener(this);
        fix_password.setOnClickListener(this);
        init();
    }

    @SuppressLint("CheckResult")
    private void init() {
        student_num = getIntent().getStringExtra("fixnumber");
        Log.e(TAG, "----------" + student_num);
        if (student_num == null) {
            student_num = getIntent().getStringExtra("number");
        }
        Log.e(TAG, "-------" + student_num);
        List<Student> studentList = DataSupport.where("student_number = ?", student_num).find(Student.class);
        student = studentList.get(0);

        get_grade.setText(student.getGrade());
        get_id.setText(student.getStudent_number());
        Log.e(TAG, "--------" + student.getImagePath());
        Glide.with(context).load(student.getImagePath()).into(get_image);
        get_major.setText(student.getMajor());
        get_name.setText(student.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fix_mess:
                Intent intent = new Intent(context, Fix_detail.class);
                intent.putExtra("number_2", student.getStudent_number());
                startActivity(intent);
                break;
            case R.id.fix_password:
                Intent intent2 = new Intent(context,Fix_password.class);
                intent2.putExtra("student_id",student.getStudent_number());
                Log.e("student_id = ",student.getStudent_number());
                intent2.putExtra("before_password",student.getID_password());
                startActivity(intent2);
                break;
            case R.id.quit_log:
                Intent intent1 = new Intent(context, Login.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private long presstime = 0L;

    private void exit() {
        if (System.currentTimeMillis() - presstime > 2000) {
            Toast.makeText(context, "再按一次退出程序(#^.^#)", Toast.LENGTH_SHORT).show();
            presstime = System.currentTimeMillis();
            Log.e("mainactivity", "" + System.currentTimeMillis());
        } else {
            flag2_log=flag1_log=flag_right=0;
            Intent intent = new Intent("offline");
            sendBroadcast(intent);
        }
    }
}
