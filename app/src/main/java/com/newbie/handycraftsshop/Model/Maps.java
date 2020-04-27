package com.newbie.handycraftsshop.Model;

import com.google.gson.annotations.SerializedName;

public class Maps {
    @SerializedName("nama_kota")
    private String nama;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public Maps(){
    }

    public Maps(String nama, String latitude, String longitude) {
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
