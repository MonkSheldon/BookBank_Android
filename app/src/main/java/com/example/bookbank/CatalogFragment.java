package com.example.bookbank;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CatalogFragment extends Fragment {

    private final boolean favorite;

    public CatalogFragment(boolean favorite) {
        this.favorite = favorite;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        ListView lvbooks = view.findViewById(R.id.lv_books);
        ArrayList<Book> booklist = new ArrayList<>();

        //TODO getbooks

        Book book = new Book("Book0", "Alessio", "20 $", true, "l dep");
        booklist.add(book);
        for (int i=0; i<20; ++i) {
            book = new Book("Book" + i, "Alessio", "20 $", false, "l dep");
            booklist.add(book);
        }

        BookListAdapter adapter = new BookListAdapter(view.getContext(), R.layout.adapter_view_book, booklist);
        lvbooks.setAdapter(adapter);

        return view;
    }
}
