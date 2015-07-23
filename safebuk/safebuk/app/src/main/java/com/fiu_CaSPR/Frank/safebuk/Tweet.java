package com.fiu_CaSPR.Frank.safebuk;

import android.net.Uri;

/**
 * Created by ivan.minev on 22.1.2015 г..
 */
public class Tweet
{

    private String author;

    private String username;

    private long twittTimeMilliseconds;

    private Uri imageUri;

    private String text;

    public Uri getImageUri()
    {
        return imageUri;
    }
    public void setImageUri(Uri imageUri)
    {
        this.imageUri = imageUri;
    }
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
    public String getAuthor()
    {
        return author;
    }
    public void setAuthor(String author)
    {
        this.author = author;
    }
    public long getTwittTimeMilliseconds()
    {
        return twittTimeMilliseconds;
    }
    public void setTwittTimeMilliseconds(long twittTimeMilliseconds)
    {
        this.twittTimeMilliseconds = twittTimeMilliseconds;
    }
    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        this.text = text;
    }
}
