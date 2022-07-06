package com.example.bookclub;

import com.example.bookclub.models.Book;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Library")
public class Library extends ParseObject {
    public static final String KEY_BOOK_AUTHOR = "authorName";
    public static final String KEY_COVER_URL = "bookImage";
    public static final String KEY_BOOK_TITLE = "bookTitle";

    public Library() {

    }

    public Library(Book book) {
        setAuthorName(book.getAuthor());
        setBookTitle(book.getTitle());
        setCoverUrl(book.getCoverUrl());

    }

    public String getAuthorName() {
        return getString(KEY_BOOK_AUTHOR);
    }

    public String getCoverUrl() {
        return getString(KEY_COVER_URL);
    }

    public String getBookTitle() {
        return getString(KEY_BOOK_TITLE);
    }

    public void setAuthorName(String name) {
        put(KEY_BOOK_AUTHOR, name);
    }

    public void setCoverUrl(String imageUrl) {
        put(KEY_COVER_URL, imageUrl);
    }

    public void setBookTitle(String title) {
        put(KEY_BOOK_TITLE, title);
    }

}
