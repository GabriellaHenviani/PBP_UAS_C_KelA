package com.kelompok_a.tubes_sewa_kos;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditProfilePresenterTest {

    @Mock
    private EditProfileView view;
    @Mock
    private EditProfileService service;
    private EditProfilePresenter presenter;

    @Mock
    private SharedPref sharedPref = Mockito.mock(SharedPref.class);;
    private Context context;

    @Before
    public void setUp() throws Exception {
        presenter =  new EditProfilePresenter(view, service);
        sharedPref = Mockito.mock(SharedPref.class);
        context = Mockito.mock(Context.class);
    }

    @Test
    public void showMessageWhenNamaEmpty() throws Exception {
        when(view.getNama()).thenReturn("");
        System.out.println("nama: " + view.getNama());

        when(view.getNoHp()).thenReturn("082123456079");
        System.out.println("noHp: " + view.getNoHp());

        presenter.onEditClicked(view.getActivity(), sharedPref,view.getFragmentManager());

        verify(view).showNamaError("Nama lengkap tidak boleh kosong");
    }

    @Test
    public void showMessageWhenNoHpEmpty() {
        when(view.getNama()).thenReturn("Aldri");
        System.out.println("nama: " + view.getNama());

        when(view.getNoHp()).thenReturn("");
        System.out.println("noHp: " + view.getNoHp());

        presenter.onEditClicked(view.getActivity(), sharedPref, view.getFragmentManager());

        verify(view).showNoHpError("Nomor Handphone Tidak Boleh Kosong");
    }

    @Test
    public void showErrorWhenNoHpInvalid() throws Exception {
        when(view.getNama()).thenReturn("Aldri");
        System.out.println("nama    : " + view.getNama());

        when(view.getNoHp()).thenReturn("08123");
        System.out.println("password : "+view.getNoHp());
        presenter.onEditClicked(view.getActivity(), sharedPref, view.getFragmentManager());
        verify(view).showNoHpError("Nomor handphone harus 11-12 digit");
    }

    @Test
    public void showErrorWhenNoHpInvalid2() throws Exception {
        when(view.getNama()).thenReturn("Aldri");
        System.out.println("nama    : " + view.getNama());

        when(view.getNoHp()).thenReturn("08123123123123123");
        System.out.println("password : "+view.getNoHp());
        presenter.onEditClicked(view.getActivity(), sharedPref, view.getFragmentManager());
        verify(view).showNoHpError("Nomor handphone harus 11-12 digit");
    }

    @Test
    public void shouldEditWhenNamaAndNoHpAreCorrect() throws Exception {
        when(view.getNama()).thenReturn("Aldri");
        System.out.println("nama    : " + view.getNama());

        when(view.getNoHp()).thenReturn("081234560796");
        System.out.println("password : "+view.getNoHp());

        when(service.getValid(view, view.getNama(), view.getNoHp())).thenReturn(true);
        System.out.println("Hasil : "+service.getValid(view,view.getNama(), view.getNoHp()));
        presenter.onEditClicked(view.getActivity(), sharedPref, view.getFragmentManager());
        //verify(view).startMainActivity();
    }
}