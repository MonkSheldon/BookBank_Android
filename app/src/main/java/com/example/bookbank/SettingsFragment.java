package com.example.bookbank;


import static android.content.Context.MODE_PRIVATE;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView tvemail = view.findViewById(R.id.tv_email);
        AppCompatButton acbttchangepwd = view.findViewById(R.id.acbtt_change_pwd);
        Context context = view.getContext();

        SharedPreferences pref = context.getSharedPreferences("Session Data", MODE_PRIVATE);

        tvemail.setText(pref.getString("email", null));

        acbttchangepwd.setOnClickListener(view1 -> {
            Intent i = new Intent(context.getString(R.string.launch_change_pwd));
            startActivity(i);
        });

        return view;
    }
}
