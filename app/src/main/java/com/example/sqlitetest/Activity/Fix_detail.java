package com.example.sqlitetest.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sqlitetest.R;
import com.example.sqlitetest.Bean.Student;
import com.example.sqlitetest.Httlpackage.getPhotoFromPhotoAlbum;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.sqlitetest.Activity.FindStudent.findStudentList;
import static com.example.sqlitetest.Activity.Login.flag2_log;
import static com.example.sqlitetest.Activity.MainActivity.list_adapter;
import static com.example.sqlitetest.Activity.MainActivity.students;

public class Fix_detail extends BaseActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks {
    private static final String[] spinner_arr1 = {"大一", "大二", "大三", "大四"};
    private static final String[] spinner_arr2 = {"计算机科学与技术", "网络工程", "软件工程", "信息安全"};
    private static final String TAG = "123========";
    private CircleImageView fix_image;
    private EditText fill_name;
    private EditText fill_id;
    private Spinner grade_spinner;
    private Spinner major_spinner;
    private Button save_mess;
    private ImageView back;
    private TextView title;
    ArrayAdapter adapter;
    ArrayAdapter adapter1;
    Context context = Fix_detail.this;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String cameraSavePath;//拍照照片路径
    private String path;//原来传过来的照片路径
    Student student1;
    Student student;
    private Uri uri;//照片uri
    String mess;
    int selection;//spinner默认值
    int selection1;
    String photoPath = path;//修改之后的photopath,先给他一个初值就是原来的那个path
    String student_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_detail);
        Intent intent = getIntent();

        if(flag2_log==1) {
            int position1 = intent.getIntExtra("position2", -1);
            path = getIntent().getStringExtra("path");
            if (position1 != -1) {
                student1 = findStudentList.get(position1);
            } else {
                position1 = intent.getIntExtra("position3", -1);
                student1 = students.get(position1);
            }
        }
        else {

            student_num = getIntent().getStringExtra("number_2");
            List<Student> studentList = DataSupport.where("student_number = ?", student_num).find(Student.class);
            if (studentList.size() != 0)
                student1 = studentList.get(0);
            path = student1.getImagePath();
        }
        fix_image = findViewById(R.id.circle_image);
        fill_id = findViewById(R.id.fill_id);
        fill_name = findViewById(R.id.fill_name);
        grade_spinner = findViewById(R.id.grade_spinner);
        major_spinner = findViewById(R.id.major_spinner);
        save_mess = findViewById(R.id.save_message);
        back = findViewById(R.id.open_nav);
        title = findViewById(R.id.title);
        back.setOnClickListener(this);
        init();
        iniAdapter();
        iniSpinner();
        iniView();
        save_mess.setOnClickListener(this);
        fix_image.setOnClickListener(this);
    }

    private void init() {
        fill_id.setText(student1.getStudent_number());
        mess = fill_id.getText().toString();//传过来的id信息
        fill_name.setText(student1.getName());
        Glide.with(context).load(path).into(fix_image);
        for (int i=0;i<4;i++){
            if (student1.getGrade().equals(spinner_arr1[i])){
                selection = i;
                Log.e(TAG, "----------------"+i);
            }
        }
        for (int i=0;i<4;i++){
            if (student1.getMajor().equals(spinner_arr2[i])){
                selection1 = i;
                Log.e(TAG, "----------------"+i);
            }
        }
    }

    private void iniView() {
        back.setImageResource(R.drawable.back);
        title.setText("学生信息");
    }

    private void Save() {
        int flag = 1;
        int flag1 = 0;
        String mess1 = fill_id.getText().toString();
        if (mess1.equals(mess)){
            flag1 = 1;//原来的id没有变
        }
        if (fill_id.getText().toString().length() != 12) {
            Toast("请检查你的学号是否填写错误");
            flag = 0;
        }
        List<Student> findStudentList1 = DataSupport.where("student_number = ?",fill_id.getText().toString()).find(Student.class);
        if (flag1 == 0 && findStudentList1.size()  != 0 ){
            Toast("该学号的学生已存在");
            flag = 0;
        }
        student1.setStudent_number(fill_id.getText().toString());

        if (fill_name.getText().toString().length() == 0) {
            flag = 0;
            Toast("名字不能为空");
        } else {
            student1.setName(fill_name.getText().toString());
        }
        student1.setImagePath(photoPath);
        if (flag == 1) {
            student1.save();
            //Toast.makeText(this, ""+student.isSaved(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "" + students.size(), Toast.LENGTH_SHORT).show();
            if (flag2_log==1) {
                list_adapter.notifyDataSetChanged();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(context,Show_Student.class);
                intent.putExtra("fixnumber",student1.getStudent_number());
                startActivity(intent);
            }

        }
    }

    private void iniSpinner() {
        grade_spinner.setAdapter(adapter);
        major_spinner.setAdapter(adapter1);
        grade_spinner.setSelection(selection);
        major_spinner.setSelection(selection1);
        grade_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Toast("选择的是"+spinner_arr1[arg2]);
                arg0.setVisibility(View.VISIBLE);
                student1.setGrade(spinner_arr1[arg2]);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                Toast("没有选择");
            }
        });
        major_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //dispToast("选择的是"+spinner_arr2[position]);
                parent.setVisibility(View.VISIBLE);
                student1.setMajor(spinner_arr2[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast("没有选择");
            }
        });
    }

    public void Toast(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    private void iniAdapter() {
        adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, spinner_arr1);
        adapter1 = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, spinner_arr2);
        ((ArrayAdapter) adapter).setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ((ArrayAdapter) adapter1).setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_message:
                Save();
                //Toast.makeText(this, "" + students.size(), Toast.LENGTH_SHORT).show();

                break;
            case R.id.open_nav:
                finish();
                break;
            case R.id.circle_image:
                getPermission();
                goPhotoAlbum();
            default:
        }
    }
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册权限", 1, permissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = uri.getEncodedPath();
            }
            Log.d("拍照返回图片路径:", photoPath);
            Glide.with(Fix_detail.this).load(photoPath).into(fix_image);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Glide.with(Fix_detail.this).load(photoPath).into(fix_image);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
