package com.kelompok_a.tubes_sewa_kos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import android.util.Patterns;

import static android.util.Patterns.EMAIL_ADDRESS;

public class LoginActivity extends AppCompatActivity {
    Button cancel, login;
    TextView link;
    TextInputEditText email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cancel = findViewById(R.id.btn_cancel);
        login = findViewById(R.id.btn_login);
        link = findViewById(R.id.link_signup);
        email = findViewById(R.id.input_email);
        pass = findViewById(R.id.input_password);

        cancel.setOnClickListener(new View.OnClickListener() {
            //cancel kalo tidak jadi login
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail;
                mail = email.getText().toString();
                // Exception sementara selama belum ada penanganan Login account dengan database
                if(email.length() == 0 && !EMAIL_ADDRESS.matcher(mail).matches()){
                    Toast.makeText(LoginActivity.this, "Email Invalid", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length()<6){
                    Toast.makeText(LoginActivity.this, "Password terlalu pendek", Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login Berhasil !!", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code untuk link ke layout register
            }
        });
    }
}