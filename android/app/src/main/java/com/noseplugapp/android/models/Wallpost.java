package com.noseplugapp.android.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to represent wallposts, announcements, and surveys in odorEvents, to be held by the odorEvent and in the database
 * Owner: John Blum
 */

public class Wallpost {
    public String author;
    public Type type;
    public String content;

    public Wallpost(String author, String content, Type type) {
        this.author = author;
        this.content = content;
        this.type = type;
    }

    public Wallpost(String author, String content) {
        this.author = author;
        this.content = content;
        this.type = Type.normal;
    }

    public Wallpost() {
        this.author = "TestAuthor";
        this.content = "Test Content Please Ignore";
        this.type = Type.normal;
    }

    public enum Type {
        normal,
        pinned
    }

    public String toString() {
        return String.format("%s: %s", author, content);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}

