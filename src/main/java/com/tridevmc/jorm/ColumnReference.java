package com.tridevmc.jorm;

import org.jooq.DataType;

public class ColumnReference {
    public String name;
    public DataType dataType;

    public ColumnReference(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }
}
