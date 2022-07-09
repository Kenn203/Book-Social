package com.example.bookclub.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("LibraryItem")
public class LibraryItem extends ParseObject {
    public static final String KEY_AUTHOR_NAME = "authorName";
    public static final String KEY_BOOK_TITLE = "bookTitle";
    public static final String KEY_COVER_URL = "coverUrl";

    public LibraryItem(){}

    public LibraryItem(Book book){
        setAuthorName(book.getAuthor());
        setBookTitle(book.getTitle());
        setCoverUrl(book.getCoverUrl());

    }

    public String getAuthorName(){return getString(KEY_AUTHOR_NAME);}

    public String getBookTitle(){return getString(KEY_BOOK_TITLE);}

    public String getCoverUrl(){return getString(KEY_COVER_URL);}

    public void setAuthorName(String name){put(KEY_AUTHOR_NAME, name);}

    public void setBookTitle(String title){put(KEY_BOOK_TITLE, title);}

    public void setCoverUrl(String imageUrl){put(KEY_COVER_URL, imageUrl);}
}
