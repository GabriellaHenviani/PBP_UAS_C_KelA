package com.kelompok_a.tubes_sewa_kos;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentManager;

public class EditProfilePresenter {
    private EditProfileView view;
    private EditProfileService service ;

    public EditProfilePresenter(EditProfileView view, EditProfileService service) {
        this.view = view;
        this.service = service;
    }

    public void onEditClicked(Context context, SharedPref sharedPref, FragmentManager fragmentManager){
//        namaInput = etNama.getText().toString();
//        noHpInput = etNoHp.getText().toString();
        if (view.getNama().isEmpty() || view.getNoHp().isEmpty() ||
                view.getNoHp().length() < 11 || view.getNoHp().length() > 12 ) {
            if (view.getNama().isEmpty()) {
                view.showNamaError("Nama lengkap tidak boleh kosong");
            }
            if (view.getNoHp().isEmpty()) {
                view.showNoHpError("Nomor Handphone Tidak Boleh Kosong");
            } else if (view.getNoHp().length() < 11 || view.getNoHp().length() > 12) {
                view.showNoHpError("Nomor handphone harus 11-12 digit");
            }
        }
        else{
            service.editUser(view, sharedPref, view.getNama(), view.getNoHp(), new EditProfileCallback() {
                @Override
                public void onSuccess(boolean value) {
                    MainActivity.changeMenu(MainActivity.binding.bottomNavigation);
                    view.startMainActivity(fragmentManager);
                }

                @Override
                public void onError() {

                }
            }, context);
            return;
        }
    }
}
