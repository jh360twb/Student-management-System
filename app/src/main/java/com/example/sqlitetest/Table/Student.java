package com.example.sqlitetest.Table;

import org.litepal.crud.DataSupport;

public class Student extends DataSupport {
    String name;//名字
    String grade;//年级
    String student_number;//学号
    String major;//专业
    String imagePath;//头像路径

    public Student(){ }

    public Student(String name, String grade, String student_number, String major,String imagePath){
        this.name = name;
        this.grade = grade;
        this.student_number = student_number;
        this.major = major;
        this.imagePath = imagePath;
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
