package com.newbie.handycraftsshop.Notifications;

/**
 * Created by wahyu_septiadi on 26, April 2020.
 * Visit My GitHub --> https://github.com/WahyuSeptiadi
 */

public class Sender {
    public Data data;
    public String to;

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public Sender() {
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}