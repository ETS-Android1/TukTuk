package com.ndkapp.www.tuktuk;

public class users {
    String name;
    String mobilenumber;
    String email;

    users() {

    }

    public users(String n, String mn, String em) {
        name = n;
        mobilenumber = mn;
        email = em;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getEmail() {
        return email;
    }
}
