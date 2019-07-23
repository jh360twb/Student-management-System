package com.example.sqlitetest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlitetest.Adapter.list_Adapter;
import com.example.sqlitetest.Table.Student;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

//主要知识点:litepal的增删查改,recyclerview的数据传递;
//一些小知识点:侧滑菜单,menu的使用,spinner的使用,调用相册等;
//花费了2天多去做这个小项目,UI完善,bug改完时间2019年7月23日16:31:46
//加上布局一共2000行左右

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static List<Student> students = new ArrayList<>();
    public static list_Adapter list_adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView open;
    private TextView title;
    private RecyclerView recyclerView;
    Context mcontext = MainActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav);
        open = findViewById(R.id.open_nav);
        recyclerView = findViewById(R.id.student_list);
        title = findViewById(R.id.title);
        open.setOnClickListener(this);
        iniview();
        ininav();
        iniRecyclerview();
        initdata();
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    @Override
    public Object getLastCustomNonConfigurationInstance() {
        return super.getLastCustomNonConfigurationInstance();
    }

    private void initdata() {
        students.clear();
        List<Student> students1 = DataSupport.findAll(Student.class);
        for (Student student : students1) {
            students.add(student);
        }
        list_adapter.notifyDataSetChanged();
        //Toast.makeText(mcontext, ""+students.size(), Toast.LENGTH_SHORT).show();
    }

    private void iniRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        list_adapter = new list_Adapter(students, MainActivity.this);
        recyclerView.setAdapter(list_adapter);
    }

    private void ininav() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.add:
                        //Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mcontext, AddStudent.class);
                        startActivity(intent);
                        LitePal.getDatabase();
                        drawerLayout.closeDrawer(navigationView);
                        break;
                    case R.id.find:
                        Intent intent1 = new Intent(mcontext, FindStudent.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(navigationView);
                        break;
                    case R.id.explain:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("使用说明");
                        builder.setMessage("本软件提供学生的增删查改功能,其中删除和修改功能在查询学生的详细界面");
                        builder.setCancelable(true);
                        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        //drawerLayout.closeDrawer(navigationView);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void iniview() {
        open.setImageResource(R.drawable.more);
        title.setText("学生列表");
        View view = navigationView.getHeaderView(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_nav:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                break;

        }
    }
    @Override
    public void onBackPressed() {
        exit();
    }
    private long presstime = 0L;
    private void exit() {
        if (System.currentTimeMillis()-presstime > 2000){
            Toast.makeText(MainActivity.this,"再按一次退出程序(#^.^#)", Toast.LENGTH_SHORT).show();
            presstime = System.currentTimeMillis();
            Log.e("mainactivity",""+System.currentTimeMillis());
        }else{
            Intent intent = new Intent("offline");
            sendBroadcast(intent);
        }
    }


}