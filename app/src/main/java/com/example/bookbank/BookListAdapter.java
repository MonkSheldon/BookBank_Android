package com.example.bookbank;

import static android.content.Context.MODE_PRIVATE;

import static misc.Constants.path_backend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import interfaces.RemoveItem;

public class BookListAdapter extends ArrayAdapter<Book> {

    private final String TAG = "Book List Adapter";

    private final Context context;
    private final int resource;

    private final boolean pageFavorite;
    private final RemoveItem removeItem;

    public BookListAdapter(Context context, int resource, ArrayList<Book> objects, boolean pageFavorite, RemoveItem removeItem) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.pageFavorite = pageFavorite;
        this.removeItem = removeItem;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int id = getItem(position).getId();
        String name = getItem(position).getName();
        String author = getItem(position).getAuthor();
        String price = getItem(position).getPrice();
        AtomicBoolean favorite = new AtomicBoolean(getItem(position).isFavorite());
        String tags = getItem(position).getTags();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        LinearLayout lladapter = convertView.findViewById(R.id.ll_adapter);
        TextView tvbook = convertView.findViewById(R.id.tv_book);
        TextView tvauthor = convertView.findViewById(R.id.tv_author);
        TextView tvprice = convertView.findViewById(R.id.tv_price);
        ImageButton ibttfavorite = convertView.findViewById(R.id.ibtt_favorite);
        TextView tvtags = convertView.findViewById(R.id.tv_tags);

        tvbook.setText(name);
        tvauthor.setText(author);
        tvprice.setText(price);
        tvtags.setText(tags);

        if (favorite.get())
            ibttfavorite.setImageResource(R.drawable.ic_favorite_black);

        lladapter.setOnClickListener(view -> {
            Intent i = new Intent(context.getString(R.string.launch_book));
            i.putExtra("id", id);
            context.startActivity(i);
        });

        ibttfavorite.setOnClickListener(view -> {
            final boolean newfavorite = !favorite.get();
            SharedPreferences pref = context.getSharedPreferences("Session Data", MODE_PRIVATE);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", pref.getString("email", null));
                jsonObject.put("idbook", id);
                jsonObject.put("favorite", newfavorite);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    path_backend + "/api/updatefavorite",
                    jsonObject,
                    response -> {
                        String msgtoast;
                        try {
                            if (response.getBoolean("success")) {
                                Log.i(TAG, "Update favorite ok");
                                favorite.set(newfavorite);
                                getItem(position).setFavorite(newfavorite);
                                if (newfavorite) {
                                    msgtoast = "Aggiunto ai preferiti.";
                                    ibttfavorite.setImageResource(R.drawable.ic_favorite_black);
                                }
                                else {
                                    msgtoast = "Rimosso dai preferiti.";
                                    if (pageFavorite)
                                        removeItem.onRemoveItem(position);
                                    else
                                        ibttfavorite.setImageResource(R.drawable.ic_favorite);
                                }
                            }
                            else {
                                Log.i(TAG, "Update favorite failed");
                                if (newfavorite)
                                    msgtoast = "Non è stato aggiunto ai preferiti. Riprova.";
                                else
                                    msgtoast = "Non è stato rimosso dai preferiti. Riprova.";
                            }
                            Toast.makeText(context, msgtoast, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.i(TAG, error.toString())
            );

            requestQueue.add(jsonObjectRequest);
        });

        return convertView;
    }
}
