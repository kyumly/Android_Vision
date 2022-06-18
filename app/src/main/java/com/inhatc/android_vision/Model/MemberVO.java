package com.inhatc.android_vision.Model;

public class MemberVO {
    private String password;
    private String Email;
    private String userId;
    private String name;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public MemberVO(String password, String email, String name){
        this.password = password;
        this.Email = email;
        this.name = name;
    }


}