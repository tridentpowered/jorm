package com.tridevmc.jorm.api.internal;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Internal class, used for caching data related to a table declaration.
 *
 * DO NOT USE THIS outside of Jorm, we offer no guarantee of support for it.
 */
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

    /**
     * Add a column to the table.
     * @param field The column to add.
     */
    public void addColumn(ColumnReference field) {
        columns.add(field);
    }

    /**
     * Add a PK constraint to the table.
     * @param constraint The PK constraint to add.
     */
    public void addPrimaryKey(PrimaryKeyReference constraint) {
        primaryKeys.add(constraint);
    }

    /**
     * Add an FK constraint to the table.
     * @param constraint The FK constraint to add.
     */
    public void addForeignKey(ForeignKeyReference constraint) {
        foreignKeys.add(constraint);
    }
}
