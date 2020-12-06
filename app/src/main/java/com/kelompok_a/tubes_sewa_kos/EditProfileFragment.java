package com.kelompok_a.tubes_sewa_kos;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import static android.widget.Toast.LENGTH_SHORT;

public class EditProfileFragment extends Fragment implements EditProfileView{
    private SharedPref sharedPref;
    private User user;
    private Button batal, edit;
    private TextInputEditText etNama, etEmail, etNoHp;
    private EditProfilePresenter presenter;
    private boolean done;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        sharedPref = new SharedPref(getActivity());

        user = (User) getArguments().getSerializable("user");

        etEmail = view.findViewById(R.id.input_email);
        etNama = view.findViewById(R.id.input_nama);
        etNoHp = view.findViewById(R.id.input_no_hp);
        batal = view.findViewById(R.id.btn_cancel);
        edit = view.findViewById(R.id.btn_edit);

        init();
        presenter = new EditProfilePresenter(this, new EditProfileService());

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onEditClicked(getActivity(), sharedPref, getActivity().getSupportFragmentManager());
            }
        });

        return view;
    }

    public void init()  {
        etNama.setText(user.getNama());
        etNoHp.setText(user.getNoHp());
        etEmail.setText(user.getEmail());
    }

    @Override
    public String getNama() {
        return etNama.getText().toString();
    }

    @Override
    public void showNamaError(String message) {
        etNama.setError(message);
    }

    @Override
    public String getNoHp() {
        return etNoHp.getText().toString();
    }

    @Override public void showNoHpError(String message) {
        etNoHp.setError(message);
    }

    @Override
    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void startMainActivity(FragmentManager fragmentManager) {
        new ActivityUtil(getActivity()).startMainActivity(fragmentManager);
    }

    @Override
    public void showEditError(String message) {
        Toast.makeText(getActivity(), message, LENGTH_SHORT).show();
    }

    @Override
    public void showErrorResponse(String message) {
        Toast.makeText(getActivity(), message, LENGTH_SHORT).show();
    }
}