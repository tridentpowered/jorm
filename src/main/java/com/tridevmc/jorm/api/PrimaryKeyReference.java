package com.tridevmc.jorm.api;


/**
 * Internal class, used for caching data related to a primary key constraint on a Jorm {@link TableReference}.
 *
 * DO NOT USE THIS outside of Jorm, we offer no guarantee of support for it.
 */
public class PrimaryKeyReference {
    public String name;
    public String field;

    public PrimaryKeyReference(String name, String field) {
        this.name = name;
        this.field = field;
    }
}
