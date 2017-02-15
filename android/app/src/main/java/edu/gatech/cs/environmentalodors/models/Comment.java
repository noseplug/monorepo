package edu.gatech.cs.environmentalodors.models;

/**
 * Class to represent comments, announcements, and surveys in odorEvents, to be held by the odorEvent and in the database
 * Owner: John Blum
 */

public class Comment {
    public String author;
    public CommentType type;
    public String content;

    public Comment(String author, String content, CommentType type) {
        this.author = author;
        this.content = content;
        this.type = type;
    }

    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
        this.type = CommentType.normal;
    }

    public Comment() {
        this.author = "TestAuthor";
        this.content = "Test Content Please Ignore";
        this.type = CommentType.normal;
    }

    public enum CommentType {
        normal,
        pinned
    }

    public String toString() {
        return String.format("%s: %s", author, content);
    }
}

