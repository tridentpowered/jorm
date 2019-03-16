package com.tridevmc.jorm;

import com.tridevmc.jorm.api.*;
import org.jooq.CreateTableColumnStep;
import org.jooq.CreateTableConstraintStep;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.DSL.constraint;

public class DatabaseUtility {
    public static boolean createTableIfNotExists(Class clazz) {
        if(!clazz.isAnnotationPresent(Table.class)) {
           throw new IllegalArgumentException(String.format("'%s' doesn't have the @Table annotation!",
                   clazz.getName()));
        }

        var t = (Table)clazz.getAnnotation(Table.class);

        var context = DSL.using(SQLDialect.POSTGRES);

        var tableName = t.name().equals("") ? clazz.getSimpleName() : t.name();
        TableReference table = new TableReference(tableName);

        for(var f : clazz.getDeclaredFields()) {
            if(f.isAnnotationPresent(Column.class)) {
                var column = f.getAnnotation(Column.class);
                var columnName = column.name().equals("") ? f.getName() : column.name();
                table.addColumn(new ColumnReference(columnName,
                        SQLDataType.VARCHAR(column.length()).nullable(!f.isAnnotationPresent(NotNull.class))));

                if(f.isAnnotationPresent(PrimaryKey.class)) {
                    var constraint = f.getAnnotation(PrimaryKey.class);
                    var constraintName = constraint.name().equals("") ? "PK_" + tableName.toUpperCase() : constraint.name();
                    table.addPrimaryKey(new PrimaryKeyReference(constraintName, columnName));
                }

                // TODO - negotiate table name based on annotation
                if(f.isAnnotationPresent(ForeignKey.class)) {
                    var constraint = f.getAnnotation(ForeignKey.class);
                    var constraintName = constraint.name().equals("") ? "FK_"
                            + tableName.toUpperCase() + "_TO_" + constraint.tableClass().getSimpleName().toUpperCase() : constraint.name();
                    table.addForeignKey(new ForeignKeyReference(constraintName, columnName,
                            constraint.tableClass().getSimpleName(), constraint.referenceField()));
                }
            }
        }

        if(table.columns.size() <= 0) {
            throw new RuntimeException("Unable to jormify '"+tableName+"' - no columns defined!");
        }

        var statement = context.createTableIfNotExists(table.name);

        CreateTableColumnStep columnStep = null;

        for(var c : table.columns) {
            if(columnStep == null) {
                columnStep = statement.column(c.name, c.dataType);
            } else {
                columnStep = columnStep.column(c.name, c.dataType);
            }
        }

        CreateTableConstraintStep constraintStep = null;
        if(table.primaryKeys.size() != 0) {
            for (var c : table.primaryKeys) {
                if (constraintStep == null) {
                    constraintStep = columnStep.constraint(constraint(c.name).primaryKey(c.field));
                } else {
                    constraintStep = constraintStep.constraint(constraint(c.name).primaryKey(c.field));
                }
            }
        }

        if(table.foreignKeys.size() != 0) {
            for (var c : table.foreignKeys) {
                if (constraintStep == null) {
                    constraintStep = columnStep.constraint(constraint(c.name).foreignKey(c.field)
                            .references(c.table, c.referenceField));
                } else {
                    constraintStep = constraintStep.constraint(constraint(c.name).foreignKey(c.field)
                            .references(c.table, c.referenceField));
                }
            }
        }

        System.out.println(constraintStep != null ? constraintStep.getSQL() : columnStep.getSQL());

        return true;
    }
}
