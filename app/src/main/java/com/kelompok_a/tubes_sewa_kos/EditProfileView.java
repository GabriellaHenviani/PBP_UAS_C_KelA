package com.kelompok_a.tubes_sewa_kos;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;

public interface EditProfileView {
    FragmentManager getFragmentManager();
    Activity getActivity();
    String getNama();
    void showNamaError(String message);
    String getNoHp();
    void setDone(boolean done);
    boolean isDone();
    void showNoHpError(String message);
    void startMainActivity(FragmentManager fragmentManager);
    void showEditError(String message);
    void showErrorResponse(String message);
}
