package com.example.bookbank;

import static misc.Check.isEmpty;
import static misc.Check.pwdAreEquals;
import static misc.Constants.path_backend;

import android.app.Activity;
import android.content.SharedPreferences;
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

public class ChangePwdActivity extends Activity {

    private final String TAG = "Change Pwd Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        Button bttback = findViewById(R.id.btt_back);
        TextInputLayout tilcurrentpwd = findViewById(R.id.til_current_pwd);
        TextInputLayout tilnewpwd = findViewById(R.id.til_new_pwd);
        TextInputLayout tilconfirmpwd = findViewById(R.id.til_confirm_pwd);
        Button bttchangepwd = findViewById(R.id.btt_change_pwd);

        bttback.setOnClickListener(view -> onBackPressed());

        Objects.requireNonNull(tilcurrentpwd.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilcurrentpwd.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        Objects.requireNonNull(tilnewpwd.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilnewpwd.setError(null);
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

        bttchangepwd.setOnClickListener(view -> {
            boolean isEmptyCurrentPwd = isEmpty(tilcurrentpwd);
            boolean isEmptyNewPwd = isEmpty(tilnewpwd);
            boolean isEmptyConfirmPwd = isEmpty(tilconfirmpwd);

            if (!isEmptyCurrentPwd && !isEmptyNewPwd && !isEmptyConfirmPwd &&
                pwdAreEquals(tilconfirmpwd, tilnewpwd)) {
                String currentpwd = Objects.requireNonNull(tilcurrentpwd.getEditText()).getText().toString();
                String newpwd = Objects.requireNonNull(tilnewpwd.getEditText()).getText().toString();
                String confirmpwd = Objects.requireNonNull(tilconfirmpwd.getEditText()).getText().toString();

                SharedPreferences pref = ChangePwdActivity.this.getSharedPreferences("Session Data", MODE_PRIVATE);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", pref.getString("email", null));
                    jsonObject.put("currentpwd", currentpwd);
                    jsonObject.put("newpwd", newpwd);
                    jsonObject.put("confirmpwd", confirmpwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        path_backend + "/api/changepwd",
                        jsonObject,
                        response -> {
                            try {
                                if (response.getBoolean("success")) {
                                    Log.i(TAG, "Change pwd ok");
                                    Toast.makeText(this, "La password è stata cambiata.", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                                else {
                                    Log.i(TAG, "Change pwd failed");
                                    Toast.makeText(this, "La password non è stata cambiata. Riprova.", Toast.LENGTH_LONG).show();
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
