package com.noseplugapp.android.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Odor describes an environmental odor as experienced by an individual.
 */
public class Odor {
    private Strength strength;
    private Type type;
    private String description;

    public Odor(Strength strength, Type type, String description) {
        this.strength = strength;
        this.type = type;
        this.description = description;
    }

    public Strength getStrength() {
        return strength;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public enum Strength {
        LIGHT,          // "Barely noticeable."
        MODERATE,       // "You can smell it, but it doesn't affect normal life."
        STRONG,         // "Can't go outside."
        VERY_STRONG     // "Makes  you feel sick."
    }

    public enum Type {
        FRAGRANT,
        PUNGENT,
        PUTRID,
        FECAL,
        FISHY,
        EARTHY,
        PINE,
        CHEMICAL,
        MEDICINAL,
        SULFUR,
        OTHER
    }

    @Override
    public String toString() {
        return String.format("%s %s odor: %s", strength, type, description);
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
