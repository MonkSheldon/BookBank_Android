package com.example.bookbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout tilemail = findViewById(R.id.til_email);
        TextView txtforgotpwd = findViewById(R.id.txt_forgot_pwd);
        TextInputLayout tilpwd = findViewById(R.id.til_pwd);
        Button bttsingin = findViewById(R.id.btt_sing_in);
        Button bttsingup = findViewById(R.id.btt_sing_up);

        txtforgotpwd.setOnClickListener(view -> {
            Intent i = new Intent(getString(R.string.launch_forgot_pwd));
            startActivity(i);
        });

        bttsingin.setOnClickListener(view -> {
            String email = Objects.requireNonNull(tilemail.getEditText()).getText().toString();
            String pwd = Objects.requireNonNull(tilpwd.getEditText()).getText().toString();

            if (email.isEmpty() || pwd.isEmpty()) {
                if (email.isEmpty()) {
                    tilemail.setError(getString(R.string.error_obligatory_field));
                    tilemail.requestFocus();
                }
                if (pwd.isEmpty()) {
                    tilpwd.setError(getString(R.string.error_obligatory_field));
                    tilpwd.requestFocus();
                }
            }
            else {

            }
        });

        bttsingup.setOnClickListener(view -> {
            Intent i = new Intent(getString(R.string.launch_sing_up));
            startActivity(i);
        });
    }
}
