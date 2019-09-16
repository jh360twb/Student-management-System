package com.example.sqlitetest.Bean;

import org.litepal.crud.DataSupport;

public class Saved_Student extends DataSupport {
    String account;
    String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
