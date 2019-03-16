package com.tridevmc.jorm;

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
