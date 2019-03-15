package com.tridevmc.jorm_test;

import com.tridevmc.jorm.Column;
import com.tridevmc.jorm.PrimaryKey;
import com.tridevmc.jorm.Table;

@Table
public class Author {
    @Column
    @PrimaryKey
    public int id;

    @Column(length = 16)
    public String firstName;

    @Column(length = 16)
    public String lastName;
}
