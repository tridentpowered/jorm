package com.tridevmc.jorm;

/*
 * Copyright 2019 Trident Development (Tridev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.tridevmc.jorm.api.internal.ColumnReference;
import com.tridevmc.jorm.api.internal.ForeignKeyReference;
import com.tridevmc.jorm.api.internal.PrimaryKeyReference;
import com.tridevmc.jorm.api.internal.TableReference;
import com.tridevmc.jorm.api.annotation.*;
import com.tridevmc.jorm.api.annotation.ForeignKey;
import com.tridevmc.jorm.api.annotation.Table;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.lang.reflect.Field;

import static org.jooq.impl.DSL.constraint;

/**
 * The main Jorm class - defines most of the crucial operations that Jorm is able to perform.
 */
public class Jorm {
    public static void createTableIfNotExists(Class clazz) {

        // If we're not dealing with a Table annotated class, just throw. We don't
        // need to bother with it.
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException(String.format("'%s' doesn't have the @Table annotation!",
                    clazz.getName()));
        }

        // Find the table annotation, and get it.
        Table t = (Table) clazz.getAnnotation(Table.class);

        // Set up our JOOQ DSL context (we're using Postgres by default)
        // TODO: Configuration options for SQL Dialects
        DSLContext context = DSL.using(SQLDialect.POSTGRES);

        // Assemble the table's name - this should be either extracted from the annotation,
        // or failing that, constructed from the lowercased version of the class's name.
        String tableName = t.name().isEmpty() ? clazz.getSimpleName().toLowerCase() : t.name();

        // Finally, set up a TableReference object, and the fun can begin.
        TableReference table = new TableReference(tableName);

        // Iterate over every field in our target class to find the data we need.
        for (Field f : clazz.getDeclaredFields()) {
            // If the field in question isn't a column, just ignore it.
            if (f.isAnnotationPresent(Column.class)) {
                /* The step to registering a column is currently twofold, roughly:
                 *
                 *      a.) Construct the actual ColumnReference object from all of
                 *          the metadata we can assemble from both the field AND the
                 *          annotation.
                 *
                 *      b.) Assemble all constraint-level data from the extra annotations
                 *          on the field, and add those as objects on the TableReference.
                 *          Note that we _need_ to do this here, so that we don't retread
                 *          ground later trying to find the information from these fields.
                 */

                // First, get the Column annotation from the field.
                Column column = f.getAnnotation(Column.class);

                // Next, assemble the column's name - this should either be the name from the
                // annotation, or just the unmodified name of the field failing that.
                String columnName = column.name().isEmpty() ? f.getName() : column.name();

                // We also check to make sure that the field isn't marked explicitly non-nullable.
                // Note that this _should_ (and inevitably is) ignored by most SQL servers and
                // dialects when the column in question is also a primary key.
                boolean isNullable = !f.isAnnotationPresent(NotNull.class);

                // Add the column to the table, as a column reference (to be converted into SQL later on).
                table.addColumn(new ColumnReference(columnName,
                        SQLDataType.VARCHAR(column.length()).nullable(isNullable)));

                // Now, we begin phase two - extracting the constraints.

                // First, check for primary key status.
                if (f.isAnnotationPresent(PrimaryKey.class)) {
                    // Extract the PrimaryKey annotation...
                    PrimaryKey constraint = f.getAnnotation(PrimaryKey.class);

                    // Get the name, next - by default, this is either the name provided in the annotation,
                    // or PK_[TABLE NAME] (all uppercased). This is an arbitrary convention, but it helps
                    // to establish a hard standard.
                    String constraintName = constraint.name().isEmpty() ? "PK_" + tableName.toUpperCase() : constraint.name();

                    // Finally, add that primary key as a PrimaryKeyReference (to be consumed in the constraint
                    // building step later on).
                    table.addPrimaryKey(new PrimaryKeyReference(constraintName, columnName));
                }

                // We also check for Foreign Key status here.
                // TODO - make these mutually exclusive - declaring both of these on a field should ABSOLUTELY trigger an exception.
                if (f.isAnnotationPresent(ForeignKey.class)) {

                    // Extract the ForeignKey annotation..
                    ForeignKey constraint = f.getAnnotation(ForeignKey.class);

                    // Get the name, next - by default, this is either the name provided in the annotation,
                    // or FK_[TABLE NAME]_TO_[REFERENCED TABLE NAME] (all uppercased). This is an arbitrary convention,
                    // but it helps to establish a hard standard.
                    String constraintName = constraint.name().isEmpty() ? "FK_"
                            + tableName.toUpperCase() + "_TO_" +
                            constraint.tableClass().getSimpleName().toUpperCase() : constraint.name();

                    // Finally, add that foreign key as a ForeignKeyReference (to be consumed in the constraint
                    // building step later on).
                    table.addForeignKey(new ForeignKeyReference(constraintName, columnName,
                            constraint.tableClass().getSimpleName().toLowerCase(), constraint.referenceField()));
                }
            }
        }

        // If we haven't gotten any columns here, it's pointless to continue - defining
        // an empty table is considered an invalid use of Jorm.
        if (table.columns.size() <= 0) {
            throw new RuntimeException("Unable to jormify '" + tableName + "' - no columns defined!");
        }

        /*
         * Now, we enter the fun part.
         *
         * Jorm uses JOOQ as its underlying API for DSL handling, figuring rewriting dialect agnostic
         * native SQL handling seems like retreading pointless ground. In exchange, we have to use
         * its API, which can be a little difficult to work with when you're attempting to build a
         * fully fledged POJO ORM over it.
         *
         * We're trying to accomplish this in three rough steps, similar to the above.
         *
         *      a.) We create the table itself, starting with a CreateTableAsStep object.
         *
         *      b.) We initialize an _empty_ CreateTableColumnStep, _knowing_ that it will not
         *          stay empty, given the above assertion that our column list is not empty.
         *          We then build individual columns from our references, using the cached
         *          data we assembled above. The reason we cache the data is because
         *          everything in JOOQ is accomplished sequentially - we need to assemble
         *          EVERY column before we can initialize ANY constraints - so, we need
         *          to have already traversed our POJOs FULLY before we can start any
         *          actual DSL work.
         *
         *      c.) We initialize an _empty_ CreateTableConstraintStep, this time with a
         *          shakier assertion that it won't remain empty. Most tables will have
         *          some data here, but we can't be sure of that. We operate in two
         *          discrete sub-steps here - building the primary keys, then the foreign
         *          keys.
         *
         *          NOTE that we do not perform any validation on the table constraints -
         *          that's not the job of this method, at least currently. If the statement
         *          fails, it'll throw, and it'll be on you to handle that.
         */

        // First, we'll create that beginning step that we need to reference. This is
        // the official starting point for the JOOQ DSL, laying out our table's name.
        CreateTableAsStep<Record> statement = context.createTableIfNotExists(table.name);

        // Next, create the empty CreateTableColumnStep.
        CreateTableColumnStep columnStep = null;

        // Iterate over every single ColumnReference we added to the table.
        for (ColumnReference c : table.columns) {
            // If our columnStep is empty, create it from the statement. Otherwise,
            // continue from the step we've got.
            if (columnStep == null) {
                columnStep = statement.column(c.name, c.dataType);
            } else {
                columnStep = columnStep.column(c.name, c.dataType);
            }
        }

        // Next, create the empty CreateTableConstraintStep. It'll be used for both of the
        // following iterations.
        CreateTableConstraintStep constraintStep = null;

        // If we've got any PrimaryKeyReferences, now's the time to use them.
        for (PrimaryKeyReference c : table.primaryKeys) {
            if (constraintStep == null) {
                constraintStep = columnStep.constraint(constraint(c.name).primaryKey(c.field));
            } else {
                constraintStep = constraintStep.constraint(constraint(c.name).primaryKey(c.field));
            }
        }

        // If we've got any ForeignKeyReferences, now's the time to use them.
        for (ForeignKeyReference c : table.foreignKeys) {
            if (constraintStep == null) {
                constraintStep = columnStep.constraint(constraint(c.name).foreignKey(c.field)
                        .references(c.table, c.referenceField));
            } else {
                constraintStep = constraintStep.constraint(constraint(c.name).foreignKey(c.field)
                        .references(c.table, c.referenceField));
            }
        }

        // TODO - establish DB connection, use it, create the table, profit?
        // Remember going forward that we DO NOT have an assertion that constraintStep has ANYTHING in it,
        // therefore it NEEDS to be unpacked. columnStep can be used in its place to generate the SQL if
        // if it turns out to be empty.
    }
}
