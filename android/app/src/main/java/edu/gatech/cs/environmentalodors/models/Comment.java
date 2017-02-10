package edu.gatech.cs.environmentalodors.models;

/**
 * Created by John on 2/7/2017.
 */

public class Comment {
    public String author;
    public commentType type;
    public String content;

    public Comment(String author, String content, commentType type)
    {
        this.author = author;
        this.content = content;
        this.type = type;
    }

    public Comment(String author, String content)
    {
        this.author = author;
        this.content = content;
        this.type = commentType.normal;
    }

    public Comment()
    {
        this.author = "TestAuthor";
        this.content = "Test Content Please Ignore";
        this.type = commentType.normal;
    }

    enum commentType
    {
        normal,
        pinned
    }

    public String toString()
    {
        return String.format("%s: %s", author, content);
    }
}

