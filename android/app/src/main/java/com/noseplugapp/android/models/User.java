package com.noseplugapp.android.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class User {

    public String uuid = UUID.randomUUID().toString();
    private String name;
    private String gender;
    private Type type;

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        CITIZEN, RESEARCHER, GOV_OFFICIAL, ADMIN
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
