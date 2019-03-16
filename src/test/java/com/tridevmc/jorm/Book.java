package com.tridevmc.jorm;

import com.tridevmc.jorm.api.Column;
import com.tridevmc.jorm.api.ForeignKey;
import com.tridevmc.jorm.api.PrimaryKey;
import com.tridevmc.jorm.api.Table;

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
