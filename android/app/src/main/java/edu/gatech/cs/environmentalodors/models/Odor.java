package edu.gatech.cs.environmentalodors.models;

/**
 * Odor describes an environmental odor as experienced by an individual.
 */
public class Odor {
    private final Strength strength;
    private final Type type;
    private final String description;

    public Odor(Strength strength, Type type, String description) {
        this.strength = strength;
        this.type = type;
        this.description = description;
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
}
