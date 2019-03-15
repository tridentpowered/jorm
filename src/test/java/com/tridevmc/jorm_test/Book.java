package com.tridevmc.jorm_test;

import com.tridevmc.jorm.Column;
import com.tridevmc.jorm.ForeignKey;
import com.tridevmc.jorm.PrimaryKey;
import com.tridevmc.jorm.Table;

@Table
public class Book {
    @Column
    @PrimaryKey
    public int id;

    @Column(length = 64)
    public String name;

    @Column
    @ForeignKey(tableClass = Author.class, field = "id")
    public int authorId;
}
