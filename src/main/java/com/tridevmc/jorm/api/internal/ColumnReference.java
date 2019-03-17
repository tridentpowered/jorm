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
