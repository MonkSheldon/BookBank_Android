package com.example.bookbank;

import static misc.Constants.path_backend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends Activity {

    private final String TAG = "Login Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO control session

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
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("pwd", pwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        path_backend + "/api/login",
                        jsonObject,
                        response -> {
                            try {
                                if (response.getBoolean("success")) {
                                    Log.i(TAG, "Login email/pwd ok");
                                    SharedPreferences pref = LoginActivity.this.getSharedPreferences("Session Data", MODE_PRIVATE);
                                    SharedPreferences.Editor edit = pref.edit();
                                    edit.putString("email", email);
                                    edit.apply();
                                    Intent i = new Intent(getString(R.string.launch_catalog));
                                    startActivity(i);
                                }
                                else {
                                    Log.i(TAG, "Login email/pwd failed");
                                    Toast.makeText(LoginActivity.this, "Email and/or password are wrong", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> Log.i(TAG, error.toString())
                );

                requestQueue.add(jsonObjectRequest);
            }
        });

        bttsingup.setOnClickListener(view -> {
            Intent i = new Intent(getString(R.string.launch_sing_up));
            startActivity(i);
        });
    }
}
