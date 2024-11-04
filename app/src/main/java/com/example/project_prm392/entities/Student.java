package com.example.project_prm392.entities;

public class Student {
    private String student_roll_number;
    private String student_name;
    private String student_email;
    private String student_phone;
    private int student_amount;
    private String student_password;
    private String created_date;
    private String modified_date;
    private String student_PIN;
    private boolean status;

    public Student() {
    }

    public String getStudent_PIN() {
        return student_PIN;
    }


    public String getStudent_phone() {
        return student_phone;
    }


    public String getStudent_roll_number() {
        return student_roll_number;
    }

    public String getStudent_name() {
        return student_name;
    }


    public String getStudent_email() {
        return student_email;
    }


    public String getStudent_password() {
        return student_password;
    }


    public boolean getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "Student{" +
                "student_roll_number='" + student_roll_number + '\'' +
                ", student_name='" + student_name + '\'' +
                ", student_email='" + student_email + '\'' +
                ", student_phone='" + student_phone + '\'' +
                ", student_amount=" + student_amount +
                ", student_password='" + student_password + '\'' +
                ", created_date='" + created_date + '\'' +
                ", modified_date='" + modified_date + '\'' +
                ", student_PIN='" + student_PIN + '\'' +
                ", status=" + status +
                '}';
    }
}
