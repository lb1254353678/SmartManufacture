package com.goodbaby.push.model;

import java.util.List;

/**
 * Created by goodbaby on 17/2/10.
 */

public class ResponsePushMessageModel {
    private String state;
    private List<PushMessageModel> list;

    public String getState() {
        return state;
    }

    public List<PushMessageModel> getList() {
        return list;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setList(List<PushMessageModel> list) {
        this.list = list;
    }
}
