package com.example.Daftar_Buku_Pintar;

import com.google.gson.annotations.SerializedName;


public class Buku {

    @SerializedName("id")
    private int id;
    @SerializedName("judul buku")
    private String judulbuku;
    @SerializedName("namapengarang")
    private String namapengarang;
    @SerializedName("penerbit")
    private String penerbit;
    @SerializedName("pilihtema")
    private int pilihtema;
    @SerializedName("tahunterbit")
    private String tahunterbit;
    @SerializedName("picture")
    private String picture;
    @SerializedName("thumb")
    private Boolean thumb;
    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudulbuku() {
        return judulbuku;
    }

    public void setJudulbuku(String judulbuku) {
        this.judulbuku = judulbuku;
    }

    public String getNamapengarang() {
        return namapengarang;
    }

    public void setNamapengarang(String namapengarang) {
        this.namapengarang = namapengarang;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String breed) {
        this.penerbit = penerbit;
    }

    public int getPilihtema() {
        return pilihtema;
    }

    public void setPilihtema(int pilihtema) {
        this.pilihtema = pilihtema;
    }

    public String getTahunterbit() {
        return tahunterbit;
    }

    public void setTahunterbit(String tahunterbit) {
        this.tahunterbit = tahunterbit;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getThumb() {
        return thumb;
    }

    public void setThumb(Boolean thumb) {
        this.thumb = thumb;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

}





