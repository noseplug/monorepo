package com.noseplugapp.android.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.UUID;
import java.util.Calendar;

/**
 * Class to represent wallposts, announcements, and surveys in odorEvents, to be held by the odorEvent and in the database
 * Owner: John Blum
 */

public class Wallpost {
    public String id;
    public String author;
    public Type type;
    public String content;
    public Date date;

    public Wallpost(String author, String content, Type type) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.content = content;
        this.type = type;
        this.date = new Date(Calendar.getInstance().getTimeInMillis());
    }

    public Wallpost(String author, String content) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.content = content;
        this.type = Type.normal;
        this.date = new Date(Calendar.getInstance().getTimeInMillis());
    }

    public Wallpost() {
        this.id = UUID.randomUUID().toString();
        this.author = "TestAuthor";
        this.content = "Test Content Please Ignore";
        this.type = Type.normal;
        this.date = new Date(Calendar.getInstance().getTimeInMillis());
    }

    public enum Type {
        normal,
        pinned
    }

    public String toString() {
        return String.format("%s: %s: %s", date, author, content);
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

