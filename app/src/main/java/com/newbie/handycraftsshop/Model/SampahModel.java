package com.newbie.handycraftsshop.Model;

import com.google.gson.annotations.SerializedName;

public class SampahModel {
    @SerializedName("userID")
    private String userID;
    @SerializedName("nama")
    private String nama;
    @SerializedName("harga")
    private int harga;
    @SerializedName("stock")
    private int stockbarang;
    @SerializedName("deskripsi")
    private String deskripsi;
    @SerializedName("image")
    private String image;
    @SerializedName("imagepublisher")
    private String imagepublisher;
    @SerializedName("usernamepublisher")
    private String usernamepublisher;

    public SampahModel(){
    }

    public SampahModel(String userID, String nama, int harga, int stockbarang, String deskripsi, String image, String imagepublisher, String usernamepublisher) {
        this.userID = userID;
        this.nama = nama;
        this.harga = harga;
        this.stockbarang = stockbarang;
        this.deskripsi = deskripsi;
        this.image = image;
        this.imagepublisher = imagepublisher;
        this.usernamepublisher = usernamepublisher;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStockbarang() {
        return stockbarang;
    }

    public void setStockbarang(int stockbarang) {
        this.stockbarang = stockbarang;
    }

    public String getImagepublisher() {
        return imagepublisher;
    }

    public void setImagepublisher(String imagepublisher) {
        this.imagepublisher = imagepublisher;
    }

    public String getUsernamepublisher() {
        return usernamepublisher;
    }

    public void setUsernamepublisher(String usernamepublisher) {
        this.usernamepublisher = usernamepublisher;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
