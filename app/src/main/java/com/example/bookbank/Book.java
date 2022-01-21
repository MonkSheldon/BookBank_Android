package com.example.bookbank;

public class Book {

    private final String name;
    private final String author;
    private final String price;
    private final boolean favorite;
    private final String tags;

    public Book(String name, String author, String price, boolean favorite, String tags) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.favorite = favorite;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrice() {
        return price;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getTags() {
        return tags;
    }
}
