package com.android.oedermealapp.bean;

import java.util.List;

/**
 * 2020/4/13
 */
public class FormListBean {
    private List<FormBean> list;

    public FormListBean(List<FormBean> list) {
        this.list = list;
    }

    public List<FormBean> getList() {
        return list;
    }

    public void setList(List<FormBean> list) {
        this.list = list;
    }
}
