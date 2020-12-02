package com.kelompok_a.tubes_sewa_kos;

import java.io.Serializable;

public class User implements Serializable {
    private String email, noHp, nama;

    public User(String nama, String noHp, String email) {
        this.email = email;
        this.noHp = noHp;
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
