package com.example.Daftar_Buku_Pintar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {

    @POST("list_buku.php")
    Call<List<Buku>> getBuku();

    @FormUrlEncoded
    @POST("tambah_buku.php")
    Call<Buku> insertBuku(
            @Field("key") String key,
            @Field("judulbuku") String judulbuku,
            @Field("namapengarang") String namapengarang,
            @Field("penerbit") String penerbit,
            @Field("pilihtema") int pilihtema,
            @Field("tahunterbit") String tahunterbit,
            @Field("picture") String picture);

    @FormUrlEncoded
    @POST("update_buku.php")
    Call<Buku> updateBuku(
            @Field("key") String key,
            @Field("id") int id,
            @Field("judulbuku") String judulbuku,
            @Field("namapengarang") String namapengarang,
            @Field("penerbit") String penerbit,
            @Field("pilihtema") int pilihtema,
            @Field("tahunterbit") String tahunterbit,
            @Field("picture") String picture);

    @FormUrlEncoded
    @POST("hapus_buku.php")
    Call<Buku> deleteBuku(
            @Field("key") String key,
            @Field("id") int id,
            @Field("picture") String picture);

    @FormUrlEncoded
    @POST("update_thumb.php")
    Call<Buku> updateThumb(
            @Field("key") String key,
            @Field("id") int id,
            @Field("thumb") boolean thumb);

}
