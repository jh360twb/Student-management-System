package com.example.sqlitetest.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sqlitetest.Bean.Saved_Student;
import com.example.sqlitetest.Bean.Student;
import com.example.sqlitetest.Httlpackage.MD5HTTL;
import com.example.sqlitetest.R;

import org.litepal.crud.DataSupport;

import java.util.List;

import static com.example.sqlitetest.Activity.Login.arrayList;
import static com.example.sqlitetest.Activity.Login.flag_right;
import static com.example.sqlitetest.Activity.Login.list;

public class Fix_password extends BaseActivity implements View.OnClickListener {
    private AppCompatEditText old;
    private AppCompatEditText new1;
    private AppCompatEditText new2;
    private ImageView back;
    private Button define;
    Student student;
    Saved_Student saved_student;
    String mess1;
    String mess;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_password);
        old = findViewById(R.id.old_pass);
        new1 = findViewById(R.id.new_pass);
        new2 = findViewById(R.id.new2_pass);
        back = findViewById(R.id.open_nav);
        back.setImageResource(R.drawable.back);
        define = findViewById(R.id.define);
        define.setOnClickListener(this);
        back.setOnClickListener(this);
        ID = getIntent().getStringExtra("student_id");
        List<Student> list = DataSupport.where("student_number = ?", ID).find(Student.class);
        Log.e("ID====",ID);
        if (list.size() != 0) {
            student = list.get(0);
            Log.e("ID:==",student.getStudent_number()+"--------");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.define:
                if (CheckOld()) {
                    if (CheckNew()) {
                        student.setID_password(MD5HTTL.md5(mess1));
                        //DataSupport.updateAll("acc")
                        student.updateAll("ID_password=?",MD5HTTL.md5(mess1));
                        student.save();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getAccount().equals(student.getStudent_number())) {
                                list.remove(i);
                                //Log.e("FIx-----",student.getStudent_number());
                                //Log.e("Fix-----------",list.get(i).getAccount());
                                DataSupport.deleteAll(Saved_Student.class,"account=?",student.getStudent_number());
                                List<Saved_Student> saved_studentList = DataSupport.findAll(Saved_Student.class);
                                //Log.e("save_student.size=",saved_studentList.size()+"");
                                //Log.e("list size--------",""+list.size());
                                //list.clear();
                            }
                        }
                        flag_right = 0;
                        Intent intent = new Intent(Fix_password.this, Login.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.open_nav:
                finish();
                break;
        }
    }

    private boolean CheckNew() {
        String mess = new1.getText().toString();
        mess1 = new2.getText().toString();
        if (mess.equals(mess1) && mess.length() >= 6) {
            return true;
        } else {
            if (mess.length() < 6) {
                Toast.makeText(this, "新密码应大于等于6位数", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "你填写的新密码两次不一致", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    private boolean CheckOld() {
        //学号
        //原来的密码
        mess = getIntent().getStringExtra("before_password");
        //你填写的旧密码
        String password = old.getText().toString();

        if (MD5HTTL.md5(password).equals(mess)) {
            return true;
        } else {
            Toast.makeText(this, "你填写的旧密码错误", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
