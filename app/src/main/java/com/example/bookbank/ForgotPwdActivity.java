package com.example.bookbank;

import static misc.Check.isEmpty;
import static misc.Constants.path_backend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class ForgotPwdActivity extends Activity {

    private final String TAG = "Forgot Pwd Activity";

    private String email = "";
    private JsonObjectRequest jsonObjectRequest;
    private String generatedcode = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        Button bttback = findViewById(R.id.btt_back);
        TextInputLayout tilemail = findViewById(R.id.til_email);
        Button bttsearch = findViewById(R.id.btt_search);
        LinearLayout llverify = findViewById(R.id.ll_verify);
        TextInputLayout tilcode = findViewById(R.id.til_code);
        Button bttverify = findViewById(R.id.btt_verify);
        TextView txtreturncode = findViewById(R.id.txt_return_code);

        bttback.setOnClickListener(view -> {
            Intent i = new Intent(getString(R.string.launch_sing_in));
            startActivity(i);
        });

        bttsearch.setOnClickListener(view -> {
            if (!isEmpty(tilemail)) {
                email = Objects.requireNonNull(tilemail.getEditText()).getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        path_backend + "/api/forgotpwd",
                        jsonObject,
                        response -> {
                            try {
                                generatedcode = response.getString("code");
                                if (generatedcode.isEmpty()) {
                                    Log.i(TAG, "Search account failed");
                                    Toast.makeText(ForgotPwdActivity.this, "Scusaci, non abbiamo trovato il tuo account.", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Log.i(TAG, "Search account ok");
                                    tilemail.getEditText().setEnabled(false);
                                    llverify.setVisibility(View.VISIBLE);
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

        bttverify.setOnClickListener(view -> {
            if (!isEmpty(tilcode)) {
                String code = Objects.requireNonNull(tilcode.getEditText()).getText().toString();

                if (code.equals(generatedcode)) {
                    Log.i(TAG, "Verify ok");
                    Intent i = new Intent(getString(R.string.launch_change_pwd));
                    i.putExtra("email", email);
                    startActivity(i);
                }
                else {
                    Log.i(TAG, "Verify failed");
                    Toast.makeText(ForgotPwdActivity.this, "Il codice non è corretto. Riprova.", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtreturncode.setOnClickListener(view -> {
            Log.i(TAG, "Send code");
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
            Toast.makeText(ForgotPwdActivity.this, "Il codice è stato inviato.", Toast.LENGTH_LONG).show();
        });
    }
}
