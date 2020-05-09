package com.newbie.handycraftsshop.Model;

public class HistoryModel {
    private String idBarang;
    private int banyakBarang, hargaBarang, totalHarga;

    public HistoryModel (String idBarang, int banyakBarang, int hargaBarang, int totalHarga){
        this.idBarang = idBarang;
        this.banyakBarang = banyakBarang;
        this.hargaBarang = hargaBarang;
        this.totalHarga = totalHarga;
    }
    public HistoryModel (){

    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public int getBanyakBarang() {
        return banyakBarang;
    }

    public void setBanyakBarang(int banyakBarang) {
        this.banyakBarang = banyakBarang;
    }

    public int getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(int hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }


}
