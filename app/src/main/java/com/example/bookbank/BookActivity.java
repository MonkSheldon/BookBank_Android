package com.example.bookbank;

import static misc.Constants.path_backend;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class BookActivity extends Activity {

    private final String TAG = "Book Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Button bttback = findViewById(R.id.btt_back);
        ImageView ivbook = findViewById(R.id.iv_book);
        TextView tvtitle = findViewById(R.id.tv_title);
        TextView tvauthor = findViewById(R.id.tv_author);
        TextView tvprice = findViewById(R.id.tv_price);
        TextView tvbook = findViewById(R.id.tv_book);
        TextView tvreviews = findViewById(R.id.tv_reviews);

        bttback.setOnClickListener(view -> onBackPressed());

        SharedPreferences pref = BookActivity.this.getSharedPreferences("Session Data", MODE_PRIVATE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", pref.getString("email", null));
            jsonObject.put("idbook", getIntent().getExtras().getString("id"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                path_backend + "/api/getbook",
                jsonObject,
                response -> {
                    try {
                        if (response.isNull("book")) {
                            Log.i(TAG, "Get book failed");
                            Toast.makeText(this, "Impossibile visualizzare le informaziooni del libro. Riprova.", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        else {
                            Log.i(TAG, "Get book ok");
                            JSONObject book = response.getJSONObject("book");

                            String urlimg = path_backend + book.getString("path_image");
                            Picasso.get().load(urlimg).into(ivbook);

                            tvtitle.setText(book.getString("title"));
                            tvauthor.setText(book.getString("author"));
                            tvprice.setText(book.getString("price"));
                            tvbook.setText(book.getString("book"));
                            tvreviews.setText(book.getString("reviews"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.i(TAG, error.toString())
        );

        requestQueue.add(jsonObjectRequest);
    }
}
