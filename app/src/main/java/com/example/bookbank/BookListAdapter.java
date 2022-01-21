package com.example.bookbank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookListAdapter extends ArrayAdapter<Book> {

    private final String TAG = "Book List Adapter";

    private final Context context;
    private final int resource;

    public BookListAdapter(Context context, int resource, ArrayList<Book> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String author = getItem(position).getAuthor();
        String price = getItem(position).getPrice();
        boolean favorite = getItem(position).isFavorite();
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

        if (favorite) {
            ibttfavorite.setImageResource(R.drawable.ic_favorite_black);
        }

        lladapter.setOnClickListener(view -> {
            //TODO view book
        });

        ibttfavorite.setOnClickListener(view -> {
            //TODO update favorite
        });

        return convertView;
    }
}
