package com.tridevmc.jorm.api;

import org.jooq.DataType;

/**
 * Internal class, used for caching data related to a column declaration on a Jorm {@link TableReference}
 *
 * DO NOT USE THIS outside of Jorm, we offer no guarantee of support for it.
 */
public class ColumnReference {
    public String name;
    public DataType dataType;

    public ColumnReference(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }
}
