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

import com.tridevmc.jorm.api.annotation.Column;
import com.tridevmc.jorm.api.annotation.ForeignKey;
import com.tridevmc.jorm.api.annotation.PrimaryKey;
import com.tridevmc.jorm.api.annotation.Table;

@Table
public class Book {
    @Column
    @PrimaryKey
    public int id;

    @Column
    public String name;

    @Column
    @ForeignKey(tableClass = Author.class, referenceField = "id")
    public int authorId;
}
