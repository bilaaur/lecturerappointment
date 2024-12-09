package com.example.project;

public class User {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String studentId;
    private String phoneNumber;

    // Empty constructor for Firebase
    public User() {}

    // Full constructor
    public User(String userId, String name, String email, String password, String studentId, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
