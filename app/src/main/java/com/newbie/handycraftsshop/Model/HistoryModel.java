package com.newbie.handycraftsshop.Model;

import com.google.firebase.firestore.PropertyName;

public class HistoryModel {
    @PropertyName("banyak_barang")
    private int banyak_barang;
    @PropertyName("hargaTotal")
    private int hargaTotal;
    @PropertyName("harga_barang")
    private int harga_barang;
    @PropertyName("id_barang")
    private String id_barang;
    @PropertyName("imagesampah")
    private String imagesampah;
    @PropertyName("nama_barang")
    private String nama_barang;

    public HistoryModel(){

    }

    public HistoryModel(int banyak_barang, int hargaTotal, int harga_barang, String id_barang, String imagesampah, String nama_barang) {
        this.banyak_barang = banyak_barang;
        this.hargaTotal = hargaTotal;
        this.harga_barang = harga_barang;
        this.id_barang = id_barang;
        this.imagesampah = imagesampah;
        this.nama_barang = nama_barang;
    }

    public int getBanyak_barang() {
        return banyak_barang;
    }

    public void setBanyak_barang(int banyak_barang) {
        this.banyak_barang = banyak_barang;
    }

    public int getHargaTotal() {
        return hargaTotal;
    }

    public void setHargaTotal(int hargaTotal) {
        this.hargaTotal = hargaTotal;
    }

    public int getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(int harga_barang) {
        this.harga_barang = harga_barang;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getImagesampah() {
        return imagesampah;
    }

    public void setImagesampah(String imagesampah) {
        this.imagesampah = imagesampah;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }
}
