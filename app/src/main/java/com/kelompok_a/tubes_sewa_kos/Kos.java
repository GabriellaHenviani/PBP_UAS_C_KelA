package com.kelompok_a.tubes_sewa_kos;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kelompok_a.tubes_sewa_kos.API.KostAPI;

import java.io.Serializable;

public class Kos implements Serializable {
    private String nama;
    private String tipe;
    private String alamat;
    private int harga, id;
    private String imgURL;
    private double longitude, latitude;
    private int idUser;

    public Kos(String nama, String tipe, String alamat, int harga, String imgURL, double longitude, double latitude, int idUser) {
        this.nama = nama;
        this.tipe = tipe;
        this.alamat = alamat;
        this.harga = harga;
        this.imgURL = imgURL;
        this.longitude = longitude;
        this.latitude = latitude;
        this.idUser = idUser;
    }

    public Kos(int id, String nama, String tipe, String alamat, int harga, String imgURL, Double longitude, Double latitude, int idUser) {
        this.id = id;
        this.nama = nama;
        this.tipe = tipe;
        this.alamat = alamat;
        this.harga = harga;
        this.imgURL = imgURL;
        this.longitude = longitude;
        this.latitude = latitude;
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getHarga() {
        String hargaString = "Rp " + harga + " / bulan";
        return hargaString;
    }
    public String getHargaOnlyNumber() {
        return String.valueOf(harga);
    }

    public void setHarga(int harga) { this.harga = harga; }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getImgURL() {
        return KostAPI.URL_IMAGES + imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @BindingAdapter({"imgURL"})
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }
}
