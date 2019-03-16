package com.tridevmc.jorm;

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
