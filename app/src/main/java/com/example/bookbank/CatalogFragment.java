package com.example.bookbank;

import static android.content.Context.MODE_PRIVATE;

import static misc.Constants.path_backend;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CatalogFragment extends Fragment {

    private final String TAG = "Catalog Fragment";

    private Context context;
    private ListView lvbooks;
    private TextView tvnobooks;
    private final boolean favorite;

    public CatalogFragment(boolean favorite) {
        this.favorite = favorite;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        EditText etsearchbooks = view.findViewById(R.id.et_search_books);
        context = view.getContext();
        lvbooks = view.findViewById(R.id.lv_books);
        tvnobooks = view.findViewById(R.id.tv_no_book);

        etsearchbooks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getbooks(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        getbooks("");

        return view;
    }

    private void getbooks(String search) {
        ArrayList<Book> booklist = new ArrayList<>();
        SharedPreferences pref = context.getSharedPreferences("Session Data", MODE_PRIVATE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", pref.getString("email", null));
            jsonObject.put("favorite", favorite);
            jsonObject.put("search", search);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                path_backend + "/api/getbooks",
                jsonObject,
                response -> {
                    try {
                        JSONArray books = response.getJSONArray("books");
                        if (books.length() == 0) {
                            tvnobooks.setVisibility(View.VISIBLE);
                        }
                        else {
                            for (int i=0; i<books.length(); ++i) {
                                JSONObject jsonbook = books.getJSONObject(i);
                                booklist.add(new Book(jsonbook.getInt("id"),
                                        jsonbook.getString("name"),
                                        jsonbook.getString("author"),
                                        jsonbook.getString("price"),
                                        jsonbook.getBoolean("favorite"),
                                        jsonbook.getString("tags")));
                            }

                            BookListAdapter adapter = new BookListAdapter(context, R.layout.adapter_view_book, booklist);
                            lvbooks.setAdapter(adapter);
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
