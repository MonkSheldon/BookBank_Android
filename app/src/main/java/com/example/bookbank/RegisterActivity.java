package com.example.bookbank;

import static misc.Check.isEmpty;
import static misc.Check.pwdAreEquals;
import static misc.Constants.path_backend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
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

public class RegisterActivity extends Activity {

    private final String TAG = "Register Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button bttback = findViewById(R.id.btt_back);
        TextInputLayout tilemail = findViewById(R.id.til_email);
        TextInputLayout tilpwd = findViewById(R.id.til_pwd);
        TextInputLayout tilconfirmpwd = findViewById(R.id.til_confirm_pwd);
        Button bttsingup = findViewById(R.id.btt_sing_up);

        Intent isingin = new Intent(getString(R.string.launch_sing_in));

        bttback.setOnClickListener(view -> startActivity(isingin));

        Objects.requireNonNull(tilpwd.getEditText()).addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilpwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        Objects.requireNonNull(tilconfirmpwd.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilconfirmpwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        bttsingup.setOnClickListener(view -> {
            boolean isEmptyEmail = isEmpty(tilemail);
            boolean isEmptyPwd = isEmpty(tilpwd);
            boolean isEmptyConfirmPwd = isEmpty(tilconfirmpwd);

            if (!isEmptyEmail && !isEmptyPwd && !isEmptyConfirmPwd && pwdAreEquals(tilconfirmpwd, tilpwd)) {
                String email = Objects.requireNonNull(tilemail.getEditText()).getText().toString();
                String pwd = Objects.requireNonNull(tilpwd.getEditText()).getText().toString();
                String confirmpwd = Objects.requireNonNull(tilconfirmpwd.getEditText()).getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                    jsonObject.put("pwd", pwd);
                    jsonObject.put("confirmpwd", confirmpwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        path_backend + "/api/register",
                        jsonObject,
                        response -> {
                            try {
                                if (response.getBoolean("success")) {
                                    Log.i(TAG, "Register ok");
                                    Toast.makeText(RegisterActivity.this, "La registrazione è andata a buon fine. Accedi.", Toast.LENGTH_LONG).show();
                                    startActivity(isingin);
                                }
                                else {
                                    Log.i(TAG, "Register failed");
                                    Toast.makeText(RegisterActivity.this, "La registrazione non è andata a buon fine.", Toast.LENGTH_LONG).show();
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
    }
}
