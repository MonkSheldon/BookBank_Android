package com.example.bookbank;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

public class BookActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Button bttback = findViewById(R.id.btt_back);

        bttback.setOnClickListener(view -> onBackPressed());
    }
}