package com.android.oedermealapp.bean;

import java.util.ArrayList;

/**
 * 2020/4/16
 */
public class UserList {
    private ArrayList<UserBean> list;

    public UserList(ArrayList<UserBean> list) {
        this.list = list;
    }

    public ArrayList<UserBean> getList() {
        return list;
    }

    public void setList(ArrayList<UserBean> list) {
        this.list = list;
    }
}
