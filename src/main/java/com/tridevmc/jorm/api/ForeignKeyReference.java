package com.tridevmc.jorm.api;

/**
 * Internal class, used for caching data related to a foreign key constraint on a Jorm {@link TableReference}.
 *
 * DO NOT USE THIS outside of Jorm, we offer no guarantee of support for it.
 */
public class ForeignKeyReference {
    public String name;
    public String field;
    public String table;
    public String referenceField;

    public ForeignKeyReference(String name, String field, String table, String referenceField) {
        this.name = name;
        this.field = field;
        this.table = table;
        this.referenceField = referenceField;
    }
}
