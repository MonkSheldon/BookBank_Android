package com.example.bookbank;

public class Book {

    private final int id;
    private final String name;
    private final String author;
    private final String price;
    private boolean favorite;
    private final String tags;

    public Book(int id, String name, String author, String price, boolean favorite, String tags) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.favorite = favorite;
        this.tags = tags;
    }

    public int getId() { return id; }

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

    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public String getTags() {
        return tags;
    }
}
