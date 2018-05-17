package com.example.lanto.popularmovies.Data;

public class Review {

    private final String mAuthor;
    private final String mContent;

    public Review(String mAuthor, String mContent) {
        this.mAuthor = mAuthor;
        this.mContent = mContent;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmContent() {
        return mContent;
    }
}
