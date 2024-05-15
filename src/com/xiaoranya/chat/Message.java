package com.xiaoranya.chat;

import java.io.Serializable;

public class Message {

    private  String from;
    private  int type;
    private String info;

    public Message(String from, int type, String info) {
        this.from = from;
        this.type = type;
        this.info = info;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
