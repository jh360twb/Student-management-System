package com.example.sqlitetest.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlitetest.Bean.Saved_Student;
import com.example.sqlitetest.Httlpackage.MD5HTTL;
import com.example.sqlitetest.R;
import com.example.sqlitetest.Bean.Student;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Login extends BaseActivity implements View.OnClickListener {
    public static ArrayList arrayList = new ArrayList();
    public static List<Saved_Student> list = new ArrayList<>();
    String TAG = "lOGIN";
    private CheckBox login_stud;
    private CheckBox login_mana;
    private CheckBox remember_password;
    private AutoCompleteTextView account;
    private AutoCompleteTextView password;
    private TextView register;
    String mess = "";
    String mess1;
    public static int flag1_log = 0;
    public static int flag2_log = 0;
    public static int flag_right = 0;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.getDatabase();
        setContentView(R.layout.activity_login);
        login_stud = findViewById(R.id.login_student);
        login_mana = findViewById(R.id.login_manager);
        remember_password = findViewById(R.id.remember_password);
        account = (AutoCompleteTextView) findViewById(R.id.account);
        password = (AutoCompleteTextView) findViewById(R.id.password);
        register = findViewById(R.id.register);
        Button login = (Button) findViewById(R.id.log_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        init();
        //complete();
    }


    //自动补全
    private void init() {
        //把已有的保存了的去提示补全
        arrayList.clear();
        list.clear();
        List<Saved_Student> list1 = DataSupport.findAll(Saved_Student.class);
        for (Saved_Student saved_student : list1) {
            list.add(saved_student);
        }

        for (Saved_Student saved_student : list) {
            if (!arrayList.contains(saved_student.getAccount()))
                arrayList.add(saved_student.getAccount());
            //Log.e(TAG,"--------------"+test[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        //输入3位自动补全
        account.setThreshold(3);
        account.setAdapter(adapter);
        account.addTextChangedListener(textWatcher);

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            mess = account.getText().toString();
            complete();
        }
    };


    private void complete() {
        //List<Saved_Student> saved_studentList = DataSupport.where("account = ?", mess).find(Saved_Student.class);
        //Log.e(TAG, "" + "++=+=+++" + saved_studentList.size());
        for (int i = 0; i < list.size(); i++) {
            //Log.e(TAG, list.get(i).getAccount());
            if (list.get(i).getAccount().equals(mess)) {
                password.setText(list.get(i).getPassword());
                flag1_log = 1;
                flag_right = 1;
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_in: {
                boolean flag = checkbox_order();
                if (flag) {
                    boolean flag1 = validate();
                    if (flag1) {
                        if (login() && flag2_log == 1) {

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        } else if ((flag_right == 1 || login()) && flag1_log == 1) {
                            //记住密码的话就保存在一个数据库中
                            if (remember_password.isChecked()) {
                                Saved_Student saved_student = new Saved_Student();
                                saved_student.setAccount(student.getStudent_number());
                                saved_student.setPassword(student.getID_password());
                                saved_student.save();
                                list.add(saved_student);
                            }
                            Intent intent = new Intent(Login.this, Show_Student.class);
                            intent.putExtra("number", student.getStudent_number());
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "请检查你的用户名和密码是否输入错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {

                }
            }
            break;
            case R.id.register:
                Intent intent = new Intent(Login.this, AddStudent.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void show(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private boolean login() {
        String username = account.getText().toString();
        String passwords = password.getText().toString();
        List<Student> studentList = DataSupport.where("student_number = ?", username).find(Student.class);
        //是否可以搜索到学生
        if (studentList.size() != 0) {
            student = studentList.get(0);
        } else if (login_stud.isChecked() && studentList.size() == 0) {
            Toast.makeText(this, "请检查你的学号和密码是否错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        //管理员的密码是不变的,只有管理员自己知道
        if (login_mana.isChecked() && username.equals("admin") && passwords.equals("123456")) {
            flag2_log = 1;
            return true;
        } else if (login_stud.isChecked() && username.equals(student.getStudent_number()) && MD5HTTL.md5(passwords).equals(student.getID_password())) {
            flag1_log = 1;
            return true;
        } else if (flag_right != 1) {
            Toast.makeText(this, "请选中一种身份进行登录", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    private boolean validate() {
        String username = account.getText().toString();
        if (username.equals("")) {
            show("请输入用户名");
            return false;
        }
        String passwords = password.getText().toString();
        if (passwords.equals("")) {
            show("请输入密码");
            return false;
        }
        return true;
    }

    private boolean checkbox_order() {
        if (login_stud.isChecked() && login_mana.isChecked()) {
            Toast.makeText(this, "只能选中一种身份进行登录", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!login_stud.isChecked() && !login_mana.isChecked()) {
            Toast.makeText(this, "请选中一种身份进行登录", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        flag2_log = flag1_log = 0;
        Intent intent = new Intent("offline");
        sendBroadcast(intent);
    }
}

