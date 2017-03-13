package com.noseplugapp.android.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class User {

    public final UUID uuid = UUID.randomUUID();

    /* // Things that may or may not be useful to us.
    public enum Sex {MALE, FEMALE, NOT_AVAILABLE}

    private Date birthday; // TODO: age range instead of actual DOB?
    private Sex sex;
    private String address;
    private String email;
    private String phoneNumber;
    private String username;
    */

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
