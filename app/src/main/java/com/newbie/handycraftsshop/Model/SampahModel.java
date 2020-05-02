package com.newbie.handycraftsshop.Model;

import com.google.gson.annotations.SerializedName;

public class SampahModel {
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

    public SampahModel(){
    }

    public SampahModel(String nama, int harga, int stockbarang, String deskripsi, String image) {
        this.nama = nama;
        this.harga = harga;
        this.stockbarang = stockbarang;
        this.deskripsi = deskripsi;
        this.image = image;
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
}
