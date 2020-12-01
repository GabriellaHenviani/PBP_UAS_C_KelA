package com.kelompok_a.tubes_sewa_kos.Model;

public class User {
    private int id;
    private String email, password, noHp, nama;

    public User(String email, String password, String noHp, String nama) {
        this.email = email;
        this.password = password;
        this.noHp = noHp;
        this.nama = nama;
    }

    public User(int id, String email, String noHp, String nama) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.noHp = noHp;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
