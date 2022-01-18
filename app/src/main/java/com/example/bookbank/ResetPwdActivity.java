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

public class ResetPwdActivity extends Activity {

    private final String TAG = "Reset Pwd Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        TextInputLayout tilnewpwd = findViewById(R.id.til_new_pwd);
        TextInputLayout tilconfirmpwd = findViewById(R.id.til_confirm_pwd);
        Button bttresetpwd = findViewById(R.id.btt_reset_pwd);

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

        bttresetpwd.setOnClickListener(view -> {
            boolean isEmptyNewPwd = isEmpty(tilnewpwd);
            boolean isEmptyConfirmPwd = isEmpty(tilconfirmpwd);

            if (!isEmptyNewPwd && !isEmptyConfirmPwd && pwdAreEquals(tilconfirmpwd, tilnewpwd)) {
                String newpwd = Objects.requireNonNull(tilnewpwd.getEditText()).getText().toString();
                String confirmpwd = Objects.requireNonNull(tilconfirmpwd.getEditText()).getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", getIntent().getExtras().getString("email"));
                    jsonObject.put("newpwd", newpwd);
                    jsonObject.put("confirmpwd", confirmpwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        path_backend + "/api/resetpwd",
                        jsonObject,
                        response -> {
                            try {
                                if (response.getBoolean("success")) {
                                    Log.i(TAG, "Reset pwd ok");
                                    Toast.makeText(ResetPwdActivity.this, "La password è stata resettata. Accedi.", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getString(R.string.launch_sing_in));
                                    startActivity(i);
                                }
                                else {
                                    Log.i(TAG, "Reset pwd failed");
                                    Toast.makeText(ResetPwdActivity.this, "La password non è stata resettata. Riprova.", Toast.LENGTH_LONG).show();
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
