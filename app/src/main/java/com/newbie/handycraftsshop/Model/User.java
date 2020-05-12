package com.newbie.handycraftsshop.Model;

public class User {

    private String id;
    private String username;
    private String imageUrl;
    private String saldo;

    public User(String id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public User(String id, String username, String imageUrl, String saldo) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.saldo = saldo;
    }

    public User() {

    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
