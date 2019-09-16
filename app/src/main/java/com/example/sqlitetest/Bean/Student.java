package com.example.sqlitetest.Bean;

import org.litepal.crud.DataSupport;

public class Student extends DataSupport {
    String name;//名字
    String grade;//年级
    String student_number;//学号
    String major;//专业
    String imagePath;//头像路径
    String ID_password = "123456";//密码
    public Student(){ }

    public Student(String name, String grade, String student_number, String major,String imagePath,String IDpassword){
        this.name = name;
        this.grade = grade;
        this.student_number = student_number;
        this.major = major;
        this.imagePath = imagePath;
        this.ID_password = IDpassword;
    }

    public String getID_password() {
        return ID_password;
    }

    public void setID_password(String ID_password) {
        this.ID_password = ID_password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGrade() {
        return grade;
    }

    public String getName() {
        return name;
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public String getMajor() {
        return major;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
