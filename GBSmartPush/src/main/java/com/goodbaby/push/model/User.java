package com.goodbaby.push.model;

/**
 * Created by goodbaby on 17/2/8.
 */

public class User {
    private String ID;
    private String Password;
    private String EmpID;
    private boolean rem_psw;
    private boolean autoLogin;

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmpID() {
        return EmpID;
    }

    public boolean isRem_psw() {
        return rem_psw;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public void setRem_psw(boolean rem_psw) {
        this.rem_psw = rem_psw;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }
}
