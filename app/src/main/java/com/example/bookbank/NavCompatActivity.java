package com.example.bookbank;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bookbank.R.id;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class NavCompatActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compat_activity_nav);

        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navview = findViewById(R.id.nav_view);
        ImageButton ibttclose = navview.getHeaderView(0).findViewById(id.ibtt_close);
        NavigationView navviewbottom = findViewById(R.id.nav_view_bottom);

        SharedPreferences pref = NavCompatActivity.this.getSharedPreferences("Session Data", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.light_red));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CatalogFragment(false)).commit();
            navview.setCheckedItem(R.id.nav_home);
        }

        ibttclose.setOnClickListener(view -> drawer.closeDrawer(GravityCompat.START));

        navview.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new CatalogFragment(false)).commit();
                    break;
                case R.id.nav_favorite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new CatalogFragment(true)).commit();
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        navviewbottom.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SettingsFragment()).commit();
                    break;
                case R.id.nav_exit_to_app:
                    SharedPreferences.Editor edit = pref.edit();
                    edit.clear();
                    edit.apply();
                    Intent i = new Intent(getString(R.string.launch_sing_in));
                    startActivity(i);
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}
