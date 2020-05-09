package com.newbie.handycraftsshop.Model;

import com.google.gson.annotations.SerializedName;

public class BuyModel {
    @SerializedName("id_barang")
    private String id_barang;
    @SerializedName("banyak_barang")
    private int banyak_barang;
    @SerializedName("hargaTotal")
    private int hargaTotal;
    @SerializedName("harga_barang")
    private int harga_barang;

    public BuyModel(){
    }

    public BuyModel(String id_barang, int banyak_barang, int hargaTotal, int harga_barang) {
        this.id_barang = id_barang;
        this.banyak_barang = banyak_barang;
        this.hargaTotal = hargaTotal;
        this.harga_barang = harga_barang;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
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
}
