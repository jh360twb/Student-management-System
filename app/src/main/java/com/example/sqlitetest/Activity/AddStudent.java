package com.example.sqlitetest.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import com.example.sqlitetest.Httlpackage.MD5HTTL;
import com.example.sqlitetest.R;
import com.example.sqlitetest.Bean.Student;
import com.example.sqlitetest.Httlpackage.getPhotoFromPhotoAlbum;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.sqlitetest.Activity.Login.flag1_log;
import static com.example.sqlitetest.Activity.MainActivity.list_adapter;
import static com.example.sqlitetest.Activity.MainActivity.students;

public class AddStudent extends BaseActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks {
    private static final String[] spinner_arr1 = {"大一", "大二", "大三", "大四"};
    private static final String[] spinner_arr2 = {"计算机科学与技术", "网络工程", "软件工程", "信息安全"};
    private static final String TAG = "123---" ;
    private CircleImageView circleImageView;
    private EditText fill_name;
    private EditText fill_id;
    private EditText IDpassword;
    private ImageView back;
    private TextView title;
    private Button save_mess;
    Student student = new Student();
    ArrayAdapter adapter;
    ArrayAdapter adapter1;
    Context context = AddStudent.this;
    private Spinner grade_spinner;
    private Spinner major_spinner;
    private String mess;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String photoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        circleImageView = findViewById(R.id.circle_image);
        fill_name = findViewById(R.id.fill_name);
        fill_id = findViewById(R.id.fill_id);
        IDpassword = findViewById(R.id.IDpassword);
        grade_spinner = findViewById(R.id.grade_spinner);
        major_spinner = findViewById(R.id.major_spinner);
        save_mess = findViewById(R.id.save_message);
        back = findViewById(R.id.open_nav);
        title = findViewById(R.id.title);
        back.setOnClickListener(this);
        iniAdapter();
        iniSpinner();
        save_mess.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        iniView();
//      Save();
    }

    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }


    private void iniView() {
        back.setImageResource(R.drawable.back);
        title.setText("学生信息");
    }

    private void Save() {
        int flag = 1;

        if (fill_id.getText().toString().length() != 12) {
            Toast("学号位数规定为12位");
            flag = 0;
        }
        //不能和已存在的一致
        List<Student> findStudentList1 = DataSupport.where("student_number=?",fill_id.getText().toString()).find(Student.class);
        if (findStudentList1.size() != 0){
            Toast("该学号的学生已存在");
            flag = 0;
        }
        student.setStudent_number(fill_id.getText().toString());
        if (fill_name.getText().toString().length() == 0) {
            flag = 0;
            Toast("名字不能为空");
        } else {
            student.setName(fill_name.getText().toString());
        }if (photoPath == null){
            Toast("请选择你的头像");
            flag = 0;
        }else {
            student.setImagePath(photoPath);
        }
        if (IDpassword.getText().toString().length()<6){
            Toast("密码应不小于六位");
            flag =0;
        }else{
            //密码通过MD5加密存入数据库
            student.setID_password(MD5HTTL.md5(IDpassword.getText().toString()));
            Log.e(TAG,student.getID_password());
        }
        if (flag == 1) {
            student.save();
            //Log.e(TAG, ""+student.getImagePath());
            // Toast.makeText(this, ""+student.isSaved(), Toast.LENGTH_SHORT).show();
            students.add(student);
            //list_adapter.notifyDataSetChanged();
            //注册完毕之后跳转
            Intent intent = new Intent(context,Show_Student.class);
            intent.putExtra("number",student.getStudent_number());
            startActivity(intent);
        }
    }

    private void iniSpinner() {
        grade_spinner.setAdapter(adapter);
        major_spinner.setAdapter(adapter1);
        grade_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //Toast("选择的是"+spinner_arr1[arg2]);
                arg0.setVisibility(View.VISIBLE);
                student.setGrade(spinner_arr1[arg2]);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                Toast("没有选择");
            }
        });
        major_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast("选择的是"+spinner_arr2[position]);
                parent.setVisibility(View.VISIBLE);
                student.setMajor(spinner_arr2[position]);
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
                //Toast.makeText(this, ""+students.size(), Toast.LENGTH_SHORT).show();
                //finish();
                break;
            case R.id.open_nav:
                Intent intent1 = new Intent(context,Login.class);
                startActivity(intent1);
                break;
                case R.id.circle_image:
                    //调用相册
                    getPermission();
                    goPhotoAlbum();
                    break;
            default:
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(context,Login.class);
        startActivity(intent1);
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
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Glide.with(AddStudent.this).load(photoPath).into(circleImageView);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
