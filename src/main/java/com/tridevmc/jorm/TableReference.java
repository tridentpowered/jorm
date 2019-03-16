package com.tridevmc.jorm;

import com.tridevmc.jorm.api.ForeignKey;

import java.util.ArrayList;
import java.util.List;

public class TableReference {
    public String name;
    public List<ColumnReference> columns;
    public List<PrimaryKeyReference> primaryKeys;
    public List<ForeignKeyReference> foreignKeys;

    public TableReference(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
        this.primaryKeys = new ArrayList<>();
        this.foreignKeys = new ArrayList<>();
    }

    public void addColumn(ColumnReference field) {
        columns.add(field);
    }

    public void addPrimaryKey(PrimaryKeyReference constraint) {
        primaryKeys.add(constraint);
    }

    public void addForeignKey(ForeignKeyReference constraint) {
        foreignKeys.add(constraint);
    }
}
