package com.tridevmc.jorm;

import com.tridevmc.jorm.api.ForeignKey;
import com.tridevmc.jorm.api.Table;
import com.tridevmc.jorm.api.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.lang.reflect.Field;

import static org.jooq.impl.DSL.constraint;

public class Jorm {
    public static boolean createTableIfNotExists(Class clazz) {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException(String.format("'%s' doesn't have the @Table annotation!",
                    clazz.getName()));
        }

        Table t = (Table) clazz.getAnnotation(Table.class);
        DSLContext context = DSL.using(SQLDialect.POSTGRES);
        String tableName = t.name().isEmpty() ? clazz.getSimpleName() : t.name();
        TableReference table = new TableReference(tableName);

        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Column.class)) {
                Column column = f.getAnnotation(Column.class);
                String columnName = column.name().isEmpty() ? f.getName() : column.name();
                table.addColumn(new ColumnReference(columnName,
                        SQLDataType.VARCHAR(column.length()).nullable(!f.isAnnotationPresent(NotNull.class))));

                if (f.isAnnotationPresent(PrimaryKey.class)) {
                    PrimaryKey constraint = f.getAnnotation(PrimaryKey.class);
                    String constraintName = constraint.name().isEmpty() ? "PK_" + tableName.toUpperCase() : constraint.name();
                    table.addPrimaryKey(new PrimaryKeyReference(constraintName, columnName));
                }

                // TODO - negotiate table name based on annotation
                if (f.isAnnotationPresent(ForeignKey.class)) {
                    ForeignKey constraint = f.getAnnotation(ForeignKey.class);
                    String constraintName = constraint.name().isEmpty() ? "FK_"
                            + tableName.toUpperCase() + "_TO_" + constraint.tableClass().getSimpleName().toUpperCase() : constraint.name();
                    table.addForeignKey(new ForeignKeyReference(constraintName, columnName,
                            constraint.tableClass().getSimpleName(), constraint.referenceField()));
                }
            }
        }

        if (table.columns.size() <= 0) {
            throw new RuntimeException("Unable to jormify '" + tableName + "' - no columns defined!");
        }

        CreateTableAsStep<Record> statement = context.createTableIfNotExists(table.name);

        CreateTableColumnStep columnStep = null;

        for (ColumnReference c : table.columns) {
            if (columnStep == null) {
                columnStep = statement.column(c.name, c.dataType);
            } else {
                columnStep = columnStep.column(c.name, c.dataType);
            }
        }

        CreateTableConstraintStep constraintStep = null;
        if (table.primaryKeys.size() != 0) {
            for (PrimaryKeyReference c : table.primaryKeys) {
                if (constraintStep == null) {
                    constraintStep = columnStep.constraint(constraint(c.name).primaryKey(c.field));
                } else {
                    constraintStep = constraintStep.constraint(constraint(c.name).primaryKey(c.field));
                }
            }
        }

        if (table.foreignKeys.size() != 0) {
            for (ForeignKeyReference c : table.foreignKeys) {
                if (constraintStep == null) {
                    constraintStep = columnStep.constraint(constraint(c.name).foreignKey(c.field)
                            .references(c.table, c.referenceField));
                } else {
                    constraintStep = constraintStep.constraint(constraint(c.name).foreignKey(c.field)
                            .references(c.table, c.referenceField));
                }
            }
        }

        return true;
    }
}
